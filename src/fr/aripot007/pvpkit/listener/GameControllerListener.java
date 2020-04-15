package fr.aripot007.pvpkit.listener;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

import fr.aripot007.pvpkit.GameController;
import fr.aripot007.pvpkit.PvPKit;
import fr.aripot007.pvpkit.game.Game;
import fr.aripot007.pvpkit.game.GameType;
import fr.aripot007.pvpkit.game.PvPKitPlayer;
import fr.aripot007.pvpkit.manager.PvPKitPlayerManager;

public class GameControllerListener implements Listener {
	
	PvPKitPlayerManager playerManager = PvPKit.getInstance().getPvPKitPlayerManager();
	GameController controller =PvPKit.getInstance().getGameController();

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
			event.getDrops().clear();
			Game game = controller.getGame(victim);
			
			if(killer != null) {
				if(killer.isInGame()) {
					victim.addDeath();
					if(game.getType().equals(GameType.UHC)) {
						event.getDrops().add(new ItemStack(Material.GOLDEN_APPLE));
					} else {
						killer.getPlayer().setHealth(20.0);
					}
					game.sendMessage(PvPKit.prefix+"§b"+victim.getPlayer().getName()+" §6s'est fait tué par §b"+killer.getPlayer().getName()+" §6!");
					killer.addKill();
					int killstreak = killer.getKillstreak();
					if(killstreak > 0 && (killstreak % 10 == 0 || killstreak == 5)) {
						game.sendMessage(PvPKit.prefix+"§b"+killer.getPlayer().getName()+" §6a fait une série de §c"+killstreak+" §6kills !");
					}
				} else {
					return;
				}
			} else {
				victim.addDeath();
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
		}
	}
	
	@EventHandler
	public void onRightClick(PlayerInteractEvent event) {
		if(event.getItem() == null || event.getItem().getType() == Material.AIR)
			return;
		PvPKitPlayer p = playerManager.getPlayer(event.getPlayer());
		if(p.isInGame()) {
			if(event.getItem().equals(controller.getKitMenuItem())) {
				controller.openKitMenu(p);
			} else if(event.getItem().equals(controller.getLeaveItem())) {
				controller.leaveGame(p);
			}
		}
	}
	
	public void onItemDrop(PlayerDropItemEvent event) {
		PvPKitPlayer p = playerManager.getPlayer(event.getPlayer());
		if(p.isInGame()) {
			event.setCancelled(true);
		}
	}
	
}
