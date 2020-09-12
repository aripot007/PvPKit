package fr.aripot007.pvpkit.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import fr.aripot007.pvpkit.PvPKit;
import net.md_5.bungee.api.chat.BaseComponent;

/**
 * A game that a player can join.
 * 
 * @author Aristide
 *
 */
@SerializableAs("Game")
public class Game implements ConfigurationSerializable {

	/** The arena in wich the game takes place */
	private Arena arena;
	
	/** The name of the game, used to identify it*/
	private String name;
	
	/** The status of the game. Used to determine if the game can be joined by players */
	private GameStatus status;
	
	/** The type of the game */
	private GameType type;
	
	/** The players currently in the game */
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
	
	/**
	 * Check if the game configuration is valid and if the game is ready for players to join.+
	 */
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

	
	/**
	 * Get the errors in the configuration of this game.
	 * Return an empty list if there is no error.
	 * @return A list of the errors.
	 */
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

	/** Serialize this game to a map */
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("name", name);
		result.put("arena", arena != null ? arena.getName() : null);
		result.put("type", type.toString());
		result.put("status", status.toString());
		return result;
	}
	
	/** Create a game from a map */
	public static Game deserialize(Map<String, Object> map) {
		String name = (String) map.get("name");
		Arena arena = PvPKit.getInstance().getArenaManager().getArena((String) map.get("arena"));
		GameType type = GameType.valueOf((String) map.get("type"));
		GameStatus status = GameStatus.valueOf((String) map.get("status"));
		return new Game(name, arena, status, type);
	}

	/** Send a message to all players in the game */
	public void sendMessage(String message) {
		for(PvPKitPlayer p : players) {
			p.getPlayer().sendMessage(message);
		}
	}
	
	/** Send a message from a spigot BaseComponent to all players in the game */
	public void sendMessage(BaseComponent component) {
		for(PvPKitPlayer p : players) {
			p.getPlayer().spigot().sendMessage(component);
		}
	}
	
}
