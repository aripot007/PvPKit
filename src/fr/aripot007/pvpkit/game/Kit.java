package fr.aripot007.pvpkit.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@SerializableAs("Kit")
public class Kit implements ConfigurationSerializable {

	private String name;
	private List<PotionEffect> effects = new ArrayList<PotionEffect>();
	private boolean regiven = false;
	private byte regiveKills = 3;
	private ItemStack icon;
	private ItemStack[] inventoryContent;
	
	public Kit(String name){
		this.name = name;
	}

	public Kit(String name, ItemStack icon, ItemStack[] content, List<PotionEffect> effects, boolean regiven, byte regiveKills) {
		this.name = name;
		this.icon = icon;
		this.inventoryContent = content;
		this.effects = effects;
		this.regiven = regiven;
		this.regiveKills = regiveKills;
	}
	
	public boolean isValid() {
		return (inventoryContent != null && inventoryContent.length > 0 
				&& !inventoryContent.equals(new ItemStack[41]) && icon != null);
	}

	public List<String> getErrors(){
		List<String> errors = new ArrayList<String>();
		if(name == null || name.equals(""))
			errors.add("Le nom du kit n'est pas valide.");
		if(icon == null || icon.getType().isAir())
			errors.add("Le kit n'a pas d'icone valide");
		if(inventoryContent == null || inventoryContent.equals(new ItemStack[41]))
			errors.add("Le kit ne contient rien.");
		return errors;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public ItemStack getIcon() {
		return icon;
	}
	public void setIcon(ItemStack icon) {
		this.icon = icon;
	}

	public ItemStack[] getInventoryContent() {
		return inventoryContent;
	}
	public void setInventoryContent(ItemStack[] inventoryContent) {
		this.inventoryContent = inventoryContent;
	}

	public List<PotionEffect> getEffects() {
		return effects;
	}
	
	public void setEffects(List<PotionEffect> effects) {
		this.effects = effects;
	}
	
	public void addEffect(PotionEffect effect) {
		effects.add(effect);
	}
	
	public void removeEffect(PotionEffectType etype) {
		for(PotionEffect e : effects) {
			if(e.getType().equals(etype)) {
				effects.remove(e);
				return;
			}
		}
	}

	
	public boolean isRegiven() {
		return regiven;
	}

	public void setRegiven(boolean regiven) {
		this.regiven = regiven;
	}

	public byte getRegiveKills() {
		return regiveKills;
	}

	public void setRegiveKills(byte regiveKills) {
		this.regiveKills = regiveKills;
	}

	@Override
	public Map<String, Object> serialize() {
		
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> inv = new HashMap<String, Object>();
		result.put("name", name); //$NON-NLS-1$
		result.put("effects", effects);
		result.put("regiven", regiven);
		result.put("regive-kills", regiveKills);
		result.put("icon", icon); //$NON-NLS-1$
		if(inventoryContent != null) {
			for(int i = 0; i < inventoryContent.length; i++) {
				if(inventoryContent[i] != null)
					inv.put(""+i, inventoryContent[i]); //$NON-NLS-1$
			}
			result.put("items", inv); //$NON-NLS-1$
		} else {
			result.put("items", null); //$NON-NLS-1$
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public static Kit deserialize(Map<String, Object> map) {
		ItemStack icon = (ItemStack) map.get("icon"); //$NON-NLS-1$
		ItemStack[] content = new ItemStack[41];
		Map<String, Object> inv = (Map<String, Object>) map.get("items"); //$NON-NLS-1$
		if(inv != null && !inv.isEmpty()) {
			for(Entry<String, Object> item : inv.entrySet()) {
				content[Integer.parseInt(item.getKey())] = (ItemStack) item.getValue();
			}
		}
		String name = (String) map.get("name"); //$NON-NLS-1$
		List<PotionEffect> effects = (List<PotionEffect>) map.getOrDefault("effects", null);
		boolean regiven = (boolean) map.getOrDefault("regiven", false);
		byte regivenKills = (byte) map.getOrDefault("regiven-kills", 3);
		return new Kit(name, icon, content, effects, regiven, regivenKills);
	}
	
}
