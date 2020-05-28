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
import fr.aripot007.pvpkit.game.GameType;
import fr.aripot007.pvpkit.game.Kit;
import fr.aripot007.pvpkit.game.PvPKitPlayer;
import fr.aripot007.pvpkit.manager.PvPKitPlayerManager;
import fr.aripot007.pvpkit.manager.StatsScoreboardManager;

public class GameControllerListener implements Listener {
	
	PvPKitPlayerManager playerManager = PvPKit.getInstance().getPvPKitPlayerManager();
	GameController controller =PvPKit.getInstance().getGameController();
	StatsScoreboardManager statManager = PvPKit.getInstance().getScoreboardManager();
	

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		PvPKitPlayer p = playerManager.getPlayer(event.getPlayer());
		if(p.isInGame())
			controller.leaveGame(p);
		playerManager.removePlayer(event.getPlayer());
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		playerManager.registerPlayer(event.getPlayer());
	}
	
	@EventHandler
	public void onTeleport(PlayerTeleportEvent event) {
		PvPKitPlayer p = playerManager.getPlayer(event.getPlayer());
		if(p.isInGame()) {
			if(!event.getFrom().getWorld().equals(event.getTo().getWorld()))
				controller.leaveGame(p);
		}
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		PvPKitPlayer victim = playerManager.getPlayer(event.getEntity().getPlayer());
		if(victim.isInGame()) {
			PvPKitPlayer killer = playerManager.getPlayer(event.getEntity().getKiller());
			event.setDroppedExp(0);
			event.setDeathMessage(null);
			int apples = 0;
			for(ItemStack i : victim.getPlayer().getInventory().getContents()) {
				if (i.getType().equals(Material.GOLDEN_APPLE))
					apples =+ i.getAmount();
			}
			event.getDrops().clear();
			Game game = controller.getGame(victim);
			
			if(killer != null) {
				if(killer.isInGame()) {
					victim.addDeath();
					if(!victim.equals(killer)) {
						if(game.getType().equals(GameType.UHC)) {
							event.getDrops().add(new ItemStack(Material.GOLDEN_APPLE, 1 + apples));
						} else {
							killer.getPlayer().setHealth(20.0);
						}
						game.sendMessage(PvPKit.prefix+"§b"+victim.getPlayer().getName()+" §6s'est fait tué par §b"+killer.getPlayer().getName()+" §6!");
						killer.addKill();
						int killstreak = killer.getKillstreak();
						if(killer.getKit().isRegiven() && killstreak % killer.getKit().getRegiveKills() == 0)
							safeRegiveKit(killer.getPlayer(), killer.getKit());
						if(killstreak > 0 && (killstreak % 10 == 0 || (killstreak % 5 == 0 && killstreak < 30))) {
							game.sendMessage(PvPKit.prefix+"§b"+killer.getPlayer().getName()+" §6a fait une série de §c"+killstreak+" §6kills !");
						}
						statManager.showScoreboard(victim);
						statManager.showScoreboard(killer);
						return;
						
					} else {
						
						if(game.getType().equals(GameType.UHC))
							event.getDrops().add(new ItemStack(Material.GOLDEN_APPLE, 1 + apples));
						game.sendMessage(PvPKit.prefix+"§b"+victim.getPlayer().getName()+" §6s'est suicidé !");
						statManager.showScoreboard(victim);
						return;
					}
					
				} else {
					return;
				}
			} else {
				victim.addDeath();
				statManager.showScoreboard(victim);
				DamageCause cause = event.getEntity().getLastDamageCause().getCause();
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

	@EventHandler
	public void onRespawn(PlayerRespawnEvent event) {
		PvPKitPlayer player = playerManager.getPlayer(event.getPlayer());
		if(player.isInGame()) {
			event.setRespawnLocation(controller.getGame(player).getArena().getSpawn());
			player.getPlayer().getInventory().clear();
			player.setKit(null);
			player.getPlayer().getInventory().setContents(controller.getMenuContent());
			player.getPlayer().updateInventory();
		}
		return;
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		PvPKitPlayer p = playerManager.getPlayer(event.getPlayer());
		if(p.isInGame()) {
			event.setCancelled(true);
		} else if (event.getBlock().getType().equals(controller.getLeaveItem().getType()) && event.getPlayer().getWorld().getName().equals("MiniJeux")) {
			event.setCancelled(true);
		}
	}
	
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
			}
		}
	}
	
	@EventHandler
	public void onItemDrop(PlayerDropItemEvent event) {
		PvPKitPlayer p = playerManager.getPlayer(event.getPlayer());
		if(p.isInGame()) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPvP(EntityDamageByEntityEvent event) {
		if(event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
			
			PvPKitPlayer attacker = playerManager.getPlayer((Player) event.getDamager());
			PvPKitPlayer victim = playerManager.getPlayer((Player) event.getEntity());
			
			if(victim.isInGame() && attacker.isInGame()) {
				
				if(attacker.getKit() == null) {
					
					event.setCancelled(true);
					attacker.getPlayer().teleport(controller.getGame(attacker).getArena().getSpawn());
					attacker.getPlayer().sendMessage(PvPKit.prefix+"§cVous devez choisir un kit avant de pvp !");
					
				} else if (victim.getKit() == null) {
					
					event.setCancelled(true);
					victim.getPlayer().teleport(controller.getGame(victim).getArena().getSpawn());
					victim.getPlayer().sendMessage(PvPKit.prefix+"§cVous devez choisir un kit avant de pvp !");
					
				}
				
				return;
			}
			
		}
	}

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
	
	@EventHandler
	public void onRegen(EntityRegainHealthEvent event) {
		if(!(event.getEntity() instanceof Player))
			return;
		
		Player p = (Player) event.getEntity();
		
		if(playerManager.getPlayer(p).isInGame() && (event.getRegainReason().equals(RegainReason.SATIATED) || event.getRegainReason().equals(RegainReason.EATING))) {
			
			event.setCancelled(true);
			
		}
		
	}
	
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

	
	public void safeRegiveKit(Player p, Kit kit) {
		Bukkit.getScheduler().runTaskLater(PvPKit.getInstance(), new Runnable() {

			@Override
			public void run() {
				ItemStack[] playerInv = p.getInventory().getContents();
				for(ItemStack item : kit.getInventoryContent()) {
					if(item != null && !item.getType().isAir() && !playerHasItem(playerInv, item))
						p.getInventory().addItem(item);
				}
				for(PotionEffect effect : kit.getEffects())
					if(!p.getActivePotionEffects().contains(effect))
						p.addPotionEffect(effect);
				p.updateInventory();
			}
			
		}, 1L);
	}
	
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
	
}
