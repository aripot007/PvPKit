package fr.aripot007.pvpkit.util;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import fr.aripot007.pvpkit.PvPKit;
import fr.aripot007.pvpkit.game.Session;

public class Timer {

	private Duration time;
	private Long duration;
	private BukkitTask task;
	private BossBar bossbar;
	private Session session;
	
	public Timer(Session session, Long duration) {
		this.session = session;
		this.duration = duration;
		time = Duration.ofSeconds(duration);
		bossbar = Bukkit.createBossBar("Temps restant : ", BarColor.YELLOW, BarStyle.SOLID);
		bossbar.setVisible(false);
	}
	
	public boolean isRunning() {
		return (task != null ? !task.isCancelled() : false);
	}
	
	public void addPlayer(Player player) {
		bossbar.addPlayer(player);
	}
	
	public void removePlayer(Player player) {
		bossbar.removePlayer(player);
	}
	
	public void start() {
		
		if (isRunning())
			return;
		
		bossbar.setColor(BarColor.YELLOW);
		bossbar.setVisible(true);
		
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
		task = scheduler.runTaskTimer(PvPKit.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				
				// Update the time
				time = time.minusSeconds(1L);
				
				// Update the timer
				bossbar.setTitle(String.format("§6Temps restant : §a%02d:%02d", time.toMinutes(), time.toSecondsPart()));
				bossbar.setProgress(1.0d * time.getSeconds() / duration);
				
				// If we reach 0, stop the session
				if (time.getSeconds() <= 0) {
					
					session.stop(DateTimeFormatter.ofPattern("dd-MM HH-mm-ss").format(LocalDateTime.now()));
					
				}
				
			}
			
		}, 0L, 20L);
		
	}
	
	public Duration stop() {
		
		if(isRunning())
			task.cancel();
		
		task = null;
		
		return time;
		
	}
	
	public void pause() {
		stop();
		bossbar.setTitle(String.format("§6Partie en pause ! §9(Temps restant : §a%02d:%02d§9)", time.toMinutes(), time.toSecondsPart()));
		bossbar.setColor(BarColor.PURPLE);
	}
	
	public void reset() {

		stop();
		
		// Hide the bossbar
		bossbar.setVisible(false);
		
		time = Duration.ofSeconds(duration);
		
	}
	
	public void setVisible(boolean visible) {
		bossbar.setVisible(visible);
	}

	public Duration getTime() {
		return time;
	}

	public void setTime(Duration time) {
		this.time = time;
	}

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}
	
}
