package com.alchemi.tbb.cmd;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler implements CommandExecutor {

	private final List<String> mapsAliases = Arrays.asList("maps", "map", "m");
	
	CommandMaps maps = new CommandMaps();
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player && command.getName().equals("tbb") && args.length > 0) {
			
			String[] newArgs = new String[args.length - 1];
			
			for (int i = 0; i < args.length - 1; i++) {
				newArgs[i] = args[i+1];
			}
			
			if (mapsAliases.contains(args[0])) try { maps.onCommand(sender, command, newArgs); } catch (Exception ig) {}
			
		}
		
		return true;
	}

}
