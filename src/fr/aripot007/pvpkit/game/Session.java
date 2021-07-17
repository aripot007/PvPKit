package fr.aripot007.pvpkit.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import fr.aripot007.pvpkit.PvPKit;
import fr.aripot007.pvpkit.manager.SessionPlayerManager;

@SerializableAs("Session")
public class Session implements ConfigurationSerializable {

	private String name;

	private Game game;
	
	private SessionStatus status;
	
	/** A list of the uuids of the allowed players */
	private List<String> allowedPlayers;
	
	private SessionPlayerManager playerManager;
	
	public Session() {
		playerManager = new SessionPlayerManager(this);
		status = SessionStatus.ERROR;
	}
	
	public Session(String name) {
		this.name = name;
		this.status = SessionStatus.ERROR;
		this.allowedPlayers = new ArrayList<String>();
		this.playerManager = new SessionPlayerManager(this);
	}
	
	public Session(String name, Game game, List<String> allowedPlayers) {
		this.name = name;
		this.game = game;
		this.allowedPlayers = allowedPlayers;
		this.playerManager = new SessionPlayerManager(this);
		isValid();
	}

	/**
	 * Reset the stats of every players in the session.
	 */
	public void resetStats() {
		playerManager.resetStats();
	}
	
	public void start() {
		
		switch (status) {
		
		case STARTED:
			break;
		
		case READY:
			
			// Teleport every players waiting to the spawn
			// Make them leave and join again in order to be teleported
			// TODO: Give random kits and spread players in the arena ?
			for (PvPKitPlayer p : playerManager.getPlayers().values()) {
				PvPKit.getInstance().getGameController().leaveGame(p);
				PvPKit.getInstance().getGameController().joinGame(p, game);
			}
			status = SessionStatus.STARTED;
			game.sendMessage(PvPKit.prefix+"§aLa session vient de commencer !");
			break;
			
		case PAUSED:
			
			status = SessionStatus.STARTED;
			game.sendMessage(PvPKit.prefix+"§aLa session vient de reprendre !");
		
		default:
			break;
		}
		
	}
	
	public void stop() {
		status = SessionStatus.READY;
		for (PvPKitPlayer p : playerManager.getPlayers().values()) {
			PvPKit.getInstance().getGameController().leaveGame(p);
			p.getPlayer().sendMessage(PvPKit.prefix+"§c§lLa session est terminée !\n"+
													"§6 Résultats :");
			
			// TODO : Donner les résultats (classement + score perso)
			
		}
	}
	
	public void pause() {
		
	}
	
	/**
	 * Reset the session to its initial state
	 */
	public void reset() {
		resetStats();
		status = SessionStatus.READY;
		isValid();
	}
	
	public void delete() {
		if (this.game != null)
			this.game.setStatus(GameStatus.CONFIG);
	}
	
	public boolean isValid() {
		if(game != null && game.isValid()) {
			if(this.status.equals(SessionStatus.ERROR))
				this.status = SessionStatus.READY;
			return true;
		} else {
			this.status = SessionStatus.ERROR;
			return false;
		}
	}

	/**
	 * Get the errors in the configuration of this session.
	 * Return an empty list if there is no error.
	 * @return A list of the errors.
	 */
	public List<String> getErrors() {
		List<String> errors = new ArrayList<String>();
		if(game == null)
			errors.add("La session n'est liée à aucune partie");
		else if(!game.isValid())
			errors.add("La session "+game.getName()+" n'est pas valide");
		return errors;
	}
	
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("name", name);
		result.put("game", game != null ? game.getName() : null);
		result.put("players", allowedPlayers);
		return result;
	}
	
	public static Session deserialize(Map<String, Object> map) {
		String name = (String) map.get("name");
		Game game = PvPKit.getInstance().getGameManager().getGame((String) map.get("game"));
		List<String> players = (List<String>) map.get("players");
		return new Session(name, game, players);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		// Reset the old game if there is any
		if (this.game != null) {
			this.game.setStatus(GameStatus.CONFIG);
		}
		this.game = game;
		
		if (game != null)
			game.setStatus(GameStatus.SESSION);
		
	}

	public SessionStatus getStatus() {
		return status;
	}

	public void setStatus(SessionStatus status) {
		this.status = status;
	}

	public SessionPlayerManager getPlayerManager() {
		return playerManager;
	}

	public void setPlayerManager(SessionPlayerManager playerManager) {
		this.playerManager = playerManager;
	}

	public List<String> getAllowedPlayers() {
		return allowedPlayers;
	}

	public void setAllowedPlayers(List<String> allowedPlayers) {
		this.allowedPlayers = allowedPlayers;
	}
	
	public void allowPlayer(String uuid) {
		allowedPlayers.add(uuid);
	}
	
	public void disallowPlayer(String uuid) {
		if (uuid.equals("all")) {
			allowedPlayers.clear();
		} else {
			allowedPlayers.remove(uuid);
		}
	}
	
}
