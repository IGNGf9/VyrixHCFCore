/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.chunklimiter;

import lombok.Getter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.UUID;

public class ChunkLimiterListener implements Listener {

    @Getter
    private static final HashMap<UUID, Integer> viewDistances = new HashMap<>();
    private final HashMap<UUID, Integer> defaultView = new HashMap<>();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        //defaultView.put(event.getPlayer().getUniqueId(), ((CraftPlayer) event.getPlayer()).spigot().getViewDistance());
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        defaultView.remove(event.getPlayer().getUniqueId());
        viewDistances.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (event.getFrom().getBlockX() >> 4 == event.getTo().getBlockX() >> 4 && event.getFrom().getBlockY() >> 4 == event.getTo().getBlockY() >> 4 && event.getFrom().getBlockZ() >> 4 == event.getTo().getBlockZ() >> 4) {
            return;
        }

        if (viewDistances.containsKey(event.getPlayer().getUniqueId())) {
            return;
        }

        /*int view = ((CraftPlayer) event.getPlayer()).spigot().getViewDistance();
        int target = view;

        Team team = LandBoard.getInstance().getTeam(event.getTo());
        if (team != null && !team.getKitName().equalsIgnoreCase("Warzone")) {
            if (team.getKitName().equalsIgnoreCase("spawn")) {
                target = 3;
            } else {
                target = defaultView.get(event.getPlayer().getUniqueId());
            }
        } else {
            if (event.getTo().getBlockY() < 20) {
                target = 1;
            } else {
                target = defaultView.get(event.getPlayer().getUniqueId());
            }
        }

        if (target != view) {
            ((CraftPlayer) event.getPlayer()).spigot().setViewDistance(target);
        }
        */
    }

}
