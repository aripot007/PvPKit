package fr.aripot007.pvpkit.manager;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import fr.aripot007.pvpkit.PvPKit;
import fr.aripot007.pvpkit.game.PvPKitPlayer;

/**
 * Manages the game selection menu.
 * @author Aristide
 *
 */
public class GameMenuManager {

	private Inventory menu;
	private ItemStack biomeItem;
	private ItemStack canyonItem;
	private ItemStack lavaItem;
	
	private GameManager gameMgr;
	
	static final String MENU_TITLE = "§9§lPvPKit";
	
	public GameMenuManager() {
		
	}
	
	/**
	 * Initialize the menu by creating the inventory and the items.
	 * Hardcoded for now.
	 */
	public void init() {
		
		this.gameMgr = PvPKit.getInstance().getGameManager();
		
		this.menu = Bukkit.createInventory(null, 36, MENU_TITLE);
		
		// Biome game
		biomeItem = new ItemStack(Material.GRASS_BLOCK);
		ItemMeta biomeMeta = biomeItem.getItemMeta();
		biomeMeta.setDisplayName("§aBiomes");
		biomeMeta.setLore(Arrays.asList("§6Mode : §9"+gameMgr.getGame("Biome").getType(),"§bJoueurs : 0", "§d► Cliquez ici pour rejoindre la partie !"));
		biomeItem.setItemMeta(biomeMeta);
		menu.setItem(11, biomeItem);
		
		// Canyon game
		canyonItem = new ItemStack(Material.SAND);
		ItemMeta canyonMeta = canyonItem.getItemMeta();
		canyonMeta.setDisplayName("§eCanyon");
		canyonMeta.setLore(Arrays.asList("§6Mode : §9"+gameMgr.getGame("Canyon").getType(),"§bJoueurs : 0", "§d► Cliquez ici pour rejoindre la partie !"));
		canyonItem.setItemMeta(canyonMeta);
		menu.setItem(13, canyonItem);
		
		// Lava-factory game
		lavaItem = new ItemStack(Material.LAVA_BUCKET);
		ItemMeta lavaMeta = lavaItem.getItemMeta();
		lavaMeta.setDisplayName("§cLava factory");
		lavaMeta.setLore(Arrays.asList("§6Mode : §9"+gameMgr.getGame("Lava-Factory").getType(),"§bJoueurs : 0", "§d► Cliquez ici pour rejoindre la partie !"));
		lavaItem.setItemMeta(lavaMeta);
		menu.setItem(15, lavaItem);
		
		// Back item
		ItemStack backItem = new ItemStack(Material.ARROW);
		ItemMeta backMeta = backItem.getItemMeta();
		backMeta.setDisplayName("§r§fRetour");
		backItem.setItemMeta(backMeta);
		menu.setItem(27, backItem);
		
		
		PvPKit.getInstance().getServer().getPluginManager().registerEvents(new GameMenuListener(), PvPKit.getInstance());
	}
	
	/**
	 * Open the menu to a player
	 */
	public void openMenu(Player p) {
		
		PvPKitPlayer pkp = PvPKit.getInstance().getPvPKitPlayerManager().getPlayer(p);
		
		// Player head to display stats
		ItemStack playerStatItem = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta smeta = (SkullMeta) playerStatItem.getItemMeta();
		smeta.setOwningPlayer(Bukkit.getOfflinePlayer(p.getUniqueId()));
		smeta.setDisplayName("§9§l"+p.getName());
		
		smeta.setLore(Arrays.asList("§6Kills : §b" + pkp.getKills(),
									"§6Morts : §b" + pkp.getDeaths(),
									"§6Ratio : §b" + 1.0f * pkp.getKills() / (pkp.getDeaths() != 0 ? pkp.getDeaths() : 1),
									"§6Meilleur Killstreak : §b" + pkp.getBestKillStreak(),
									"§6Killstreak : §b" + pkp.getKillstreak()));
		playerStatItem.setItemMeta(smeta);
		
		// Create the inventory
		Inventory pmenu = Bukkit.createInventory(null, 36, MENU_TITLE);
		pmenu.setContents(menu.getContents());
		pmenu.setItem(35, playerStatItem);
		
		// Open the menu
		p.openInventory(pmenu);
	}
	
