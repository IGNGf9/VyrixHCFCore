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

public class ReduceDTRTeamUpgrade implements TeamUpgrade, Listener {

    @Override
    public String getUpgradeName() {
        return "Reduce DTR Regen";
    }

    @Override
    public String getDescription() {
        return "Reduce your factions dtr regen timer";
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Material.WATCH);
    }

    @Override
    public int getPrice(final int tier) {
        switch (tier) {
            case 1: {
                return 20;
            }
            case 2: {
                return 35;
            }
            case 3: {
                return 50;
            }
            default: {
                throw new RuntimeException("This shouldn't happen");
            }
        }
    }

    @Override
    public void onPurchase(final Player player, final Team team, final int tier, final int price) {
        team.setDtrRegenUpgrade((team.getDtrRegenUpgrade() + 1));
    }

    @Override
    public int getTierLimit() {
        return 3;
    }
}
