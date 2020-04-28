package fr.aripot007.pvpkit.manager;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import fr.aripot007.pvpkit.PvPKit;
import fr.aripot007.pvpkit.game.PvPKitPlayer;

public class StatsScoreboardManager {
	
	ScoreboardManager mgr = Bukkit.getScoreboardManager();
	
	public void showScoreboard(PvPKitPlayer pkp) {
		
		Scoreboard b = mgr.getNewScoreboard();
		
		Objective o = b.registerNewObjective("PKKills", "", "§6§lPvP Kit");
		o.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		o.getScore("§r§7===============").setScore(11);
		o.getScore("§e Kills : §b"+pkp.getKills()).setScore(10);
		o.getScore("§e Morts : §b"+pkp.getDeaths()).setScore(9);
		o.getScore("§e Ratio : §b"+1.0f * pkp.getKills() / (pkp.getDeaths() != 0 ? pkp.getDeaths() : 1)).setScore(8);
		o.getScore("§e Killstreak : §b"+pkp.getKillstreak()).setScore(7);
		o.getScore("§r§r§7===============").setScore(6);
		o.getScore("§e Kit : §b"+(pkp.getKit() != null ? pkp.getKit().getName() : "§cAucun")).setScore(5);
		o.getScore("§e Arène : §b"+PvPKit.getInstance().getGameController().getGame(pkp).getName()).setScore(4);
		o.getScore("§e Mode : §b"+PvPKit.getInstance().getGameController().getGame(pkp).getType().toString()).setScore(3);
		o.getScore("§7===============").setScore(2);
		o.getScore("§6§l    MJC Craft").setScore(1);
		
		pkp.getPlayer().setScoreboard(b);
		
		return;
		
	}
	
	public void hideScoreboard(PvPKitPlayer p) {
		p.getPlayer().setScoreboard(mgr.getNewScoreboard());
	}

}
