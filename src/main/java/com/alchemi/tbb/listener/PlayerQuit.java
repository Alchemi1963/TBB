package com.alchemi.tbb.listener;

import com.alchemi.tbb.main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e){
        main.getInstance().getPlayerDatabase().setOnline(e.getPlayer(), false);
    }
}
