package fr.aripot007.pvpkit;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.alecgorge.minecraft.jsonapi.JSONAPI;

import fr.aripot007.pvpkit.api.StatsCallHandler;
import fr.aripot007.pvpkit.command.PvPKitAdminCommand;
import fr.aripot007.pvpkit.command.PvPKitCommand;
import fr.aripot007.pvpkit.game.Arena;
import fr.aripot007.pvpkit.game.Game;
import fr.aripot007.pvpkit.game.Kit;
import fr.aripot007.pvpkit.game.Session;
import fr.aripot007.pvpkit.listener.GameControllerListener;
import fr.aripot007.pvpkit.manager.ArenaManager;
import fr.aripot007.pvpkit.manager.GameManager;
import fr.aripot007.pvpkit.manager.GameMenuManager;
import fr.aripot007.pvpkit.manager.KitManager;
import fr.aripot007.pvpkit.manager.PvPKitPlayerManager;
import fr.aripot007.pvpkit.manager.SessionManager;
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
	
	public SessionManager sessionManager;
	
	public GameControllerListener gmList;
	
	private static PvPKit instance = null;
	
	public PvPKit() {
		instance = this;
	}
	
	@Override
	public void onEnable(){
		
		// Register classes for serialization
		ConfigurationSerialization.registerClass(Kit.class, "Kit");
		ConfigurationSerialization.registerClass(Arena.class, "Arena");
		ConfigurationSerialization.registerClass(Game.class, "Game");
		ConfigurationSerialization.registerClass(Session.class, "Session");
		
		prefix = this.getConfig().getString("prefix").replaceAll("&", "§");
		
		// Create entities managers
		kitManager = new KitManager();
		arenaManager = new ArenaManager();
		gameManager = new GameManager();
		playerManager = new PvPKitPlayerManager();
		statManager = new StatsScoreboardManager();
		gameMenuManager = new GameMenuManager();
		
		sessionManager = new SessionManager();
		gameController = new GameController(statManager, kitManager, gameMenuManager, sessionManager);
		
		playerManager.reloadData();
		kitManager.loadKits();
		Bukkit.getScheduler().runTaskLater(this, new Runnable() {

			@Override
			public void run() {
				arenaManager.loadArenas();
				gameManager.loadGames();
				gameMenuManager.init();
			}
			
		}, 1L);
		
		getCommand("pvpkit").setExecutor(new PvPKitCommand(playerManager, gameManager, gameController, sessionManager));
		getCommand("pvpkitadmin").setExecutor(new PvPKitAdminCommand(kitManager, arenaManager, gameManager, sessionManager, gameController, playerManager));
		
		gmList = new GameControllerListener(playerManager, gameController, statManager, sessionManager);
		
		getServer().getPluginManager().registerEvents(gmList, this);
		
		/*
		 * Load JSONAPI if the plugin is present
		 */
		
		Plugin pl = this.getServer().getPluginManager().getPlugin("JSONAPI-RELOADED");
		
		if(pl != null) {
			
			log.info("JSONAPI-RELOADED detected ! Initializing api ...");
			
			JSONAPI jsonapi = (JSONAPI) pl;
			
			jsonapi.registerAPICallHandler(new StatsCallHandler());
			
		} else {
			log.info("JSONAPI-RELOADED was not found on this server, the API will not be enabled.");
		}
		
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

	public GameControllerListener getGmList() {
		return gmList;
	}

	public SessionManager getSessionManager() {
		return sessionManager;
	}
	
}
