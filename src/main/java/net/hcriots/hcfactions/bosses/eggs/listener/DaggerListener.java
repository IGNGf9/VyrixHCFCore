/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.bosses.eggs.listener;

import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.util.Vector;

public class DaggerListener implements Listener {

    @EventHandler
    public void onTame(EntityTameEvent event) {
        if (event.getEntity().getType() == EntityType.WOLF && event.getEntity().getCustomName() != null && event.getEntity().getCustomName().equals(
                ChatColor.AQUA + ChatColor.BOLD.toString() + "Dagger"
        )) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        LivingEntity entity = (LivingEntity) event.getEntity();
        if (event.getEntity().getType() == EntityType.WITHER && entity.getCustomName() != null && entity.getCustomName().equals(
                ChatColor.RED + ChatColor.BOLD.toString() + "Reaper"
        )) {
            event.getEntity().setVelocity(new Vector());
        }
    }
}