	/**
	 * Update all opens menu
	 */
	private void updateMenus() {
		
		menu.setItem(11, biomeItem);
		menu.setItem(13, canyonItem);
		menu.setItem(15, lavaItem);
		
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(p.getWorld().equals(Bukkit.getWorld("MiniJeux")) && p.getOpenInventory() != null && p.getOpenInventory().getTitle().equals(MENU_TITLE)) {
				p.getOpenInventory().getTopInventory().setItem(11, biomeItem);
				p.getOpenInventory().getTopInventory().setItem(13, canyonItem);
				p.getOpenInventory().getTopInventory().setItem(15, lavaItem);
				//p.updateInventory();
			}
		}
	}
	
	/**
	 * Update the number of players in the lore of the items in every opened menu.
	 */
	public void updatePlayers() {
		
		ItemMeta canyonMeta = canyonItem.getItemMeta();
		canyonMeta.setLore(Arrays.asList("§6Mode : §9"+gameMgr.getGame("Canyon").getType(),"§bJoueurs : "+gameMgr.getGame("Canyon").getPlayers().size(), "§d► Cliquez ici pour rejoindre la partie !"));
		canyonItem.setItemMeta(canyonMeta);
		
		ItemMeta biomeMeta = biomeItem.getItemMeta();
		biomeMeta.setLore(Arrays.asList("§6Mode : §9"+gameMgr.getGame("Biome").getType(),"§bJoueurs : "+gameMgr.getGame("Biome").getPlayers().size(), "§d► Cliquez ici pour rejoindre la partie !"));
		biomeItem.setItemMeta(biomeMeta);
		
		ItemMeta lavaMeta = lavaItem.getItemMeta();
		lavaMeta.setLore(Arrays.asList("§6Mode : §9"+gameMgr.getGame("Lava-Factory").getType(),"§bJoueurs : "+gameMgr.getGame("Lava-Factory").getPlayers().size(), "§d► Cliquez ici pour rejoindre la partie !"));
		lavaItem.setItemMeta(lavaMeta);
		
		updateMenus();
	}
	
	/**
	 * Listens to events related to the game selection menu.
	 */
	class GameMenuListener implements Listener {
		
		@EventHandler
		public void onClick(InventoryClickEvent event) {
			
			ItemStack currentItem = event.getCurrentItem();
			InventoryView view = event.getView();
			Player player = (Player) event.getWhoClicked();
			
			// If the click didn't occur in the menu, do nothing
			if (!view.getTitle().equals(MENU_TITLE) || currentItem == null)
				return;
			
			event.setCancelled(true);
			
			// Check the name and type of the clicked item and do the correct action.
			if (currentItem.getType() == Material.ARROW && currentItem.getItemMeta().getDisplayName().equals("§r§fRetour")) {
				player.closeInventory();
				player.performCommand("menu");
				return;
			} else if (currentItem.getType() == biomeItem.getType() && currentItem.getItemMeta().getDisplayName().equals("§aBiomes")) {
				player.closeInventory();
				player.performCommand("pk join Biome");
				return;
			} else if (currentItem.getType() == canyonItem.getType() && currentItem.getItemMeta().getDisplayName().equals("§eCanyon")) {
				player.closeInventory();
				player.performCommand("pk join Canyon");
				return;
			}  else if (currentItem.getType() == lavaItem.getType() && currentItem.getItemMeta().getDisplayName().equals("§cLava factory")) {
				player.closeInventory();
				player.performCommand("pk join Lava-Factory");
				return;
			}
			
		}
		
		
	}
}

