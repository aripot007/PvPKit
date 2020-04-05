package fr.aripot007.pvpkit.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import fr.aripot007.pvpkit.manager.ArenaManager;

@SerializableAs("Game")
public class Game implements ConfigurationSerializable {

	private Arena arena;
	private String name;
	private GameStatus status;
	private GameType type;
	private Set<PvPKitPlayer> players = new HashSet<PvPKitPlayer>();
	
	public Game(String name){
		this.name = name;
		this.status = GameStatus.CONFIG;
		this.type = GameType.NORMAL;
		isValid();
	}

	public Game(String name, Arena arena, GameStatus status, GameType type) {
		this.arena = arena;
		this.name = name;
		this.status = status;
		this.type = type;
		isValid();
	}
	
	public boolean isValid() {
		if(arena != null && arena.isValid() && type !=null && status != null) {
			if(this.status.equals(GameStatus.CONFIG))
				this.status = GameStatus.OPEN;
			return true;
		} else {
			if(!this.getStatus().equals(GameStatus.CONFIG) || !this.getStatus().equals(GameStatus.MAINTENANCE))
				this.status = GameStatus.CONFIG;
			return false;
		}
		
	}

	public List<String> getErrors() {
		List<String> errors = new ArrayList<String>();
		if(arena == null)
			errors.add("Le jeu n'est lié à aucune arène");
		else if(!arena.isValid())
			errors.add("L'arène "+arena.getName()+" n'est pas valide");
		if(type == null)
			errors.add("Aucun type de jeu n'a été précisé");
		return errors;
	}
	
	public Arena getArena() {
		return arena;
	}
	
	public void setArena(Arena arena) {
		this.arena = arena;
		isValid();
	}

	public String getName() {
		return name;
	}

	public GameStatus getStatus() {
		return status;
	}
	public void setStatus(GameStatus status) {
		this.status = status;
		isValid();
	}

	public GameType getType() {
		return type;
	}
	public void setType(GameType type) {
		this.type = type;
		isValid();
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

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("name", name);
		result.put("arena", arena.getName());
		result.put("type", type.toString());
		result.put("status", status.toString());
		return result;
	}
	
	public static Game deserialize(Map<String, Object> map) {
		String name = (String) map.get("name");
		Arena arena = ArenaManager.arenas.get((String) map.get("arena"));
		GameType type = GameType.valueOf((String) map.get("type"));
		GameStatus status = GameStatus.valueOf((String) map.get("status"));
		return new Game(name, arena, status, type);
	}

}
