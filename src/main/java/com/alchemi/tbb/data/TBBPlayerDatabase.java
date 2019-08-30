package com.alchemi.tbb.data;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TBBPlayerDatabase {
    private final List<TBBPlayer> players;

    public TBBPlayerDatabase(){
        players = new ArrayList<>();
    }

    public void setOnline(Player player, boolean online){

        for(TBBPlayer item : players){
            if(player.getUniqueId() == item.getPlayer().getUniqueId()){
                item.setOnline(online);
                return;
            }
        }
        players.add(new TBBPlayer(player));
    }

    public TBBPlayer getMPPlayer(Player player){

        return players.stream().filter(item -> player.getUniqueId() == item.getPlayer().getUniqueId()).findFirst().orElse(null);
    }

    public List<TBBPlayer> getOnline(){
        List<TBBPlayer> list = new ArrayList<>();
        for(TBBPlayer player : players){
            if(player.isOnline()) list.add(player);
        }
        return list;
    }
}
