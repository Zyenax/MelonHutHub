package net.melonhut.main.utils;

import java.util.HashMap;
import java.util.List;

import net.melonhut.main.Main;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Listener;

public class HoloGrams implements Listener{
	
	private static Main plugin;
	public HoloGrams(Main listener) {
		HoloGrams.plugin = listener;		
	}
	
	static String b = "&c&lERROR: &eHoloGram is already made!";
	public static HashMap<Integer, Entity> g = new HashMap<Integer, Entity>();
	public static void createHoloGram(Location loc, String Name, Integer ID, boolean small, boolean firework, Color color1, Color color2, Color color3){
		if(!g.containsKey(ID)){
		final ArmorStand s = (ArmorStand) loc.getWorld().spawn(loc.add(0, 0, 0), ArmorStand.class);
		s.setVisible(false);
		s.setCustomName(Utils.color(Name));
		s.setCustomNameVisible(true);
		s.setGravity(false);
		s.setCanPickupItems(false);
		s.setSmall(small);
		g.put(ID, s);
		if(firework == true){
			FireworkEffect u = FireworkEffect.builder().trail(true).flicker(true).withColor(new Color[] { color1,color2,color3 }).with(FireworkEffect.Type.BURST).build();
			Packets.playFirework(s.getLocation().add(0.5, 0, 0.5), u, 1);
		}else{
			return;
		}
		}else{
			ConsoleCommandSender v = Bukkit.getConsoleSender();
			v.sendMessage(Utils.color(b));
		}
	}
	public static Integer Id;
	public static void createTempHoloGram(Location loc, String name, final Integer ID, Integer timeInSecondsBeforeDespawn,boolean glowing, boolean small, boolean firework, Color color1, Color color2, Color color3){
		Id = 0;
		if(!g.containsKey(ID)){
			createHoloGram(loc, name, ID, small, firework, color1, color2, color3);
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
	    		public void run() {
	    			removeHoloGram(ID);
	    		}
	    	}, timeInSecondsBeforeDespawn * 20);
		}else{
			Id = ID + Utils.randomNum(1, 1000);
			createHoloGram(loc, name, Id, small, firework, color1, color2, color3);
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
	    		public void run() {
	    			removeHoloGram(Id);
	    		}
	    	}, timeInSecondsBeforeDespawn * 20);
		}
	}
	
	public static void removeHoloGram(Integer ID){
		g.get(ID).remove();
		g.remove(ID);
	}
	
	public static void removeAllHoloGramsInWorld(String world){
		List<Entity> e1 = Bukkit.getWorld(world).getEntities();
		for ( Entity e : e1){
			if(e.equals(EntityType.ARMOR_STAND)){
				e.remove();
			}
		}
	}
	
	public static void teleportHoloGram(Integer ID, Location loc){
		g.get(ID).teleport(loc.add(0.5, 0, 0.5));
	}
	
	public static String getHoloGramName(Integer ID){
		return g.get(ID).getCustomName();
	}
	
	public static Location getHoloGramLoc(Integer ID){
		return g.get(ID).getLocation();
	}
	
	public static void renameHoloGram(String Name, Integer ID){
		g.get(ID).setCustomName(Utils.color(Name));
		g.get(ID).setCustomNameVisible(true);
	}

}
