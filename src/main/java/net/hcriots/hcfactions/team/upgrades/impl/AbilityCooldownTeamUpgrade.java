/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.team.upgrades.impl;

import net.hcriots.hcfactions.team.Team;
import net.hcriots.hcfactions.team.upgrades.TeamUpgrade;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class AbilityCooldownTeamUpgrade implements TeamUpgrade, Listener {

    @Override
    public String getUpgradeName() {
        return "Ability Cooldown Upgrade";
    }

    @Override
    public String getDescription() {
        return "Decreases the Cooldown on abilities";
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Material.BONE);
    }

    @Override
    public int getPrice(final int tier) {
        switch (tier) {
            case 1: {
                return 15;
            }
            case 2: {
                return 25;
            }
            case 3: {
                return 35;
            }
            default: {
                throw new RuntimeException("This shouldn't happen");
            }
        }
    }

    @Override
    public void onPurchase(final Player player, final Team team, final int tier, final int price) {
        team.setAbilityCooldownUpgrade(team.getAbilityCooldownUpgrade() + 1);
    }

    @Override
    public int getTierLimit() {
        return 3;
    }
}
