package fr.aripot007.pvpkit.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.aripot007.pvpkit.GameController;
import fr.aripot007.pvpkit.PvPKit;
import fr.aripot007.pvpkit.manager.SessionPlayerManager;
import fr.aripot007.pvpkit.util.Timer;

@SerializableAs("Session")
public class Session implements ConfigurationSerializable {
	
	private static final Random random = new Random();

	private String name;
	
	private Long duration;
	
	private Timer timer;

	private Game game;
	
	private SessionStatus status;
	
	/** A list of the uuids of the allowed players */
	private List<String> allowedPlayers;
	
	private SessionPlayerManager playerManager;
	
	public Session() {
		playerManager = new SessionPlayerManager(this);
		status = SessionStatus.ERROR;
		duration = 0L;
		timer= new Timer(this, 0L);
	}
	
	public Session(String name) {
		this.name = name;
		this.status = SessionStatus.ERROR;
		this.allowedPlayers = new ArrayList<String>();
		timer = new Timer(this, 0L);
		this.playerManager = new SessionPlayerManager(this);
	}
	
	public Session(String name, Game game, List<String> allowedPlayers, Long duration) {
		this.name = name;
		this.game = game;
		this.duration = duration;
		this.allowedPlayers = allowedPlayers;
		this.timer = new Timer(this, duration);
		this.playerManager = new SessionPlayerManager(this);
		isValid();
	}

	/**
	 * Reset the stats of every players in the session.
	 */
	public void resetStats() {
		playerManager.resetStats();
	}
	
	public void saveStats(String filename) {
		playerManager.saveStats(filename);
	}
	
	public void start() {
		
		switch (status) {
		
		case STARTED:
			break;
		
		case READY:
			
			// Reset the stats
			resetStats();
			
			// Reset the timer
			timer.reset();
			
			// Teleport every players waiting to the spawn
			// Make them leave and join again in order to be teleported
			for (int i = 0; i < game.getPlayers().size(); i++) {
				
				PvPKitPlayer player = playerManager.getPlayer(game.getPlayers().get(i).getPlayer());
				
				Player p = player.getPlayer();
				
				p.teleport(game.getArena().getSpawn());
				
				for (PotionEffect effect : p.getActivePotionEffects())
				    p.removePotionEffect(effect.getType());
				p.setHealth(20d);
				
				// Give a random kit to the player
				GameController.safeGiveKit(player, getRandomKit(game.getArena()));
			}
			
			// If the session has a duration, start the timer
			if (duration != 0)
				timer.start();
			
			status = SessionStatus.STARTED;
			game.sendMessage(PvPKit.prefix+"§6La session vient de commencer !");
			break;
			
		case PAUSED:
			
			pause();
		
		default:
			break;
		}
		
	}
	
	public Kit getRandomKit(Arena arena) {
		
		return PvPKit.getInstance().getKitManager().getKit(arena.getKits().get(random.nextInt(arena.getKits().size())));
		
	}
	
	public void stop(String filename) {
		
		status = SessionStatus.READY;
		
		// If the session has a duration, stop and reset the timer
		if (duration != 0)
			timer.reset();
		
		// Sauvegarde des résultats
		playerManager.saveStats(filename);
		
		
		for (int i = 0; i < game.getPlayers().size(); i++) {
			
			PvPKitPlayer p = game.getPlayers().get(i);
			PvPKitPlayer sessionPlayer = playerManager.getPlayer(p.getPlayer());
			
			sessionPlayer.getPlayer().sendMessage(PvPKit.prefix+"§c§lLa session est terminée !\n \n"+
					"§a==========[ §bRésultats §a]==========\n \n"+
					"§6Kills : §3"+sessionPlayer.getKills()+
					"\n§6Morts : §3"+sessionPlayer.getDeaths()+
					"\n§6Meilleur killstreak : §3"+sessionPlayer.getBestKillStreak()+
					"\n \n§a==========[ §bRésultats §a]==========");
			
			PvPKit.getInstance().getGameController().leaveGame(p);
			i--;
			
		}
	}
	
	public void pause() {
		
		switch (status) {
		
		case STARTED:
			status = SessionStatus.PAUSED;
			game.sendMessage(PvPKit.prefix+"§6La session a été mise en pause !");
			
			// If the session has a duration, stop the timer
			if (duration != 0)
				timer.pause();
			
			// Give potion effects to all players
			for (PvPKitPlayer p : game.getPlayers()) {
				p.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 254, false, false));
				p.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 254, false, false));
				p.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 249, false, false));
			}
			
			break;
			
		case PAUSED:
			status = SessionStatus.STARTED;
			
			// Remove potion effects from all players
			for (PvPKitPlayer p : game.getPlayers()) {
				p.getPlayer().removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
				p.getPlayer().removePotionEffect(PotionEffectType.SLOW);
				p.getPlayer().removePotionEffect(PotionEffectType.JUMP);
			}
			
			// If the session has a duration, stop the timer
			if (duration != 0)
				timer.start();
			
			game.sendMessage(PvPKit.prefix+"§6La session vient de reprendre !");
			break;
		
		default:
			break;
		
		}
		
	}
	
	/**
	 * Reset the session to its initial state
	 */
	public void reset() {
		resetStats();
		timer.reset();
		status = SessionStatus.READY;
		isValid();
	}
	
	public void delete() {
		if (this.game != null)
			this.game.setStatus(GameStatus.CONFIG);
	}
	
	public boolean isValid() {
		if(game != null && game.isValid()) {
			if(this.status == null || this.status.equals(SessionStatus.ERROR))
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
		result.put("duration", duration);
		return result;
	}
	
	public static Session deserialize(Map<String, Object> map) {
		String name = (String) map.get("name");
		Game game = PvPKit.getInstance().getGameManager().getGame((String) map.get("game"));
		@SuppressWarnings("unchecked")
		List<String> players = (List<String>) map.get("players");
		
		Long duration = Long.parseLong(map.getOrDefault("duration", 0).toString());
		
		// Make sure the game has the SESSION status
		game.setStatus(GameStatus.SESSION);
		
		return new Session(name, game, players, duration);
	}
	
	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
		timer.setDuration(duration);
		timer.reset();
	}

	public Timer getTimer() {
		return timer;
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
