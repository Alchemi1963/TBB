package com.alchemi.tbb.cmd;

import java.io.IOException;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.alchemi.al.Messenger;
import com.alchemi.tbb.main;

public interface CommandBase {
	
	Messenger messenger = main.instance.messenger;
	
	String getUsage();
	
	default void sendString(CommandSender rec, String message) {
		Messenger.sendMsg(message, rec);
	}
	
	default void sendUsage(CommandSender rec, String usage) {
		Messenger.sendMsg(messenger.getMessage("Command.Wrong-Format") + usage, rec);
	}
	
	default void sendMessage(CommandSender rec, String messagePath) {
		Messenger.sendMsg(messenger.getMessage(messagePath), rec);
	}
	
	default void sendMessage(CommandSender rec, String messagePath, Map<String, String> vars) {
		Messenger.sendMsg(messenger.getMessage(messagePath), rec, vars);
	}
	
	boolean onCommand(CommandSender sender, Command command, String[] args)  throws IOException;

}
