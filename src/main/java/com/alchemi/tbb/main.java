package com.alchemi.tbb;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.alchemi.tbb.cmd.CommandHandler;
import com.alchemi.tbb.maps.MapRegistry;

import me.alchemi.al.configurations.Messenger;
import me.alchemi.al.objects.base.PluginBase;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public class main extends PluginBase implements Listener {

	private static main instance;
	
	private Economy econ;
	private Permission perms;
	private MapRegistry mapReg;
	
	private boolean VaultPerms = false;
	
	private Config config;
	
	@Override
	public void onEnable() {

		instance = this;
		
		setMessenger(new Messenger(this));
		
		try {
			config = new Config();
		} catch (IOException | InvalidConfigurationException e) {
			System.err.println("Unable to load configs! Disabling plugin...");
			Bukkit.getPluginManager().disablePlugin(this);
			e.printStackTrace();
		}
		
		//register stuff
		mapReg = new MapRegistry(Config.ConfigEnum.MAPS.getConfig(), this);
		registerCommands();
		
//		chatListener = new ChatListener();
//		getServer().getPluginManager().registerEvents(chatListener, this);		
		
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

		mapReg.saveWorlds();
		
		getCommand("tbb").setExecutor(new CommandHandler());
		
	}

	@Override
	public void onDisable() {
		config.save();
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
		
		
		
	}

	/**
	 * @return the instance
	 */
	public static final main getInstance() {
		return instance;
	}

	/**
	 * @return the econ
	 */
	public final Economy getEcon() {
		return econ;
	}

	/**
	 * @return the perms
	 */
	public final Permission getPerms() {
		return perms;
	}

	/**
	 * @return the mapReg
	 */
	public final MapRegistry getMapReg() {
		return mapReg;
	}

	/**
	 * @return the vaultPerms
	 */
	public final boolean isVaultPerms() {
		return VaultPerms;
	}
}
