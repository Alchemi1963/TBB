package com.alchemi.tbb.maps;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.alchemi.tbb.main;

public class MapRegistry {

	private HashMap<String, World> maps = new HashMap<String, World>();
	private HashMap<String, MapHandler> mapHandlers = new HashMap<String, MapHandler>();
	
	private FileConfiguration registry;
	
	private File mapFolder;
	private File dataFolder;
	
	private main plug;
	
	public MapRegistry(FileConfiguration reg, main plug) {
		
		this.plug = plug;
		registry = reg;
		dataFolder = plug.getDataFolder();
		mapFolder = new File(plug.getDataFolder(), "maps");
		
		for (String ent : reg.getValues(false).keySet()) {
			if (new File(reg.getString(ent)).exists()) {
				
				maps.put(ent, Bukkit.getServer().createWorld(WorldCreator.name(reg.getString(ent))));
				mapHandlers.put(ent, new MapHandler(maps.get(ent)));
				plug.getMessenger().print("&4World &9" + ent + " &4loaded!");
				
			}
		}
		
	}
	
	public void saveWorlds() {
		for (World w : maps.values()) {
			w.save();
		}
	}
	
	public void updateRegistry(FileConfiguration reg) {
		registry = reg;
		
		for (String ent : reg.getValues(false).keySet()) {
			if (new File(reg.getString(ent)).exists()) {
				
				maps.put(ent, Bukkit.getServer().createWorld(WorldCreator.name(reg.getString(ent))));
				mapHandlers.put(ent, new MapHandler(maps.get(ent)));
				plug.getMessenger().print("&4World &9" + ent + " &4loaded!");
				
			}
		}
	}
	
	public void deleteMap(String world) { 
		
		MapHandler mh = mapHandlers.get(world);
		mh.delete();
		maps.remove(world);
		mapHandlers.remove(world);
		registry.set(world, null);
		try { registry.save(new File(dataFolder, "maps.yml")); } catch (IOException e) {}
		
	}
	
	public void emptyRegistry() {
		maps = new HashMap<String, World>();
		registry = new YamlConfiguration();
	}
	
	public void registerMap(String name, World world) throws IOException {
		maps.put(name, world);
		mapHandlers.put(name, new MapHandler(world));
		registry.set(name, dataFolder.getParentFile().getParentFile().toPath().relativize(new File(mapFolder, name).toPath()).toString());
		registry.save(new File(dataFolder, "maps.yml"));
	}
	
	public MapHandler registerMap(String name) throws IOException {
		if (!new File(mapFolder, name).exists()) new File(mapFolder, name).mkdir();
		
		System.out.println("Creating...");
		WorldCreator wc = WorldCreator.name(dataFolder.getParentFile().toPath().relativize(new File(mapFolder, name).toPath()).toString());
		wc.type(WorldType.FLAT);
		wc.generator(new EmptyGenerator());
		World w = wc.createWorld();
		
		w.setSpawnLocation(0, 64, 0);
		for (int x = -3; x < 4; x++) {
			for (int z = -3; z < 4; z++) {
				w.getBlockAt(x, 64, z).setType(Material.PINK_CONCRETE);
			}
		}
		
		MapHandler h = new MapHandler(w);
		
		System.out.println("Registering...");
		maps.put(name, w);
		mapHandlers.put(name, h);
		System.out.println(dataFolder.getParentFile().getParentFile());
		System.out.println("Saving..." + new File(mapFolder, name).toString().replaceAll(".*(?=plugins)", ""));
		registry.set(name, dataFolder.getParentFile().getParentFile().toPath().relativize(new File(mapFolder, name).toPath()).toString());
		registry.save(new File(dataFolder, "maps.yml"));
		
		return h;
	}
	
	public MapHandler getMapHandler(String name) {
		if (!mapHandlers.containsKey(name)) return null;
		
		return mapHandlers.get(name);
	}
	
	public MapHandler getMapHandler(World world) {
		if (!maps.containsValue(world)) return null;
		
		for (Entry<String, World> entry : maps.entrySet()) {
			if (entry.getValue().equals(world)) return mapHandlers.get(entry.getKey());
		}
		
		return null;
	}
	
	public HashMap<String, World> getMaps() {
		return maps;
	}
}
