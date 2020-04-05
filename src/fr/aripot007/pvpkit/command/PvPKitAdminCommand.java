package fr.aripot007.pvpkit.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.aripot007.pvpkit.PvPKit;
import fr.aripot007.pvpkit.game.Kit;
import fr.aripot007.pvpkit.manager.KitManager;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class PvPKitAdminCommand implements CommandExecutor {

	private KitManager kitmg;
	
	public PvPKitAdminCommand(KitManager kitmg) {
		this.kitmg = kitmg;
	}
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		
		if(!(sender instanceof Player)) {
			sender.sendMessage("Cette commande ne peut être exécutée que par un joueur");
			return false;
		}
		
		Player p = (Player) sender;
		
		if(args.length == 0) {
			
			
			
		} else if(args[0].equalsIgnoreCase("kit")) {
			
			/*
			 * Commande /pka kit
			 */

			if(args.length == 1) {
					
				p.sendMessage("§e========[ §9/pka kit §e]========");
				p.sendMessage("§6Commandes disponibles :");
				p.sendMessage("§b/pka kit list");
				p.sendMessage("§b/pka kit create <nom>");
				p.sendMessage("§b/pka kit remove <kit>");
				p.sendMessage("§b/pka kit setinv <kit>");
				p.sendMessage("§b/pka kit seticon <kit> [nom]");
				p.sendMessage("§e========[ §9/pka kit §e]========");
				return true;
					
			} else if (args[1].equalsIgnoreCase("list")){
				
				p.sendMessage("§e========[ §9Kits §e]========");
				for(Kit kit : KitManager.kits.values()) {
					if(kit.isValid()) {
						p.sendMessage("§a"+kit.getName());
					} else {
						TextComponent txt = new TextComponent("§c"+kit.getName());
						String str = "";
						for(String s : kit.getErrors()) {
							str	+= "\n"+s;
						}
						str.replaceFirst("\n", "");
						txt.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(str).create()));
						p.sendMessage(txt.toString());
					}
				}
				p.sendMessage("§e========[ §9Kits §e]========");
				return true;	
				
			} else if (args[1].equalsIgnoreCase("create")) {
				
				if(args.length == 2) { // Pas de nom précisé
					p.sendMessage(PvPKit.prefix+"§cMerci de préciser un nom !");
					p.sendMessage(PvPKit.prefix+"§cSyntaxe : /pka kit create <nom>");
					
				} else if(args.length > 3) { // Trop d'arguments
					p.sendMessage(PvPKit.prefix+"§cLe nom du kit ne doit pas contenir d'espace !");
					
				} else {
					if(KitManager.kits.containsKey(args[2])) { // Nom déjà pris
						p.sendMessage(PvPKit.prefix+"§cUn kit avec le même nom existe déjà !");
					} else {
						kitmg.putKit(new Kit(args[2]));
						kitmg.saveKits();
						p.sendMessage(PvPKit.prefix+"§aKit §b"+args[2]+" créé avec succès !");
						p.sendMessage(PvPKit.prefix+"§6Définissez son contenu avec §2/pka kit setinv "+args[2]);
						p.sendMessage(PvPKit.prefix+"§6Définissez son icone avec §2/pka kit setinv "+args[2]+" [nom]");
					}
				}
				return true;
				
			} else if (args[1].equalsIgnoreCase("remove")) {
				if(args.length == 2) {
					p.sendMessage(PvPKit.prefix+"§cMerci de préciser un kit !");
				} else {
					if(KitManager.kits.containsKey(args[2])) {
						kitmg.removeKit(args[2]);
						kitmg.saveKits();
						p.sendMessage(PvPKit.prefix+"§aKit §b"+args[2]+" §asupprimé avec succès !");
					} else {
						p.sendMessage(PvPKit.prefix+"§cCe kit n'existe pas !");
					}
				}
				return true;
				
			} else if (args[1].equalsIgnoreCase("seticon")) {
				if(args.length == 2) {
					p.sendMessage(PvPKit.prefix+"§cMerci de préciser un kit !");
				} else if(KitManager.kits.containsKey(args[2])) {
					
					ItemStack icon = p.getInventory().getItemInMainHand();
					if(icon != null && !icon.getType().isAir()) {
						Kit kit = KitManager.kits.get(args[2]);
						ItemMeta meta = icon.getItemMeta();
						if(args.length > 3) {
							String name = "";
							for(int i = 3; i < args.length; i++) {
								name += args[i];
								if(i+1<args.length)
									name += " ";
							}
							meta.setDisplayName(name.replaceAll("&", "§"));
						} else {
							meta.setDisplayName("§b"+kit.getName());
						}
						icon.setItemMeta(meta);
						kit.setIcon(icon);
						kitmg.putKit(kit);
						kitmg.saveKits();
						p.sendMessage(PvPKit.prefix+"§aIcone du kit §b"+kit.getName()+" §adéfinie avec succès !");
						
					} else {
						p.sendMessage(PvPKit.prefix+"§cVous devez tenir un item dans votre main !");
					}
				} else {
					p.sendMessage(PvPKit.prefix+"§cCe kit n'existe pas !");
				}
				
				
			} else if (args[1].equalsIgnoreCase("setinv")) {
				if(args.length == 2) {
					p.sendMessage(PvPKit.prefix+"§cMerci de préciser un kit !");
				} else if(KitManager.kits.containsKey(args[2])) {

					Kit kit = KitManager.kits.get(args[2]);
					kit.setInventoryContent(p.getInventory().getContents());
					kitmg.putKit(kit);
					kitmg.saveKits();
					p.sendMessage(PvPKit.prefix+"§aContenu du kit §b"+kit.getName()+" §adéfinie avec succès !");

				} else {
					p.sendMessage(PvPKit.prefix+"§cCe kit n'existe pas !");
				}

			} else {
				p.sendMessage(PvPKit.prefix+"§cCommande inconnue !");
				p.sendMessage(PvPKit.prefix + "§cPour une liste des commandes disponibles, entrez &b/pka kit");
			}
		
		} else if(args[0].equalsIgnoreCase("arena")) {
			
			
		} else {
			sender.sendMessage(PvPKit.prefix+"§cCommande inconnue !");
			sender.sendMessage(PvPKit.prefix + "§cPour une liste des commandes disponibles, entrez &b/pka help");
			return false;
		}
		return false;
	}
}
