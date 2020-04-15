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

import fr.aripot007.pvpkit.game.PvPKitPlayer;

public class PvPKitPlayerManager {

	private Map<Player, PvPKitPlayer> players;
	private Logger log;
	private File playersFile;
	private FileConfiguration playersData;
	
	public PvPKitPlayerManager() {
		log = Bukkit.getPluginManager().getPlugin("PvPKit").getLogger();
		playersFile = new File(Bukkit.getPluginManager().getPlugin("PvPKit").getDataFolder(), "players.yml");
		players = new HashMap<Player, PvPKitPlayer>();
		reloadData();
	}
	
	public void reloadData() {
		playersData = YamlConfiguration.loadConfiguration(playersFile);
		players.clear();
		
		for(Player p : Bukkit.getOnlinePlayers()) {
			registerPlayer(p);
		}
		return;
	}
	
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
	
	@SuppressWarnings("unchecked")
	public void registerPlayer(Player p) {
		
		String key = p.getUniqueId().toString();
		
		if(playersData.getKeys(false).contains(key)) {
			
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
			
			
			
			players.put(p, PvPKitPlayer.deserialize(map, p));
		} else {
			
			PvPKitPlayer player = new PvPKitPlayer(p);
			players.put(p, player);
		}
		return;
	}
	
	public void dumpPlayers() {
		System.out.println("PlayerManager players :");
		for(Entry<Player, PvPKitPlayer> e : players.entrySet()) {
			System.out.println(e.getKey().toString()+" : "+e.getValue());
		}
	}

}
