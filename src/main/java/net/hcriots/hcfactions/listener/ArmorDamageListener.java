/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;

import java.util.Random;

public class ArmorDamageListener implements Listener {

    private static final Random random = new Random();

    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onItemDamage(PlayerItemDamageEvent e) {

        if (30 < random.nextInt(100)) {
            e.setCancelled(true);
        }

    }
}
