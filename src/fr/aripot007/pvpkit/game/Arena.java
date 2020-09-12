package fr.aripot007.pvpkit.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import fr.aripot007.pvpkit.PvPKit;

/**
 * An arena with customizable settings.
 * 
 * @author Aristide
 *
 */
@SerializableAs("Arena")
public class Arena implements ConfigurationSerializable {
	
	/** The location where the player spawn */
	private Location spawn;
	
	/** The name of the arena used to identify it */
	private String name;
	
	/**
	 * A list of enabled kits in this arena
	 */
	private List<String> kits;
	
	public Arena(String name) {
		this.spawn = null;
		this.name = name;
		this.kits = new ArrayList<String>(PvPKit.getInstance().getKitManager().getKitsKeySet());
	}
	
	public Arena(String name, Location spawn,List<String> kits) {
		this.spawn = spawn;
		this.name = name;
		this.kits = kits;
	}

	/**
	 * Check if the arena settings are valid and the arena is ready to be used.
	 */
	public boolean isValid() {
		if(spawn != null && !kits.isEmpty()) {
			for(String s : kits) {
				if(!PvPKit.getInstance().getKitManager().containsKit(s) || !PvPKit.getInstance().getKitManager().getKit(s).isValid())
					return false;
			}
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Get the errors in the configuration of this arena.
	 * Return an empty list if there is no error.
	 * @return A list of the errors.
	 */
	public List<String> getErrors() {
		List<String> errors = new ArrayList<String>();
		if(spawn == null)
			errors.add("Le spawn n'est pas défini.");
		if(kits.isEmpty())
			errors.add("L'arène n'a aucun kit autorisé.");
		for(String s : kits) {
			if(!PvPKit.getInstance().getKitManager().containsKit(s))
				errors.add("Le kit "+s+" n'existe pas");
			else if(!PvPKit.getInstance().getKitManager().getKit(s).isValid()) {
				errors.add("Le kit "+s+" n'est pas valide");
			}
		}
		return errors;
	}
	
	public Location getSpawn() {
		return spawn;
	}

	public void setSpawn(Location spawn) {
		this.spawn = spawn;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getKits() {
		return kits;
	}

	public void setKits(List<String> kits) {
		this.kits = kits;
	}
	
	public void addKit(String kit) {
		if(!kits.contains(kit)){
			kits.add(kit);
		}
		return;
	}

	public void removeKit(String kit) {
		if(kits.contains(kit)){
			kits.remove(kit);
		}
		return;
	}

	/** Serialize this arena to a map */
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("name", name);
		result.put("spawn", spawn);
		result.put("kits", kits.toArray());
		return result;
	}
	
	/** Create an arena from a map */
	@SuppressWarnings("unchecked")
	public static Arena deserialize(Map<String, Object> map) {
		String name = (String) map.get("name");
		Location spawn = (Location) map.get("spawn");
		List<String> kits = (List<String>)map.get("kits");
		return new Arena(name, spawn, kits);
	}
	
}