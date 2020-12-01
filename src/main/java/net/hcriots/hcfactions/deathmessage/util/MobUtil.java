/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.deathmessage.util;

import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

public class MobUtil {

    public static String getItemName(ItemStack itemStack) {
        if (itemStack.getItemMeta().hasDisplayName()) {
            return (ChatColor.stripColor(itemStack.getItemMeta().getDisplayName()));
        }

        return (WordUtils.capitalizeFully(itemStack.getType().name().replace('_', ' ')));
    }

}