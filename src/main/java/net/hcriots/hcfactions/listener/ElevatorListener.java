/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.listener;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Created by InspectMC
 * Date: 7/29/2020
 * Time: 6:26 PM
 */

public class ElevatorListener implements Listener {


    String elevatorsign = ChatColor.translateAlternateColorCodes('&', "&9[Elevator]");

    @EventHandler
    public void signPlace(SignChangeEvent event) {

        if (event.getLine(0).equalsIgnoreCase("[Elevator]") && event.getLine(1).equalsIgnoreCase("Up")) {
            event.setLine(0, ChatColor.translateAlternateColorCodes('&', "" + elevatorsign));
            event.setLine(1, ChatColor.translateAlternateColorCodes('&', "Up"));
        }
        if (event.getLine(0).equalsIgnoreCase("[Elevator]") && event.getLine(1).equalsIgnoreCase("Down")) {
            event.setLine(0, ChatColor.translateAlternateColorCodes('&', "" + elevatorsign));
            event.setLine(1, ChatColor.translateAlternateColorCodes('&', "Down"));
        }
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock() != null) {
            Player player = event.getPlayer();
            Block block = event.getClickedBlock();
            if (block.getType() == Material.SIGN || block.getType() == Material.SIGN_POST || block.getType() == Material.WALL_SIGN) {
                Sign sign = (Sign) block.getState();
                if (sign.getLine(0).equalsIgnoreCase(elevatorsign)) {
                    Location loc = block.getLocation();
                    if (sign.getLine(1).equalsIgnoreCase("Up")) {
                        while (true) {
                            Location location1 = loc.add(0.0, 1.0, 0.0);
                            Location location2 = loc.add(0.0, 1.0, 0.0);
                            if (location1.getY() >= 250.0) {
                                break;
                            }
                            if (location1.getBlock().getType() == Material.AIR && location2.getBlock().getType() == Material.AIR) {
                                player.teleport(new Location(location1.getWorld(), (double) location1.getBlockX() + 0.5, (double) location1.getBlockY() - 1, (double) location1.getBlockZ() + 0.5));
                                break;
                            }
                        }
                    }
                    if (sign.getLine(1).equalsIgnoreCase("Down")) {
                        while (true) {
                            Location location1 = loc.subtract(0.0, 1.0, 0.0);
                            Location location2 = loc.subtract(0.0, 1.0, 0.0);
                            if (location1.getY() <= 0.0) {
                                break;
                            }
                            if (location1.getBlock().getType() == Material.AIR && location2.getBlock().getType() == Material.AIR) {
                                player.teleport(new Location(location2.getWorld(), (double) location2.getBlockX() + 0.5, (double) location2.getBlockY() - 1, (double) location2.getBlockZ() + 0.5));
                                break;
                            }
                        }
                    }
                }
            }

        }
    }
}