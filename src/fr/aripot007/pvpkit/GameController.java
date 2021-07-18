package fr.aripot007.pvpkit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

import fr.aripot007.pvpkit.game.Game;
import fr.aripot007.pvpkit.game.Kit;
import fr.aripot007.pvpkit.game.PvPKitPlayer;
import fr.aripot007.pvpkit.listener.KitMenuListener;
import fr.aripot007.pvpkit.manager.GameMenuManager;
import fr.aripot007.pvpkit.manager.KitManager;
import fr.aripot007.pvpkit.manager.StatsScoreboardManager;
import fr.aripot007.pvpkit.util.GUIUtil;

/**
 * Handle the joining and leaving of a game and the kit selection menu.
 * @author Aristide
 *
 */
public class GameController {

	private Map<PvPKitPlayer, Game> ingamePlayers = new HashMap<PvPKitPlayer, Game>();
	public final ItemStack kitMenuItem;
	public final ItemStack leaveItem;
	
	private StatsScoreboardManager statManager;
	private KitManager kitManager = PvPKit.getInstance().getKitManager();
	private GameMenuManager gmMenuMgr = PvPKit.getInstance().getGameMenuManager();
	
	
	public GameController() {
		statManager = PvPKit.getInstance().getScoreboardManager();
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
	
	/**
	 * Make a player join a game.
	 * @param player The player
	 * @param game The game
	 */
	public void joinGame(PvPKitPlayer player, Game game) {
		
		if(player.isInGame()) {
			return;
		}	
		
		Player p = player.getPlayer();
		
		p.teleport(game.getArena().getSpawn());
		
		player.setInGame(true);
		player.setGame(game);
		game.addPlayer(player);
		
		game.sendMessage(PvPKit.prefix+"§b"+player.getPlayer().getName()+" §aa rejoint la partie !");
		
		ingamePlayers.put(player, game);
		
		p.setGameMode(GameMode.ADVENTURE);
		
		statManager.showScoreboard(player);
		
		for (PotionEffect effect : p.getActivePotionEffects())
		    p.removePotionEffect(effect.getType());
		p.setHealth(20d);
		
		gmMenuMgr.updatePlayers();
		
		Bukkit.getScheduler().runTaskLater(PvPKit.getInstance(), new Runnable() { // set the player's inventory after 5 ticks

			@Override
			public void run() {
				p.getInventory().setContents(getMenuContent());
			}
			
		}, 5L);
		
	}
	
	/**
	 * Make a player leave a game.
	 * @param player The player
	 */
	public void leaveGame(PvPKitPlayer player) {
		if(!player.isInGame())
			return;
		
		Game game = ingamePlayers.remove(player);
		statManager.hideScoreboard(player);
		player.setKit(null);
		player.getPlayer().setGameMode(GameMode.SURVIVAL);
		player.getPlayer().getInventory().clear();
		player.setInGame(false);
		player.setGame(null);
		if(game != null) {
			game.sendMessage(PvPKit.prefix+"§b"+player.getPlayer().getName()+" §ca quitté la partie !");
			game.removePlayer(player);
		}
		for (PotionEffect effect : player.getPlayer().getActivePotionEffects())
			player.getPlayer().removePotionEffect(effect.getType());
		player.getPlayer().setHealth(20d);
		player.getPlayer().performCommand("spawn");
		gmMenuMgr.updatePlayers();
	}
	
	public Game getGame(PvPKitPlayer p) {
		return ingamePlayers.get(p);
	}
	
	/**
	 * Get the content of the inventory to give to a player when he joins a game
	 * @return An ItemStack array representing the inventory content
	 */
	public ItemStack[] getMenuContent() {
		ItemStack[] content = new ItemStack[41];
		content[0] = kitMenuItem;
		content[8] = leaveItem;
		return content;
	}

	/**
	 * Open the kit menu to a player and register a {@link KitMenuListener} for this player.
	 */
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
	
	/**
	 * Debug only
	 */
	public void dumpInGamePlayers() {
		System.out.println("Joueurs :");
		for(Entry<PvPKitPlayer, Game> entry : ingamePlayers.entrySet()) {
			System.out.println("  "+entry.getKey().getPlayer().getName()+"(ig:"+entry.getKey().isInGame()+") : "+entry.getValue().getName());
		}
	}
	
	public ItemStack getKitMenuItem() {
		return kitMenuItem;
	}
	
	public ItemStack getLeaveItem() {
		return leaveItem;
	}
	
}
