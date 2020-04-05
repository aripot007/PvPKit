package fr.aripot007.pvpkit.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import fr.aripot007.pvpkit.manager.KitManager;
import fr.aripot007.pvpkit.util.Messages;

@SerializableAs("Arena")
public class Arena implements ConfigurationSerializable {
	
	private Location spawn;
	private String name;
	private Set<String> kits;
	
	public Arena(String name) {
		this.spawn = null;
		this.name = name;
		this.kits = KitManager.kits.keySet();
	}
	
	public Arena(String name, Location spawn,Set<String> kits) {
		this.spawn = spawn;
		this.name = name;
		this.kits = kits;
	}

	public boolean isValid() {
		if(spawn != null && !kits.isEmpty()) {
			for(String s : kits) {
				if(!KitManager.kits.containsKey(s))
					return false;
			}
			return true;
		} else {
			return false;
		}
	}
	
	public List<String> getErrors() {
		List<String> errors = new ArrayList<String>();
		if(spawn == null)
			errors.add(Messages.getString("errors.arena.no_spawn")); //$NON-NLS-1$
		if(kits.isEmpty())
			errors.add(Messages.getString("errors.arena.no_kit")); //$NON-NLS-1$
		for(String s : kits) {
			if(!KitManager.kits.containsKey(s))
				errors.add("Le kit "+s+" n'existe pas");
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

	public Set<String> getKits() {
		return kits;
	}

	public void setKits(Set<String> kits) {
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

	
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("name", name); //$NON-NLS-1$
		result.put("spawn", spawn); //$NON-NLS-1$
		result.put("kits", kits); //$NON-NLS-1$
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public static Arena deserialize(Map<String, Object> map) {
		String name = (String) map.get("name"); //$NON-NLS-1$
		Location spawn = (Location) map.get("spawn"); //$NON-NLS-1$
		Set<String> kits = (Set<String>) map.get("kits"); //$NON-NLS-1$
		return new Arena(name, spawn, kits);
	}
	
}