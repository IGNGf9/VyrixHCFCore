/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.listener;

import net.hcriots.hcfactions.Hulu;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.metadata.FixedMetadataValue;

/**
 * Created by InspectMC
 * Date: 7/29/2020
 * Time: 6:22 PM
 */

public class MobSackerListener implements Listener {

    private static final double STACK_RADIUS = 5.0;

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        LivingEntity entity = event.getEntity();
        for (Entity nearbyEntity : entity.getNearbyEntities(STACK_RADIUS, STACK_RADIUS, STACK_RADIUS)) {
            if (nearbyEntity instanceof LivingEntity) {
                if (nearbyEntity.hasMetadata("stacked")) {
                    //add entity to stack
                    break;
                }

                //make entity a new stack
                entity.setMetadata("stacked", new FixedMetadataValue(Hulu.getInstance(), 1));
            }
        }
    }

}
