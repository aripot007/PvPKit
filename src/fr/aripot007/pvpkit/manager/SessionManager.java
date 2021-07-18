package fr.aripot007.pvpkit.manager;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import fr.aripot007.pvpkit.game.Game;
import fr.aripot007.pvpkit.game.GameStatus;
import fr.aripot007.pvpkit.game.Session;

public class SessionManager {

	private Logger log;
	private File sessionsFile;
	private FileConfiguration sessionsData;
	private Map<String, Session> sessions;
	// Maps a game to its session
	private Map<String, Session> sessionsGame;


	public SessionManager() {
		log = Bukkit.getPluginManager().getPlugin("PvPKit").getLogger();
		sessionsFile = new File(Bukkit.getPluginManager().getPlugin("PvPKit").getDataFolder(), "sessions.yml");
		sessions = new HashMap<String, Session>();
		sessionsGame = new HashMap<String, Session>();
		loadSessions();
	}
	
	/**
	 * Load all the games from the sessions config file
	 */
	public void loadSessions() {
		sessionsData = YamlConfiguration.loadConfiguration(sessionsFile);
		sessions.clear();
		for(String s : sessionsData.getKeys(false)) {
			sessions.put(s, (Session) sessionsData.get(s));
		}
		
		for (Session sess : sessions.values()) {
			if (sess.getGame() != null)
				sessionsGame.put(sess.getGame().getName(), sess);
		}
		
	}
	
	/**
	 * Load a session from the sessions config file
	 */
	public Session loadSession(String name) {
		return (Session) sessionsData.get(name);
	}
	
	/**
	 * Save all the sessions to the sessions config file
	 */
	public void saveSessions() {
		for(Session session : sessions.values()) {
			sessionsData.set(session.getName(), null);
			sessionsData.set(session.getName(), session);
		}
		try {
			sessionsData.save(sessionsFile);
		} catch (IOException e) {
			log.severe("Erreur lors de la sauvegarde des sessions :");
			e.printStackTrace();
		}
	}
	
	/**
	 * Get a session by its name
	 * @param name The name of the session
	 */
	public Session getSession(String name) {
		return sessions.get(name);
	}
	
	/**
	 * Get the session associated to a game
	 * @param game
	 * @return
	 */
	public Session getSession(Game game) {
		return sessionsGame.get(game.getName());
	}
	
	public Map<String, Session> getSessions() {
		return sessions;
	}
	
	public boolean containsSession(String name) {
		return sessions.containsKey(name);
	}
	
	public void putSession(Session session) {
		sessions.put(session.getName(), session);
	}

	public void removeSession(String name) {
		Session session = getSession(name);
		session.getGame().setStatus(GameStatus.CONFIG);
		sessions.remove(name);
	}

}
