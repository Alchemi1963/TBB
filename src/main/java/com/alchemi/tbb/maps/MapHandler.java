package com.alchemi.tbb.maps;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Container;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MapHandler {

	private final World world;
	
	private FileConfiguration mapConfiguration;
	private File mapConfigurationFile;
	private HashMap<Location, Inventory> chests = new HashMap<Location, Inventory>();
	
	private Random rand;
	
	public MapHandler(World world) {
		rand = new Random(1963);
		this.world = world;
		
		mapConfigurationFile = new File(new File(world.getName()), "configuration.yml");
		mapConfiguration = YamlConfiguration.loadConfiguration(mapConfigurationFile);
		
		if (!mapConfiguration.isSet("chests") || mapConfiguration.getList("chests") == null) {
			mapConfiguration.set("chests", chests.keySet().toArray(new Location[chests.size()]));
			try {
				mapConfiguration.save(mapConfigurationFile);
			} catch (IOException e) {}
			
			return;
		}
		
		for (Object key : mapConfiguration.getList("chests")) {
			
			if (key instanceof Location) {
			if (world.getBlockAt((Location)key).getState() instanceof Container) chests.put((Location) key, ((Container)world.getBlockAt((Location)key).getState()).getInventory());
				else {
					world.getBlockAt((Location)key).setType(Material.CHEST);
					chests.put((Location) key, ((Container)world.getBlockAt((Location)key).getState()).getInventory());
				}
			}
		}
	}
	
	public void delete() {
		World toGoTo = Bukkit.getServer().getWorlds().get(0);
		
		for (Player goer : world.getPlayers()) {
			goer.sendMessage("YOU MOTHER SMELT OF ELDERBERRIES");
			goer.teleport(toGoTo.getSpawnLocation());
		}
		
		Bukkit.getServer().unloadWorld(world, false);
		File worldDir = mapConfigurationFile.getParentFile();
		rmDir(worldDir);
		
	}
	
	private void rmDir(File dir) {
		for (File f : dir.listFiles()) {
			if (f.isDirectory()) rmDir(f);
			else f.delete();
		}
		dir.delete();
	}
	
	public void fillChests() {
		for (Entry<Location, Inventory> ent : chests.entrySet()) {
			Inventory inv = ent.getValue();
			for (int i = 0 ; i < inv.getSize(); i++) {
				if (rand.nextInt(4) == 1) {
					inv.setItem(i, new ItemStack(Material.ACACIA_BOAT));
					
				}
			}
		}
	}

	public World getWorld() {
		return world;
	}
	
	public void setChest(Location loc) {
		if (world.getBlockAt(loc).getState() instanceof Container) chests.put(loc, ((Container)world.getBlockAt(loc).getState()).getInventory());
		else {
			world.getBlockAt(loc).setType(Material.CHEST);
			chests.put(loc, ((Container) world.getBlockAt(loc).getState()).getInventory());
		}
		mapConfiguration.set("chests", chests.keySet().toArray(new Location[chests.size()]));
		try {
			mapConfiguration.save(mapConfigurationFile);
		} catch (IOException e) {}
	}
	
	public boolean gotoWorld(Player player) {
		
		fillChests();
		return player.teleport(world.getSpawnLocation());	
	}
}
