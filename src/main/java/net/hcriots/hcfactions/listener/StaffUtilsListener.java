/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.listener;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class StaffUtilsListener implements Listener {

    private Location lastDamageLocation;

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            lastDamageLocation = event.getEntity().getLocation();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (lastDamageLocation != null && event.getItem() != null && event.getItem().getType() == Material.EMERALD && event.getPlayer().getGameMode() == GameMode.CREATIVE) {
            event.getPlayer().teleport(lastDamageLocation);
        }
    }

    @EventHandler
    public void onPlayerInteract2(PlayerInteractEvent event) {
        if (event.getPlayer().hasPermission("stark.admin") || event.getPlayer().isOp()) {
            return;
        }

        if (event.getAction() == Action.PHYSICAL && event.getPlayer().hasMetadata("invisible") && (event.getClickedBlock().getType() == Material.CROPS || event.getClickedBlock().getType() == Material.SOIL)) {
            event.setCancelled(true);
        }
    }

}