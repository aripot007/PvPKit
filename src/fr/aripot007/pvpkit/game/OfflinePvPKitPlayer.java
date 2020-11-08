package fr.aripot007.pvpkit.game;

import java.util.HashMap;
import java.util.Map;

/** Use to manipulates statistics of offline players */
public class OfflinePvPKitPlayer {
	
	/** The uuid of this player*/
	private String uuid;
	
	/** Statistics of the player*/
	private int kills, deaths, killstreak, bestKs;
	
	
	public OfflinePvPKitPlayer(String uuid){
		this.uuid = uuid;
		this.kills = 0;
		this.deaths = 0;
		this.killstreak = 0;
		this.bestKs = 0;
	}
	
	public OfflinePvPKitPlayer(String uuid, int kills, int deaths, int killstreak, int bestKillstreak) {
		this.uuid = uuid;
		this.kills = kills;
		this.deaths = deaths;
		this.killstreak = killstreak;
		this.bestKs = bestKillstreak;
	}

	
	public int getKills() {
		return kills;
	}

	public void setKills(int kills) {
		this.kills = kills;
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
	

	public String getUUID() {
		return uuid;
	}
	

	public int getBestKillStreak() {
		return bestKs;
	}
	
	public void setBestKillStreak(int best) {
		bestKs = best;
	}
	
	public float getRatio() {
		return 1.0f * kills / (deaths != 0 ? deaths : 1);
	}
	
	/** Serialize this player to a map */
	public Map<String, Object> serialize() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("kills", kills);
		result.put("deaths", deaths);
		result.put("killstreak", killstreak);
		result.put("best-killstreak", bestKs);
		return result;
	}
	
	/** Create a player from a map */
	public static OfflinePvPKitPlayer deserialize(Map<String, Object> map, String uuid) {
		int kills = (int) map.getOrDefault("kills", 0);
		int deaths = (int) map.getOrDefault("deaths", 0);
		int killstreak = (int) map.getOrDefault("killstreak", 0);
		int bestKs = (int) map.getOrDefault("best-killstreak", 0);
		return new OfflinePvPKitPlayer(uuid, kills, deaths, killstreak, bestKs);
	}

	
}
