/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.events.purge.listener;

import net.hcriots.hcfactions.events.purge.commands.PurgeCommands;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Created by InspectMC
 * Date: 8/13/2020
 * Time: 3:31 PM
 */
public class PurgeListener implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onPlayerBuild(BlockBreakEvent event) {
        if (PurgeCommands.isPurgeTimer()) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onPlayerInteract(PlayerInteractEvent event) {
        //TODO: Check if they are in a claim
        if (PurgeCommands.isPurgeTimer() && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Material m = event.getClickedBlock().getType(); //get the block type clicked
            event.setCancelled(!m.equals(Material.CHEST) || !m.equals(Material.FENCE_GATE));
        }
    }
}
