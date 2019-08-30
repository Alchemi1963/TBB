package com.alchemi.tbb.data;

import me.alchemi.al.configurations.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class GameMapData {
    private FileConfiguration fileConfig;
    private final String name;
    private Location lobby;
    private boolean enabled;

    private Location start;
    private Location end;
    private Location beastCase;
    private Game game;

    Location getGlobalSpawn(){
        //ToDo Get default lobby location
        return getLocationFromString("world 0 0 0"/*main.get().getFileManager().getConfig().getString("spawn")*/);
    }

    public GameMapData(String name){
        this.name = name;
        start = null;
        end = null;
        beastCase = null;
        enabled = false;
        game = new Game(this);
    }

    public GameMapData(FileConfiguration fileConfig){
        this.fileConfig = fileConfig;
        name = fileConfig.getString("worldname");
        try {
            lobby = getLocationFromString(fileConfig.getString("lobby"));
            enabled = fileConfig.getBoolean("enabled");

            start = getLocationFromString(fileConfig.getString("start"));
            end = getLocationFromString(fileConfig.getString("end"));
            beastCase = getLocationFromString(fileConfig.getString("beastCase"));

            game = new Game(this);
        }
        catch (Exception ex){
            //Todo senconsole
            //Messages.sendConsole("Couldn't load data from file: " + getName() + ". Skipping loading data.");
        }
    }

    public String getName() {
        return name;
    }
    Location getLobby() {
          return lobby;
    }
    public boolean isEnabled(){
        return enabled;
    }
    public Game getGame(){
        return game;
    }
    Location getStartLocation(){ return start;}
    public String getStartString(){
        return "&6Start " + this.name + ": &7[X; Y; Z; Yaw; Pitch;]" + "\n " + start.getX() + "; " + start.getY() + "; " + start.getZ() + "; " + start.getYaw() + "; " + start.getPitch() + ";";
    }
    Location getEndLocation(){ return end;}
    public String getEndString(){
        return "&6End " + this.name + ": &7[X; Y; Z; Yaw; Pitch;]" + "\n " + end.getX() + "; " + end.getY() + "; " + end.getZ() + "; " + end.getYaw() + "; " + end.getPitch() + ";";
    }
    Location getBeastCaseLocation(){ return beastCase;}
    public String getBeastCaseString(){
        return "&6BeastCase " + this.name + ": &7[X; Y; Z; Yaw; Pitch;]" + "\n " + beastCase.getX() + "; " + beastCase.getY() + "; " + beastCase.getZ() + "; " + beastCase.getYaw() + "; " + beastCase.getPitch() + ";";
    }


    public String setEnable(boolean setEnabled){
        if (getGame().isInGame())
            return Messenger.colourMessage("World %WORLD% is ingame").replaceAll("%WORLD%", this.name);

        if(!setEnabled) {
            enabled = false;
            save();
            game.getPlayerList().forEach((TBBPlayer p) -> game.removePlayer(p));
            return Messenger.colourMessage("MarioParty.Item.Edited").replaceAll("%NUMBER%", this.name);
        }
        if(!isComplete())
            return Messenger.colourMessage("MarioParty.World.Incomplete".replaceAll("%WORLD%", this.name));
        enabled = true;
        save();
        return Messenger.colourMessage("MarioParty.Item.Edited").replaceAll("%NUMBER%", this.name);
    }

    public String setWaitLobby(Location locationTemp){
        if(canEdit() != null)
            return canEdit();
        lobby = roundToHalf(locationTemp);
        save();
        return Messenger.colourMessage("MarioParty.Item.Edited").replaceAll("%NUMBER%", "WaitLobby");
    }

    public String setStart(Location locationTemp){
        if(canEdit() != null)
            return canEdit();

        start = roundToHalf(locationTemp);
        save();
        return Messenger.colourMessage("MarioParty.Item.Edited");
    }

    public String setEnd(Location locationTemp){
        if(canEdit() != null)
            return canEdit();

        end = roundToHalf(locationTemp);
        save();
        return Messenger.colourMessage("MarioParty.Item.Edited");
    }

    public String setBeastCase(Location locationTemp){
        if(canEdit() != null)
            return canEdit();

        beastCase = roundToHalf(locationTemp);
        save();
        return Messenger.colourMessage("MarioParty.Item.Edited");
    }

    public boolean isComplete(){
        return (start == null) || (end == null) || (lobby == null) || (beastCase == null);
    }

    private String handleRemove(List<Location> list, Location location, int position) {
        if(location == null){
            if(position == -1) {
                list.remove(list.size() - 1);
                save();
                return Messenger.colourMessage("MarioParty.Item.Edited").replaceAll("%NUMBER%", list.size() + "");
            }

            if(position >= list.size())
                return Messenger.colourMessage("MarioParty.Command.Invalid-Number").replaceAll("%NUMBER%", String.valueOf(position));

            list.remove(position);
            save();
            return Messenger.colourMessage("MarioParty.Item.Edited").replaceAll("%NUMBER%", list.size() + "");
        }
        return null;
    }


    private Location roundToHalf(Location location){
        location.setX(roundToHalf(location.getX()));
        location.setY(roundToHalf(location.getY()));
        location.setZ(roundToHalf(location.getZ()));
        location.setYaw(roundToHalf(location.getYaw()));
        location.setPitch(roundToHalf(location.getPitch()));
        return location;
    }

    private void save(){
        fileConfig.set("worldname", name);
        fileConfig.set("lobby", getStringFromLocation(lobby));
        fileConfig.set("enabled", enabled);
        fileConfig.set("start", getStringFromLocation(start));
        fileConfig.set("end", getStringFromLocation(end));
    }

    private double roundToHalf(double d) {
        return Math.round(d * 2) / 2.0;
    }
    private float roundToHalf(float d) {
        return (float) (Math.round(d * 2) / 2.0);
    }

    private Location getLocationFromString(String s) {
        if (s == null || s.trim().equals("")) {
            return null;
        }
        s += " ";
        final String[] parts = s.split(" ");
        if(parts.length >= 4)
        {
            World w = Bukkit.getServer().getWorld(parts[0]);
            double x = Double.parseDouble(parts[1]);
            double y = Double.parseDouble(parts[2]);
            double z = Double.parseDouble(parts[3]);

            if (parts.length == 6) {
                float yaw = Float.parseFloat(parts[4]);
                float pitch = Float.parseFloat(parts[5]);
                return new Location(w, x, y, z, yaw, pitch);
            }
            return new Location(w, x, y, z, 90, 0);
        }
        return null;
    }

    private String getStringFromLocation(Location loc) {
        if (loc == null) {
            return "";
        }
        char sepChar = ' ';
        return loc.getWorld().getName() + sepChar + loc.getX() + sepChar + loc.getY() + sepChar + loc.getZ() + sepChar + loc.getYaw() + sepChar + loc.getPitch();
    }

    private List<String> getStringFromLocation(List<Location> loclist) {
        List<String> stringlist = new ArrayList<>();
        loclist.forEach((Location loc) -> stringlist.add(getStringFromLocation(loc)));
        return stringlist;
    }

    private String canEdit(){
        if (getGame().isInGame())
            return Messenger.colourMessage("MarioParty.World.Ingame").replaceAll("%WORLD%", this.name);
        if (isEnabled())
            return Messenger.colourMessage("MarioParty.World.Enabled").replaceAll("%WORLD%", this.name);
        return null;
    }
}
