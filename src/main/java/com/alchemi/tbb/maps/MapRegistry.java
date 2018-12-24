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
import org.bukkit.plugin.Plugin;

import com.alchemi.tbb.main;

public class MapRegistry {

	private static HashMap<String, World> maps = new HashMap<String, World>();
	private static HashMap<String, MapHandler> mapHandlers = new HashMap<String, MapHandler>();
	
	private FileConfiguration registry;
	
	private static File mapFolder;
	private static File dataFolder;
	
	public MapRegistry(FileConfiguration reg, Plugin plug) {
		
		registry = reg;
		dataFolder = plug.getDataFolder();
		mapFolder = new File(plug.getDataFolder(), "maps");
		
		for (String ent : reg.getValues(false).keySet()) {
			if (new File(reg.getString(ent)).exists()) {
				
				maps.put(ent, Bukkit.getServer().createWorld(WorldCreator.name(reg.getString(ent))));
				mapHandlers.put(ent, new MapHandler(maps.get(ent)));
				main.instance.messenger.print("&4World &9" + ent + " &4loaded!");
				
			}
		}
		
	}
	
	public void updateRegistry(FileConfiguration reg) {
		registry = reg;
		
		for (String ent : reg.getValues(false).keySet()) {
			if (new File(reg.getString(ent)).exists()) {
				
				maps.put(ent, Bukkit.getServer().createWorld(WorldCreator.name(reg.getString(ent))));
				mapHandlers.put(ent, new MapHandler(maps.get(ent)));
				main.instance.messenger.print("&4World &9" + ent + " &4loaded!");
				
			}
		}
	}
	
	public void deleteMap(String world) { 
		
		mapHandlers.get(world).delete();
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
		
		System.out.println("Saving..." + dataFolder.getParentFile().getParentFile().toPath().relativize(new File(mapFolder, name).toPath()).toString());
		registry.set(name, dataFolder.getParentFile().getParentFile().toPath().relativize(new File(mapFolder, name).toPath()).toString());
		registry.save(new File(dataFolder, "maps.yml"));
		
		return h;
	}
	
	public static MapHandler getMapHandler(String name) {
		if (!mapHandlers.containsKey(name)) return null;
		
		return mapHandlers.get(name);
	}
	
	public static MapHandler getMapHandler(World world) {
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