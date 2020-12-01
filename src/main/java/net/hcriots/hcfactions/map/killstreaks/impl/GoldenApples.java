/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.map.killstreaks.impl;

import net.hcriots.hcfactions.map.killstreaks.Killstreak;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GoldenApples extends Killstreak {

    @Override
    public String getName() {
        return "5 Golden Apples";
    }

    @Override
    public int[] getKills() {
        return new int[]{
                3
        };
    }

    @Override
    public void apply(Player player) {
        give(player, new ItemStack(Material.GOLDEN_APPLE, 5));
    }

}