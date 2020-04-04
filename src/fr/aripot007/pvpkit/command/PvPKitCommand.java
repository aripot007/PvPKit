package fr.aripot007.pvpkit.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class PvPKitCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		
		if(args.length > 0){
			
			if(args[0].equalsIgnoreCase("join")){
				//TODO Commande join
				
			} else if(args[0].equalsIgnoreCase("leave")){
				//TODO Commande leave
				
			} else if(args[0].equalsIgnoreCase("")) {
				
			} else if(args[0].equalsIgnoreCase("")) {
				
			}
			
		} else {
			
		}
		return false;
	}

}
