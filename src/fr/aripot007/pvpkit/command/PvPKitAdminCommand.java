package fr.aripot007.pvpkit.command;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.aripot007.pvpkit.PvPKit;
import fr.aripot007.pvpkit.game.Arena;
import fr.aripot007.pvpkit.game.Game;
import fr.aripot007.pvpkit.game.GameStatus;
import fr.aripot007.pvpkit.game.GameType;
import fr.aripot007.pvpkit.game.Kit;
import fr.aripot007.pvpkit.manager.ArenaManager;
import fr.aripot007.pvpkit.manager.GameManager;
import fr.aripot007.pvpkit.manager.KitManager;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

/**
 * Handle administration commands
 * @author Aristide
 *
 */
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
			sender.sendMessage("Cette commande ne peut être exécutée que par un joueur"); 
			return false;
		}
		
		if(args.length == 0) {
			
			sender.sendMessage(PvPKit.prefix+"§cMerci de préciser un argument !\n§eCommandes disponibles : §b/pka help/kit/arena/game");
			
		} else if(args[0].equalsIgnoreCase("kit")) { //$NON-NLS-1$
		
			return onKitCommand(sender, cmd, msg, args);
			
		} else if(args[0].equalsIgnoreCase("arena")) { //$NON-NLS-1$
			
			return onArenaCommand(sender, cmd, msg, args);
			
		} else if(args[0].equalsIgnoreCase("game")) {
			
			return onGameCommand(sender, cmd, msg, args);
			
		} else {
			sender.sendMessage(PvPKit.prefix+"§cCommande inconnue !"); 
			sender.sendMessage(PvPKit.prefix + "§cPour une liste des commandes disponibles, entrez §b/pka help"); 
			return false;
		}
		return false;
	}
	
	/**
	 * Handle the commands used to manage kits
	 * @return A boolean indicating if the command execution was successful
	 */
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
			p.sendMessage("§b/pka kit effects <kit>"); //$NON-NLS-1$
			p.sendMessage("§b/pka kit addeffect <kit> <potion effect> [level] [duration]"); //$NON-NLS-1$
			p.sendMessage("§b/pka kit deleffect <kit> <potion effect>"); //$NON-NLS-1$
			p.sendMessage("§e========[ §9/pka kit §e]========"); //$NON-NLS-1$
			return true;
				
		} else if (args[1].equalsIgnoreCase("list")){ //$NON-NLS-1$
			
			p.sendMessage("§e========[ §9Kits §e]========"); //$NON-NLS-1$
			for(Kit kit : kitmg.getKits().values()) {
				if(kit.isValid()) {
					p.sendMessage("§a"+kit.getName()); //$NON-NLS-1$
				} else {
					TextComponent txt = new TextComponent("§c"+kit.getName()); //$NON-NLS-1$
					String str = ""; //$NON-NLS-1$
					for(String s : kit.getErrors()) {
						str	+= "§c"+s; //$NON-NLS-1$
						if(kit.getErrors().indexOf(s)+1 < kit.getErrors().size())
							str +="\n"; //$NON-NLS-1$
					}
					txt.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(str).create()));
					p.spigot().sendMessage(txt);
				}
			}
			p.sendMessage("§e========[ §9Kits §e]========"); //$NON-NLS-1$
			return true;	
			
		} else if (args[1].equalsIgnoreCase("create")) { //$NON-NLS-1$
			
			if(args.length == 2) { // Pas de nom précisé
				p.sendMessage(PvPKit.prefix+"§cMerci de préciser un nom !"); 
				p.sendMessage(PvPKit.prefix+"§cSyntaxe : /pka kit create <nom>"); //$NON-NLS-1$
				
			} else if(args.length > 3) { // Trop d'arguments
				p.sendMessage(PvPKit.prefix+"§cLe nom du kit ne doit pas contenir d'espaces !"); 
				
			} else {
				if(kitmg.containsKit(args[2])) { // Nom déjà pris
					p.sendMessage(PvPKit.prefix+"§cUn kit avec le même nom existe déjà !"); 
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
				p.sendMessage(PvPKit.prefix+"§cMerci de préciser un kit !"); 
			} else {
				if(kitmg.containsKit(args[2])) {
					kitmg.removeKit(args[2]);
					kitmg.saveKits();
					p.sendMessage(PvPKit.prefix+"§aKit §b"+args[2]+" §asupprimé avec succès !"); //$NON-NLS-1$ //$NON-NLS-2$
				} else {
					p.sendMessage(PvPKit.prefix+"§cCe kit n'existe pas !"); 
				}
			}
			return true;
			
		} else if (args[1].equalsIgnoreCase("seticon")) { //$NON-NLS-1$
			if(args.length == 2) {
				p.sendMessage(PvPKit.prefix+"§cMerci de préciser un kit !"); 
			} else if(kitmg.containsKit(args[2])) {
				
				ItemStack icon = p.getInventory().getItemInMainHand().clone();
				if(icon != null && !icon.getType().isAir()) {
					Kit kit = kitmg.getKit(args[2]);
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
					kitmg.saveKits();
					p.sendMessage(PvPKit.prefix+"§aIcone du kit §b"+kit.getName()+" §adéfinie avec succès !"); //$NON-NLS-1$ //$NON-NLS-2$
					
				} else {
					p.sendMessage(PvPKit.prefix+"§cVous devez tenir un item dans votre main !"); 
				}
			} else {
				p.sendMessage(PvPKit.prefix+"§cCe kit n'existe pas !"); 
			}
			
			
		} else if (args[1].equalsIgnoreCase("setinv")) { //$NON-NLS-1$
			if(args.length == 2) {
				p.sendMessage(PvPKit.prefix+"§cMerci de préciser un kit !"); 
			} else if(kitmg.containsKit(args[2])) {

				Kit kit = kitmg.getKit(args[2]);
				kit.setInventoryContent(p.getInventory().getContents());
				kitmg.saveKits();
				p.sendMessage(PvPKit.prefix+"§aContenu du kit §b"+kit.getName()+" §adéfinie avec succès !"); //$NON-NLS-1$ //$NON-NLS-2$

			} else {
				p.sendMessage(PvPKit.prefix+"§cCe kit n'existe pas !"); 
			}

		} else if(args[1].equalsIgnoreCase("effects")) {
			
			if(args.length == 2) {
				p.sendMessage(PvPKit.prefix+"§cMerci de préciser un kit !"); 
			} else if(kitmg.containsKit(args[2])) {

				Kit kit = kitmg.getKit(args[2]);
				List<PotionEffect> effects = kit.getEffects();
				p.sendMessage("§e========[ §9"+kit.getName()+" - effets §e]========");
				if(effects.isEmpty()) {
					p.sendMessage("§cCe kit n'& aucun effet de potion actif.");
				} else {
					for(PotionEffect ef : effects) {
						p.sendMessage("  §a- "+ef.getType().getName().toLowerCase()+" §2("+(ef.getDuration() == Integer.MAX_VALUE ? 9999 : ef.getDuration() / 20)+"s / lvl "+(ef.getAmplifier()+1)+")");
					}
				}
				p.sendMessage("§e========[ §9"+kit.getName()+" - effets §e]========");
				
			} else {
				p.sendMessage(PvPKit.prefix+"§cCe kit n'existe pas !"); 
			}
			
		} else if(args[1].equalsIgnoreCase("addeffect")) {
			
			if(args.length == 2) {
				p.sendMessage(PvPKit.prefix+"§cMerci de préciser un kit !\n§cSyntaxe : §b/pka kit addeffect <kit> <effet> [niveau] [durée]"); 
			} else if(kitmg.containsKit(args[2])) {

				Kit kit = kitmg.getKit(args[2]);
				
				if(args.length < 4) { // No effect specified
					p.sendMessage(PvPKit.prefix+"§cMerci de préciser un effet de potion !\n§cSyntaxe : §b/pka kit addeffect <kit> <effet> [niveau] [durée]");
				} else {
					
					PotionEffectType etype = PotionEffectType.getByName(args[3].toUpperCase());
					
					if(etype == null) {
						p.sendMessage(PvPKit.prefix+"§cMerci de préciser une potion valide !");
						String available = "";
						for(PotionEffectType type : PotionEffectType.values()) {
							available += ", "+type.getName().toLowerCase();
						}
						available = available.substring(2);
						p.sendMessage("§6Effets disponibles : §e"+available);
						return true;
					}
					
					if(!kit.getEffects().isEmpty()) {
						for(PotionEffect ef : kit.getEffects()) {
							if(ef.getType().equals(etype)) {
								p.sendMessage(PvPKit.prefix+"§cLe kit contient déjà cet effet de potion !");
								return true;
							}
						}
					}
					
					int duration = Integer.MAX_VALUE;
					int amplifier = 0;
					
					try {
						if(args.length > 4)
							amplifier = Integer.parseInt(args[4]) - 1;
						if(args.length > 5)
							duration = Integer.parseInt(args[5]) * 20;
						
						if(amplifier < 0 || duration <= 0)
							throw new Exception();
						
					} catch(Exception e) {
						p.sendMessage(PvPKit.prefix+"§cMerci d'entrer un nombre valide !\n§cSyntaxe : §b/pka kit addeffect <kit> <effet> [niveau] [durée]");
					}
					
					kit.addEffect(new PotionEffect(etype, duration, amplifier, false, false));
					kitmg.saveKits();
					p.sendMessage(PvPKit.prefix+"§aEffet §b"+args[3]+"§a ajouté avec succès au kit §b"+kit.getName()+"§a !");
					
				}
				
			} else {
				p.sendMessage(PvPKit.prefix+"§cCe kit n'existe pas !"); 
			}
			
		} else if(args[1].equalsIgnoreCase("deleffect")) {
			
			if(args.length == 2) {
				p.sendMessage(PvPKit.prefix+"§cMerci de préciser un kit !\n§cSyntaxe : §b/pka kit deleffect <kit> <effet>"); 
			} else if(kitmg.containsKit(args[2])) {

				Kit kit = kitmg.getKit(args[2]);
				
				if(args.length < 4) { // No effect specified
					p.sendMessage(PvPKit.prefix+"§cMerci de préciser un effet de potion !\n§cSyntaxe : §b/pka kit deleffect <kit> <effet>");
				} else {
					PotionEffectType etype;
					try {
						etype = PotionEffectType.getByName(args[3].toUpperCase());
					} catch(Exception e) {
						p.sendMessage(PvPKit.prefix+"§cMerci de préciser une potion valide !");
						String available = "";
						for(PotionEffectType type : PotionEffectType.values()) {
							available += ", "+type.getName().toLowerCase();
						}
						available = available.substring(2);
						p.sendMessage("§6Effets disponibles : §e"+available);
						return true;
					}
					
					for(PotionEffect ef : kit.getEffects()) {
						if(ef.getType().equals(etype)) {
							kit.removeEffect(etype);
							kitmg.saveKits();
							p.sendMessage(PvPKit.prefix+"§aEffet §b"+args[3]+"§a retiré avec succès du kit §b"+kit.getName()+"§a !");
							return true;
						}
					}
					
					p.sendMessage(PvPKit.prefix+"§cLe kit ne contient pas cet effet de potion !");
					return true;
					
				}
				
			} else {
				p.sendMessage(PvPKit.prefix+"§cCe kit n'existe pas !"); 
			}
			
		} else {
			p.sendMessage(PvPKit.prefix+"§cCommande inconnue !"); 
			p.sendMessage(PvPKit.prefix + "§cPour une liste des commandes disponibles, entrez &b/pka kit"); //$NON-NLS-1$
			return false;
		}
		return false;
	}
	
	/**
	 * Handle the commands used to manage arenas
	 * @return A boolean indicating if the command execution was successful
	 */
	private boolean onArenaCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		Player p = (Player) sender;
		
		if(args.length < 2) { // Aide
			
			p.sendMessage("§e========[ §9/pka arena §e]========"); //$NON-NLS-1$
			p.sendMessage("§6Commandes disponibles :"); //$NON-NLS-1$
			p.sendMessage("§b/pka arena list"); //$NON-NLS-1$
			p.sendMessage("§b/pka arena create <nom>"); //$NON-NLS-1$
			p.sendMessage("§b/pka arena remove <arena>"); //$NON-NLS-1$
			p.sendMessage("§b/pka arena setspawn <arena>"); //$NON-NLS-1$
			p.sendMessage("§b/pka arena listkit <arena>"); //$NON-NLS-1$
			p.sendMessage("§b/pka arena addkit <arena> <kit>"); //$NON-NLS-1$
			p.sendMessage("§b/pka arena removekit <arena> <kit>"); //$NON-NLS-1$
			p.sendMessage("§e========[ §9/pka arena §e]========"); //$NON-NLS-1$
			
		} else if (args[1].equalsIgnoreCase("list")){ // list //$NON-NLS-1$
			
			p.sendMessage("§e========[ §9Arènes §e]========"); //$NON-NLS-1$
			for(Arena ar : armg.getArenas().values()) {
				if(ar.isValid()) {
					p.sendMessage("§a"+ar.getName()); //$NON-NLS-1$
				} else {
					TextComponent txt = new TextComponent("§c"+ar.getName()); //$NON-NLS-1$
					String str = ""; //$NON-NLS-1$
					for(String s : ar.getErrors()) {
						str	+= "§c"+s; //$NON-NLS-1$
						if(ar.getErrors().indexOf(s)+1 < ar.getErrors().size())
							str +="\n"; //$NON-NLS-1$
					}
					txt.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(str).create()));
					p.spigot().sendMessage(txt);
				}
			}
			p.sendMessage("§e========[ §9Arènes §e]========"); //$NON-NLS-1$
			return true;	
			
		} else if (args[1].equalsIgnoreCase("create")) { // Create //$NON-NLS-1$
			
			if(args.length == 2) { // Pas de nom précisé
				p.sendMessage(PvPKit.prefix+"§cMerci de préciser un nom !"); 
				p.sendMessage(PvPKit.prefix+"§cSyntaxe : /pka arena create <nom>"); //$NON-NLS-1$
				
			} else if(args.length > 3) { // Trop d'arguments
				p.sendMessage(PvPKit.prefix+"§cLe nom de l'arène ne doit pas contenir d'espaces !"); 
				
			} else {
				if(armg.containsArena(args[2])) { // Nom déjà pris
					p.sendMessage(PvPKit.prefix+"§cUne arène avec le même nom existe déjà !"); 
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
				p.sendMessage(PvPKit.prefix+"§cMerci de préciser une arène !"); 
			} else {
				if(armg.containsArena(args[2])) {
					armg.removeArena(args[2]);
					armg.saveArenas();
					p.sendMessage(PvPKit.prefix+"§aArène §b"+args[2]+" §asupprimée avec succès !"); //$NON-NLS-1$ //$NON-NLS-2$
				} else {
					p.sendMessage(PvPKit.prefix+"§cCette arène n'existe pas !"); 
				}
			}
			return true;
			
		} else if (args[1].equalsIgnoreCase("setspawn")) { //$NON-NLS-1$
			if(args.length < 3) {
				p.sendMessage(PvPKit.prefix+"§cMerci de préciser une arène !"); 
			} else {
				if(armg.containsArena(args[2])) {
					Arena arena = armg.getArena(args[2]);
					arena.setSpawn(p.getLocation());
					armg.saveArenas();
					p.sendMessage(PvPKit.prefix+"§aSpawn de l'arène §b"+args[2]+" §adéfini avec succès !"); //$NON-NLS-1$ //$NON-NLS-2$
				} else {
					p.sendMessage(PvPKit.prefix+"§cCette arène n'existe pas !"); 
				}
				
			}
			return true;
			
		} else if (args[1].equalsIgnoreCase("listkit")) { //$NON-NLS-1$
			if(args.length < 3) {
				p.sendMessage(PvPKit.prefix+"§cMerci de préciser une arène !"); 
			} else {
				if(armg.containsArena(args[2])) {
					p.sendMessage("§e========[ §9"+args[2]+" - kits §e]========"); //$NON-NLS-1$ //$NON-NLS-2$
					p.sendMessage("§6Liste des kits autorisés dans l'arène §b"+args[2]+" :"); //$NON-NLS-1$ //$NON-NLS-2$
					for(String s : armg.getArena(args[2]).getKits())
						p.sendMessage("§a"+s); //$NON-NLS-1$
					p.sendMessage("§e========[ §9"+args[2]+" - kits §e]========"); //$NON-NLS-1$ //$NON-NLS-2$
				} else {
					p.sendMessage(PvPKit.prefix+"§cCette arène n'existe pas !"); 
				}
			}
			return true;
			
		} else if (args[1].equalsIgnoreCase("addkit")) { //$NON-NLS-1$
			if(args.length < 3) {
				p.sendMessage(PvPKit.prefix+"§cMerci de préciser une arène !"); 
			} else {
				if(armg.containsArena(args[2])) {
					if(args.length < 4) {
						p.sendMessage(PvPKit.prefix+"§cMerci de préciser un kit !"); 
					} else {					
						
						Arena arena = armg.getArena(args[2]);
						
						if(arena.getKits().contains(args[3])) {
							p.sendMessage(PvPKit.prefix+"§cCe kit est déjà autorisé dans cette arène !"); 
							
						} else if(kitmg.containsKit(args[3])) {
							
							Kit kit = kitmg.getKit(args[3]);
							
							if(!kit.isValid()) {
								p.sendMessage(PvPKit.prefix+"§cCe kit n'est pas valide :"); 
								for(String s : kit.getErrors())
									p.sendMessage("§c"+s); //$NON-NLS-1$
								
							} else {
								arena.addKit(kit.getName());
								armg.saveArenas();
								p.sendMessage(PvPKit.prefix+"§aKit §b"+args[3]+" §aactivé avec succès dans l'arène §b"+arena.getName()+" §a!"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
							}
							
						} else {
							p.sendMessage(PvPKit.prefix+"§cCe kit n'existe pas !"); 
						}
					}
				} else {
					p.sendMessage(PvPKit.prefix+"§cCette arène n'existe pas !"); 
				}
			}
			return true;
			
		} else if (args[1].equalsIgnoreCase("removekit")) { //$NON-NLS-1$
			if(args.length < 3) {
				p.sendMessage(PvPKit.prefix+"§cMerci de préciser une arène !"); 
			} else {
				if(armg.containsArena(args[2])) {
					if(args.length < 4) {
						p.sendMessage(PvPKit.prefix+"§cMerci de préciser un kit !"); 
					} else {					
						
						Arena arena = armg.getArena(args[2]);
						
						if(arena.getKits().contains(args[3])) {
							arena.removeKit(args[3]);
							armg.saveArenas();
							p.sendMessage(PvPKit.prefix+"§aKit §b"+args[3]+" §cdésactivé §aavec succès dans l'arène §b"+arena.getName()+" §a!"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

						} else if(kitmg.containsKit(args[3])) {
							p.sendMessage(PvPKit.prefix+"§cCe kit est déjà désactivé dans cette arène !"); 
							
						} else {
							p.sendMessage(PvPKit.prefix+"§cCe kit n'existe pas !"); 
						}
					}
				} else {
					p.sendMessage(PvPKit.prefix+"§cCette arène n'existe pas !"); 
				}
			}
			return true;
		} else {
			p.sendMessage(PvPKit.prefix+"§cCommande inconnue !"); 
			p.sendMessage(PvPKit.prefix + "§cPour une liste des commandes disponibles, entrez &b/pka arena"); //$NON-NLS-1$
			return false;
		}
		
		return false;
	}
	
	/**
	 * Handle the commands used to manage games
	 * @return A boolean indicating if the command execution was successful
	 */
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
			for(Game game : gamemg.getGames().values()) {
				if(game.isValid()) {
					p.sendMessage("§a"+game.getName()+" ("+game.getType()+" | "+game.getStatus()+")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				} else {
					TextComponent txt = new TextComponent("§c"+game.getName()+" ("+game.getType()+" | "+game.getStatus()+")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
					String str = ""; //$NON-NLS-1$
					for(String s : game.getErrors()) {
						str	+= "§c"+s; //$NON-NLS-1$
						if(game.getErrors().indexOf(s)+1 < game.getErrors().size())
							str +="\n"; //$NON-NLS-1$
					}
					txt.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(str).create()));
					p.spigot().sendMessage(txt);
				}
			}
			p.sendMessage("§e========[ §9Games §e]========"); //$NON-NLS-1$
			return true;	
			
		} else if (args[1].equalsIgnoreCase("create")) { //$NON-NLS-1$
			
			if(args.length == 2) { // Pas de nom précisé
				p.sendMessage(PvPKit.prefix+"§cMerci de préciser un nom !"); 
				p.sendMessage(PvPKit.prefix+"§cSyntaxe : /pka game create <nom>"); //$NON-NLS-1$
				
			} else if(args.length > 3) { // Trop d'arguments
				p.sendMessage(PvPKit.prefix+"§cLe nom de la partie ne doit pas contenir d'espaces !"); 
				
			} else {
				if(gamemg.containsGame(args[2])) { // Nom déjà pris
					p.sendMessage(PvPKit.prefix+"§cUne partie avec le même nom existe déjà !"); 
				} else {
					gamemg.putGame(new Game(args[2]));
					gamemg.saveGames();
					p.sendMessage(PvPKit.prefix+"§aPartie §b"+args[2]+" créée avec succès !"); //$NON-NLS-1$ //$NON-NLS-2$
					p.sendMessage(PvPKit.prefix+"§6Définissez son arène avec §2/pka game setarena "+args[2]+" <arène>"); //$NON-NLS-1$ //$NON-NLS-2$
					p.sendMessage(PvPKit.prefix+"§6Définissez son type avec §2/pka game settype "+args[2]+" <type>"); //$NON-NLS-1$ //$NON-NLS-2$
					p.sendMessage(PvPKit.prefix+"§6Définissez son status avec §2/pka game setstatus "+args[2]+" <statut>"); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
			return true;
			
		} else if (args[1].equalsIgnoreCase("remove")) { //$NON-NLS-1$
			if(args.length == 2) {
				p.sendMessage(PvPKit.prefix+"§cMerci de préciser une partie !"); 
			} else {
				if(gamemg.containsGame(args[2])) {
					gamemg.removeGame(args[2]);
					gamemg.saveGames();
					p.sendMessage(PvPKit.prefix+"§aPartie §b"+args[2]+" §asupprimée avec succès !"); //$NON-NLS-1$ //$NON-NLS-2$
				} else {
					p.sendMessage(PvPKit.prefix+"§cCette partie n'existe pas !"); 
				}
			}
			return true;
			
		} else if (args[1].equalsIgnoreCase("setarena")) { //$NON-NLS-1$
			if(args.length < 3) {
				p.sendMessage(PvPKit.prefix+"§cMerci de préciser une partie !"); 
			} else if(gamemg.containsGame(args[2])) {
				if(args.length < 4) {
					p.sendMessage(PvPKit.prefix+"§cMerci de préciser une arène !"); 
				} else if (armg.containsArena(args[3])) {
					Game game = gamemg.getGame(args[2]);
					game.setArena(armg.getArena(args[3]));
					gamemg.saveGames();
					p.sendMessage(PvPKit.prefix+"§aArène de la partie §b"+game.getName()+" §adéfinie avec succès !"); //$NON-NLS-1$ //$NON-NLS-2$
					
				} else {
					p.sendMessage(PvPKit.prefix+"§cCette arène n'existe pas !"); 
				}
					
			} else {
				p.sendMessage(PvPKit.prefix+"§cCette partie n'existe pas !"); 
			}
			
			
		} else if (args[1].equalsIgnoreCase("settype")) { //$NON-NLS-1$
			if(args.length == 2) {
				p.sendMessage(PvPKit.prefix+"§cMerci de préciser une partie !"); 
			} else if(gamemg.containsGame(args[2])) {
				if(args.length < 4) {
					p.sendMessage(PvPKit.prefix+"§cMerci de préciser un type de jeu ! (normal|uhc)"); 
				} else {
					Game game = gamemg.getGame(args[2]);
					try {
						GameType type = GameType.valueOf(args[3].toUpperCase());
						game.setType(type);
						gamemg.saveGames();
						p.sendMessage(PvPKit.prefix+"§aType de jeu pour la partie §b"+game.getName()+" §adéfini avec succès !"); //$NON-NLS-1$ 
					} catch (Exception e) {
						p.sendMessage(PvPKit.prefix+"§cMerci de préciser un type de jeu valide ! (normal|uhc)"); 
					}
				}

			} else {
				p.sendMessage(PvPKit.prefix+"§cCette partie n'existe pas !"); 
			}
			return true;

		} else if (args[1].equalsIgnoreCase("setstatus")) { //$NON-NLS-1$
			if(args.length == 2) {
				p.sendMessage(PvPKit.prefix+"§cMerci de préciser une partie !"); 
			} else if(gamemg.containsGame(args[2])) {
				if(args.length < 4) {
					p.sendMessage(PvPKit.prefix+"§cMerci de préciser un status ! (open|closed|maintenance)"); 
				} else {
					Game game = gamemg.getGame(args[2]);
					try {
						GameStatus status = GameStatus.valueOf(args[3].toUpperCase());
						if(game.isValid()){
							game.setStatus(status);
							gamemg.saveGames();
							p.sendMessage(PvPKit.prefix+"§aStatut de la partie §b"+game.getName()+" §adéfini avec succès !"); //$NON-NLS-1$ //$NON-NLS-2$
						} else {
							p.sendMessage(PvPKit.prefix+"§cImpossible de changer le statut de la partie tant qu'elle n'est pas valide !"); 
							p.sendMessage("§cMerci de corriger ces erreurs :");//$NON-NLS-1$
							for(String s : game.getErrors())
								p.sendMessage("§e"+s); //$NON-NLS-1$
						}
					} catch (Exception e) {
						p.sendMessage(PvPKit.prefix+"§cMerci de préciser un status valide ! (open|closed|maintenance)"); 
					}
				}

			} else {
				p.sendMessage(PvPKit.prefix+"§cCette partie n'existe pas !"); 
			}
			return true;
		
		} else if (args[1].equalsIgnoreCase("info")) { //$NON-NLS-1$
			if(args.length == 2) {
				p.sendMessage(PvPKit.prefix+"§cMerci de préciser une partie !"); 
			} else if(gamemg.containsGame(args[2])) {
				
				Game game = gamemg.getGame(args[2]);
				p.sendMessage("§e========[ §9"+game.getName()+" §e]========"); //$NON-NLS-1$ //$NON-NLS-2$
				p.sendMessage("§6Type : §b"+(game.getType() != null ? game.getType().toString() : "§cAucun")); //$NON-NLS-1$ //$NON-NLS-2$
				p.sendMessage("§6Status : §b"+(game.getStatus() != null ? game.getStatus().toString() : "§cAucun")); //$NON-NLS-1$ //$NON-NLS-2$
				p.sendMessage("§6Arène : §b"+(game.getArena() != null ? game.getArena().getName() : "§cAucune")); //$NON-NLS-1$ //$NON-NLS-2$
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
				p.sendMessage(PvPKit.prefix+"§cCette partie n'existe pas !"); 
			}
			return true;
			
		} else {
			p.sendMessage(PvPKit.prefix+"§cCommande inconnue !"); 
			p.sendMessage(PvPKit.prefix + "§cPour une liste des commandes disponibles, entrez &b/pka game"); //$NON-NLS-1$
			return false;
		}
		return false;
	}
	
}
