/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.pvpclasses.pvpclasses.bard;

import lombok.Getter;
import org.bukkit.potion.PotionEffect;

import java.util.HashMap;
import java.util.Map;

public class BardEffect {

    @Getter
    private final PotionEffect potionEffect;
    @Getter
    private final int energy;

    // For the message we send when you select the (de)buff in your hotbar.
    @Getter
    private final Map<String, Long> lastMessageSent = new HashMap<>();

    private BardEffect(PotionEffect potionEffect, int energy) {
        this.potionEffect = potionEffect;
        this.energy = energy;
    }

    public static BardEffect fromPotion(PotionEffect potionEffect) {
        return (new BardEffect(potionEffect, -1));
    }

    public static BardEffect fromPotionAndEnergy(PotionEffect potionEffect, int energy) {
        return (new BardEffect(potionEffect, energy));
    }

    public static BardEffect fromEnergy(int energy) {
        return (new BardEffect(null, energy));
    }

}