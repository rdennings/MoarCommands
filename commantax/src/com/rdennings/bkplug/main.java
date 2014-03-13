package com.rdennings.bkplug;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
//------CustomClasses



public class main extends JavaPlugin implements Listener  {
	
	
	String userConfig = null;
	    
        @Override
        public void onEnable(){
        	Bukkit.getServer().broadcastMessage("MoarCommands started without errors...");
        	
        	//-----
        	getServer().getPluginManager().registerEvents(new Listener() {
        		  @EventHandler
                public void playerJoin(PlayerJoinEvent event) {
        			  
                  	//String getName = event.getPlayer().getName();
                	userConfig = event.getPlayer().getName();
                	
        			String getNick = getCustomConfig().getString("nickname");
                	if (getNick == null){
                		getNick = userConfig;
                        saveCustomConfig();
                	}else{

                	}
            		getCustomConfig().set("nickname", getNick);
                	
                    event.getPlayer().setDisplayName(getNick);
                    
                    Bukkit.getServer().broadcastMessage(userConfig + " has logged in with nickname " + getNick);

                    
                    //Clear Variables for next person...
                    //getName = null;
                    userConfig = null;
                    getNick = null;
                }
            }, this);
        	//--------
        	
            }
        
	    @Override
        public void onDisable() {
        	Bukkit.getServer().broadcastMessage("MoarCommand has encountered errors, (or the server is being reloaded), and will now be disabled...");
        }
        
	    
	    
	    @SuppressWarnings("deprecation")
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
/*getworld*/if (cmd.getName().equalsIgnoreCase("getworld")) {
        		
        		if (!(sender instanceof Player)) {
        			sender.sendMessage("This command can only be run by a player.");
        		} else {

            		Player player = (Player) sender;
            		Location playerLoc = player.getLocation();
            		String worldName = playerLoc.getWorld().getName();
            		
            		sender.sendMessage(worldName);

        		}
        		return true;
/*nick*/	} else if (cmd.getName().equalsIgnoreCase("nick")) {

        		if (!(sender instanceof Player)) {
        			sender.sendMessage("This command can only be run by a player.");
        		} else {
        			//--------
        			if(args.length < 1){
                		Player player = (Player) sender;
                		String senderName = player.getName();
                		
        				userConfig = senderName;
        				player.setDisplayName(senderName);
        				sender.sendMessage("You have removed your nickname");
        				getCustomConfig().set("nickname", null);
        				saveCustomConfig();
        				
        			}else if (args[0] != sender.getName()){
        				
                		Player player = (Player) sender;
                		String senderName = player.getName();
                		userConfig = senderName;

                		player.setDisplayName(args[0]);
                		String displayName = player.getDisplayName(); 
                		
                		sender.sendMessage("your new name is: " + displayName);
                		sender.sendMessage(sender.getName() + ", " + args[0]);
                		getCustomConfig().set("nickname", displayName);
                		saveCustomConfig();
                		
        			}
            		userConfig = null;
        		}
        		
        		return true;
/*bannick*/	} else if (cmd.getName().equalsIgnoreCase("bannick")) {

				//String banName = getConfig().getString("nicknames." + args[0]);
				userConfig = args[0];
				String banName = getCustomConfig().getString("nickname");
				sender.sendMessage("You banned: " + banName);
				
				OfflinePlayer PLAYER_TO_BAN = Bukkit.getOfflinePlayer(banName);
				PLAYER_TO_BAN.setBanned(true);
				userConfig = null;
				return true;
			}
        return false;
	    }
	    
	    
	    
	    //CUSTOM CONFIG
	    
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

}



