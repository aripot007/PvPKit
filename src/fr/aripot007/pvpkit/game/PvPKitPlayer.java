package fr.aripot007.pvpkit.game;

import org.bukkit.entity.Player;

public class PvPKitPlayer {
	
	private Player player;
	private int kills, deaths, killstreak;
	
	public PvPKitPlayer(Player player){
		this.player = player;
		this.kills = 0;
		this.deaths = 0;
		this.killstreak = 0;
	}
	
	public PvPKitPlayer(Player player, int kills, int deaths, int killstreak) {
		this.player = player;
		this.kills = kills;
		this.deaths = deaths;
		this.killstreak = killstreak;
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
	

	public Player getPlayer() {
		return player;
	}
	
}
