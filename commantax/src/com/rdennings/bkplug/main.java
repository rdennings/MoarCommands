package com.rdennings.bkplug;

import java.io.File;
import java.io.IOException;
//import java.io.InputStream; //used to be for reloadCustomConfig(), not needed for creating multiple files.
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

//-----Start CustomClasses

//-----End CustomClasses

public class main extends JavaPlugin implements Listener  {
	
String userConfig = null; //ONLY use to determine file path for player config.yml.
						  //SHOULD NOT be used to set another variable.
	    
//-----Start onEnable
@Override
public void onEnable(){
	//this stopped showing up for some reason...
	Bukkit.getServer().broadcastMessage("MoarCommands started without errors..."); 
        	
	getServer().getPluginManager().registerEvents(new Listener() {
		@EventHandler
        public void playerJoin(PlayerJoinEvent event) { 
			//checks to see if player has a <player>.yml file made. If not, then make one.
			//if the user has a nickname in <player>.yml, then change their display name.
			String getName = event.getPlayer().getName();
    		userConfig = event.getPlayer().getName();
        	String getNick = getCustomConfig().getString("nickname");
        	
        	if (getNick == null){
        		getNick = getName;
        		saveCustomConfig();
        	}else{

        	}
    		getCustomConfig().set("nickname", getNick);
        	event.getPlayer().setDisplayName(getNick);        
            Bukkit.getServer().broadcastMessage(userConfig + " has logged in with nickname " + getNick);

                    
            //Clear Variables for next person...
            //This prevents the next person from overriting the configurations of the preveous player
            getName = null; 
            userConfig = null;
            getNick = null;
        }
    }, this);
       	
}
//-----End onEnable

//-----Start onDisable
@Override
public void onDisable() {
	Bukkit.getServer().broadcastMessage("MoarCommand has encountered errors, (or the server is being reloaded), and will now be disabled...");
}
//-----End onDisable

//-----Start onCommand
@SuppressWarnings("deprecation")
public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
	if (cmd.getName().equalsIgnoreCase("getworld")) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("This command can only be run by a player.");
		} else {
			Player player = (Player) sender;	//converts CommandSender to Player
    		Location playerLoc = player.getLocation();	
    		String worldName = playerLoc.getWorld().getName();	//gets world name from player
    		sender.sendMessage(worldName);
		}
		return true;
	} else if (cmd.getName().equalsIgnoreCase("nick")) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("This command can only be run by a player.");
			//Going to get rid of instance check because 
			//the console may wish to remove a players' nickname.
		} else {
			if(args.length < 1){	//player provides no arguments, restore their real name
        		Player player = (Player) sender;
        		String senderName = player.getName();
        		
				userConfig = senderName;
				player.setDisplayName(senderName);
				sender.sendMessage("You have removed your nickname");
				getCustomConfig().set("nickname", null);
				saveCustomConfig();
			}else{ //Makes sure a file is made, then writes the new nickname
        		Player player = (Player) sender;
        		String senderName = player.getName();
        		userConfig = senderName;
        		getCustomConfig().get("nickname");

        		player.setDisplayName(args[0]);
        		String displayName = player.getDisplayName(); 
        		
        		sender.sendMessage("your new name is: " + displayName);
        		sender.sendMessage(sender.getName() + ", " + args[0]);
        		getCustomConfig().set("nickname", displayName);
        		saveCustomConfig();		
			}

		}
		return true;
	} else if (cmd.getName().equalsIgnoreCase("bannick")) { 
		//this command will have an option in config.yml to disable
		//for comparability with custom ban plugins that rely on logging.
		userConfig = args[0];
		String banName = getCustomConfig().getString("nickname");
		sender.sendMessage("You banned: " + banName);	
		OfflinePlayer PLAYER_TO_BAN = Bukkit.getOfflinePlayer(banName);
		PLAYER_TO_BAN.setBanned(true);
		userConfig = null;
		return true;
	} else if (cmd.getName().equalsIgnoreCase("kill")) { 
		String killName = args[0];
		Player playerToKill = Bukkit.getPlayer(killName);
		playerToKill.sendRawMessage("You have been killed by a waffle.");
		Bukkit.getServer().broadcastMessage(ChatColor.BLUE + playerToKill.getName() + " GOT KILLED BY A WAFFLE!");
		playerToKill.setHealth(0);
		
		
		return true;
	} else if (cmd.getName().equalsIgnoreCase("day")) { 
		Player player = (Player) sender;
		String playerLoc = player.getWorld().getName();
		Bukkit.getServer().getWorld(playerLoc).setTime(0);
		Bukkit.getServer().broadcastMessage(ChatColor.LIGHT_PURPLE + sender.getName() + " set the time to day in world: " + playerLoc);
		
		return true;
	} else if (cmd.getName().equalsIgnoreCase("night")) { 
		Player player = (Player) sender;
		String playerLoc = player.getWorld().getName();
		Bukkit.getServer().getWorld(playerLoc).setTime(14000);
		Bukkit.getServer().broadcastMessage(ChatColor.DARK_PURPLE + sender.getName() + " set the time to night in world: " + playerLoc);
		
		return true;
	} else if (cmd.getName().equalsIgnoreCase("gm")) {
		Player player = (Player) sender;
		String stringMode = "1";
		if(args[0] == "0") {
			stringMode = "SURVIVAL";
		} else if (args[0] == "1") {
			stringMode = "CREATIVE";
		} else if (args[0] == "2") {
			stringMode = "ADVENTURE";
		} else {

			sender.sendMessage("Something went wrong, make sure you type 0, 1, or 2");
			sender.sendMessage(stringMode);
		}
		sender.sendMessage(stringMode);
		GameMode modeVal = GameMode.valueOf(stringMode);
		player.setGameMode(modeVal);
		
		return true;
	}
	return false;
}
//-----End onCommand

//-----Start <player>.yml
//I am not fully aware of how this works, which is most likely causing a lot of my problems... 
//note to self: learn java before doing stuff...

//ADAPTED FROM http://wiki.bukkit.org/Configuration_API_Reference#Mirroring_the_JavaPlugin_implementation
//ALMOST ALL CREDIT GOES TO THE ORIGIONAL OWNER OF THIS CODE
private FileConfiguration customConfig = null;
private File customConfigFile = null;

public void reloadCustomConfig() {
    customConfigFile = new File(getDataFolder() + "/users", userConfig + ".yml");
    if (customConfigFile == null) {
    	//customConfigFile = new File(getDataFolder() + "/users", userConfig + ".yml");
    }
    customConfig = YamlConfiguration.loadConfiguration(customConfigFile);	      
}
	 
public FileConfiguration getCustomConfig() {
    reloadCustomConfig();
    return customConfig;
}
 
public void saveCustomConfig() {
	customConfigFile = new File(getDataFolder() + "/users", userConfig + ".yml");
	if (customConfig == null || customConfigFile == null) {
	    return;
	}
	try {
		customConfigFile = new File(getDataFolder() + "/users", userConfig + ".yml");
	    getCustomConfig().save(customConfigFile);
	} catch (IOException ex) {
		this.getLogger().log(Level.SEVERE, "Could not save config to " + customConfigFile, ex);
    }
}

public void saveDefaultConfig() {
    if (customConfigFile == null) {
    	customConfigFile = new File(getDataFolder() + "/users", userConfig + ".yml");
    }
    if (!customConfigFile.exists()) {            
    	saveResource("/users" + userConfig + ".yml", false);
     }
}
//-----End <player>.yml

}



