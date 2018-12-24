package com.alchemi.tbb;

import java.util.Arrays;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.alchemi.al.FileManager;
import com.alchemi.al.Messenger;
import com.alchemi.tbb.cmd.CommandHandler;
import com.alchemi.tbb.maps.MapRegistry;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public class main extends JavaPlugin implements Listener {

	public static main instance;
	
	public Messenger messenger;
	public FileManager fileManager;
	public Economy econ;
	public Permission perms;
	public MapRegistry mapReg;
	
	public static boolean VaultPerms = false;
	
	public FileConfiguration config;
	
	public static int CONFIG_FILE_VERSION = 0;
	public static int MESSAGES_FILE_VERSION = 0;
	
	@Override
	public void onEnable() {

		instance = this;
		
		fileManager = new FileManager(this, new String[] {"config.yml", "messages.yml", "maps.yml"}, null, null, null);
		messenger = new Messenger(this, fileManager);
		
		fileManager.saveDefaultYML("config.yml");
		fileManager.saveDefaultYML("messages.yml");
		fileManager.saveDefaultYML("maps.yml");
		
		if (!fileManager.getConfig("messages.yml").isSet("File-Version-Do-Not-Edit") || fileManager.getConfig("messages.yml").getInt("File-Version-Do-Not-Edit") != MESSAGES_FILE_VERSION) {
			messenger.print("Messages file out of date! Updating...");
			fileManager.reloadConfig("messages.yml");
			fileManager.updateConfig("messages.yml");
			fileManager.getConfig("messages.yml").set("File-Version-Do-Not-Edit", MESSAGES_FILE_VERSION);
			fileManager.saveConfig("messages.yml");
			messenger.print("Messages file updated.");
		}
		
		if (!getConfig().isSet("File-Version-Do-Not-Edit") || getConfig().getInt("File-Version-Do-Not-Edit") != CONFIG_FILE_VERSION) {
			messenger.print("Config file out of date! Updating...");
			fileManager.reloadConfig("config.yml");
			fileManager.updateConfig("config.yml");
			fileManager.getConfig("config.yml").set("File-Version-Do-Not-Edit", CONFIG_FILE_VERSION);
			fileManager.saveConfig("config.yml");
			messenger.print("Config file updated.");
		}
		
		mapReg = new MapRegistry(fileManager.getConfig("maps.yml"), this);
		registerCommands();
		
		if (!setupEconomy() ) {
			messenger.print("[%s] - Disabled due to no Vault dependency found!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
		
		if (setupPermission()) {
			messenger.print("Using Vault based permissions!");
			VaultPerms = true;
		}
		
		messenger.print("&8A horse and a man, above, below. \n"
				+ "&8One has a plan, but both must go. \n"
				+ "&8Mile after mile, above, beneath. \n"
				+ "&8One has a smile, and one has teeth.\r\n" + 
				"&8Though the man above might say hello, expect no love from &4THE BEAST BELOW&8.");
	}
	
	private void registerCommands() {

		getCommand("tbb").setExecutor(new CommandHandler());
		
	}

	@Override
	public void onDisable() {
		messenger.print("&8All that &1pain &8and &cmisery&8... and &9loneliness&8.\n"
				+ "&8And it just made it &6kind&8.");
	}
		
	private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) return false;
        
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) return false;
        
        econ = rsp.getProvider();
        return econ != null;
    }
	
	private boolean setupPermission() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) return false;
		RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
		if (rsp == null) return false;
		
		perms = rsp.getProvider();
		return perms != null;
	}

	public void reloadConfigValues() {
		
		this.config = fileManager.getConfig("config.yml");
		
	}
	
}
