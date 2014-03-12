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
	
	
	public String userConfig = null;
	
		/*
	    @EventHandler
	    public void onPlayerMove(PlayerMoveEvent event){
	            Player player = event.getPlayer();
	            Location playerLoc = player.getLocation();
	            player.sendMessage("Your X Coordinates : " + playerLoc.getX());
	            player.sendMessage("Your Y Coordinates : " + playerLoc.getY());
	            player.sendMessage("Your Z Coordinates : " + playerLoc.getZ());
	    }
		*/
	    
        @Override
        public void onEnable(){
        	Bukkit.getServer().broadcastMessage("MoarCommands started without errors...");
        	
        	//-----
        	getServer().getPluginManager().registerEvents(new Listener() {
        		  @EventHandler
                public void playerJoin(PlayerJoinEvent event) {
                    // On player join send them the message from config.yml
                  	String getName = event.getPlayer().getName();
                	userConfig = getName;
                	
        			String getNick = getCustomConfig().getString("nickname");
                	if (getNick == null){
                		getNick = getName;
                	}
            		getCustomConfig().set("nickname", getNick);
                    event.getPlayer().setDisplayName(getNick);
                    Bukkit.getServer().broadcastMessage(getName + " has logged in with nickname " + getNick);
                    
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
                		
        				this.getConfig().set("nicknames." + senderName, null);
        				
        				player.setDisplayName(senderName);
        				sender.sendMessage("You have removed your nickname");
        					
        			}else if (args[0] != sender.getName()){
        				
        				
                		Player player = (Player) sender;
                		String senderName = player.getName();
                		userConfig = senderName;

                		player.setDisplayName(args[0]);
                		String displayName = player.getDisplayName(); 
                		
                		sender.sendMessage("your new name is: " + displayName);
                		sender.sendMessage(sender.getName() + ", " + args[0]);
                		this.getCustomConfig().set("nickname", displayName);
                		this.saveCustomConfig();
                		
        			}
        		}
        		
        		return true;
/*bannick*/	} else if (cmd.getName().equalsIgnoreCase("bannick")) {

				//String banName = getConfig().getString("nicknames." + args[0]);
				userConfig = args[0];
				String banName = getCustomConfig().getString("nickname");
				sender.sendMessage("You banned: " + banName);
				
				OfflinePlayer PLAYER_TO_BAN = Bukkit.getOfflinePlayer(banName);
				PLAYER_TO_BAN.setBanned(true);

				return true;
			}
        return false;
	    }
	    
	    
	    private FileConfiguration customConfig = null;
	    private File customConfigFile = null;
	 
	    public void reloadCustomConfig() {
	        if (customConfigFile == null) {
	        customConfigFile = new File(getDataFolder() + "/users", userConfig + ".yml");
	        }
	        customConfig = YamlConfiguration.loadConfiguration(customConfigFile);
	     
	        // Look for defaults in the jar
	        InputStream defConfigStream = this.getResource(userConfig + ".yml");
	        if (defConfigStream != null) {
	            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	            customConfig.setDefaults(defConfig);
	        }
	      
	        userConfig = null;
	    }
	 
	    public FileConfiguration getCustomConfig() {
	        if (customConfig == null) {
	            reloadCustomConfig();
	        }
	        userConfig = null;
	        return customConfig;
	    }
	 
	    public void saveCustomConfig() {
	        if (customConfig == null || customConfigFile == null) {
	            return;
	        }
	        try {
	            getCustomConfig().save(customConfigFile);
	        } catch (IOException ex) {
	            this.getLogger().log(Level.SEVERE, "Could not save config to " + customConfigFile, ex);
	        }
	        userConfig = null;
	    }
	    
	    public void saveDefaultConfig() {
	        if (customConfigFile == null) {
	            customConfigFile = new File(getDataFolder() + "/users", userConfig + ".yml");
	        }
	        if (!customConfigFile.exists()) {            
	             saveResource(userConfig + ".yml", false);
	         }
	        userConfig = null;
	    }

}



