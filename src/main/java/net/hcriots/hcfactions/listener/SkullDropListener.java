/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

/**
 * Created by InspectMC
 * Date: 7/14/2020
 * Time: 1:14 AM
 */
public class SkullDropListener implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void dropPlayerSkull(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Player killer = player.getKiller();
        if (killer != null && killer.hasPermission("hulu.playerhead.drop")) {
            ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
            SkullMeta meta = (SkullMeta) skull.getItemMeta();
            meta.setOwner(player.getName());
            skull.setItemMeta(meta);
            event.getDrops().add(skull);
        }
    }
}
