package fr.aripot007.pvpkit.manager;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import fr.aripot007.pvpkit.game.Kit;

public class KitManager {
	
	private Logger log;
	private File kitsFile;
	private FileConfiguration kitsData;
	private Map<String, Kit> kits;
	
	public KitManager() {
		log = Bukkit.getPluginManager().getPlugin("PvPKit").getLogger();
		kitsFile = new File(Bukkit.getPluginManager().getPlugin("PvPKit").getDataFolder(), "kits.yml");
		kits = new HashMap<String, Kit>();
		loadKits();
	}
	
	public void loadKits() {
		kitsData = YamlConfiguration.loadConfiguration(kitsFile);
		kits.clear();
		for(String s : kitsData.getKeys(false)) {
			Kit kit = (Kit) kitsData.get(s);
			kits.put(kit.getName(), kit);
		}
	}

	public void saveKits() {
		for(Kit kit : kits.values()) {
			kitsData.set(kit.getName(), null);
			kitsData.set(kit.getName(), kit);
		}
		try {
			kitsData.save(kitsFile);
		} catch (IOException e) {
			log.severe("Erreur lors de la sauvegarde des kits :");
			e.printStackTrace();
		}
		loadKits();
	}

	public Kit loadKit(String name) {
		kitsData = YamlConfiguration.loadConfiguration(kitsFile);
		return (Kit) kitsData.get(name);
	}

	public Set<Kit> getValidKits() {
		Set<Kit> result = new HashSet<Kit>();
		for(Kit kit : kits.values()) {
			if(kit.isValid())
				result.add(kit);
		}
		return result;
	}
	
	public Set<Kit> getInvalidKits() {
		Set<Kit> result = new HashSet<Kit>();
		for(Kit kit : kits.values()) {
			if(!kit.isValid())
				result.add(kit);
		}
		return result;
	}

	public Map<String, Kit> getKits() {
		return kits;
	}
	
	public Set<String> getKitsKeySet() {
		return new HashSet<String>(kits.keySet());
	}
	
	public Kit getKit(String name) {
		return kits.get(name);
	}
	
	public boolean containsKit(String name) {
		return kits.containsKey(name);
	}
	
	public void putKit(Kit kit) {
		kits.put(kit.getName(), kit);
		return;
	}
	
	public void removeKit(String name) {
		kits.remove(name);
	}

}
