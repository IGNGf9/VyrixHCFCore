/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.team.upgrades.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.hcriots.hcfactions.team.upgrades.TeamUpgrade;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

/**
 * Created by InspectMC
 * Date: 7/16/2020
 * Time: 9:30 PM
 */

@AllArgsConstructor
public class ClaimEffectTeamUpgrade implements TeamUpgrade {

    private final String upgradeName;
    private final int basePrice;
    private final int priceIncrement;
    private final int tierLimit;
    @Getter
    private final ItemStack icon;
    @Getter
    private final PotionEffectType potionEffectType;

    @Override
    public String getUpgradeName() {
        return upgradeName;
    }

    @Override
    public String getDescription() {
        return "Receive " + upgradeName + " while in your claim";
    }

    @Override
    public int getTierLimit() {
        return tierLimit;
    }

    @Override
    public int getPrice(int tier) {
        return basePrice + ((tier - 1) * priceIncrement);
    }

}
