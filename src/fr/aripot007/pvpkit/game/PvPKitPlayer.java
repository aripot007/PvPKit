package fr.aripot007.pvpkit.game;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

/** A player wrapper that adds some stats*/
public class PvPKitPlayer {
	
	/** The player corresponding to this PvPKitPlayer*/
	private Player player;
	
	/** Statistics of the player*/
	private int kills, deaths, killstreak, bestKs;
	private Kit kit;
	
	/** Is this player in a game ? **/
	private boolean inGame;
	
	public PvPKitPlayer(Player player){
		this.player = player;
		this.kills = 0;
		this.deaths = 0;
		this.killstreak = 0;
		this.bestKs = 0;
		this.inGame = false;
	}
	
	public PvPKitPlayer(Player player, int kills, int deaths, int killstreak, int bestKillstreak) {
		this.player = player;
		this.kills = kills;
		this.deaths = deaths;
		this.killstreak = killstreak;
		this.bestKs = bestKillstreak;
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
		if(killstreak > bestKs)
			bestKs = killstreak;
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

	public int getBestKillStreak() {
		return bestKs;
	}
	
	public void setBestKillStreak(int best) {
		bestKs = best;
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
	public static PvPKitPlayer deserialize(Map<String, Object> map, Player p) {
		int kills = (int) map.getOrDefault("kills", 0);
		int deaths = (int) map.getOrDefault("deaths", 0);
		int killstreak = (int) map.getOrDefault("killstreak", 0);
		int bestKs = (int) map.getOrDefault("best-killstreak", 0);
		return new PvPKitPlayer(p, kills, deaths, killstreak, bestKs);
	}

	@Override
	public String toString() {
		return "PvPKitPlayer@" + Integer.toHexString(hashCode()) + " [player=" + player + ", kills=" + kills + ", deaths=" + deaths + ", killstreak="
				+ killstreak + ", bestKs=" + bestKs + ", kit=" + kit + ", game=" + game + ", inGame=" + inGame + "]";
	}
	
	

	
}
