package com.alchemi.tbb.listener;

import com.alchemi.tbb.main;
import me.alchemi.al.configurations.Messenger;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        player.sendMessage(Messenger.colourMessage("MarioParty.Message.Welcome").replaceAll("%PLAYER%", player.getName()));
        e.setJoinMessage(Messenger.colourMessage("MarioParty.Message.Welcome-Broadcast").replaceAll("%PLAYER%", player.getName()));

        main.getInstance().getPlayerDatabase().setOnline(player, true);
    }
}
