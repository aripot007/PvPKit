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

/**
 * Manages the games.
 * 
 * Used to load and save games
 * @author Aristide
 *
 */
public class GameManager {
	
	private Logger log;
	private File gameFile;
	private FileConfiguration gameData;
	private Map<String, Game> games;

	public GameManager() {
		log = Bukkit.getPluginManager().getPlugin("PvPKit").getLogger();
		gameFile = new File(Bukkit.getPluginManager().getPlugin("PvPKit").getDataFolder(), "games.yml");
		games = new HashMap<String, Game>();
		loadGames();
	}
	
	/**
	 * Load all the games from the games config file
	 */
	public void loadGames() {
		gameData = YamlConfiguration.loadConfiguration(gameFile);
		games.clear();
		for(String s : gameData.getKeys(false)) {
			games.put(s, (Game) gameData.get(s));
		}
	}
	
	/**
	 * Load a game from the games config file
	 */
	public Game loadGame(String name) {
		return (Game) gameData.get(name);
	}
	
	/**
	 * Save all the games to the games config file
	 */
	public void saveGames() {
		for(Game game : games.values()) {
			gameData.set(game.getName(), null);
			gameData.set(game.getName(), game);
		}
		try {
			gameData.save(gameFile);
		} catch (IOException e) {
			log.severe("Erreur lors de la sauvegarde des parties :");
			e.printStackTrace();
		}
	}
	
	public Game getGame(String name) {
		return games.get(name);
	}
	
	public Map<String, Game> getGames() {
		return games;
	}
	
	public boolean containsGame(String name) {
		return games.containsKey(name);
	}
	
	public void putGame(Game game) {
		games.put(game.getName(), game);
	}

	public void removeGame(String name) {
		games.remove(name);
	}

	
}
