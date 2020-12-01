/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.util;

import net.hcriots.hcfactions.bosses.entity.ReaperClass;
import net.minecraft.server.v1_7_R4.Entity;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;

import java.util.Map;

import static net.hcriots.hcfactions.util.EntityNMS.getPrivateField;

public enum EntityTypes {
    CUSTOM_ZOMBIE("Reaper", 64, ReaperClass.class);

    EntityTypes(String name, int id, Class<? extends Entity> custom) {
        addToMaps(custom, name, id);
    }

    public static void spawnEntity(Entity entity, Location loc) {
        entity.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        ((CraftWorld) loc.getWorld()).getHandle().addEntity(entity);
    }

    private static void addToMaps(Class clazz, String name, int id) {
        //getPrivateField is the method from above.
        //Remove the lines with // in front of them if you want to override default entities (You'd have to remove the default entity from the map first though).
        ((Map) getPrivateField("c", net.minecraft.server.v1_7_R4.EntityTypes.class, null)).put(name, clazz);
        ((Map) getPrivateField("d", net.minecraft.server.v1_7_R4.EntityTypes.class, null)).put(clazz, name);
        //((Map)getPrivateField("e", net.minecraft.server.v1_7_R4.EntityTypes.class, null)).put(Integer.valueOf(id), clazz);
        ((Map) getPrivateField("f", net.minecraft.server.v1_7_R4.EntityTypes.class, null)).put(clazz, Integer.valueOf(id));
        //((Map)getPrivateField("g", net.minecraft.server.v1_7_R4.EntityTypes.class, null)).put(name, Integer.valueOf(id));
    }
}