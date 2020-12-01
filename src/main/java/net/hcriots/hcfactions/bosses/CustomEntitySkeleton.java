/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.bosses;

import net.minecraft.server.v1_7_R4.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftLivingEntity;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.entity.CreatureSpawnEvent;

/**
 * Created by InspectMC
 * Date: 7/20/2020
 * Time: 6:17 PM
 */

public class CustomEntitySkeleton extends EntitySkeleton {

    public CustomEntitySkeleton(World world) {
        super(world);
    }

    public static Skeleton spawn(Location location) {
        World world = ((CraftWorld) location.getWorld()).getHandle();
        final CustomEntitySkeleton customEntity = new CustomEntitySkeleton(world);

        customEntity.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        ((CraftLivingEntity) customEntity.getBukkitEntity()).setRemoveWhenFarAway(false);
        world.addEntity(customEntity, CreatureSpawnEvent.SpawnReason.CUSTOM);
        return (Skeleton) customEntity.getBukkitEntity();
    }

    protected void initAttributes() {
        this.getAttributeInstance(GenericAttributes.e).setValue(10.0D);
        this.getAttributeInstance(GenericAttributes.maxHealth).setValue(150.0D);
        this.setCustomName(ChatColor.DARK_RED + ChatColor.BOLD.toString() + "Goliath");
        this.setCustomNameVisible(true);

        //If you want it to be fast like the flash are something idk
        //  this.getAttributeInstance(GenericAttributes.d).setValue(2.0D);
    }

    protected Item getLoot() {
        return Items.APPLE;
    }
}