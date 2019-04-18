package com.alchemi.tbb.cmd;

import java.io.IOException;
import java.util.HashMap;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.alchemi.tbb.main;
import com.alchemi.tbb.maps.MapHandler;

public class CommandMaps implements CommandBase{

	private final String usage; 
	
	private final String uCreate = " create [worldname]";
	private final String uDelete = " delete [worldname]";
	private final String uLoad = " load [worldname]";
	private final String uUnload = " unload [worldname]";
	private final String uTeleport = " teleport [worldname]";
	
	public CommandMaps() {
		usage = "/tbb map";
	}
	
	@SuppressWarnings("serial")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String[] args) throws IOException {
		
		Player player = (Player) sender;
		
		if (args.length == 0) {
//			sendUsage(sender, usage);
			main.instance.mapGui.openGUI(player);
		} else if (args.length == 1) {
			
			if (args[0].equals("create")) {
				sendUsage(sender, usage + uCreate);
			} else if (args[0].equals("delete")) {
				sendUsage(sender, usage + uDelete);
			} else if (args[0].equals("load")) {
				sendUsage(sender, usage + uLoad);
			} else if (args[0].equals("unload")) {
				sendUsage(sender, usage + uUnload);
			} else if (args[0].equals("teleport")) {
				sendUsage(sender, usage + uTeleport);
			}
			
		} else if (args.length == 2) {
			
			if (args[0].equals("create")) {
				
				MapHandler mh = main.instance.mapReg.registerMap(args[1]);
				sendMessage(sender, "Maps.Created", new HashMap<String, String>(){
					{
						put("$name$", args[1]);
					}
				});
				mh.gotoWorld(player);
				player.setGameMode(GameMode.CREATIVE);
				main.instance.mapReg.saveWorlds();
				
			} else if (args[0].equals("delete")) {
				
				main.instance.mapReg.deleteMap(args[1]);
				sendMessage(sender, "Maps.Deleted", new HashMap<String, String>(){
					{
						put("$name$", args[1]);
					}
				});
				
			} else if (args[0].equals("unload")) {
				
			} else if (args[0].equals("teleport")) {
				
			}
			
		}
		
		return true;
	}

	@Override
	public String getUsage() {return usage;}
	
	
	
}
