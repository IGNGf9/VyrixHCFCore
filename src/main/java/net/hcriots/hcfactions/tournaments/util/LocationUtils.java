/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.tournaments.util;

import org.bukkit.Location;
import org.bukkit.World;

/**
 * Created by InspectMC
 * Date: 8/3/2020
 * Time: 5:49 PM
 */

public class LocationUtils {

    public static String getString(Location loc) {
        StringBuilder builder = new StringBuilder();
        if (loc == null) {
            return "Location Not Found";
        }
        builder.append(loc.getX()).append("|");
        builder.append(loc.getY()).append("|");
        builder.append(loc.getZ()).append("|");
        builder.append(loc.getWorld().getName()).append("|");
        builder.append(loc.getYaw()).append("|");
        builder.append(loc.getPitch());
        return builder.toString();
    }

    public static Location getLocation(String s) {
        if ((s == null) || (s.equals("Location Not Found")) || (s.equals(""))) {
            return null;
        }
        String[] data = s.split("\\|");
        double x = Double.parseDouble(data[0]);
        double y = Double.parseDouble(data[1]);
        double z = Double.parseDouble(data[2]);
        World world = org.bukkit.Bukkit.getWorld(data[3]);
        Float yaw = Float.valueOf(Float.parseFloat(data[4]));
        Float pitch = Float.valueOf(Float.parseFloat(data[5]));
        return new Location(world, x, y, z, yaw.floatValue(), pitch.floatValue());
    }

    public static boolean isSameLocation(Location loc1, Location loc2) {
        return (loc1 != null) && (loc2 != null) && (loc1.equals(loc2));
    }
}
