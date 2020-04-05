package fr.aripot007.pvpkit;

import java.util.logging.Logger;

import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import fr.aripot007.pvpkit.command.PvPKitAdminCommand;
import fr.aripot007.pvpkit.command.PvPKitCommand;
import fr.aripot007.pvpkit.game.Kit;
import fr.aripot007.pvpkit.manager.KitManager;

public class PvPKit extends JavaPlugin {

	Logger log = this.getLogger();
	public static String prefix;
	private KitManager kitManager;
	
	@Override
	public void onEnable(){
		ConfigurationSerialization.registerClass(Kit.class, "Kit");
		prefix = this.getConfig().getString("prefix").replaceAll("&", "§");
		
		this.kitManager = new KitManager();
		
		getCommand("pvpkit").setExecutor(new PvPKitCommand());
		getCommand("pvpkitadmin").setExecutor(new PvPKitAdminCommand(kitManager));
	}
	
	@Override
	public void onDisable(){
		
	}

}
