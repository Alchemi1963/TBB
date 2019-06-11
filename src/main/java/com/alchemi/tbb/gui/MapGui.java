package com.alchemi.tbb.gui;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;

import com.alchemi.tbb.Config.Messages;
import com.alchemi.tbb.main;

import me.alchemi.al.configurations.Messenger;
import me.alchemi.al.objects.GUI.GUIBase;
import me.alchemi.al.objects.handling.ItemFactory;
import me.alchemi.al.objects.handling.SexyRunnable;

public class MapGui extends GUIBase{

	public MapGui(Player player) {
		
		super(main.getInstance(),"Map Handling", 27, player, player);
		
		setContents();
		setCommands();
		openGUI();
	}

	@Override
	public void setContents() {
		
		ItemStack createMapStack = new ItemFactory(Material.MAP).setName("&d&l&oCREATE MAP");
		ItemStack toMapStack = new ItemFactory(Material.COMPASS).setName("&d&l&oTELEPORT TO MAP");
		ItemStack deleteMapStack = new ItemFactory(Material.BARRIER).setName("&4&o&lDELETE MAP");
		ItemStack listMapStack = new ItemFactory(Material.BOOK)
				.setName("&d&o&lLIST MAPS")
				.setLore(new ArrayList<String>() {
					{
						add("List all maps");
					}})
				.addEnch(Enchantment.CHANNELING, false);
		ItemStack currentMap = new ItemFactory(Material.GRASS_BLOCK).setName("current map");
		
		contents.put(2, createMapStack);
		contents.put(3, toMapStack);
		contents.put(4, deleteMapStack);
		contents.put(8, listMapStack);
		contents.put(9, currentMap);
		
	}

	@Override
	public void setCommands() {
		commands.put(2, createMap);
		commands.put(3, toMap);
		commands.put(4, deleteMap);
		commands.put(8, listMaps);
		
		commands.put(9, new SexyRunnable() {
			
			@Override
			public void run(Object... arg0) {
				
				Player pl = (Player)arg0[0];
				pl.sendMessage(pl.getWorld().getName());
				
			}
		});
		
		arguments.put(2, new Object[] {"<player>"});
		arguments.put(3, new Object[] {"<player>"});
		arguments.put(4, new Object[] {"<player>"});
		arguments.put(8, new Object[] {"<player>"});
		arguments.put(9, new Object[] {"<player>"});
	}
	
	SexyRunnable createMap = new SexyRunnable() {
		
		@Override
		public void run(Object... arg0) {
			//<player>
			Player listen = (Player)arg0[0];
			listen.sendMessage(Messenger.formatString("&dPlease type the &amap name&d:")); 
			
			Listener listener = new Listener() {
				
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
			
			new TPGui((Player)arg0[0]);
			
		}
	};
	
	SexyRunnable deleteMap = new SexyRunnable() {
		
		@Override
		public void run(Object... arg0) {
			
			Player player = (Player) arg0[0];
			
			World w = player.getWorld();
			
			String mapName = w.getName().replace("plugins\\TheBeastBelow\\maps\\", "");
			
			main.getInstance().getMapReg().deleteMap(mapName);
			sender.sendMessage(Messenger.formatString(Messages.MAPS_DELETED.value()
					.replace("$name$", mapName)));
			
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
			for (String key : main.getInstance().getMapReg().getMaps().keySet()) {
				if (keys == "") keys = key;
				else keys += ", " + key;
			}
			pl.sendMessage(keys);
			
		}
	};

	@Override
	public void onClose() {
		// TODO Auto-generated method stub
		
	}
}

