package com.alchemi.tbb.gui;

import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.alchemi.al.SexyRunnable;
import com.alchemi.tbb.main;
import com.alchemi.tbb.items.ItemFactory;
import com.alchemi.tbb.maps.MapHandler;
import com.alchemi.tbb.maps.MapRegistry;

public class TPGui extends GuiBase {

	public TPGui(main plugin, String name, int size) {
		super(plugin, name, size);
		plugin.guiListener.registerGui(this);
		
	}
	
	public void resetContents() {
		
		int s = 9;
		for (Entry<String, World> ent : plugin.mapReg.getMaps().entrySet()) {
			contents.put(s, ItemFactory.getItemStack(Material.GRASS_BLOCK, "&d" + ent.getKey()));
			commands.put(contents.get(s), new SexyRunnable() {
				
				@Override
				public void run(Object... arg0) {
					
					MapHandler mh = MapRegistry.getMapHandler(ent.getKey());
					mh.gotoWorld((Player)arg0[0]);
					System.out.println(((Player)arg0[0]).getLocation().getWorld().getName());
						
				}
			});
			arguments.put(contents.get(s), new Object[] {"<player>"});
			s++;
		}
		
	}
	
	@Override
	public void openGUI(Player pl) {
		resetContents();
		super.openGUI(pl);
	}
	
	@Override
	void setContents() {}
	
	

	@Override
	void setCommands() {}
}
