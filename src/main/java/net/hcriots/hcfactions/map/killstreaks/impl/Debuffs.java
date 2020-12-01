/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.map.killstreaks.impl;

import net.hcriots.hcfactions.map.killstreaks.Killstreak;
import org.bukkit.entity.Player;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

public class Debuffs extends Killstreak {

    @Override
    public String getName() {
        return "Debuffs";
    }

    @Override
    public int[] getKills() {
        return new int[]{
                9
        };
    }

    @Override
    public void apply(Player player) {
        Potion poison = new Potion(PotionType.POISON);
        poison.setSplash(true);

        Potion slowness = new Potion(PotionType.SLOWNESS);
        slowness.setSplash(true);

        give(player, poison.toItemStack(1));
        give(player, slowness.toItemStack(1));
    }

}
