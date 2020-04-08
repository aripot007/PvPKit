package fr.aripot007.pvpkit.manager;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
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
		playersFile = new File(Bukkit.getPluginManager().getPlugin("PvPKit").getDataFolder(), "kits.yml");
		players = new HashMap<Player, PvPKitPlayer>();
		reloadData();
	}
	
	public void reloadData() {
		playersData = YamlConfiguration.loadConfiguration(playersFile);
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

	@SuppressWarnings("unchecked")
	public PvPKitPlayer getPlayer(Player p) {
		if(players.containsKey(p))
			return players.get(p);
		if(playersData.getKeys(false).contains(p.getUniqueId().toString())) {
			PvPKitPlayer player = PvPKitPlayer.deserialize((Map<String, Object>) playersData.get(p.getUniqueId().toString()), p);
			players.put(p, player);
		} else {
			
		}
			
		return new PvPKitPlayer(p);
	}
	
	public void removePlayer(Player p) {
		savePlayers();
		players.remove(p);
	}

}
