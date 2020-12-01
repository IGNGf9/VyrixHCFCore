/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.map.killstreaks.impl;

import net.hcriots.hcfactions.map.killstreaks.PersistentKillstreak;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Strength extends PersistentKillstreak {

    public Strength() {
        super("Strength", 18);
    }

    public void apply(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 45 * 20, 1));
    }

}