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

import fr.aripot007.pvpkit.game.Arena;

/**
 * Manages the arenas.
 * 
 * Used to load and save arenas
 * @author Aristide
 *
 */
public class ArenaManager {
	
	private Logger log;
	private File arFile;
	private FileConfiguration arData;
	private Map<String, Arena> arenas;

	public ArenaManager() {
		log = Bukkit.getPluginManager().getPlugin("PvPKit").getLogger();
		arFile = new File(Bukkit.getPluginManager().getPlugin("PvPKit").getDataFolder(), "arenas.yml");
		arenas = new HashMap<String, Arena>();
		loadArenas();
		}
	
	
	/**
	 * Load all the arenas from the arenas config file
	 */
	public void loadArenas() {
		arData = YamlConfiguration.loadConfiguration(arFile);
		arenas.clear();
		for(String s : arData.getKeys(false)) {
			Arena arena = (Arena) arData.get(s);
			arenas.put(arena.getName(), arena);
		}
	}
	
	/**
	 * Load an arena from the arenas config file
	 */
	public Arena loadArena(String name) {
		arData = YamlConfiguration.loadConfiguration(arFile);
		return (Arena) arData.get(name);
	}
	
	/**
	 * Save all the arenas to the arenas config file
	 */
	public void saveArenas() {
		for(Arena arena : arenas.values()) {
			arData.set(arena.getName(), null);
			arData.set(arena.getName(), arena);
		}
		try {
			arData.save(arFile);
		} catch (IOException e) {
			log.severe("Erreur lors de la sauvegarde des arènes :");
			e.printStackTrace();
		}
	}
	
	public Map<String, Arena> getArenas(){
		return arenas;
	}
	
	public Arena getArena(String name) {
		return arenas.get(name);
	}
	
	public boolean containsArena(String name) {
		return arenas.containsKey(name);
	}
	
	public void putArena(Arena arena) {
		arenas.put(arena.getName(), arena);
	}
	
	public void removeArena(String name) {
		arenas.remove(name);
	}
	
	/**
	 * Get the valid arenas.
	 * @return a list of valid arenas
	 */
	public Set<Arena> getValidArenas(){
		Set<Arena> valid = new HashSet<Arena>();
		for(Arena a : arenas.values()) {
			if(a.isValid())
				valid.add(a);
		}
		return valid;
	}

	/**
	 * Get the invalid arenas.
	 * @return a list of invalid arenas
	 */
	public Set<Arena> getInvalidArenas(){
		Set<Arena> invalid = new HashSet<Arena>();
		for(Arena a : arenas.values()) {
			if(!a.isValid())
				invalid.add(a);
		}
		return invalid;
	}
	
}
