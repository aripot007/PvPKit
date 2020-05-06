package fr.aripot007.pvpkit.listener;

import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.potion.PotionEffect;

import fr.aripot007.pvpkit.PvPKit;
import fr.aripot007.pvpkit.game.Kit;

public class KitMenuListener implements Listener {

	UUID player;
	String invName;
	Kit[] kits;
	
	public KitMenuListener(UUID player, String invName, Kit[] kits) {
		this.player = player;
		this.invName = invName;
		this.kits = kits;
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if(e.getWhoClicked().getUniqueId().equals(player)
			&& e.getView().getTitle().equals(invName)
			&& e.getCurrentItem() != null
			&& e.getCurrentItem().getType() != Material.AIR) {
			
			e.setCancelled(true);
			if(e.getCurrentItem().equals(PvPKit.getInstance().getGameController().getKitMenuItem()) || e.getCurrentItem().equals(PvPKit.getInstance().getGameController().getLeaveItem()))
				return;
			Kit kit = kits[e.getSlot()];
			Player p = (Player) e.getWhoClicked();
			if(kit != null) {
				PvPKit.getInstance().getPvPKitPlayerManager().getPlayer(p).setKit(kit);
				safeGiveKit(p, kit);
			} else {
				e.getWhoClicked().sendMessage(PvPKit.prefix+"§cUne erreur est survenue.");
				Logger log = PvPKit.getInstance().getLogger();
				log.severe("Erreur lors du chargement d'un kit depuis le menu pour"+p.getName()+" (slot : "+e.getSlot()+" )");
				safeClose(p);
			}
			HandlerList.unregisterAll(this);
		}
	}
	
	private void safeClose(Player p) {
		Bukkit.getScheduler().runTaskLater(PvPKit.getInstance(), new Runnable() {

			@Override
			public void run() {
				p.getOpenInventory().close();
			}
			
		}, 1L);
	}
	
	private void safeGiveKit(Player p, Kit kit) {
		Bukkit.getScheduler().runTaskLater(PvPKit.getInstance(), new Runnable() {

			@Override
			public void run() {
				p.getOpenInventory().close();
				p.getInventory().setContents(kit.getInventoryContent());
				for(PotionEffect effect : kit.getEffects())
					p.addPotionEffect(effect);
				p.updateInventory();
				PvPKit.getInstance().getScoreboardManager().showScoreboard(PvPKit.getInstance().getPvPKitPlayerManager().getPlayer(p)); // Update player scoreboard
			}
			
		}, 1L);
	}
	
}
