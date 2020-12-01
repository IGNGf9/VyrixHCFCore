/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.server;

import net.hcriots.hcfactions.Hulu;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.event.player.PlayerMoveEvent;

public interface RegionMoveHandler {

    RegionMoveHandler ALWAYS_TRUE = new RegionMoveHandler() {

        @Override
        public boolean handleMove(PlayerMoveEvent event) {
            return (true);
        }

    };

    RegionMoveHandler PVP_TIMER = new RegionMoveHandler() {

        @Override
        public boolean handleMove(PlayerMoveEvent event) {
            if (Hulu.getInstance().getPvPTimerMap().hasTimer(event.getPlayer().getUniqueId()) && event.getPlayer().getGameMode() != GameMode.CREATIVE) {
                event.getPlayer().sendMessage(ChatColor.RED + "You cannot do this while your PVPTimer is active!");
                event.getPlayer().sendMessage(ChatColor.GRAY.toString() + ChatColor.ITALIC + "To remove your PvPTimer type '" + ChatColor.WHITE + "/pvp enable" + ChatColor.GRAY.toString() + ChatColor.ITALIC + "'.");
                event.setTo(event.getFrom());
                return (false);
            }

            return (true);
        }

    };

    boolean handleMove(PlayerMoveEvent event);

}