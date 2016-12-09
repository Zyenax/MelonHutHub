package net.melonhut.main;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import net.melonhut.main.utils.BungeeUtil;
import net.melonhut.main.utils.HashMapStorage;
import net.melonhut.main.utils.HoloGrams;
import net.melonhut.main.utils.Packets;
import net.melonhut.main.utils.Utils;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class Main extends JavaPlugin implements Listener, PluginMessageListener{
	
	public static String GetServer;
	public static String[] serverList;
	
	public void onEnable(){
		registerListeners();
		registerCommands();
		ConsoleCommandSender console = Bukkit.getConsoleSender();
		console.sendMessage(Utils.color("&8[&cMelon&aHut&8] &aHas been enabled!"));
		Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(this,"BungeeCord");
	    Bukkit.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);
	}
	
	public void onDisable(){
		ConsoleCommandSender console = Bukkit.getConsoleSender();
		console.sendMessage(Utils.color("&8[&cMelon&aHut&8] &cHas been disabled!"));
	}
	
	public void registerListeners(){
		PluginManager manager = Bukkit.getServer().getPluginManager();
		manager.registerEvents(new Utils(this), this);
		manager.registerEvents(new BungeeUtil(this), this);
		manager.registerEvents(new HashMapStorage(this), this);
		manager.registerEvents(new HoloGrams(this), this);
		manager.registerEvents(new Packets(this), this);
	}
	
	public void registerCommands(){
		//getCommand("spawn").setExecutor(new CommandHandler(this));
	}
	
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
	    if (!channel.equals("BungeeCord")) {
	      return;
	    }
	    try{
	    	DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
		    String subchannel = in.readUTF();
	    if (subchannel.equals("PlayerCount")) {
	    	String PlayerCountServer = in.readUTF();
	    	Integer playercount = in.readInt();
	    	HashMapStorage.PlayerCount.remove(PlayerCountServer);
	    	HashMapStorage.PlayerCount.put(PlayerCountServer, playercount);
        } else if (subchannel.equals("GetServers")) {
        	serverList = in.readUTF().split("\n");
        } else if (subchannel.equals("GetServer")) {
            // Example: GetServer subchannel
        	GetServer = in.readUTF();
        }
	    }catch (Exception e){
	    	e.printStackTrace();
	    }
	  }

}
