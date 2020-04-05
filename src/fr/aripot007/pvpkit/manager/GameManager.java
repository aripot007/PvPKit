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

public class GameManager {
	
	private Logger log = Bukkit.getPluginManager().getPlugin("PvPKit").getLogger();
	private File gameFile = new File(Bukkit.getPluginManager().getPlugin("PvPKit").getDataFolder(), "games.yml");
	private FileConfiguration gameData;
	public static Map<String, Game> games;

	public GameManager() {
		games = new HashMap<String, Game>();
		loadGames();
	}
	
	public void loadGames() {
		gameData = YamlConfiguration.loadConfiguration(gameFile);
		games.clear();
		for(String s : gameData.getKeys(false)) {
			games.put(s, (Game) gameData.get(s));
		}
	}
	
	public Game loadGame(String name) {
		return (Game) gameData.get(name);
	}
	
	public void saveGames() {
		for(Game game : games.values()) {
			gameData.set(game.getName(), null);
			gameData.set(game.getName(), game);
		}
		try {
			gameData.save(gameFile);
		} catch (IOException e) {
			log.severe("Erreur lors de la sauvegarde des jeux :");
			e.printStackTrace();
		}
	}
	
	public void putGame(Game game) {
		games.put(game.getName(), game);
	}

	public void removeGame(String name) {
		games.remove(name);
	}

	
}
