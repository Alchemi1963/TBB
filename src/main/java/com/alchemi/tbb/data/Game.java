package com.alchemi.tbb.data;

import com.alchemi.tbb.main;
import me.alchemi.al.configurations.Messenger;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private final GameMapData gameMapData;
    private List<TBBPlayer> playerList;
    private boolean inGame;
    private int countdown;
    private Thread thread;

    Game(GameMapData gameMapData){
        this.gameMapData = gameMapData;
        playerList = new ArrayList<>();
        inGame = false;
        countdown = -1;
        thread = null;
    }

    void setPlayerList(List<TBBPlayer> list){
        this.playerList = list;
    }
    GameMapData getGameMapData(){ return gameMapData;}
    public List<TBBPlayer> getPlayerList() {return playerList;}
    boolean isInGame() {
        return inGame;
    }

    public void stop() {
        broadCastTitle("&4&lStopping", "Plugin is forced to stop");
        getPlayerList().forEach(this::removePlayer);
    }

    public String addPlayer(TBBPlayer player){
        if(player == null)
            return "";
        if (!gameMapData.isEnabled())
            return Messenger.colourMessage("MarioParty.Game.Disabled").replaceAll("%WORLD%", gameMapData.getName());
        if (inGame)
            return Messenger.colourMessage("MarioParty.World.Ingame").replaceAll("%WORLD%", gameMapData.getName());
        if(playerList.size() >= 8)
            return Messenger.colourMessage("MarioParty.Game.Full").replaceAll("%WORLD%", gameMapData.getName());

        playerList.add(player);

        player.getPlayer().teleport(gameMapData.getLobby());

        broadCastMessage(Messenger.colourMessage("MarioParty.Game.Join").replaceAll("%PLAYER%", player.getPlayer().getName()).replaceAll("%GAME%", gameMapData.getName()));

        //ToDo get players amount from config
        if(true/*main.getInstance().getFileManager().getConfig().getInt("game.countdown-start") <= playerList.size()*/)
            CountDown();

        return "";
    }

    public String removePlayer(TBBPlayer player){
        if(player == null)
            return "";

        broadCastMessage(Messenger.colourMessage("MarioParty.Game.Leave").replaceAll("%PLAYER%", player.getPlayer().getName()).replaceAll("%GAME%", gameMapData.getName()));

        playerList.remove(player);
        player.getPlayer().resetTitle();
        player.getPlayer().getInventory().clear();

        if(playerList.size() <= 1) {
            if (countdown != -1) {
                Bukkit.getServer().getScheduler().cancelTask(countdown);
                countdown = -1;
            } else if (thread != null) {
                thread.interrupt();
                inGame = false;
                thread = null;
            }
        }
        player.getPlayer().teleport(gameMapData.getGlobalSpawn());
        return Messenger.colourMessage("MarioParty.Game.Leave").replaceAll("%PLAYER%", player.getPlayer().getName()).replaceAll("%GAME%", gameMapData.getName());
    }

    public void broadCastTitle(String title, String subtitle){
        playerList.forEach((TBBPlayer p) -> p.getPlayer().sendTitle(Messenger.colourMessage(title),Messenger.colourMessage(subtitle), 40, 100, 40));
    }
    private void broadCastMessage(String message){
        playerList.forEach((TBBPlayer p) -> p.getPlayer().sendMessage(message));
    }



    private void StartGame(){
        inGame = true;
        if(thread != null){
            thread.interrupt();
            //ToDo this chould never happened
            //Messages.colorBroadcastMessage("Task stopped");
        }

        thread = new Thread(() -> {
            while(thread != null){
                Run();
            }
        });
        thread.start();
    }

    private void CountDown(){
        final int[] x = {10};
        countdown = Bukkit.getScheduler().scheduleSyncRepeatingTask( main.getInstance(), () -> {

            broadCastTitle("&2&l" + x[0], "&2&lStarting In...");
            x[0]--;

            if(x[0] == 0) {
                StartGame();
                Bukkit.getServer().getScheduler().cancelTask(countdown);
                countdown = -1;
            }
        },20, 40);
    }

    private void Run() {
        //ToDo Calculate Beast
        //ToDo Free players
        //ToDo Cage Beast
        //ToDo Free Beast
        //ToDo ...
    }
}
