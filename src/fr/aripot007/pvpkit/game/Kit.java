package fr.aripot007.pvpkit.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.inventory.ItemStack;

import fr.aripot007.pvpkit.util.Messages;

@SerializableAs("Kit")
public class Kit implements ConfigurationSerializable {

	private String name;
	private ItemStack icon;
	private ItemStack[] inventoryContent;
	
	public Kit(String name){
		this.name = name;
	}

	public Kit(String name, ItemStack icon, ItemStack[] content) {
		this.name = name;
		this.icon = icon;
		this.inventoryContent = content;
	}
	
	public boolean isValid() {
		return (inventoryContent != null && inventoryContent.length > 0 
				&& !inventoryContent.equals(new ItemStack[41]) && icon != null);
	}

	public List<String> getErrors(){
		List<String> errors = new ArrayList<String>();
		if(name == null || name.equals(""))
			errors.add(Messages.getString("errors.kit.name"));
		if(icon == null || icon.getType().isAir())
			errors.add(Messages.getString("errors.kit.icon"));
		if(inventoryContent == null || inventoryContent.equals(new ItemStack[41]))
			errors.add(Messages.getString("errors.kit.content"));
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

	
	@Override
	public Map<String, Object> serialize() {
		
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> inv = new HashMap<String, Object>();
		result.put("name", name); //$NON-NLS-1$
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
		ItemStack icon = ItemStack.deserialize((Map<String, Object>) map.get("icon")); //$NON-NLS-1$
		ItemStack[] content = new ItemStack[41];
		Map<String, Object> inv = (Map<String, Object>) map.get("items"); //$NON-NLS-1$
		for(Entry<String, Object> item : inv.entrySet()) {
			content[Integer.parseInt(item.getKey())] = ItemStack.deserialize((Map<String, Object>) item.getValue());
		}
		String name = (String) map.get("name"); //$NON-NLS-1$
		return new Kit(name, icon, content);
	}
	
}
