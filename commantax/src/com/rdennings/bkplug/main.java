package com.rdennings.bkplug;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class main extends JavaPlugin implements Listener  {

	@Override
	public void onEnable(){
	    	
	}

	@Override
	public void onDisable() {
	
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		String command = cmd.getName().toLowerCase();
		Player senderPlayer = (Player) sender;
		World senderWorld = senderPlayer.getWorld();
		
		switch (command) {
		case "getworld":
				sender.sendMessage(senderWorld.getName());
			return true;
			
		case "day":
			if (!(args.length == 0)){
				World targetWorld = Bukkit.getWorld(args[0]);
				targetWorld.setTime(0);
				Bukkit.getServer().broadcastMessage(ChatColor.AQUA + senderPlayer.getName() + " made it day for world: " + targetWorld.getName());
			}else{
				senderWorld.setTime(0);
				Bukkit.getServer().broadcastMessage(ChatColor.AQUA + senderPlayer.getName() + " made it day for world: " + senderWorld.getName());
			}
			return true;
		
		case "night":
			if (!(args.length == 0)){
				World targetWorld = Bukkit.getWorld(args[0]);
				targetWorld.setTime(14000);
				Bukkit.getServer().broadcastMessage(ChatColor.AQUA + senderPlayer.getName() + " made it night for world: " + targetWorld.getName());
			}else{
				senderWorld.setTime(14000);
				Bukkit.getServer().broadcastMessage(ChatColor.AQUA + senderPlayer.getName() + " made it night for world: " + senderWorld.getName());
			}
			return true;
		
		case "gm":
			if (!(args.length == 0)){
				switch (args[0]){
				case "1":
					senderPlayer.setGameMode(GameMode.valueOf("CREATIVE"));
					break;
				case "0":
					senderPlayer.setGameMode(GameMode.valueOf("SURVIVAL"));
					break;
				case "2":
					senderPlayer.setGameMode(GameMode.valueOf("ADVENTURE"));
					break;
				case "creative":
					senderPlayer.setGameMode(GameMode.valueOf("CREATIVE"));
					break;
				case "survival":
					senderPlayer.setGameMode(GameMode.valueOf("SURVIVAL"));
					break;
				case "adventure":
					senderPlayer.setGameMode(GameMode.valueOf("ADVENTURE"));
					break;
				}

			}else{
				sender.sendMessage("You need to enter a gamemode!");
			}
				return true;
		case "rape":
			if (!(args.length == 0)){
			String rapee = args[0].toLowerCase();
			Bukkit.getPlayer(rapee).setHealth(0);
			Bukkit.getServer().broadcastMessage(rapee + " Got raped by " + sender.getName() + "!");
			return true;
		}else {
			String rapee = sender.getName();
			Bukkit.getPlayer(rapee).setHealth(0);
			Bukkit.getServer().broadcastMessage(rapee + " raped himself!");
		}
		
		default:
			break;
		}
		
	return false;
	}

}



