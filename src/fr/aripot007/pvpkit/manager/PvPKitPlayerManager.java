package fr.aripot007.pvpkit.manager;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import fr.aripot007.pvpkit.game.OfflinePvPKitPlayer;
import fr.aripot007.pvpkit.game.PvPKitPlayer;

/**
 * Manages the players.
 * 
 * Used to load and save players
 * @author Aristide
 *
 */
public class PvPKitPlayerManager {

	Map<Player, PvPKitPlayer> players;
	Logger log;
	File playersFile;
	FileConfiguration playersData;
	
	public PvPKitPlayerManager() {
		log = Bukkit.getPluginManager().getPlugin("PvPKit").getLogger();
		playersFile = new File(Bukkit.getPluginManager().getPlugin("PvPKit").getDataFolder(), "players.yml");
		players = new HashMap<Player, PvPKitPlayer>();
	}
	
	/**
	 * Reload the data from the players config file
	 */
	public void reloadData() {
		playersData = YamlConfiguration.loadConfiguration(playersFile);
		players.clear();
		
		for(Player p : Bukkit.getOnlinePlayers()) {
			registerPlayer(p);
		}
		return;
	}
	
	/**
	 * Save all the players to the players config file
	 */
	public void savePlayers() {
		try {
			for(Entry<Player, PvPKitPlayer> entry : players.entrySet()) {
				playersData.set(entry.getKey().getUniqueId().toString(), entry.getValue().serialize());
			}
			playersData.save(playersFile);
		} catch (Exception e) {
			log.severe("Erreur lors de l'enregistrement des joueurs :");
			e.printStackTrace();
		}
	}

	public PvPKitPlayer getPlayer(Player p) {
		return players.get(p);
	}
	
	public void removePlayer(Player p) {
		savePlayers();
		players.remove(p);
	}
	
	
	/**
	 * Register a player to the players map.
	 * Try to get it from the config file, and if it does not exist, create a new one.
	 */
	@SuppressWarnings("unchecked")
	public void registerPlayer(Player p) {
		
		String key = p.getUniqueId().toString();
		
		if(playersData.getKeys(false).contains(key)) {
			
			// The player is saved in the config file
			
			Map<String, Object> map = new HashMap<String, Object>();
			
			/*
			 * Fix weird inconsistency where ConfigurationSection#get() returns a ConfigurationSection
			 * on the first call and then a Map<String, Object> on the other calls
			 */
			
			if(playersData.isConfigurationSection(key)) { //first call
				
				ConfigurationSection section = playersData.getConfigurationSection(key);
				for(String s : section.getKeys(false)) {
					map.put(s, section.get(s));
				}
				
			} else { //other calls
				
				map = (Map<String, Object>) playersData.get(key);
				
			}
			
			players.put(p, PvPKitPlayer.deserialize(map, p));
		} else {
			
			// The player does not exist in the config file
			
			PvPKitPlayer player = new PvPKitPlayer(p);
			players.put(p, player);
		}
		return;
	}
	
	@SuppressWarnings("unchecked")
	public OfflinePvPKitPlayer getOfflinePlayer(String uuid) {
		
		String key = uuid;
		
		if(playersData.getKeys(false).contains(key)) {
			
			// The player is saved in the config file
			
			Map<String, Object> map = new HashMap<String, Object>();
			
			/*
			 * Fix weird inconsistency wher ConfigurationSection#get() returns a ConfigurationSection
			 * on the first call and then a Map<String, Object> on the other calls
			 */
			
			if(playersData.isConfigurationSection(key)) { //first call
				
				ConfigurationSection section = playersData.getConfigurationSection(key);
				for(String s : section.getKeys(false)) {
					map.put(s, section.get(s));
				}
				
			} else { //other calls
				
				map = (Map<String, Object>) playersData.get(key);
				
			}
			
			return OfflinePvPKitPlayer.deserialize(map, uuid.toString());
			
		} else {
			
			// The player does not exist in the config file
			
			return null;
			
		}
		
	}
	
	// Debug only
	public void dumpPlayers() {
		System.out.println("PlayerManager players :");
		for(Entry<Player, PvPKitPlayer> e : players.entrySet()) {
			System.out.println(e.getKey().toString()+" : "+e.getValue());
		}
	}

}
