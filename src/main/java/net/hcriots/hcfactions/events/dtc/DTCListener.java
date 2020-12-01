/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.events.dtc;

import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.events.Event;
import net.hcriots.hcfactions.events.EventType;
import net.hcriots.hcfactions.team.dtr.DTRBitmask;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class DTCListener implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST) // so people don't get their chat spammed
    public void onBreak(BlockBreakEvent event) {
        if (event.getPlayer().getGameMode() == GameMode.CREATIVE) {
            return;
        }

        Location location = event.getBlock().getLocation();
        if (!DTRBitmask.DTC.appliesAt(location)) {
            return;
        }

        DTC activeDTC = (DTC) Hulu.getInstance().getEventHandler().getEvents().stream().filter(Event::isActive).filter(ev -> ev.getType() == EventType.DTC).findFirst().orElse(null);

        if (activeDTC == null) {
            return;
        }

        if (event.getBlock().getType() == Material.OBSIDIAN) {
            event.setCancelled(true);
            activeDTC.blockBroken(event.getPlayer());
        }
    }

}
