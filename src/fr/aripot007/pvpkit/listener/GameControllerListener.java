package fr.aripot007.pvpkit.listener;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import fr.aripot007.pvpkit.GameController;
import fr.aripot007.pvpkit.PvPKit;
import fr.aripot007.pvpkit.game.Game;
import fr.aripot007.pvpkit.game.GameStatus;
import fr.aripot007.pvpkit.game.GameType;
import fr.aripot007.pvpkit.game.Kit;
import fr.aripot007.pvpkit.game.PvPKitPlayer;
import fr.aripot007.pvpkit.game.Session;
import fr.aripot007.pvpkit.game.SessionStatus;
import fr.aripot007.pvpkit.manager.PvPKitPlayerManager;
import fr.aripot007.pvpkit.manager.SessionManager;
import fr.aripot007.pvpkit.manager.StatsScoreboardManager;

/**
 * Handle events that occurs in a game.
 * @author Aristide
 *
 */
public class GameControllerListener implements Listener {
	
	PvPKitPlayerManager playerManager;
	GameController controller;
	StatsScoreboardManager statManager;
	SessionManager sessionManager;
	
	public GameControllerListener(PvPKitPlayerManager playerManager, GameController controller, StatsScoreboardManager statManager, SessionManager sessionManager) {
		this.playerManager = playerManager;
		this.controller = controller;
		this.statManager = statManager;
		this.sessionManager = sessionManager;
	}

	/*
	 * Make the player leave the game if he's in one, and remove it from the players map.
	 */
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		PvPKitPlayer p = playerManager.getPlayer(event.getPlayer());
		
		if(p.isInGame())
			controller.leaveGame(p);
		
