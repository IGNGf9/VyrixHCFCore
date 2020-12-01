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

public class Speed2 extends PersistentKillstreak {

    public Speed2() {
        super("Speed 2", 12);
    }

    public void apply(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 300 * 20, 1));
    }

}
