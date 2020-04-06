package fr.aripot007.pvpkit;

import java.util.logging.Logger;

import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import fr.aripot007.pvpkit.command.PvPKitAdminCommand;
import fr.aripot007.pvpkit.command.PvPKitCommand;
import fr.aripot007.pvpkit.game.Arena;
import fr.aripot007.pvpkit.game.Game;
import fr.aripot007.pvpkit.game.Kit;
import fr.aripot007.pvpkit.manager.ArenaManager;
import fr.aripot007.pvpkit.manager.GameManager;
import fr.aripot007.pvpkit.manager.KitManager;

public class PvPKit extends JavaPlugin {

	Logger log = this.getLogger();
	public static String prefix;
	private static KitManager kitManager;
	private static ArenaManager arenaManager;
	private static GameManager gameManager;
	
	@Override
	public void onEnable(){
		ConfigurationSerialization.registerClass(Kit.class, "Kit");
		ConfigurationSerialization.registerClass(Arena.class, "Arena");
		ConfigurationSerialization.registerClass(Game.class, "Game");
		
		prefix = this.getConfig().getString("prefix").replaceAll("&", "§");
		
		kitManager = new KitManager();
		arenaManager = new ArenaManager();
		gameManager = new GameManager();
		kitManager.loadKits();
		arenaManager.loadArenas();
		gameManager.loadGames();
		
		getCommand("pvpkit").setExecutor(new PvPKitCommand());
		getCommand("pvpkitadmin").setExecutor(new PvPKitAdminCommand(kitManager, arenaManager, gameManager));
	}
	
	@Override
	public void onDisable(){
		
	}
	
	public static KitManager getKitManager() {
		return kitManager;
	}
	
	public static ArenaManager getArenaManager() {
		return arenaManager;
	}
	
	public static GameManager getGameManager() {
		return gameManager;
	}

}
