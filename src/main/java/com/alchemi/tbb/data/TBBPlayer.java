package com.alchemi.tbb.data;

import org.bukkit.entity.Player;

public class TBBPlayer {
    private final Player player;
    private boolean online;
    private boolean ingame;
    private boolean beast;
    private int points;
    private double beastChance;

    public TBBPlayer(Player e){
        this.player = e;
        online = true;
        ingame = false;
        beast = false;
        points = 0;
        beastChance = 0.5;
    }

    public Player getPlayer(){
        return player;
    }
    public boolean isOnline(){
        return online;
    }
    public boolean isIngame() {
        return ingame;
    }
    public boolean isBeast() {
        return beast;
    }
    public int getPoints() {
        return points;
    }
    public double getBeastChance() {
        return beastChance;
    }


    public void setBeast(boolean beast) {
        if(!ingame) {
            this.beast = beast;
        }
    }

        public boolean startGame() {
        return ingame = online;
    }

    public void endGame(boolean hasWon) {
        if(!online){
            return;
        }

        if(hasWon) {
            //ToDo Goede puntentelling bedenken
            points += 6;
        } else {
            points -= 2;
        }

        if(beast) {
            //ToDo Goede beast-chance bedenken
            beastChance *= 0.8;
        } else {
            beastChance *= 1.1;
        }

        ingame = false;
        beast = false;
    }

    public void setOnline(boolean online){
        this.online = online;
    }
}