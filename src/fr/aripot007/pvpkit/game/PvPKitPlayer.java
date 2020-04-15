package fr.aripot007.pvpkit.game;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

public class PvPKitPlayer {
	
	private Player player;
	private int kills, deaths, killstreak;
	private Kit kit;
	private boolean inGame;
	
	public PvPKitPlayer(Player player){
		this.player = player;
		this.kills = 0;
		this.deaths = 0;
		this.killstreak = 0;
		this.inGame = false;
	}
	
	public PvPKitPlayer(Player player, int kills, int deaths, int killstreak) {
		this.player = player;
		this.kills = kills;
		this.deaths = deaths;
		this.killstreak = killstreak;
		this.inGame = false;
	}

	
	public int getKills() {
		return kills;
	}

	public void setKills(int kills) {
		this.kills = kills;
	}
	
	public void addKill() {
		kills++;
		killstreak++;
	}
	
	public void addDeath() {
		deaths++;
		killstreak = 0;
	}

	public int getDeaths() {
		return deaths;
	}

	public void setDeaths(int deaths) {
		this.deaths = deaths;
	}
	

	public int getKillstreak() {
		return killstreak;
	}

	public void setKillstreak(int killstreak) {
		this.killstreak = killstreak;
	}
	

	public Player getPlayer() {
		return player;
	}
	
	public boolean isInGame() {
		return inGame;
	}
	
	public void setInGame(boolean inGame) {
		this.inGame = inGame;
	}
	
	public Kit getKit() {
		return kit;
	}
	
	public void setKit(Kit kit) {
		this.kit = kit;
	}

	
	public Map<String, Object> serialize() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("kills", kills);
		result.put("deaths", deaths);
		result.put("killstreak", killstreak);
		return result;
	}
	
	public static PvPKitPlayer deserialize(Map<String, Object> map, Player p) {
		int kills = (int) map.getOrDefault("kills", 0);
		int deaths = (int) map.getOrDefault("deaths", 0);
		int killstreak = (int) map.getOrDefault("killstreak", 0);
		return new PvPKitPlayer(p, kills, deaths, killstreak);
	}
	
}
