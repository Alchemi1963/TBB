package com.alchemi.tbb.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;

import com.alchemi.al.Messenger;
import com.alchemi.al.SexyRunnable;
import com.alchemi.tbb.main;
import com.alchemi.tbb.items.ItemFactory;

public class MapGui extends GuiBase{

	public MapGui(main plugin, String name, int size) {
		
		super(plugin, name, size);
		main.instance.guiListener.registerGui(this);
		setContents();
		setCommands();
	}

	@Override
	void setContents() {
		
		ItemStack createMapStack = ItemFactory.getItemStack(Material.MAP, "&d&l&oCREATE MAP");
		ItemStack toMapStack = ItemFactory.getItemStack(Material.COMPASS, "&d&l&oTELEPORT TO MAP");
		ItemStack deleteMapStack = ItemFactory.getItemStack(Material.BARRIER, "&4&o&lDELETE MAP");
		ItemStack listMapStack = ItemFactory.getItemStack(Material.BOOK, "&d&o&lLIST MAPS", new ArrayList<String>() {{
			add("List all maps");
		}}, Enchantment.CHANNELING, false, 1);
		
		ItemStack currentMap = ItemFactory.getItemStack(Material.GRASS_BLOCK, "current map");
		
		contents.put(2, createMapStack);
		contents.put(3, toMapStack);
		contents.put(4, deleteMapStack);
		contents.put(8, listMapStack);
		contents.put(9, currentMap);
		
	}

	@Override
	void setCommands() {
		commands.put(contents.get(2), createMap);
		commands.put(contents.get(3), toMap);
		commands.put(contents.get(4), deleteMap);
		commands.put(contents.get(8), listMaps);
		
		commands.put(contents.get(9), new SexyRunnable() {
			
			@Override
			public void run(Object... arg0) {
				
				Player pl = (Player)arg0[0];
				pl.sendMessage(pl.getWorld().getName());
				
			}
		});
		
		arguments.put(contents.get(2), new Object[] {"<player>"});
		arguments.put(contents.get(3), new Object[] {"<player>"});
		arguments.put(contents.get(4), new Object[] {"<player>"});
		arguments.put(contents.get(8), new Object[] {"<player>"});
		arguments.put(contents.get(9), new Object[] {"<player>"});
	}
	
	SexyRunnable createMap = new SexyRunnable() {
		
		@Override
		public void run(Object... arg0) {
			//<player>
			Messenger.sendMsg("&dPlease type the &amap name&d:", (Player) arg0[0]); 
			
			Listener listener = new Listener() {
				
				Player listen = (Player) arg0[0];
				
				@EventHandler
				public void onChat(AsyncPlayerChatEvent e){
					
					if (!e.getPlayer().equals(listen)) return;
					
					String name = e.getMessage().replaceAll("&[0123456789abcdefklmnor]", "");
					
					plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
						@Override public void run() {
							
							listen.performCommand("tbb maps create " + name);
							
						} 
					});
					
					e.setCancelled(true);
					HandlerList.unregisterAll(this);
				}
				
			};
			plugin.getServer().getPluginManager().registerEvents(listener, plugin);
		}
	};
	
	SexyRunnable toMap = new SexyRunnable() {
		
		@Override
		public void run(Object... arg0) {
			
			plugin.tpGui.resetContents();
			plugin.tpGui.openGUI((Player)arg0[0]);
			
		}
	};
	
	SexyRunnable deleteMap = new SexyRunnable() {
		
		@Override
		public void run(Object... arg0) {
			
			Player player = (Player) arg0[0];
			
			World w = player.getWorld();
			
			String mapName = w.getName().replace("plugins\\TheBeastBelow\\maps\\", "");
			
			plugin.mapReg.deleteMap(mapName);
			Messenger.sendMsg("Maps.Deleted", player, new HashMap<String, String>(){
				{
					put("$name$", mapName);
				}
			});
			
		}
	};
	
	SexyRunnable importMap = new SexyRunnable() {
		
		@Override
		public void run(Object... arg0) {
			// TODO Auto-generated method stub
			
		}
	};
	
	SexyRunnable unloadMap = new SexyRunnable() {
		
		@Override
		public void run(Object... arg0) {
			// TODO Auto-generated method stub
			
		}
	};
	
	SexyRunnable listMaps = new SexyRunnable() {
		
		@Override
		public void run(Object... arg0) {
			Player pl = (Player) arg0[0];
			
			String keys = "";
			for (String key : plugin.mapReg.getMaps().keySet()) {
				if (keys == "") keys = key;
				else keys += ", " + key;
			}
			Messenger.sendMsg(keys, pl);
			
		}
	};
}

