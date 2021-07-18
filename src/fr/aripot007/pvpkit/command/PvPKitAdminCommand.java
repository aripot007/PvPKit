package fr.aripot007.pvpkit.command;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.aripot007.pvpkit.GameController;
import fr.aripot007.pvpkit.PvPKit;
import fr.aripot007.pvpkit.game.Arena;
import fr.aripot007.pvpkit.game.Game;
import fr.aripot007.pvpkit.game.GameStatus;
import fr.aripot007.pvpkit.game.GameType;
import fr.aripot007.pvpkit.game.Kit;
import fr.aripot007.pvpkit.game.PvPKitPlayer;
import fr.aripot007.pvpkit.game.Session;
import fr.aripot007.pvpkit.game.SessionStatus;
import fr.aripot007.pvpkit.manager.ArenaManager;
import fr.aripot007.pvpkit.manager.GameManager;
import fr.aripot007.pvpkit.manager.KitManager;
import fr.aripot007.pvpkit.manager.PvPKitPlayerManager;
import fr.aripot007.pvpkit.manager.SessionManager;
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
	private SessionManager sessmg;
	private GameController controller;
	private PvPKitPlayerManager playermg;
	
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM HH-mm-ss");
	
	
	public PvPKitAdminCommand(KitManager kitmg, ArenaManager armg, GameManager gamemg, SessionManager sessmg, GameController controller, PvPKitPlayerManager playermg) {
		this.kitmg = kitmg;
		this.armg = armg;
		this.gamemg = gamemg;
		this.sessmg = sessmg;
		this.controller = controller;
		this.playermg = playermg;
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
			
		} else if(args[0].equalsIgnoreCase("session")) {
			
			return onSessionCommand(sender, cmd, msg, args);
			
		} else if(args[0].equalsIgnoreCase("help")) {
			
			Player p = (Player) sender;
			
			p.sendMessage("§e========[ §9/pka §e]========"); //$NON-NLS-1$
			p.sendMessage("§6Commandes disponibles :"); //$NON-NLS-1$
			p.sendMessage("§b/pka kit"); //$NON-NLS-1$
			p.sendMessage("§b/pka game"); //$NON-NLS-1$
			p.sendMessage("§b/pka arena"); //$NON-NLS-1$
			p.sendMessage("§b/pka session"); //$NON-NLS-1$
			p.sendMessage("§b/pka help"); //$NON-NLS-1$
			p.sendMessage("§b/pka join <game> <player>"); //$NON-NLS-1$
			p.sendMessage("§b/pka leave <player>"); //$NON-NLS-1$
			p.sendMessage("§b/pka reload"); //$NON-NLS-1$
			p.sendMessage("§e========[ §9/pka §e]========"); //$NON-NLS-1$
			return true;
			
		} else if(args[0].equalsIgnoreCase("join")) {
			
			return onJoinCommand(sender, cmd, msg, args);
			
		} else if(args[0].equalsIgnoreCase("leave")) {
			
			return onLeaveCommand(sender, cmd, msg, args);
			
		} else if(args[0].equalsIgnoreCase("playerinfo")) {
			
			Player p = (Player) sender;
			
			PvPKitPlayer globalPlayer = playermg.getPlayer(p);
			
			p.sendMessage("§aInfo joueur :");
			p.sendMessage("§6Joueur global : §9"+globalPlayer);
			
			if (globalPlayer.isInGame()) {
				Game game = globalPlayer.getGame();
				p.sendMessage("§aIn game §6("+game.getName()+")");
				
				if (game.getStatus() == GameStatus.SESSION) {
					p.sendMessage("§a§lIn session");
					
					PvPKitPlayer sessionPlayer = sessmg.getSession(game).getPlayerManager().getPlayer(p);
					
					p.sendMessage("§6Joueur session : "+sessionPlayer);
					
				}
				
			} else {
				p.sendMessage("§cNot in game");
			}
			
		}  else if(args[0].equalsIgnoreCase("dumpdata")) {
			
			Player p = (Player) sender;
			
			p.sendMessage("§aPlayerManager :");
			p.sendMessage(playermg.toString());
			p.sendMessage("§aSessions playermanager :");
			for (Session s : sessmg.getSessions().values()) {
				p.sendMessage("§9"+s.getName() + " : §f"+s.getPlayerManager().toString());
			}
			
		} else if(args[0].equalsIgnoreCase("dumpdata")) {
			
			Player p = (Player) sender;
			
			p.sendMessage("§aPlayerManager :");
			p.sendMessage(playermg.toString());
			p.sendMessage("§aSessions playermanager :");
			for (Session s : sessmg.getSessions().values()) {
				p.sendMessage("§9"+s.getName() + " : §f"+s.getPlayerManager().toString());
			}
			
		} else {
			sender.sendMessage(PvPKit.prefix+"§cCommande inconnue !"); 
			sender.sendMessage(PvPKit.prefix + "§cPour une liste des commandes disponibles, entrez §b/pka help"); 
			return false;
		}
		return false;
	}
	
	/**
	 * Handle the command used to join a game
	 * @return A boolean indicating if the command execution was successful
	 */
	private boolean onJoinCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		
		if (args.length < 3) {
			sender.sendMessage(PvPKit.prefix+"§cArguments manquants ! Syntaxe : §b/pka join <partie> <pseudo>");
			return false;
		}
		
		// Try to get the player
		Player p = Bukkit.getPlayer(args[1]);
		
		if (p == null) {
			sender.sendMessage(PvPKit.prefix+"§cCe joueur n'existe pas ou n'est pas en ligne !");
			return false;
		}
		
		PvPKitPlayer player = playermg.getPlayer(p);
		
		// Try to get the game / the session
		
		if(gamemg.containsGame(args[2])) {
			
			Game game = gamemg.getGame(args[2]);
			
			if(!game.isValid()) {
				sender.sendMessage(PvPKit.prefix+"§cCette partie n'est pas valide !");
			} else if(!game.getStatus().equals(GameStatus.OPEN)) {
				sender.sendMessage(PvPKit.prefix+"§cCette partie n'est pas disponible !");
			} else {
				controller.joinGame(player, game);
			}
			
		} else if (sessmg.containsSession(args[2])) {
			
			// The player is trying to join a session
			
			Session session = sessmg.getSession(args[2]);
			
			if (!session.isValid()) {
				
				sender.sendMessage(PvPKit.prefix+"§cCette partie n'est pas valide !");
				
			} else if (session.getStatus() == SessionStatus.ERROR) {
				
				sender.sendMessage(PvPKit.prefix+"§cCette partie n'est pas valide !");
				
			} else {
				
				p.sendMessage(PvPKit.prefix+"§bUn administrateur vous a fait rejoindre une partie.");
				controller.joinSession(player, session);
				
			}
			
			
		} else {
		
			sender.sendMessage(PvPKit.prefix+"§cCette partie n'existe pas !");
		}
		
		return true;
					
	}

	private boolean onLeaveCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		
		if (args.length < 2) {
			sender.sendMessage(PvPKit.prefix+"§cArguments manquants ! Syntaxe : §b/pka leave <pseudo>");
			return false;
		}
		
		// Try to get the player
		Player p = Bukkit.getPlayer(args[1]);
		
		if (p == null) {
			sender.sendMessage(PvPKit.prefix+"§cCe joueur n'existe pas ou n'est pas en ligne !");
			return false;
		}
		
		PvPKitPlayer player = playermg.getPlayer(p);
		
		if(!player.isInGame()) {
			sender.sendMessage(PvPKit.prefix+"§cCe joueur n'est pas dans une partie !");
			return true;
		} else {
			p.sendMessage(PvPKit.prefix+"§bUn administrateur vous a fait quitter la partie.");
			controller.leaveGame(player);
			return true;
		}
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
	
	/**
	 * Handle the commands used to manage sessions
	 * @return A boolean indicating if the command execution was successful
	 */
	private boolean onSessionCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		Player p = (Player) sender;
		if(args.length == 1) {
			
			p.sendMessage("§e========[ §9/pka session §e]========"); //$NON-NLS-1$
			p.sendMessage("§6Commandes disponibles :"); //$NON-NLS-1$
			p.sendMessage("§b/pka session list"); //$NON-NLS-1$
			p.sendMessage("§b/pka session create <nom>"); //$NON-NLS-1$
			p.sendMessage("§b/pka session remove <nom>"); //$NON-NLS-1$
			p.sendMessage("§b/pka session setgame <session> <game>"); //$NON-NLS-1$
			p.sendMessage("§b/pka session addplayer <session> <pseudo>"); //$NON-NLS-1$
			p.sendMessage("§b/pka session rmplayer <session> <pseudo>"); //$NON-NLS-1$
			p.sendMessage("§b/pka session start <session>"); //$NON-NLS-1$
			p.sendMessage("§b/pka session stop <session>"); //$NON-NLS-1$
			p.sendMessage("§b/pka session pause <session>"); //$NON-NLS-1$
			p.sendMessage("§b/pka session reset <session>"); //$NON-NLS-1$
			p.sendMessage("§b/pka session savestats <session> [nom du fichier]"); //$NON-NLS-1$
			p.sendMessage("§b/pka session resetstats <session>"); //$NON-NLS-1$
			p.sendMessage("§b/pka session info <session>"); //$NON-NLS-1$
			p.sendMessage("§e========[ §9/pka session §e]========"); //$NON-NLS-1$
			return true;
				
		} else if (args[1].equalsIgnoreCase("list")){ //$NON-NLS-1$
			
			p.sendMessage("§e========[ §9Sessions §e]========"); //$NON-NLS-1$
			for(Session session : sessmg.getSessions().values()) {
				if(session.isValid()) {
					p.sendMessage("§a"+session.getName()+" ("+session.getStatus()+")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				} else {
					TextComponent txt = new TextComponent("§c"+session.getName()+" ("+session.getStatus()+")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
					String str = ""; //$NON-NLS-1$
					for(String s : session.getErrors()) {
						str	+= "§c"+s; //$NON-NLS-1$
						if(session.getErrors().indexOf(s)+1 < session.getErrors().size())
							str +="\n"; //$NON-NLS-1$
					}
					txt.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(str).create()));
					p.spigot().sendMessage(txt);
				}
			}
			p.sendMessage("§e========[ §9Sessions §e]========"); //$NON-NLS-1$
			return true;	
			
		} else if (args[1].equalsIgnoreCase("create")) { //$NON-NLS-1$
			
			if(args.length == 2) { // Pas de nom précisé
				p.sendMessage(PvPKit.prefix+"§cMerci de préciser un nom !"); 
				p.sendMessage(PvPKit.prefix+"§cSyntaxe : /pka session create <nom>"); //$NON-NLS-1$
				
			} else if(args.length > 3) { // Trop d'arguments
				p.sendMessage(PvPKit.prefix+"§cLe nom de la session ne doit pas contenir d'espaces !"); 
				
			} else {
				if(sessmg.containsSession(args[2])) { // Nom déjà pris
					p.sendMessage(PvPKit.prefix+"§cUne session avec le même nom existe déjà !"); 
				} else {
					sessmg.putSession(new Session(args[2]));
					sessmg.saveSessions();
					p.sendMessage(PvPKit.prefix+"§aSession §b"+args[2]+" §acréée avec succès !"); //$NON-NLS-1$ //$NON-NLS-2$
					p.sendMessage(PvPKit.prefix+"§6Définissez sa partie avec §2/pka session setgame "+args[2]+" <partie>"); //$NON-NLS-1$ //$NON-NLS-2$
					p.sendMessage(PvPKit.prefix+"§6Utilisez §2/pka session addplayer/rmplayer "+args[2]+" <pseudo> §6 pour ajouter/retirer des joueurs autorisés dans la session"); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
			return true;
			
		} else if (args[1].equalsIgnoreCase("remove")) { //$NON-NLS-1$
			if(args.length == 2) {
				p.sendMessage(PvPKit.prefix+"§cMerci de préciser une session !"); 
			} else {
				if(sessmg.containsSession(args[2])) {
					sessmg.removeSession(args[2]);
					sessmg.saveSessions();
					p.sendMessage(PvPKit.prefix+"§aSession §b"+args[2]+" §asupprimée avec succès !"); //$NON-NLS-1$ //$NON-NLS-2$
				} else {
					p.sendMessage(PvPKit.prefix+"§cCette session n'existe pas !"); 
				}
			}
			return true;
			
		} else if (args[1].equalsIgnoreCase("setgame")) { //$NON-NLS-1$
			if(args.length < 3) {
				p.sendMessage(PvPKit.prefix+"§cMerci de préciser une session !"); 
			} else if(sessmg.containsSession(args[2])) {
				if(args.length < 4) {
					p.sendMessage(PvPKit.prefix+"§cMerci de préciser une partie !"); 
				} else if (gamemg.containsGame(args[3])) {
					Session session = sessmg.getSession(args[2]);
					Game game = gamemg.getGame(args[3]);
					session.setGame(game);
					game.setStatus(GameStatus.SESSION);
					gamemg.saveGames();
					sessmg.saveSessions();
					p.sendMessage(PvPKit.prefix+"§aPartie de la session §b"+session.getName()+" §adéfinie avec succès !"); //$NON-NLS-1$ //$NON-NLS-2$
					
				} else {
					p.sendMessage(PvPKit.prefix+"§cCette partie n'existe pas !"); 
				}
					
			} else {
				p.sendMessage(PvPKit.prefix+"§cCette session n'existe pas !"); 
			}
			
			
		} else if (args[1].equalsIgnoreCase("addplayer")) { //$NON-NLS-1$
			if(args.length == 2) {
				p.sendMessage(PvPKit.prefix+"§cMerci de préciser une session !"); 
			} else if(sessmg.containsSession(args[2])) {
				if(args.length < 4) {
					p.sendMessage(PvPKit.prefix+"§cMerci de préciser un joueur ! (pseudo ou all pour tout les joueurs)"); 
				} else {
					Session session = sessmg.getSession(args[2]);
					String pseudo = args[3];
					
					if (pseudo.equals("all")) {
						session.allowPlayer(pseudo);
					} else {
						
						@SuppressWarnings("deprecation")
						OfflinePlayer player = Bukkit.getOfflinePlayer(pseudo);
						
						if (player == null) {
							p.sendMessage(PvPKit.prefix+"§cCe joueur n'existe pas !");
							return true;
						}
						
						if (session.getAllowedPlayers().contains(player.getUniqueId().toString())) {
							p.sendMessage(PvPKit.prefix+"§cCe joueur est déjà autorisé à rejoindre cette session !");
							return true;
						}
						
						session.allowPlayer(player.getUniqueId().toString());
						
					}
					
					sessmg.saveSessions();
					p.sendMessage(PvPKit.prefix+"§6"+pseudo+" §apeut maintenant rejoindre la session §b"+session.getName()); //$NON-NLS-1$ 
				}

			} else {
				p.sendMessage(PvPKit.prefix+"§cCette session n'existe pas !"); 
			}
			return true;

		} else if (args[1].equalsIgnoreCase("rmplayer")) { //$NON-NLS-1$
			if(args.length == 2) {
				p.sendMessage(PvPKit.prefix+"§cMerci de préciser une session !"); 
			} else if(sessmg.containsSession(args[2])) {
				if(args.length < 4) {
					p.sendMessage(PvPKit.prefix+"§cMerci de préciser un joueur ! (pseudo ou all pour tout les joueurs)"); 
				} else {
					Session session = sessmg.getSession(args[2]);
					String pseudo = args[3];
					
					if (pseudo.equals("all")) {
						session.disallowPlayer(pseudo);
					} else {
						
						@SuppressWarnings("deprecation")
						OfflinePlayer player = Bukkit.getOfflinePlayer(pseudo);
						
						if (player == null) {
							p.sendMessage(PvPKit.prefix+"§cCe joueur n'existe pas !");
							return true;
						}
						
						if (!session.getAllowedPlayers().contains(player.getUniqueId().toString())) {
							p.sendMessage(PvPKit.prefix+"§cCe joueur n'est pas autorisé à rejoindre cette session !");
							return true;
						}
						
						session.disallowPlayer(player.getUniqueId().toString());
						
					}
					
					sessmg.saveSessions();
					p.sendMessage(PvPKit.prefix+"§6"+pseudo+" §an'est plus autorisé à rejoindre la session §b"+session.getName()); //$NON-NLS-1$ 
				}

			} else {
				p.sendMessage(PvPKit.prefix+"§cCette session n'existe pas !"); 
			}
			return true;

		} else if (args[1].equalsIgnoreCase("start")) { //$NON-NLS-1$
			if(args.length == 2) {
				p.sendMessage(PvPKit.prefix+"§cMerci de préciser une session !"); 
			} else if(sessmg.containsSession(args[2])) {
					Session session = sessmg.getSession(args[2]);
					
					switch(session.getStatus()) {
					
					case ERROR:
						p.sendMessage(PvPKit.prefix+"§cImpossible de démarrer la session car elle n'est pas valide ! §8(Utilisez /pka session info "+session.getName()+" pour plus d'informations)"); 
						return true;
						
					case STARTED:
						p.sendMessage(PvPKit.prefix+"§cCette session est déjà démarrée !"); 
						return true;
					
					default:
						session.start();
						p.sendMessage(PvPKit.prefix+"§aSession §B"+session.getName()+" §adémarrée avec succès !"); 
						break;					
					}

			} else {
				p.sendMessage(PvPKit.prefix+"§cCette session n'existe pas !"); 
			}
			return true;
		
		} else if (args[1].equalsIgnoreCase("stop")) { //$NON-NLS-1$
			if(args.length == 2) {
				p.sendMessage(PvPKit.prefix+"§cMerci de préciser une session !"); 
			} else if(sessmg.containsSession(args[2])) {
					Session session = sessmg.getSession(args[2]);
					
					switch(session.getStatus()) {
					
					case STARTED:
						
						String filename;
						
						if (args.length > 3) {
							// We take the other parameters as the file name
							filename = String.join(" ", Arrays.copyOfRange(args, 3, args.length));
						} else {
							// We take the date and time as the file name
							filename = formatter.format(LocalDateTime.now());
						}
						
						session.stop(filename);
						p.sendMessage(PvPKit.prefix+"§aSession §B"+session.getName()+" §cstoppée §aavec succès !");
						p.sendMessage(PvPKit.prefix+"§aStatistiques enregistrées sous le nom de §6"+filename+".yml §a!");
						break;
						
					default:
						p.sendMessage(PvPKit.prefix+"§cCette session n'est pas démarrée !"); 
						break;					
					}

			} else {
				p.sendMessage(PvPKit.prefix+"§cCette session n'existe pas !"); 
			}
			return true;
		
		} else if (args[1].equalsIgnoreCase("pause")) { //$NON-NLS-1$
			if(args.length == 2) {
				p.sendMessage(PvPKit.prefix+"§cMerci de préciser une session !"); 
			} else if(sessmg.containsSession(args[2])) {
					Session session = sessmg.getSession(args[2]);
					
					switch(session.getStatus()) {
					
					case STARTED:
						session.pause();
						p.sendMessage(PvPKit.prefix+"§aSession §B"+session.getName()+" §amise en §9pause !");
						break;
						
					case PAUSED:
						session.pause();
						p.sendMessage(PvPKit.prefix+"§aSession §B"+session.getName()+" §asortie de pause !");
						break;
					
					default:
						p.sendMessage(PvPKit.prefix+"§cCette session n'est pas démarrée !"); 
						return true;					
					}

			} else {
				p.sendMessage(PvPKit.prefix+"§cCette session n'existe pas !"); 
			}
			return true;
		
		} else if (args[1].equalsIgnoreCase("reset")) { //$NON-NLS-1$
			if(args.length == 2) {
				p.sendMessage(PvPKit.prefix+"§cMerci de préciser une session !"); 
			} else if(sessmg.containsSession(args[2])) {
					Session session = sessmg.getSession(args[2]);
					
					session.reset();
					p.sendMessage(PvPKit.prefix+"§aSession §B"+session.getName()+" §aréinitialisée avec succès !"); 

			} else {
				p.sendMessage(PvPKit.prefix+"§cCette session n'existe pas !"); 
			}
			return true;
		
		}  else if (args[1].equalsIgnoreCase("savestats")) { //$NON-NLS-1$
			if(args.length == 2) {
				p.sendMessage(PvPKit.prefix+"§cMerci de préciser une session !"); 
			} else if(sessmg.containsSession(args[2])) {
				
					Session session = sessmg.getSession(args[2]);
					
					String filename;
			
					if (args.length > 3) {
						// We take the other parameters as the file name
						filename = String.join(" ", Arrays.copyOfRange(args, 3, args.length));
					} else {
						// We take the date and time as the file name
						filename = formatter.format(LocalDateTime.now());
					}
					
					session.saveStats(filename);
					p.sendMessage(PvPKit.prefix+"§aStatistiques de la session §B"+session.getName()+" §aenregistrées avec succès sous le nom de §6"+filename+".yml §a!"); 

			} else {
				p.sendMessage(PvPKit.prefix+"§cCette session n'existe pas !"); 
			}
			return true;
		
		} else if (args[1].equalsIgnoreCase("resetstats")) { //$NON-NLS-1$
			if(args.length == 2) {
				p.sendMessage(PvPKit.prefix+"§cMerci de préciser une session !"); 
			} else if(sessmg.containsSession(args[2])) {
					Session session = sessmg.getSession(args[2]);
					
					session.resetStats();
					p.sendMessage(PvPKit.prefix+"§aStatistiques de la session §B"+session.getName()+" §aréinitialisées avec succès !"); 

			} else {
				p.sendMessage(PvPKit.prefix+"§cCette session n'existe pas !"); 
			}
			return true;
		
		} else if (args[1].equalsIgnoreCase("info")) { //$NON-NLS-1$
			if(args.length == 2) {
				p.sendMessage(PvPKit.prefix+"§cMerci de préciser une session !"); 
			} else if(sessmg.containsSession(args[2])) {
				
				Session session = sessmg.getSession(args[2]);
				p.sendMessage("§e========[ §9"+session.getName()+" §e]========"); //$NON-NLS-1$ //$NON-NLS-2$
				p.sendMessage("§6Status : §b"+(session.getStatus() != null ? session.getStatus().toString() : "§cAucun")); //$NON-NLS-1$ //$NON-NLS-2$
				p.sendMessage("§6Partie : §b"+(session.getGame() != null ? session.getGame().getName() : "§cAucune")); //$NON-NLS-1$ //$NON-NLS-2$
				if(session.isValid()) {
					p.sendMessage("§6Valide : §aoui"); //$NON-NLS-1$
				} else {
					p.sendMessage("§6Valide : §cnon"); //$NON-NLS-1$
					p.sendMessage("§6Problèmes :"); //$NON-NLS-1$
					for(String s : session.getErrors())
						p.sendMessage(" §b- §c"+s); //$NON-NLS-1$
				}
				p.sendMessage("§6Joueurs autorisés : §b"+session.getAllowedPlayers().toString());
				p.sendMessage("§e========[ §9"+session.getName()+" §e]========"); //$NON-NLS-1$ //$NON-NLS-2$
				
			} else {
				p.sendMessage(PvPKit.prefix+"§cCette session n'existe pas !"); 
			}
			return true;
			
		} else {
			p.sendMessage(PvPKit.prefix+"§cCommande inconnue !"); 
			p.sendMessage(PvPKit.prefix + "§cPour une liste des commandes disponibles, entrez §b/pka session"); //$NON-NLS-1$
			return false;
		}
		return false;
	}
	
}
