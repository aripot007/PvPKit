package fr.aripot007.pvpkit;

import java.util.logging.Logger;

import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import fr.aripot007.pvpkit.command.PvPKitAdminCommand;
import fr.aripot007.pvpkit.command.PvPKitCommand;
import fr.aripot007.pvpkit.game.Arena;
import fr.aripot007.pvpkit.game.Game;
import fr.aripot007.pvpkit.game.Kit;
import fr.aripot007.pvpkit.listener.GameControllerListener;
import fr.aripot007.pvpkit.manager.ArenaManager;
import fr.aripot007.pvpkit.manager.GameManager;
import fr.aripot007.pvpkit.manager.GameMenuManager;
import fr.aripot007.pvpkit.manager.KitManager;
import fr.aripot007.pvpkit.manager.PvPKitPlayerManager;
import fr.aripot007.pvpkit.manager.StatsScoreboardManager;

public class PvPKit extends JavaPlugin {

	Logger log = this.getLogger();
	public static String prefix;
	public KitManager kitManager;
	public ArenaManager arenaManager;
	public GameManager gameManager;
	public PvPKitPlayerManager playerManager;
	public GameController gameController;
	public StatsScoreboardManager statManager;
	public GameMenuManager gameMenuManager;
	
	private static PvPKit instance = null;
	
	public PvPKit() {
		instance = this;
	}
	
	@Override
	public void onEnable(){
		ConfigurationSerialization.registerClass(Kit.class, "Kit");
		ConfigurationSerialization.registerClass(Arena.class, "Arena");
		ConfigurationSerialization.registerClass(Game.class, "Game");
		
		prefix = this.getConfig().getString("prefix").replaceAll("&", "§");
		
		kitManager = new KitManager();
		arenaManager = new ArenaManager();
		gameManager = new GameManager();
		playerManager = new PvPKitPlayerManager();
		statManager = new StatsScoreboardManager();
		gameController = new GameController();
		gameMenuManager = new GameMenuManager();
		playerManager.reloadData();
		kitManager.loadKits();
		arenaManager.loadArenas();
		gameManager.loadGames();
		
		getCommand("pvpkit").setExecutor(new PvPKitCommand());
		getCommand("pvpkitadmin").setExecutor(new PvPKitAdminCommand(kitManager, arenaManager, gameManager));
		
		getServer().getPluginManager().registerEvents(new GameControllerListener(), this);
	}
	
	@Override
	public void onDisable(){
		playerManager.savePlayers();
	}
	
	public KitManager getKitManager() {
		return kitManager;
	}
	
	public ArenaManager getArenaManager() {
		return arenaManager;
	}
	
	public GameManager getGameManager() {
		return gameManager;
	}
	
	public PvPKitPlayerManager getPvPKitPlayerManager() {
		return playerManager;
	}
	
	public GameController getGameController() {
		return gameController;
	}
	
	public static PvPKit getInstance() {
		return instance;
	}

	public StatsScoreboardManager getScoreboardManager() {
		return statManager;
	}

	public GameMenuManager getGameMenuManager() {
		return gameMenuManager;
	}

}
