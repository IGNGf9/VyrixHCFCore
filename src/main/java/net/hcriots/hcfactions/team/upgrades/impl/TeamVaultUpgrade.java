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

public class TeamVaultUpgrade implements TeamUpgrade, Listener {

    @Override
    public String getUpgradeName() {
        return "Vault Slot Upgrade";
    }

    @Override
    public String getDescription() {
        return "Increase slots in faction vault";
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Material.CHEST);
    }

    @Override
    public int getPrice(final int tier) {
        switch (tier) {
            case 1: {
                return 15;
            }
            case 2: {
                return 30;
            }
            case 3: {
                return 45;
            }
            default: {
                throw new RuntimeException("This shouldn't happen");
            }
        }
    }

    @Override
    public void onPurchase(final Player player, final Team team, final int tier, final int price) {
        team.increaseVault(team.getFactionVault(), (tier + 1) * 9);
    }

    @Override
    public int getTierLimit() {
        return 3;
    }
}