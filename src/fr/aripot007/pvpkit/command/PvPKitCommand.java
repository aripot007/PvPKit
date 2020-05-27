package fr.aripot007.pvpkit.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.aripot007.pvpkit.GameController;
import fr.aripot007.pvpkit.PvPKit;
import fr.aripot007.pvpkit.game.Game;
import fr.aripot007.pvpkit.game.GameStatus;
import fr.aripot007.pvpkit.game.PvPKitPlayer;
import fr.aripot007.pvpkit.manager.GameManager;
import fr.aripot007.pvpkit.manager.PvPKitPlayerManager;

public class PvPKitCommand implements CommandExecutor {
	
	PvPKitPlayerManager playerManager = PvPKit.getInstance().getPvPKitPlayerManager();
	GameManager gameManager = PvPKit.getInstance().getGameManager();
	GameController controller = PvPKit.getInstance().getGameController();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		
		if(!(sender instanceof Player)) {
			sender.sendMessage("Cette commande ne peut être exécutée que par un joueur");
			return true;
		}
		
		Player p = (Player) sender;
		PvPKitPlayer player = playerManager.getPlayer(p);
		
		if(args.length > 0){
			
			if(args[0].equalsIgnoreCase("join")){
				
				return onJoinCommand(player, cmd, msg, args);
				
			} else if(args[0].equalsIgnoreCase("leave")){
				
				return onLeaveCommand(player, cmd, msg, args);
				
			} else if(args[0].equalsIgnoreCase("list")) {
				
				return onListCommand(p, cmd, msg, args);
				
			} else if(args[0].equalsIgnoreCase("stats")) {
				
				p.sendMessage("§e====[ §9PvPKit - Stats §e]====");
				p.sendMessage("\n §6Kills : §b"+player.getKills());
				p.sendMessage(" §6Morts : §b"+player.getDeaths());
				p.sendMessage(" §6Ratio : §b"+1.0f * player.getKills() / (player.getDeaths() != 0 ? player.getDeaths() : 1));
				p.sendMessage(" §6Killstreak : §b"+player.getKillstreak());
				p.sendMessage(" §6Meilleur killstreak : §b"+player.getBestKillStreak());
				p.sendMessage("\n§e====[ §9PvPKit - Stats §e]====");
				return true;
				
			} else {
				p.sendMessage(PvPKit.prefix+"§cCommande inconnue !");
				p.sendMessage("§6Commandes disponibles : §b/pk join, leave, list, stats§e.");
				return false;
			}
		
		} else {
			p.sendMessage(PvPKit.prefix+"§cMerci de préciser un argument !");
			p.sendMessage("§6Commandes disponibles : §b/pk join, leave, list, stats§e.");
			return false;
		}
	}
	
	private boolean onJoinCommand(PvPKitPlayer player, Command cmd, String msg, String[] args) {
		
		if(player.isInGame()) {
			player.getPlayer().sendMessage(PvPKit.prefix+"§cVous êtes déjà dans une partie !");
			return false;
		} else {
			if(args.length < 2) {
				PvPKit.getInstance().getGameMenuManager().openMenu(player.getPlayer());
				return false;
			} else {
				
				if(gameManager.containsGame(args[1])) {
					
					Game game = gameManager.getGame(args[1]);
					
					if(!game.isValid()) {
						player.getPlayer().sendMessage(PvPKit.prefix+"§cCette partie n'est pas valide !");
						return true;
					} else if(!game.getStatus().equals(GameStatus.OPEN)) {
						player.getPlayer().sendMessage(PvPKit.prefix+"§cCette partie n'est pas disponible !");
						return true;
					} else {
						controller.joinGame(player, game);
						return true;
					}
					
				} else {
					player.getPlayer().sendMessage(PvPKit.prefix+"§cCette partie n'existe pas !");
					return true;
				}
			}
		}
	}
	
	private boolean onLeaveCommand(PvPKitPlayer player, Command cmd, String msg, String[] args) {
		if(!player.isInGame()) {
			player.getPlayer().sendMessage(PvPKit.prefix+"§cVous n'êtes pas dans une partie !");
			return true;
		} else {
			controller.leaveGame(player);
			return true;
		}
	}

	private boolean onListCommand(Player player, Command cmd, String msg, String[] args) {
		player.sendMessage("§e========[ §9Parties disponibles §e]========");
		for(Game g : gameManager.getGames().values()) {
			if(g.isValid())
				player.sendMessage("  §b"+g.getName());
		}
		player.sendMessage("§e========[ §9Parties disponibles §e]========");
		return true;
	}
	
}
