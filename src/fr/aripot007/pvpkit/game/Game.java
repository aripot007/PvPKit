package fr.aripot007.pvpkit.game;

import java.util.HashSet;
import java.util.Set;

public class Game {

	private Arena arena;
	private String name;
	private GameStatus status;
	private GameType type;
	private Set<PvPKitPlayer> players = new HashSet<PvPKitPlayer>();
	
	public Game(String name){
		this.name = name;
		this.status = GameStatus.CONFIG;
		this.type = GameType.NORMAL;
	}

	public Game(Arena arena, String name, GameStatus status, GameType type, Set<PvPKitPlayer> players) {
		this.arena = arena;
		this.name = name;
		this.status = status;
		this.type = type;
		this.players = players;
	}
	
	public boolean isValid() {
		return (arena != null && arena.isValid() && type !=null);
	}

	public Arena getArena() {
		return arena;
	}
	public void setArena(Arena arena) {
		this.arena = arena;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public GameStatus getStatus() {
		return status;
	}
	public void setStatus(GameStatus status) {
		this.status = status;
	}

	public GameType getType() {
		return type;
	}
	public void setType(GameType type) {
		this.type = type;
	}

	public Set<PvPKitPlayer> getPlayers() {
		return players;
	}
	public void addPlayer(PvPKitPlayer p) {
		players.add(p);
	}
	public void removePlayer(PvPKitPlayer p) {
		players.remove(p);
	}

}
