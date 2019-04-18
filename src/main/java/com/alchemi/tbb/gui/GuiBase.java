package com.alchemi.tbb.gui;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.alchemi.al.SexyRunnable;
import com.alchemi.tbb.main;

public abstract class GuiBase {
	
	protected String guiName = "";
	protected int guiSize = 0;
	protected static main plugin;
	private Inventory gui;
	
	protected HashMap<Integer, ItemStack> contents = new HashMap<Integer, ItemStack>();
	protected HashMap<ItemStack, SexyRunnable> commands = new HashMap<ItemStack, SexyRunnable>();
	protected HashMap<ItemStack, Object[]> arguments = new HashMap<ItemStack, Object[]>();
	
	public GuiBase(main plugin, String name, int size) {
		GuiBase.plugin = plugin;
		guiName = name;
		guiSize = size;
		gui = Bukkit.createInventory(null, guiSize, guiName);
		
	}
	
	public void openGUI(Player pl) {
		
		System.out.println(contents);
		for (int slot = 0; slot < guiSize; slot++) {
			if (contents.containsKey(slot)) gui.setItem(slot, contents.get(slot));
			else gui.setItem(slot, new ItemStack(Material.BLUE_STAINED_GLASS_PANE));
			
		}
		
		pl.openInventory(gui);
	}
	
	public void onClicked(int slot, Player pl) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
		if (!contents.containsKey(slot)) pl.playSound(pl.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0F, 1.0F);
		
		else {
		
			ItemStack item = contents.get(slot);
			SexyRunnable m = commands.get(item);
			if (arguments.containsKey(item)) {
				Object[] args = arguments.get(item);
				
				int i = 0;
				for (Object arg : arguments.get(item)) {
					if (arg.equals("<player>")) {
						args[i] = pl;
					}
					i++;
				}
				
				m.run(args);
			}
			else m.run();
			
		}
	}
	
	abstract void setContents();
	abstract void setCommands();
	
	public String getGuiName() {
		return guiName;
	}
	
	public int getGuiSize() {
		return guiSize;
	}
}
