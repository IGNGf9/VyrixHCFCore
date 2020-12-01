/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.events.region.glowmtn.listeners;

import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.events.region.glowmtn.GlowHandler;
import net.hcriots.hcfactions.events.region.glowmtn.GlowMountain;
import net.hcriots.hcfactions.team.Team;
import net.hcriots.hcfactions.team.claims.LandBoard;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import static org.bukkit.ChatColor.*;

public class GlowListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        Location location = event.getBlock().getLocation();
        GlowHandler glowHandler = Hulu.getInstance().getGlowHandler();
        GlowMountain glowMountain = glowHandler.getGlowMountain();
        Team teamAt = LandBoard.getInstance().getTeam(location);

        // If its unclaimed, or the server doesn't even have a mountain, or not even glowstone, why continue?
        if (Hulu.getInstance().getServerHandler().isUnclaimedOrRaidable(location) || !glowHandler.hasGlowMountain() || event.getBlock().getType() != Material.GLOWSTONE) {
            return;
        }

        // Check if the block broken is even in the mountain, and lets check the team to be safe
        if (teamAt == null || !teamAt.getName().equals(GlowHandler.getGlowTeamName())) {
            return;
        }

        if (!glowMountain.getGlowstone().contains(location.toVector().toBlockVector())) {
            return;
        }

        // Right, we can break this glowstone block, lets do it.
        event.setCancelled(false);

        // Now, we will decrease the value of the remaining glowstone
        glowMountain.setRemaining(glowMountain.getRemaining() - 1);

        // Let's announce when a glow mountain is a half and fully mined
        double total = glowMountain.getGlowstone().size();
        double remaining = glowMountain.getRemaining();


        // Lets broadcast
        if (total == remaining) {
            Bukkit.broadcastMessage(GOLD + "[Glowstone Mountain]" + AQUA + " 50% of Glowstone has been mined!");
        } else if (remaining == 0) {
            Bukkit.broadcastMessage(GOLD + "[Glowstone Mountain]" + RED + "  All Glowstone has been mined!");
        }
    }
/*
    @EventHandler
    public void onHour(HourEvent event) {
        // Every hour(event) -- Since you want it every two hours lets do it this way
        GlowHandler handler = Foxtrot.getInstance().getGlowHandler();

        if (!handler.hasGlowMountain()) {
            return;
        }

        // Check if its divisible by 2 (making it an even hour)
        if (event.getHour() % 2 == 0) {
            // Reset the glowstone
            handler.getGlowMountain().reset();

            // Broadcast the reset
            Bukkit.broadcastMessage(GOLD + "[Glowstone Mountain]" + GREEN + " All glowstone has been reset!");
        }
    }*/
}