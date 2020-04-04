package fr.aripot007.pvpkit.game;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

public class Kit {

	private String name;
	private String iconName;
	private List<String> iconLore;
	private Material iconItem;
	private PlayerInventory inventory;
	
	public Kit(String name){
		this.name = name;
		this.iconName = null;
		this.iconLore = null;
		this.iconItem = null;
		this.inventory = null;
	}

	public Kit(PlayerInventory inventory, String name, String iconName, List<String> iconLore, Material iconItem) {
		this.inventory = inventory;
		this.name = name;
		this.iconName = iconName;
		this.iconLore = iconLore;
		this.iconItem = iconItem;
	}
	
	public boolean isValid() {
		return (inventory != null && inventory.getContents().length > 0 
				&& iconItem != null && iconName != null && !iconName.equals(""));
	}
	
	public ItemStack getIcon() {
		ItemStack item = new ItemStack(iconItem);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(iconName);
		meta.setLore(iconLore);
		item.setItemMeta(meta);
		return item;
	}

	public PlayerInventory getInventory() {
		return inventory;
	}
	public void setInventory(PlayerInventory inventory) {
		this.inventory = inventory;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getIconName() {
		return iconName;
	}
	public void setIconName(String iconName) {
		this.iconName = iconName;
	}

	public List<String> getIconLore() {
		return iconLore;
	}
	public void setIconLore(List<String> iconLore) {
		this.iconLore = iconLore;
	}

	public Material getIconItem() {
		return iconItem;
	}
	public void setIconItem(Material iconItem) {
		this.iconItem = iconItem;
	}
	
}
