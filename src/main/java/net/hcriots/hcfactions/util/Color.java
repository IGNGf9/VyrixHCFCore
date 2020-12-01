/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.util;

import org.bukkit.ChatColor;

/**
 * Created by InspectMC
 * Date: 7/21/2020
 * Time: 2:18 PM
 */

public class Color {

    public static String translate(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}