/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.pvpclasses.pvpclasses.mage;

import lombok.Getter;
import org.bukkit.potion.PotionEffect;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by InspectMC
 * Date: 7/30/2020
 * Time: 7:56 PM
 */

public class MageEffect {

    @Getter
    private final PotionEffect potionEffect;
    @Getter
    private final int energy;

    // For the message we send when you select the (de)buff in your hotbar.
    @Getter
    private final Map<String, Long> lastMessageSent = new HashMap<>();

    private MageEffect(PotionEffect potionEffect, int energy) {
        this.potionEffect = potionEffect;
        this.energy = energy;
    }

    public static MageEffect fromPotion(PotionEffect potionEffect) {
        return (new MageEffect(potionEffect, -1));
    }

    public static MageEffect fromPotionAndEnergy(PotionEffect potionEffect, int energy) {
        return (new MageEffect(potionEffect, energy));
    }

    public static MageEffect fromEnergy(int energy) {
        return (new MageEffect(null, energy));
    }

}