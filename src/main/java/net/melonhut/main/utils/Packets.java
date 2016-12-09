package net.melonhut.main.utils;

import java.lang.reflect.Field;

import net.melonhut.main.Main;
import net.minecraft.server.v1_10_R1.IChatBaseComponent;
import net.minecraft.server.v1_10_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_10_R1.PacketPlayOutChat;
import net.minecraft.server.v1_10_R1.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_10_R1.PacketPlayOutTitle;
import net.minecraft.server.v1_10_R1.PacketPlayOutTitle.EnumTitleAction;
import net.minecraft.server.v1_10_R1.PlayerConnection;

import org.bukkit.ChatColor;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftFirework;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.meta.FireworkMeta;

public class Packets implements Listener{
	
	@SuppressWarnings("unused")
	private static Main plugin;
	public Packets(Main listener) {
		Packets.plugin = listener;
	}
	
	public static void sendTabTitle(Player player, String header1, String header2, String header3, String footer1, String footer2, String footer3) {
		if (header1 == null) {
			header1 = "";
		}
		if(header2 == null){
			header2 = "";
		}
		if(header3 == null){
			header3 = "";
		}
		header1 = ChatColor.translateAlternateColorCodes('&', header1);
		if (footer1 == null) {
			footer1 = "";
		}
		if (footer2 == null) {
			footer2 = "";
		}
		if (footer3 == null) {
			footer3 = "";
		}
		footer1 = ChatColor.translateAlternateColorCodes('&', footer1);

		header1 = header1.replaceAll("%player%", player.getDisplayName());
		footer1 = footer1.replaceAll("%player%", player.getDisplayName());

		PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
		IChatBaseComponent tabTitle = ChatSerializer.a("{\"text\": \"" + header1 + "\n" + header2 + "\n" + header3 + "\"}");
		IChatBaseComponent tabFoot = ChatSerializer.a("{\"text\": \"" + footer1 + "\n" + footer2 + "\n" + footer3 + "\"}");
		PacketPlayOutPlayerListHeaderFooter headerPacket = new PacketPlayOutPlayerListHeaderFooter(
				tabTitle);
		try {
			Field field = headerPacket.getClass().getDeclaredField("b");
			field.setAccessible(true);
			field.set(headerPacket, tabFoot);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connection.sendPacket(headerPacket);
		}
	}

	public static void sendTitle(Player player, String titlestr,
			String subtitlestr) {
		IChatBaseComponent chatTitle = ChatSerializer.a("{\"text\": \""
				+ titlestr + "\"}");
		IChatBaseComponent chatSubTitle = ChatSerializer.a("{\"text\": \""
				+ subtitlestr + "\"}");
		PacketPlayOutTitle subtitle = new PacketPlayOutTitle(
				EnumTitleAction.SUBTITLE, chatSubTitle);
		PacketPlayOutTitle title = new PacketPlayOutTitle(
				EnumTitleAction.TITLE, chatTitle);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(title);
		((CraftPlayer) player).getHandle().playerConnection
				.sendPacket(subtitle);
	}

	public static void sendActionBar(Player player, String message) {
		CraftPlayer p = (CraftPlayer) player;
		IChatBaseComponent cbc = ChatSerializer.a("{\"text\": \"" + message
				+ "\"}");
		PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, (byte) 2);
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(ppoc);
	}
	
	public static void playFirework(Location paramLocation,
			FireworkEffect paramFireworkEffect, Integer lifespan) {
		Entity localEntity = paramLocation.getWorld().spawnEntity(
				paramLocation, EntityType.FIREWORK);
		Firework localFirework = (Firework) localEntity;
		FireworkMeta localFireworkMeta = localFirework.getFireworkMeta();
		localFireworkMeta.addEffect(paramFireworkEffect);
		localFireworkMeta.setPower(1);
		localFirework.setFireworkMeta(localFireworkMeta);

		((CraftFirework) localFirework).getHandle().expectedLifespan = lifespan;
	}
	
	public static String createJson(Player player, String message, String hoverText, String commandToRun) {
        IChatBaseComponent comp = ChatSerializer.a("{\"text\":\"\",\"extra\":[{\"text\":\"" + message + "\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"" + hoverText + "\"},\"clickEvent\":{\"action\":\"run_command\",\"value\":\"" + commandToRun + "\"}}]}");
        PacketPlayOutChat packet = new PacketPlayOutChat(comp);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
		return message;
    }
	
	
}
