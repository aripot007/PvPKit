package fr.aripot007.pvpkit.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.aripot007.pvpkit.PvPKit;
import fr.aripot007.pvpkit.game.Arena;
import fr.aripot007.pvpkit.game.Game;
import fr.aripot007.pvpkit.game.GameStatus;
import fr.aripot007.pvpkit.game.GameType;
import fr.aripot007.pvpkit.game.Kit;
import fr.aripot007.pvpkit.manager.ArenaManager;
import fr.aripot007.pvpkit.manager.GameManager;
import fr.aripot007.pvpkit.manager.KitManager;
import fr.aripot007.pvpkit.util.Messages;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class PvPKitAdminCommand implements CommandExecutor {

	private KitManager kitmg;
	private ArenaManager armg;
	private GameManager gamemg;
	
	public PvPKitAdminCommand(KitManager kitmg, ArenaManager armg, GameManager gamemg) {
		this.kitmg = kitmg;
		this.armg = armg;
		this.gamemg = gamemg;
	}
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		
		if(!(sender instanceof Player)) {
			sender.sendMessage(Messages.getString("errors.cmd.player_only")); //$NON-NLS-1$
			return false;
		}
		
		if(args.length == 0) {
			
		} else if(args[0].equalsIgnoreCase("kit")) { //$NON-NLS-1$
		
			return onKitCommand(sender, cmd, msg, args);
			
		} else if(args[0].equalsIgnoreCase("arena")) { //$NON-NLS-1$
			
			return onArenaCommand(sender, cmd, msg, args);
			
		} else if(args[0].equalsIgnoreCase("game")) {
			
			return onGameCommand(sender, cmd, msg, args);
			
		} else {
			sender.sendMessage(PvPKit.prefix+Messages.getString("errors.cmd.unknown")); //$NON-NLS-1$
			sender.sendMessage(PvPKit.prefix + Messages.getString("errors.cmd.cmd_list")); //$NON-NLS-1$
			return false;
		}
		return false;
	}
	
	private boolean onKitCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		Player p = (Player) sender;
		if(args.length == 1) {
			
			p.sendMessage("§e========[ §9/pka kit §e]========"); //$NON-NLS-1$
			p.sendMessage("§6Commandes disponibles :"); //$NON-NLS-1$
			p.sendMessage("§b/pka kit list"); //$NON-NLS-1$
			p.sendMessage("§b/pka kit create <nom>"); //$NON-NLS-1$
			p.sendMessage("§b/pka kit remove <kit>"); //$NON-NLS-1$
			p.sendMessage("§b/pka kit setinv <kit>"); //$NON-NLS-1$
			p.sendMessage("§b/pka kit seticon <kit> [nom]"); //$NON-NLS-1$
			p.sendMessage("§e========[ §9/pka kit §e]========"); //$NON-NLS-1$
			return true;
				
		} else if (args[1].equalsIgnoreCase("list")){ //$NON-NLS-1$
			
			p.sendMessage("§e========[ §9Kits §e]========"); //$NON-NLS-1$
			for(Kit kit : KitManager.kits.values()) {
				if(kit.isValid()) {
					p.sendMessage("§a"+kit.getName()); //$NON-NLS-1$
				} else {
					TextComponent txt = new TextComponent("§c"+kit.getName()); //$NON-NLS-1$
					String str = ""; //$NON-NLS-1$
					for(String s : kit.getErrors()) {
						str	+= "\n§c"+s; //$NON-NLS-1$
					}
					str.replaceFirst("\n", ""); //$NON-NLS-1$ //$NON-NLS-2$
					txt.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(str).create()));
					p.sendMessage(txt.toString());
				}
			}
			p.sendMessage("§e========[ §9Kits §e]========"); //$NON-NLS-1$
			return true;	
			
		} else if (args[1].equalsIgnoreCase("create")) { //$NON-NLS-1$
			
			if(args.length == 2) { // Pas de nom précisé
				p.sendMessage(PvPKit.prefix+Messages.getString("errors.cmd.arg.no_name")); //$NON-NLS-1$
				p.sendMessage(PvPKit.prefix+"§cSyntaxe : /pka kit create <nom>"); //$NON-NLS-1$
				
			} else if(args.length > 3) { // Trop d'arguments
				p.sendMessage(PvPKit.prefix+Messages.getString("errors.cmd.kit.no_space_in_name")); //$NON-NLS-1$
				
			} else {
				if(KitManager.kits.containsKey(args[2])) { // Nom déjà pris
					p.sendMessage(PvPKit.prefix+Messages.getString("errors.cmd.kit.name_already_exist")); //$NON-NLS-1$
				} else {
					kitmg.putKit(new Kit(args[2]));
					kitmg.saveKits();
					p.sendMessage(PvPKit.prefix+"§aKit §b"+args[2]+" créé avec succès !"); //$NON-NLS-1$ //$NON-NLS-2$
					p.sendMessage(PvPKit.prefix+"§6Définissez son contenu avec §2/pka kit setinv "+args[2]); //$NON-NLS-1$
					p.sendMessage(PvPKit.prefix+"§6Définissez son icone avec §2/pka kit seticon "+args[2]+" [nom]"); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
			return true;
			
		} else if (args[1].equalsIgnoreCase("remove")) { //$NON-NLS-1$
			if(args.length == 2) {
				p.sendMessage(PvPKit.prefix+Messages.getString("errors.cmd.arg.no_kit")); //$NON-NLS-1$
			} else {
				if(KitManager.kits.containsKey(args[2])) {
					kitmg.removeKit(args[2]);
					kitmg.saveKits();
					p.sendMessage(PvPKit.prefix+"§aKit §b"+args[2]+" §asupprimé avec succès !"); //$NON-NLS-1$ //$NON-NLS-2$
				} else {
					p.sendMessage(PvPKit.prefix+Messages.getString("errors.cmd.kit.kit_does_not_exist")); //$NON-NLS-1$
				}
			}
			return true;
			
		} else if (args[1].equalsIgnoreCase("seticon")) { //$NON-NLS-1$
			if(args.length == 2) {
				p.sendMessage(PvPKit.prefix+Messages.getString("errors.cmd.arg.no_kit")); //$NON-NLS-1$
			} else if(KitManager.kits.containsKey(args[2])) {
				
				ItemStack icon = p.getInventory().getItemInMainHand();
				if(icon != null && !icon.getType().isAir()) {
					Kit kit = KitManager.kits.get(args[2]);
					ItemMeta meta = icon.getItemMeta();
					if(args.length > 3) {
						String name = ""; //$NON-NLS-1$
						for(int i = 3; i < args.length; i++) {
							name += args[i];
							if(i+1<args.length)
								name += " "; //$NON-NLS-1$
						}
						meta.setDisplayName(name.replaceAll("&", "§")); //$NON-NLS-1$ //$NON-NLS-2$
					} else {
						meta.setDisplayName("§b"+kit.getName()); //$NON-NLS-1$
					}
					icon.setItemMeta(meta);
					kit.setIcon(icon);
					kitmg.putKit(kit);
					kitmg.saveKits();
					p.sendMessage(PvPKit.prefix+"§aIcone du kit §b"+kit.getName()+" §adéfinie avec succès !"); //$NON-NLS-1$ //$NON-NLS-2$
					
				} else {
					p.sendMessage(PvPKit.prefix+Messages.getString("errors.cmd.kit.icon.no_item_in_hand")); //$NON-NLS-1$
				}
			} else {
				p.sendMessage(PvPKit.prefix+Messages.getString("errors.cmd.kit.kit_does_not_exist")); //$NON-NLS-1$
			}
			
			
		} else if (args[1].equalsIgnoreCase("setinv")) { //$NON-NLS-1$
			if(args.length == 2) {
				p.sendMessage(PvPKit.prefix+Messages.getString("errors.cmd.arg.no_kit")); //$NON-NLS-1$
			} else if(KitManager.kits.containsKey(args[2])) {

				Kit kit = KitManager.kits.get(args[2]);
				kit.setInventoryContent(p.getInventory().getContents());
				kitmg.putKit(kit);
				kitmg.saveKits();
				p.sendMessage(PvPKit.prefix+"§aContenu du kit §b"+kit.getName()+" §adéfinie avec succès !"); //$NON-NLS-1$ //$NON-NLS-2$

			} else {
				p.sendMessage(PvPKit.prefix+Messages.getString("errors.cmd.kit.kit_does_not_exist")); //$NON-NLS-1$
			}

		} else {
			p.sendMessage(PvPKit.prefix+Messages.getString("errors.cmd.unknown")); //$NON-NLS-1$
			p.sendMessage(PvPKit.prefix + "§cPour une liste des commandes disponibles, entrez &b/pka kit"); //$NON-NLS-1$
			return false;
		}
		return false;
	}
	
	private boolean onArenaCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		Player p = (Player) sender;
		
		if(args.length < 2) { // Aide
			
			p.sendMessage("§e========[ §9/pka arena §e]========"); //$NON-NLS-1$
			p.sendMessage("§6Commandes disponibles :"); //$NON-NLS-1$
			p.sendMessage("§b/pka arena list"); //$NON-NLS-1$
			p.sendMessage("§b/pka arena create <nom>"); //$NON-NLS-1$
			p.sendMessage("§b/pka arena remove <arena>"); //$NON-NLS-1$
			p.sendMessage("§b/pka arena setspawn <arena>"); //$NON-NLS-1$
			p.sendMessage("§b/pka arena listkit"); //$NON-NLS-1$
			p.sendMessage("§b/pka arena addkit"); //$NON-NLS-1$
			p.sendMessage("§b/pka arena removekit"); //$NON-NLS-1$
			p.sendMessage("§e========[ §9/pka arena §e]========"); //$NON-NLS-1$
			
		} else if (args[1].equalsIgnoreCase("list")){ // list //$NON-NLS-1$
			
			p.sendMessage("§e========[ §9Arènes §e]========"); //$NON-NLS-1$
			for(Arena ar : ArenaManager.arenas.values()) {
				if(ar.isValid()) {
					p.sendMessage("§a"+ar.getName()); //$NON-NLS-1$
				} else {
					TextComponent txt = new TextComponent("§c"+ar.getName()); //$NON-NLS-1$
					String str = ""; //$NON-NLS-1$
					for(String s : ar.getErrors()) {
						str	+= "\n§c"+s; //$NON-NLS-1$
					}
					str.replaceFirst("\n", ""); //$NON-NLS-1$ //$NON-NLS-2$
					txt.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(str).create()));
					p.sendMessage(txt.toString());
				}
			}
			p.sendMessage("§e========[ §9Arènes §e]========"); //$NON-NLS-1$
			return true;	
			
		} else if (args[1].equalsIgnoreCase("create")) { // Create //$NON-NLS-1$
			
			if(args.length == 2) { // Pas de nom précisé
				p.sendMessage(PvPKit.prefix+Messages.getString("errors.cmd.arg.no_name")); //$NON-NLS-1$
				p.sendMessage(PvPKit.prefix+"§cSyntaxe : /pka arena create <nom>"); //$NON-NLS-1$
				
			} else if(args.length > 3) { // Trop d'arguments
				p.sendMessage(PvPKit.prefix+Messages.getString("errors.cmd.arena.no_space_in_name")); //$NON-NLS-1$
				
			} else {
				if(ArenaManager.arenas.containsKey(args[2])) { // Nom déjà pris
					p.sendMessage(PvPKit.prefix+Messages.getString("errors.cmd.arena.name_already_exists")); //$NON-NLS-1$
				} else {
					armg.putArena(new Arena(args[2]));
					armg.saveArenas();
					p.sendMessage(PvPKit.prefix+"§aArène §b"+args[2]+" créé avec succès !"); //$NON-NLS-1$ //$NON-NLS-2$
					p.sendMessage(PvPKit.prefix+"§6Définissez son spawn avec §2/pka arena setspawn "+args[2]); //$NON-NLS-1$
					p.sendMessage(PvPKit.prefix+"§6Désactivez ou activez des kits avec §b/pka arena addkit|removekit "+args[2]+" <kit>"); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
			return true;
			
		} else if (args[1].equalsIgnoreCase("remove")) { //$NON-NLS-1$
			if(args.length == 2) {
				p.sendMessage(PvPKit.prefix+Messages.getString("errors.cmd.arg.no_arena")); //$NON-NLS-1$
			} else {
				if(ArenaManager.arenas.containsKey(args[2])) {
					armg.removeArena(args[2]);
					armg.saveArenas();
					p.sendMessage(PvPKit.prefix+"§aArène §b"+args[2]+" §asupprimée avec succès !"); //$NON-NLS-1$ //$NON-NLS-2$
				} else {
					p.sendMessage(PvPKit.prefix+Messages.getString("errors.cmd.arena.arena_does_not_exist")); //$NON-NLS-1$
				}
			}
			return true;
			
		} else if (args[1].equalsIgnoreCase("setspawn")) { //$NON-NLS-1$
			if(args.length < 3) {
				p.sendMessage(PvPKit.prefix+Messages.getString("errors.cmd.arg.no_arena")); //$NON-NLS-1$
			} else {
				if(ArenaManager.arenas.containsKey(args[2])) {
					Arena arena = ArenaManager.arenas.get(args[3]);
					arena.setSpawn(p.getLocation());
					armg.putArena(arena);
					armg.saveArenas();
					p.sendMessage(PvPKit.prefix+"§aSpawn de l'arène §b"+args[2]+" §adéfini avec succès !"); //$NON-NLS-1$ //$NON-NLS-2$
				} else {
					p.sendMessage(PvPKit.prefix+Messages.getString("errors.cmd.arena.arena_does_not_exist")); //$NON-NLS-1$
				}
				
			}
			return true;
			
		} else if (args[1].equalsIgnoreCase("listkit")) { //$NON-NLS-1$
			if(args.length < 3) {
				p.sendMessage(PvPKit.prefix+Messages.getString("errors.cmd.arg.no_arena")); //$NON-NLS-1$
			} else {
				if(ArenaManager.arenas.containsKey(args[2])) {
					p.sendMessage("§e========[ §9"+args[2]+" - kits §e]========"); //$NON-NLS-1$ //$NON-NLS-2$
					p.sendMessage("§6Liste des kits autorisés dans l'arène §b"+args[2]+" :"); //$NON-NLS-1$ //$NON-NLS-2$
					for(String s : ArenaManager.arenas.get(args[2]).getKits())
						p.sendMessage("§a"+s); //$NON-NLS-1$
					p.sendMessage("§e========[ §9"+args[2]+" - kits §e]========"); //$NON-NLS-1$ //$NON-NLS-2$
				} else {
					p.sendMessage(PvPKit.prefix+Messages.getString("errors.cmd.arena.arena_does_not_exist")); //$NON-NLS-1$
				}
			}
			return true;
			
		} else if (args[1].equalsIgnoreCase("addkit")) { //$NON-NLS-1$
			if(args.length < 3) {
				p.sendMessage(PvPKit.prefix+Messages.getString("errors.cmd.arg.no_arena")); //$NON-NLS-1$
			} else {
				if(ArenaManager.arenas.containsKey(args[2])) {
					if(args.length < 4) {
						p.sendMessage(PvPKit.prefix+Messages.getString("errors.cmd.arg.no_kit")); //$NON-NLS-1$
					} else {					
						
						Arena arena = ArenaManager.arenas.get(args[2]);
						
						if(arena.getKits().contains(args[3])) {
							p.sendMessage(PvPKit.prefix+Messages.getString("errors.cmd.arena.kit.already_allowed")); //$NON-NLS-1$
							
						} else if(KitManager.kits.containsKey(args[3])) {
							
							Kit kit = KitManager.kits.get(args[3]);
							
							if(!kit.isValid()) {
								p.sendMessage(PvPKit.prefix+Messages.getString("errors.cmd.arena.kit.invalid")); //$NON-NLS-1$
								for(String s : kit.getErrors())
									p.sendMessage("§c"+s); //$NON-NLS-1$
								
							} else {
								arena.addKit(kit.getName());
								armg.putArena(arena);
								armg.saveArenas();
								p.sendMessage(PvPKit.prefix+"§aKit §b"+args[3]+" §aactivé avec succès dans l'arène §b"+arena.getName()+" §a!"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
							}
							
						} else {
							p.sendMessage(PvPKit.prefix+Messages.getString("errors.cmd.kit.kit_does_not_exist")); //$NON-NLS-1$
						}
					}
				} else {
					p.sendMessage(PvPKit.prefix+Messages.getString("errors.cmd.arena.arena_does_not_exist")); //$NON-NLS-1$
				}
			}
			return true;
			
		} else if (args[1].equalsIgnoreCase("removekit")) { //$NON-NLS-1$
			if(args.length < 3) {
				p.sendMessage(PvPKit.prefix+Messages.getString("errors.cmd.arg.no_arena")); //$NON-NLS-1$
			} else {
				if(ArenaManager.arenas.containsKey(args[2])) {
					if(args.length < 4) {
						p.sendMessage(PvPKit.prefix+Messages.getString("errors.cmd.arg.no_kit")); //$NON-NLS-1$
					} else {					
						
						Arena arena = ArenaManager.arenas.get(args[2]);
						
						if(arena.getKits().contains(args[3])) {
							arena.removeKit(args[3]);
							armg.putArena(arena);
							armg.saveArenas();
							p.sendMessage(PvPKit.prefix+"§aKit §b"+args[3]+" §cdésactivé §aavec succès dans l'arène §b"+arena.getName()+" §a!"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

						} else if(KitManager.kits.containsKey(args[3])) {
							
							p.sendMessage(PvPKit.prefix+Messages.getString("errors.cmd.arena.kit.already_disallowed")); //$NON-NLS-1$
							
						} else {
							p.sendMessage(PvPKit.prefix+Messages.getString("errors.cmd.kit.kit_does_not_exist")); //$NON-NLS-1$
						}
					}
				} else {
					p.sendMessage(PvPKit.prefix+Messages.getString("errors.cmd.arena.arena_does_not_exist")); //$NON-NLS-1$
				}
			}
			return true;
		} else {
			p.sendMessage(PvPKit.prefix+Messages.getString("errors.cmd.unknown")); //$NON-NLS-1$
			p.sendMessage(PvPKit.prefix + "§cPour une liste des commandes disponibles, entrez &b/pka arena"); //$NON-NLS-1$
			return false;
		}
		
		return false;
	}
	
	private boolean onGameCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		Player p = (Player) sender;
		if(args.length == 1) {
			
			p.sendMessage("§e========[ §9/pka game §e]========"); //$NON-NLS-1$
			p.sendMessage("§6Commandes disponibles :"); //$NON-NLS-1$
			p.sendMessage("§b/pka game list"); //$NON-NLS-1$
			p.sendMessage("§b/pka game create <nom>"); //$NON-NLS-1$
			p.sendMessage("§b/pka game remove <game>"); //$NON-NLS-1$
			p.sendMessage("§b/pka game setarena <game> <arena>"); //$NON-NLS-1$
			p.sendMessage("§b/pka game settype <game> <type>"); //$NON-NLS-1$
			p.sendMessage("§b/pka game setstatus <game> <status>"); //$NON-NLS-1$
			p.sendMessage("§b/pka game info <game>"); //$NON-NLS-1$
			p.sendMessage("§e========[ §9/pka game §e]========"); //$NON-NLS-1$
			return true;
				
		} else if (args[1].equalsIgnoreCase("list")){ //$NON-NLS-1$
			
			p.sendMessage("§e========[ §9Games §e]========"); //$NON-NLS-1$
			for(Game game : GameManager.games.values()) {
				if(game.isValid()) {
					p.sendMessage("§a"+game.getName()+" ("+game.getType()+" | "+game.getStatus()+")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				} else {
					TextComponent txt = new TextComponent("§c"+game.getName()+" ("+game.getType()+" | "+game.getStatus()+")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
					String str = ""; //$NON-NLS-1$
					for(String s : game.getErrors()) {
						str	+= "\n§c"+s; //$NON-NLS-1$
					}
					str.replaceFirst("\n", ""); //$NON-NLS-1$ //$NON-NLS-2$
					txt.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(str).create()));
					p.sendMessage(txt.toString());
				}
			}
			p.sendMessage("§e========[ §9Games §e]========"); //$NON-NLS-1$
			return true;	
			
		} else if (args[1].equalsIgnoreCase("create")) { //$NON-NLS-1$
			
			if(args.length == 2) { // Pas de nom précisé
				p.sendMessage(PvPKit.prefix+Messages.getString("errors.cmd.arg.no_name")); //$NON-NLS-1$
				p.sendMessage(PvPKit.prefix+"§cSyntaxe : /pka game create <nom>"); //$NON-NLS-1$
				
			} else if(args.length > 3) { // Trop d'arguments
				p.sendMessage(PvPKit.prefix+Messages.getString("errors.cmd.game.no_space_in_name")); //$NON-NLS-1$
				
			} else {
				if(KitManager.kits.containsKey(args[2])) { // Nom déjà pris
					p.sendMessage(PvPKit.prefix+Messages.getString("errors.cmd.game.name_already_exist")); //$NON-NLS-1$
				} else {
					kitmg.putKit(new Kit(args[2]));
					kitmg.saveKits();
					p.sendMessage(PvPKit.prefix+"§aPartie §b"+args[2]+" créée avec succès !"); //$NON-NLS-1$ //$NON-NLS-2$
					p.sendMessage(PvPKit.prefix+"§6Définissez son arène avec §2/pka game setarena "+args[2]+" <arène>"); //$NON-NLS-1$ //$NON-NLS-2$
					p.sendMessage(PvPKit.prefix+"§6Définissez son type avec §2/pka game settype "+args[2]+" <type>"); //$NON-NLS-1$ //$NON-NLS-2$
					p.sendMessage(PvPKit.prefix+"§6Définissez son status avec §2/pka game setstatus "+args[2]+" <statut>"); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
			return true;
			
		} else if (args[1].equalsIgnoreCase("remove")) { //$NON-NLS-1$
			if(args.length == 2) {
				p.sendMessage(PvPKit.prefix+Messages.getString("errors.cmd.arg.no_game")); //$NON-NLS-1$
			} else {
				if(GameManager.games.containsKey(args[2])) {
					gamemg.removeGame(args[2]);
					gamemg.saveGames();
					p.sendMessage(PvPKit.prefix+"§aPartie §b"+args[2]+" §asupprimée avec succès !"); //$NON-NLS-1$ //$NON-NLS-2$
				} else {
					p.sendMessage(PvPKit.prefix+Messages.getString("errors.cmd.game.game_does_not_exist")); //$NON-NLS-1$
				}
			}
			return true;
			
		} else if (args[1].equalsIgnoreCase("setarena")) { //$NON-NLS-1$
			if(args.length < 3) {
				p.sendMessage(PvPKit.prefix+Messages.getString("errors.cmd.arg.no_game")); //$NON-NLS-1$
			} else if(GameManager.games.containsKey(args[2])) {
				if(args.length < 4) {
					p.sendMessage(PvPKit.prefix+Messages.getString("errors.cmd.arg.no_arena")); //$NON-NLS-1$
				} else if (ArenaManager.arenas.containsKey(args[3])) {
					Game game = GameManager.games.get(args[2]);
					game.setArena(ArenaManager.arenas.get(args[3]));
					gamemg.putGame(game);
					gamemg.saveGames();
					p.sendMessage(PvPKit.prefix+"§aArène de la partie §b"+game.getName()+" §adéfinie avec succès !"); //$NON-NLS-1$ //$NON-NLS-2$
					
				} else {
					p.sendMessage(PvPKit.prefix+Messages.getString("errors.cmd.arena.arena_does_not_exist")); //$NON-NLS-1$
				}
					
			} else {
				p.sendMessage(PvPKit.prefix+Messages.getString("errors.cmd.game.game_does_not_exist")); //$NON-NLS-1$
			}
			
			
		} else if (args[1].equalsIgnoreCase("settype")) { //$NON-NLS-1$
			if(args.length == 2) {
				p.sendMessage(PvPKit.prefix+Messages.getString("errors.cmd.arg.no_game")); //$NON-NLS-1$
			} else if(GameManager.games.containsKey(args[2])) {
				if(args.length < 4) {
					p.sendMessage(PvPKit.prefix+Messages.getString("errors.cmd.arg.no_gametype")); //$NON-NLS-1$
				} else {
					Game game = GameManager.games.get(args[2]);
					if(GameType.valueOf(args[3]) == null) {
						p.sendMessage(PvPKit.prefix+Messages.getString("errors.cmd.game.invalid_gametype")); //$NON-NLS-1$
					} else {
						game.setType(GameType.valueOf(args[3]));
						gamemg.putGame(game);
						gamemg.saveGames();
						p.sendMessage(PvPKit.prefix+"§aType de jeu pour la partie §b"+game.getName()+" §adéfini avec succès !"); //$NON-NLS-1$ //$NON-NLS-2$
					}
				}

			} else {
				p.sendMessage(PvPKit.prefix+Messages.getString("errors.cmd.game.game_does_not_exist")); //$NON-NLS-1$
			}
			return true;

		} else if (args[1].equalsIgnoreCase("setstatus")) { //$NON-NLS-1$
			if(args.length == 2) {
				p.sendMessage(PvPKit.prefix+Messages.getString("errors.cmd.arg.no_game")); //$NON-NLS-1$
			} else if(GameManager.games.containsKey(args[2])) {
				if(args.length < 4) {
					p.sendMessage(PvPKit.prefix+Messages.getString("errors.cmd.arg.no_gamestatus")); //$NON-NLS-1$
				} else {
					Game game = GameManager.games.get(args[2]);
					if(GameStatus.valueOf(args[3]) == null) {
						p.sendMessage(PvPKit.prefix+Messages.getString("errors.cmd.game.invalid_gamestatus")); //$NON-NLS-1$
					} else if(game.isValid()){
						game.setStatus(GameStatus.valueOf(args[3]));
						gamemg.putGame(game);
						gamemg.saveGames();
						p.sendMessage(PvPKit.prefix+"§aStatut de la partie §b"+game.getName()+" §adéfini avec succès !"); //$NON-NLS-1$ //$NON-NLS-2$
					} else {
						p.sendMessage(PvPKit.prefix+Messages.getString("errors.cmd.game.status_change_denied_invalid_game")); //$NON-NLS-1$
						p.sendMessage("§cMerci de corriger ces erreurs :");//$NON-NLS-1$
						for(String s : game.getErrors())
							p.sendMessage("§e"+s); //$NON-NLS-1$
					}
				}

			} else {
				p.sendMessage(PvPKit.prefix+Messages.getString("errors.cmd.game.game_does_not_exist")); //$NON-NLS-1$
			}
			return true;
		
		} else if (args[1].equalsIgnoreCase("info")) { //$NON-NLS-1$
			if(args.length == 2) {
				p.sendMessage(PvPKit.prefix+Messages.getString("errors.cmd.arg.no_game")); //$NON-NLS-1$
			} else if(GameManager.games.containsKey(args[2])) {
				
				Game game = GameManager.games.get(args[2]);
				p.sendMessage("§e========[ §9"+game.getName()+" §e]========"); //$NON-NLS-1$ //$NON-NLS-2$
				p.sendMessage("§6Type : §b"+game.getType() != null ? game.getType().toString() : "§cAucun"); //$NON-NLS-1$ //$NON-NLS-2$
				p.sendMessage("§6Status : §b"+game.getStatus() != null ? game.getStatus().toString() : "§cAucun"); //$NON-NLS-1$ //$NON-NLS-2$
				p.sendMessage("§6Arène : §b"+game.getArena() != null ? game.getArena().getName() : "§cAucune"); //$NON-NLS-1$ //$NON-NLS-2$
				if(game.isValid()) {
					p.sendMessage("§6Valide : §aoui"); //$NON-NLS-1$
				} else {
					p.sendMessage("§6Valide : §cnon"); //$NON-NLS-1$
					p.sendMessage("§6Problèmes :"); //$NON-NLS-1$
					for(String s : game.getErrors())
						p.sendMessage(" §b- §c"+s); //$NON-NLS-1$
				}
				p.sendMessage("§e========[ §9"+game.getName()+" §e]========"); //$NON-NLS-1$ //$NON-NLS-2$
				
			} else {
				p.sendMessage(PvPKit.prefix+Messages.getString("errors.cmd.game.game_does_not_exist")); //$NON-NLS-1$
			}
			return true;
			
		} else {
			p.sendMessage(PvPKit.prefix+Messages.getString("errors.cmd.unknown")); //$NON-NLS-1$
			p.sendMessage(PvPKit.prefix + "§cPour une liste des commandes disponibles, entrez &b/pka game"); //$NON-NLS-1$
			return false;
		}
		return false;
	}
	
}