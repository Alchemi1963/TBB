package com.alchemi.tbb.items;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.alchemi.al.Messenger;

public class ItemFactory {

	public static ItemStack getItemStack(Material type, String name) { return getItemStack(type, name, null, null, false, 1); }
	
	public static ItemStack getItemStack(Material type, String name, int amount) { return getItemStack(type, name, null, null, false, amount); }
	
	public static ItemStack getItemStack(Material type, String name, List<String> lore) { return getItemStack(type, name, lore, null, false, 1); }
	
	public static ItemStack getItemStack(Material type, String name, List<String> lore, Enchantment ench, boolean eVisible, int amount) {
		ItemStack o = new ItemStack(type, amount);
		ItemMeta meta = o.getItemMeta();
		meta.setDisplayName(Messenger.cc(name));
		meta.setLore(lore);
		if (ench != null) {
			meta.addEnchant(ench, 1, false);
			if (!eVisible) meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		}
		
		o.setItemMeta(meta);
		return o;
	}
	
}
