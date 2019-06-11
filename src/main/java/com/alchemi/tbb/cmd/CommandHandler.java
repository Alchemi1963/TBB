package com.alchemi.tbb.cmd;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.alchemi.tbb.main;
import com.alchemi.tbb.gui.MapGui;
import com.alchemi.tbb.Config.Messages;
import com.alchemi.tbb.maps.MapHandler;

import me.alchemi.al.configurations.Messenger;

public class CommandHandler implements CommandExecutor {

	private final List<String> mapsAliases = Arrays.asList("maps", "map", "m");
	
	private final String usage = "/tbb map";
	
	private final String uCreate = " create <worldname>";
	private final String uDelete = " delete <worldname>";
	private final String uLoad = " load <worldname>";
	private final String uUnload = " unload <worldname>";
	private final String uTeleport = " teleport <worldname>";
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player && command.getName().equals("tbb") && args.length > 0) {
			
			String[] newArgs = new String[args.length - 1];
			
			for (int i = 0; i < args.length - 1; i++) {
				newArgs[i] = args[i+1];
			}
			
			if (mapsAliases.contains(args[0])) maps(sender, newArgs);
			
		}
		
		return true;
	}

	public void maps(CommandSender sender, String[] args) {
		Player player = (Player) sender;
		
		if (args.length == 0) {
//			sendUsage(sender, usage);
			new MapGui(player);
			return;
			
		} else if (args.length == 1) {
			
			if (args[0].equals("create")) {
				sender.sendMessage(usage + uCreate);
			} else if (args[0].equals("delete")) {
				sender.sendMessage(usage + uDelete);
			} else if (args[0].equals("load")) {
				sender.sendMessage(usage + uLoad);
			} else if (args[0].equals("unload")) {
				sender.sendMessage(usage + uUnload);
			} else if (args[0].equals("teleport")) {
				sender.sendMessage(usage + uTeleport);
			}
			return;
			
		} else if (args.length == 2) {
			
			if (args[0].equals("create")) {
				
				MapHandler mh;
				try {
					mh = main.getInstance().getMapReg().registerMap(args[1]);
					sender.sendMessage(Messenger.formatString(Messages.MAPS_CREATED.value()
							.replace("$name$", args[1])));
					
					mh.gotoWorld(player);
					player.setGameMode(GameMode.CREATIVE);
					main.getInstance().getMapReg().saveWorlds();
					return;
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			} else if (args[0].equals("delete")) {
				
				main.getInstance().getMapReg().deleteMap(args[1]);
				sender.sendMessage(Messenger.formatString(Messages.MAPS_DELETED.value()
						.replace("$name$", args[1])));
				return;
				
			} else if (args[0].equals("unload")) {
				
			} else if (args[0].equals("teleport")) {
				
			}
			
		}
	}
	
}
