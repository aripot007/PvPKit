package fr.aripot007.pvpkit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.aripot007.pvpkit.game.Game;
import fr.aripot007.pvpkit.game.Kit;
import fr.aripot007.pvpkit.game.PvPKitPlayer;
import fr.aripot007.pvpkit.listener.KitMenuListener;
import fr.aripot007.pvpkit.manager.KitManager;
import fr.aripot007.pvpkit.util.GUIUtil;

public class GameController {

	private Map<PvPKitPlayer, Game> ingamePlayers;
	public static final ItemStack kitMenuItem;
	public static final ItemStack leaveItem;
	
	private KitManager kitManager = PvPKit.getKitManager();
	
	static {
		kitMenuItem = new ItemStack(Material.CHEST);
		ItemMeta meta = kitMenuItem.getItemMeta();
		meta.setDisplayName("§5§lChoisir un kit");
		meta.setLore(Arrays.asList("§eFaites un click droit avec ce coffre","§epour choisir un kit !"));
		kitMenuItem.setItemMeta(meta);
		
		leaveItem = new ItemStack(Material.RED_BED);
		meta = leaveItem.getItemMeta();
		meta.setDisplayName("§c§lQuitter la partie");
		meta.setLore(Arrays.asList("§eFaites un click droit avec ce lit","§epour quitter la partie."));
		leaveItem.setItemMeta(meta);
	}
	
	public GameController() {
	}
		
	public void joinGame(PvPKitPlayer player, Game game) {
		if(player.isInGame())
			return;
		player.setInGame(true);
		game.addPlayer(player);
		game.sendMessage(PvPKit.prefix+"§b"+player.getPlayer().getName()+" §aa rejoint la partie !");
		ingamePlayers.put(player, game);
		Player p = player.getPlayer();
		p.teleport(game.getArena().getSpawn());
		p.getInventory().setContents(getMenuContent());
		p.setGameMode(GameMode.ADVENTURE);
	}
	
	public void leaveGame(PvPKitPlayer player) {
		if(!player.isInGame())
			return;
		Game game = ingamePlayers.remove(player);
		player.setKit(null);
		player.getPlayer().setGameMode(GameMode.SURVIVAL);
		player.getPlayer().getInventory().clear();
		player.setInGame(false);
		if(game != null) {
			game.sendMessage(PvPKit.prefix+"§b"+player.getPlayer().getName()+" §ca quitté la partie !");
			game.removePlayer(player);
		}
		player.getPlayer().performCommand("/spawn");
	}
	
	public Game getGame(PvPKitPlayer p) {
		return ingamePlayers.get(p);
	}
	
	public ItemStack[] getMenuContent() {
		ItemStack[] content = new ItemStack[41];
		content[0] = kitMenuItem;
		content[8] = leaveItem;
		return content;
	}

	public void openKitMenu(PvPKitPlayer p) {
		if(p.isInGame()) {
			List<Kit> kits = new ArrayList<Kit>();
			for(String s : ingamePlayers.get(p).getArena().getKits()) {
				kits.add(kitManager.getKit(s));
			}
			Kit[] kitsFormat = GUIUtil.formatMenu(kits);
			Inventory menu = Bukkit.createInventory(null, kitsFormat.length, "§5§lChoisissez un kit");
			for(int i = 0; i < kitsFormat.length; i++) {
				if(kitsFormat[i] != null)
					menu.setItem(i, kitsFormat[i].getIcon());
			}
			p.getPlayer().openInventory(menu);
			Bukkit.getPluginManager().registerEvents(new KitMenuListener(p.getPlayer().getUniqueId(), "§5§lChoisissez un kit", kitsFormat), PvPKit.getInstance());
		}
		return;
	}
	
}