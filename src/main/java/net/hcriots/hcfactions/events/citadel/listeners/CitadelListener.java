/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.events.citadel.listeners;

import cc.fyre.stark.util.event.HourEvent;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.events.citadel.CitadelHandler;
import net.hcriots.hcfactions.events.citadel.events.CitadelActivatedEvent;
import net.hcriots.hcfactions.events.citadel.events.CitadelCapturedEvent;
import net.hcriots.hcfactions.events.events.EventActivatedEvent;
import net.hcriots.hcfactions.events.events.EventCapturedEvent;
import net.hcriots.hcfactions.team.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.text.SimpleDateFormat;

public class CitadelListener implements Listener {

    @EventHandler
    public void onKOTHActivated(EventActivatedEvent event) {
        if (event.getEvent().getName().equalsIgnoreCase("Citadel")) {
            Hulu.getInstance().getServer().getPluginManager().callEvent(new CitadelActivatedEvent());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onKOTHCaptured(EventCapturedEvent event) {
        if (event.getEvent().getName().equalsIgnoreCase("Citadel")) {
            Team playerTeam = Hulu.getInstance().getTeamHandler().getTeam(event.getPlayer());

            if (playerTeam != null) {
                Hulu.getInstance().getCitadelHandler().addCapper(playerTeam.getUniqueId(), event.getPlayer());
                playerTeam.setCitadelsCapped(playerTeam.getCitadelsCapped() + 1);
            }
        }
    }

    @EventHandler
    public void onCitadelActivated(CitadelActivatedEvent event) {
        Hulu.getInstance().getCitadelHandler().resetCappers();
        Hulu.getInstance().getCitadelHandler().setCitadelCapTime(0);
    }

    @EventHandler
    public void onCitadelCaptured(CitadelCapturedEvent event) {
        Hulu.getInstance().getServer().broadcastMessage(CitadelHandler.PREFIX + " " + ChatColor.RED + "Citadel" + ChatColor.YELLOW + " is " + ChatColor.DARK_RED + "closed " + ChatColor.YELLOW + "until " + ChatColor.WHITE + (new SimpleDateFormat()).format(Hulu.getInstance().getCitadelHandler().getLootable()) + ChatColor.YELLOW + ".");

        Player player = event.getPlayer();
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "cr givekey " + player.getName() + " Koth 4");

        Hulu.getInstance().getCitadelHandler().setCitadelCapTime(System.currentTimeMillis());
    }

    @EventHandler(priority = EventPriority.MONITOR) // The monitor is here so we get called 'after' most join events.
    public void onPlayerJoin(PlayerJoinEvent event) {
        Team playerTeam = Hulu.getInstance().getTeamHandler().getTeam(event.getPlayer());

        if (playerTeam != null && Hulu.getInstance().getCitadelHandler().getCappers().contains(playerTeam.getUniqueId())) {
            event.getPlayer().sendMessage(CitadelHandler.PREFIX + " " + ChatColor.DARK_GREEN + "Your team currently controls Citadel.");
        }
    }


    @EventHandler
    public void onHour(HourEvent event) {
        // Every other hour
        if (event.getHour() % 2 == 0) {
            int respawned = Hulu.getInstance().getCitadelHandler().respawnCitadelChests();

            if (respawned != 0) {
                Hulu.getInstance().getServer().broadcastMessage(CitadelHandler.PREFIX + " " + ChatColor.GREEN + "Citadel loot chests have respawned!");
            }
        }
    }

}