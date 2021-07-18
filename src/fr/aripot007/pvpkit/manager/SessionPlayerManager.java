package fr.aripot007.pvpkit.manager;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import fr.aripot007.pvpkit.game.OfflinePvPKitPlayer;
import fr.aripot007.pvpkit.game.PvPKitPlayer;
import fr.aripot007.pvpkit.game.Session;

public class SessionPlayerManager extends PvPKitPlayerManager {
	
	private final Session session;

	public SessionPlayerManager(Session session) {
		log = Bukkit.getPluginManager().getPlugin("PvPKit").getLogger();
		this.playersFile = new File(Bukkit.getPluginManager().getPlugin("PvPKit").getDataFolder()+"/session/"+session.getName(), "players.yml");
		this.playersData = YamlConfiguration.loadConfiguration(playersFile);
		this.players = new HashMap<Player, PvPKitPlayer>();
		this.session = session;
	}
	
	public Session getSession() {
		return this.session;
	}
	
	@Override
	public PvPKitPlayer getPlayer(Player p) {
		PvPKitPlayer player = players.get(p);
		
		if (player == null)
			player = registerPlayer(p);
		
		return player;
	}

	/**
	 * Reset the stats of every players in the session
	 */
	public void resetStats() {
		
		// Reset the stats of all stored players
		
		for(String key : playersData.getKeys(false)) {
			OfflinePvPKitPlayer p = getOfflinePlayer(key);
			p.setKills(0);
			p.setDeaths(0);
			p.setBestKillStreak(0);
			p.setKillstreak(0);
			playersData.set(key, p.serialize());
		}
		
		// reset the stats of online players
		
		for (PvPKitPlayer p : players.values()) {
			p.setKills(0);
			p.setDeaths(0);
			p.setKillstreak(0);
			p.setBestKillStreak(0);
		}
		
		// Save the stats
		
		try {
			playersData.save(playersFile);
		} catch (IOException e) {
			log.severe("Error while saving session stats after reset for session "+session.getName());
			e.printStackTrace();
		}		
		
	}
	
	public Map<Player, PvPKitPlayer> getPlayers() {
		return players;
	}
	
	public void saveStats(String filename) {
		
		File originalFile = this.playersFile;
		this.playersFile = new File(Bukkit.getPluginManager().getPlugin("PvPKit").getDataFolder()+"/session/"+session.getName(), filename+".yml");
		savePlayers();
		this.playersFile = originalFile;
		
	}
	
}
