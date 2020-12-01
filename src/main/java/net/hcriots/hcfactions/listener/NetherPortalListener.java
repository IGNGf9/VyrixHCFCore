/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.listener;

import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.team.Team;
import net.hcriots.hcfactions.team.claims.LandBoard;
import net.hcriots.hcfactions.team.dtr.DTRBitmask;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class NetherPortalListener implements Listener {

    @EventHandler
    public void onPlayerPortal(PlayerPortalEvent event) {
        Player player = event.getPlayer();

        if (event.getCause() != PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) {
            return;
        }

        if (event.getTo().getWorld().getEnvironment() == World.Environment.NORMAL) {
            if (DTRBitmask.SAFE_ZONE.appliesAt(event.getFrom())) {
                event.setCancelled(true);

                player.teleport(Hulu.getInstance().getServer().getWorld("world").getSpawnLocation());
                player.sendMessage(ChatColor.GREEN + "Teleported to overworld spawn!");
            }
        }

        Location to = event.getTo();

        if (DTRBitmask.ROAD.appliesAt(to)) {
            Team team = LandBoard.getInstance().getTeam(to);

            if (team.getName().contains("North")) {
                to.add(20, 0, 0); // add 20 on the X axis
            } else if (team.getName().contains("South")) {
                to.subtract(20, 0, 0); // subtract 20 on the X axis
            } else if (team.getName().contains("East")) {
                to.add(0, 0, 20); // add 20 on the Z axis
            } else if (team.getName().contains("West")) {
                to.subtract(0, 0, 20); // subtract 20 on the Z axis
            }
        }

        event.setTo(to);
    }

}