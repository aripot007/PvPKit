package fr.aripot007.pvpkit;

import java.util.logging.Logger;

import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import fr.aripot007.pvpkit.command.PvPKitAdminCommand;
import fr.aripot007.pvpkit.command.PvPKitCommand;
import fr.aripot007.pvpkit.game.Arena;
import fr.aripot007.pvpkit.game.Kit;
import fr.aripot007.pvpkit.manager.ArenaManager;
import fr.aripot007.pvpkit.manager.KitManager;

public class PvPKit extends JavaPlugin {

	Logger log = this.getLogger();
	public static String prefix;
	private KitManager kitManager;
	private ArenaManager arenaManager;
	
	@Override
	public void onEnable(){
		ConfigurationSerialization.registerClass(Kit.class, "Kit");
		ConfigurationSerialization.registerClass(Arena.class, "Arena");
		
		prefix = this.getConfig().getString("prefix").replaceAll("&", "§");
		
		this.kitManager = new KitManager();
		this.arenaManager = new ArenaManager();
		
		getCommand("pvpkit").setExecutor(new PvPKitCommand());
		getCommand("pvpkitadmin").setExecutor(new PvPKitAdminCommand(kitManager, arenaManager));
	}
	
	@Override
	public void onDisable(){
		
	}

}
