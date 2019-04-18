package com.alchemi.tbb.gui;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.Inventory;

import com.alchemi.tbb.main;

public class GuiListener implements Listener {

	private final HashMap<String, GuiBase> guis = new HashMap<String, GuiBase>();
	
	@SuppressWarnings("unused")
	private final main plugin; 
	
	public GuiListener(main plug) {
		plugin = plug;
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		
		Player pl = (Player) e.getWhoClicked();
		Inventory inv = e.getInventory();
		
		if (guis.containsKey(inv.getName()) && e.getSlotType() != SlotType.OUTSIDE) {
			
			try {
				guis.get(inv.getName()).onClicked(e.getSlot(), pl);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| InstantiationException e1) {
				e1.printStackTrace();
			}
			
			e.setCancelled(true);
			
		}
	}
	
	public void registerGui(GuiBase gui) {
		guis.put(gui.getGuiName(), gui);
	}
	
	public GuiBase getGui(String name) {
		if (guis.containsKey(name)) return guis.get(name);
		return null;
	}
	
}
