package com.alchemi.tbb.gui;

import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.alchemi.tbb.main;
import com.alchemi.tbb.maps.MapHandler;

import me.alchemi.al.objects.GUI.GUIBase;
import me.alchemi.al.objects.GUI.GUIListener;
import me.alchemi.al.objects.handling.ItemFactory;
import me.alchemi.al.objects.handling.SexyRunnable;

public class TPGui extends GUIBase {

	public TPGui(Player player) {
		super(main.getInstance(), "Teleport", 27, player, player);
		
		new GUIListener(main.getInstance(), this);
		
		setContents();
		setCommands();
		openGUI();
		
	}
	
	@Override
	public void setContents() {
		
		int s = 9;
		for (Entry<String, World> ent : main.getInstance().getMapReg().getMaps().entrySet()) {
			contents.put(s, new ItemFactory(Material.GRASS_BLOCK).setName("&d" + ent.getKey()));
			commands.put(s, new SexyRunnable() {
				
				@Override
				public void run(Object... arg0) {
					
					MapHandler mh = main.getInstance().getMapReg().getMapHandler(ent.getKey());
					mh.gotoWorld((Player)arg0[0]);
					System.out.println(((Player)arg0[0]).getLocation().getWorld().getName());
						
				}
			});
			arguments.put(s, new Object[] {"<player>"});
			s++;
		}
		
	}

	@Override
	public void onClose() {}

	@Override
	public void setCommands() {}
}
