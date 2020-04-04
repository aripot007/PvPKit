package fr.aripot007.pvpkit.game;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;

public class Arena {
	
	private Location spawn;
	private String name;
	private Set<Kit> kits;
	
	public Arena(String name) {
		this.spawn = null;
		this.name = name;
		this.kits = new HashSet<Kit>();
	}
	
	public Arena(Location spawn, String name, Set<Kit> kits) {
		this.spawn = spawn;
		this.name = name;
		this.kits = kits;
	}

	public boolean isValid() {
		return (spawn != null && !kits.isEmpty());
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

	public Set<Kit> getKits() {
		return kits;
	}

	public void setKits(Set<Kit> kits) {
		this.kits = kits;
	}
	
	public void addKit(Kit kit) {
		if(!kits.contains(kit)){
			kits.add(kit);
		}
		return;
	}

	public void removeKit(Kit kit) {
		if(kits.contains(kit)){
			kits.remove(kit);
		}
		return;
	}
	
}