		playerManager.removePlayer(event.getPlayer());
	}
	
	/*
	 * Register the player to the PvPKitPlayer map
	 */
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		playerManager.registerPlayer(event.getPlayer());
	}
	
	/*
	 * If the player teleports between differents worlds, make him leave the game
	 */
	@EventHandler
	public void onTeleport(PlayerTeleportEvent event) {
		PvPKitPlayer p = playerManager.getPlayer(event.getPlayer());
		if(p.isInGame()) {
			if(!event.getFrom().getWorld().equals(event.getTo().getWorld())) {
				controller.leaveGame(p);
				
			}
		}
	}
	
	/*
	 * Protect against kills when someone is not in the game, and change players statistics
	 */
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
	
		PvPKitPlayer victim = playerManager.getPlayer(event.getEntity().getPlayer());
		
		if(victim.isInGame()) {
			
			Game game = victim.getGame();	
			
			event.setDroppedExp(0);
			event.setDeathMessage(null);
			
			// Count golden apples in the inventory of the victim
			int apples = 0;
			for(ItemStack i : victim.getPlayer().getInventory().getContents()) {
				if (i !=null && i.getType().equals(Material.GOLDEN_APPLE))
					apples =+ i.getAmount();
			}
			
			// Clear the drops
			event.getDrops().clear();
			
			PvPKitPlayer killer = null;
			
			if (event.getEntity().getKiller() != null) {
				
				// Check if the game is in a session
				
				if(game.getStatus() == GameStatus.SESSION) {
					
					// The game is used in a session, we use the players provided by this session's playerManager
					Session session = sessionManager.getSession(game);
					killer = session.getPlayerManager().getPlayer(event.getEntity().getKiller());
					
				} else {
					
					// The game is not in a session, we use the default playerManager
					killer = playerManager.getPlayer(event.getEntity().getKiller());
				}	
			}
			
			if(killer != null) {
				
				// The victim died of a player
				
				if(killer.isInGame()) {
					
					// The killer is in a game too
					
					victim.addDeath();
					
					if(!victim.equals(killer)) {
						
						// The victim did not kill himself (with a bow for example)
						
						// Drop a golden apple if in UHC mode or heal the killer
						if(game.getType().equals(GameType.UHC)) {
							event.getDrops().add(new ItemStack(Material.GOLDEN_APPLE, 1 + apples));
						} else {
							killer.getPlayer().setHealth(20.0);
						}
						
						game.sendMessage(PvPKit.prefix+"§b"+victim.getPlayer().getName()+" §6s'est fait tué par §b"+killer.getPlayer().getName()+" §6!");
						killer.addKill();
						
						int killstreak = killer.getKillstreak();
						
						// give the kit again to the killer if it should
						if(killer.getKit().isRegiven() && killstreak % killer.getKit().getRegiveKills() == 0)
							safeRegiveKit(killer.getPlayer(), killer.getKit());
						
						// Announce the killstrak if it hits remarquable values (multiple of 5 below 30 kills then multiple of 10)
						if(killstreak > 0 && (killstreak % 10 == 0 || (killstreak % 5 == 0 && killstreak < 30))) {
							game.sendMessage(PvPKit.prefix+"§b"+killer.getPlayer().getName()+" §6a fait une série de §c"+killstreak+" §6kills !");
						}
						
						// Update scoreboards with the default playermanger's players
						statManager.showScoreboard(playerManager.getPlayer(victim.getPlayer()));
						statManager.showScoreboard(playerManager.getPlayer(killer.getPlayer()));
						return;
						
					} else {
						
						// The victim killed himself
						
						// Drop golden apples if in uhc mode
						if(game.getType().equals(GameType.UHC))
							event.getDrops().add(new ItemStack(Material.GOLDEN_APPLE, 1 + apples));
						
						game.sendMessage(PvPKit.prefix+"§b"+victim.getPlayer().getName()+" §6s'est suicidé !");
						statManager.showScoreboard(playerManager.getPlayer(victim.getPlayer()));
						return;
					}
					
				} else {
					return;
				}
			} else {
				
				// The victim died of a natural cause
				
				victim.addDeath();
				statManager.showScoreboard(playerManager.getPlayer(victim.getPlayer()));
				
				DamageCause cause = event.getEntity().getLastDamageCause().getCause();
				
				// Send a message according to the cause of the death
				switch(cause) {
				case BLOCK_EXPLOSION:
					game.sendMessage(PvPKit.prefix+"§b"+victim.getPlayer().getName()+" §6s'est fait atomisé.");
					break;
				case DRAGON_BREATH:
					game.sendMessage(PvPKit.prefix+"§6L'enderdragon a transmis le coronavirus à §b"+victim.getPlayer().getName()+"§6.");
					break;
				case DROWNING:
					game.sendMessage(PvPKit.prefix+"§b"+victim.getPlayer().getName()+" §6s'est pris pour un poisson.");
					break;
				case FALL:
					game.sendMessage(PvPKit.prefix+"§b"+victim.getPlayer().getName()+" §6a sauté en parachute sans parachute.");
					break;
				case FIRE:
					game.sendMessage(PvPKit.prefix+"§b"+victim.getPlayer().getName()+" §6a trop cuit.");
					break;
				case FIRE_TICK:
					game.sendMessage(PvPKit.prefix+"§b"+victim.getPlayer().getName()+" §6était trop chaud pour ce monde.");
					break;
				case FLY_INTO_WALL:
					game.sendMessage(PvPKit.prefix+"§b"+victim.getPlayer().getName()+" §6s'est pris un mur.");
					break;
				case HOT_FLOOR:
					game.sendMessage(PvPKit.prefix+"§b"+victim.getPlayer().getName()+" §6n'a pas regardé ou il marchait.");
					break;
				case LAVA:
					game.sendMessage(PvPKit.prefix+"§b"+victim.getPlayer().getName()+" §6a confondu le lac de lave avec un jaccuzi.");
					break;
				case LIGHTNING:
					game.sendMessage(PvPKit.prefix+"§b"+victim.getPlayer().getName()+" §6s'est fait foudroyé.");
					break;
				case MAGIC:
					game.sendMessage(PvPKit.prefix+"§b"+victim.getPlayer().getName()+" §6s'est fait ensorcelé.");
					break;
				case MELTING:
					game.sendMessage(PvPKit.prefix+"§b"+victim.getPlayer().getName()+" §6a fondu.");
					break;
				case POISON:
					game.sendMessage(PvPKit.prefix+"§b"+victim.getPlayer().getName()+" §6a testé le cyanure de potassium (spoiler : c'était pas ouf).");
					break;
				case PROJECTILE:
					game.sendMessage(PvPKit.prefix+"§b"+victim.getPlayer().getName()+" §6a reçu quelque chose sur la tête.");
					break;
				case STARVATION:
					game.sendMessage(PvPKit.prefix+"§b"+victim.getPlayer().getName()+" §6a fait l'expérience de la sybérie en 1933.");
					break;
				case SUFFOCATION:
					game.sendMessage(PvPKit.prefix+"§b"+victim.getPlayer().getName()+" §6a suffoqué.");
					break;
				case SUICIDE:
					game.sendMessage(PvPKit.prefix+"§b"+victim.getPlayer().getName()+" §6s'est suicidé.");
					break;
				case VOID:
					game.sendMessage(PvPKit.prefix+"§b"+victim.getPlayer().getName()+" §6a sauté dans le vide.");
					break;
				default:
					game.sendMessage(PvPKit.prefix+"§b"+victim.getPlayer().getName()+" §6est mort mystérieusement.");
					break;
				
				}
			}
			
		}
	}

	/*
	 * Teleport the player to the game's spawn, and give him the menu
	 */
	@EventHandler
	public void onRespawn(PlayerRespawnEvent event) {
		PvPKitPlayer player = playerManager.getPlayer(event.getPlayer());
		if(player.isInGame()) {
			
			event.setRespawnLocation(player.getGame().getArena().getSpawn()); // Set respawn location to game's spawn

			player.getPlayer().getInventory().clear(); // Clear inventory
			
			player.setKit(null); // Reset kit
			
			// If the player is in a session, give him a random kit
			
			Game game = player.getGame();
			
			if(game.getStatus() == GameStatus.SESSION) {
				
				safeGiveKit(player, controller.getRandomKit(game.getArena()));
				
			} else {
				
				// If he is not in a game, we give him the menu
				
				player.getPlayer().getInventory().setContents(controller.getMenuContent()); // Give menu
				
				player.getPlayer().updateInventory();
			
			}
		}
		return;
	}
	
	/*
	 * Prevents in game players from placing blocks
	 */
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		PvPKitPlayer p = playerManager.getPlayer(event.getPlayer());
		if(p.isInGame()) {
			event.setCancelled(true);
		} else if (event.getBlock().getType().equals(controller.getLeaveItem().getType()) && event.getPlayer().getWorld().getName().equals("MiniJeux")) {
			event.setCancelled(true);
		}
	}
	
	/*
	 * Open the menu or leave the game according to the item used
	 */
	@EventHandler
	public void onRightClick(PlayerInteractEvent event) {
		if(event.getItem() == null || event.getItem().getType() == Material.AIR)
			return;
		PvPKitPlayer p = playerManager.getPlayer(event.getPlayer());
		if(p.isInGame()) {
			if(event.getItem().equals(controller.getKitMenuItem())) {
				event.setCancelled(true);
				controller.openKitMenu(p);
			} else if(event.getItem().equals(controller.getLeaveItem())) {
				event.setCancelled(true);
				controller.leaveGame(p);
			} else if (p.getGame().getStatus() == GameStatus.SESSION && sessionManager.getSession(p.getGame()).getStatus() == SessionStatus.PAUSED) {
				event.setCancelled(true);
			}
		}
	}
	
	/*
	 * Prevents in game players from dropping an item
	 */
	@EventHandler
	public void onItemDrop(PlayerDropItemEvent event) {
		PvPKitPlayer p = playerManager.getPlayer(event.getPlayer());
		if(p.isInGame()) {
			event.setCancelled(true);
		}
	}
	
	/*
	 * Prevents players that does not have a kit from hurting each other
	 */
	@EventHandler
	public void onPvP(EntityDamageByEntityEvent event) {
		if(event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
			
			// The damage is caused by another player
			
			PvPKitPlayer attacker = playerManager.getPlayer((Player) event.getDamager());
			PvPKitPlayer victim = playerManager.getPlayer((Player) event.getEntity());
			
			if(victim.isInGame() && attacker.isInGame()) {
				
				// The victim's session is paused
				if (victim.getGame().getStatus() == GameStatus.SESSION && sessionManager.getSession(victim.getGame()).getStatus() == SessionStatus.PAUSED) {
					event.setCancelled(true);
				}
				
				// The two players are in a game
				
				if(attacker.getKit() == null) {
					
					// The attacker doesn't have a kit, cancel the event and teleport him to the spawn
					
					event.setCancelled(true);
					attacker.getPlayer().teleport(controller.getGame(attacker).getArena().getSpawn());
					attacker.getPlayer().sendMessage(PvPKit.prefix+"§cVous devez choisir un kit avant de pvp !");
					
				} else if (victim.getKit() == null) {
					
					// The victim doesn't have a kit, cancel the event and teleport him to the spawn
					
					event.setCancelled(true);
					victim.getPlayer().teleport(controller.getGame(victim).getArena().getSpawn());
					victim.getPlayer().sendMessage(PvPKit.prefix+"§cVous devez choisir un kit avant de pvp !");
					
				}
				
				return;
			}
			
		}
	}

	/*
	 * Prevents players from losing food
	 */
	@EventHandler
	public void onFoodChange(FoodLevelChangeEvent event) {
		
		if(!(event.getEntity() instanceof Player))
			return;
		
		Player p = (Player) event.getEntity();
		
		if(playerManager.getPlayer(p).isInGame()) {
			event.setCancelled(true);
			event.setFoodLevel(20);
			p.setSaturation(20);
		}
		
	}
	
	/*
	 * Prevents regeneration from saturation or eating
	 */
	@EventHandler
	public void onRegen(EntityRegainHealthEvent event) {
		if(!(event.getEntity() instanceof Player))
			return;
		
		Player p = (Player) event.getEntity();
		
		if(playerManager.getPlayer(p).isInGame() && (event.getRegainReason().equals(RegainReason.SATIATED) || event.getRegainReason().equals(RegainReason.EATING))) {
			
			event.setCancelled(true);
			
		}
		
	}
	
	/*
	 * Remove glass bottles from players inventory when they use a potion
	 */
	@EventHandler
	public void onItemConsume(PlayerItemConsumeEvent event) {
		
		Player p = event.getPlayer();
		
		if(playerManager.getPlayer(p).isInGame() && event.getItem().getType().equals(Material.POTION)) {
			Bukkit.getScheduler().runTaskLater(PvPKit.getInstance(), new Runnable() {

				@Override
				public void run() {
					p.getInventory().remove(Material.GLASS_BOTTLE);
				}
				
			}, 1L);
			p.getInventory().remove(Material.GLASS_BOTTLE);
		}
		
	}

	/*
	 * Give a kit again to a player
	 */
	public void safeRegiveKit(Player p, Kit kit) {
		Bukkit.getScheduler().runTaskLater(PvPKit.getInstance(), new Runnable() {

			@Override
			public void run() {
				
				ItemStack[] playerInv = p.getInventory().getContents();
				
				// Add the missing items to the player's inventory
				for(ItemStack item : kit.getInventoryContent()) {
					if(item != null && !item.getType().isAir() && !playerHasItem(playerInv, item))
						p.getInventory().addItem(item);
				}
				
				// Give the missing potions effect to the player
				for(PotionEffect effect : kit.getEffects())
					if(!p.getActivePotionEffects().contains(effect))
						p.addPotionEffect(effect);
				p.updateInventory();
			}
			
		}, 1L);
	}
	
	/**
	 * Check if an inventory contains a certain item
	 * @param inventory The inventory to check
	 * @param item The item to check
	 */
	private boolean playerHasItem(ItemStack[] inventory, ItemStack item) {
		if(item == null)
			return true;
		
		List<ItemStack> inv = Arrays.asList(inventory);
		if(inv.contains(item))
			return true;
		
		for(ItemStack i : inv) {
			if(i != null){
				if(i.isSimilar(item))
					return true;
				if(i.getType().equals(item.getType()))
					return true;
			}
		}
		
		return false;
	}

	/**
	 * Safely give a kit to a player
	 */
	private void safeGiveKit(PvPKitPlayer p, Kit kit) {
		Bukkit.getScheduler().runTaskLater(PvPKit.getInstance(), new Runnable() {

			@Override
			public void run() {
				p.getPlayer().getOpenInventory().close();
				
				// Give inventory content
				p.getPlayer().getInventory().setContents(kit.getInventoryContent());
				p.setKit(kit);
				
				// Give potions effects
				for(PotionEffect effect : kit.getEffects())
					p.getPlayer().addPotionEffect(effect);
				
				p.getPlayer().updateInventory();
				
				PvPKit.getInstance().getScoreboardManager().showScoreboard(p); // Update player scoreboard
			}
			
		}, 1L);
	}
	
}
