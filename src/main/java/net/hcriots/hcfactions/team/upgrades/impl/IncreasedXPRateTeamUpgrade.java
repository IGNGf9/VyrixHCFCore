/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.team.upgrades.impl;

import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.team.Team;
import net.hcriots.hcfactions.team.claims.Claim;
import net.hcriots.hcfactions.team.upgrades.TeamUpgrade;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by InspectMC
 * Date: 7/16/2020
 * Time: 9:33 PM
 */

public class IncreasedXPRateTeamUpgrade implements TeamUpgrade, Listener {

    @Override
    public String getUpgradeName() {
        return "Increased XP Rate";
    }

    @Override
    public String getDescription() {
        return "Increase the amount of XP earned in your claim";
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Material.WATCH);
    }

    @Override
    public int getPrice(int tier) {
        switch (tier) {
            case 1:
                return 25;
            case 2:
                return 50;
            case 3:
                return 75;
            default:
                throw new RuntimeException("This shouldn't happen");
        }
    }

    @Override
    public int getTierLimit() {
        return 3;
    }

    @EventHandler
    public void onPlayerExpChangeEvent(PlayerExpChangeEvent event) {
        Team team = Hulu.getInstance().getTeamHandler().getTeam(event.getPlayer());

        if (team != null) {
            for (Claim claim : team.getClaims()) {
                if (claim.contains(event.getPlayer())) {
                    int xpToApply = event.getAmount();

                    if (getTier(team) > 0) {
                        switch (getTier(team)) {
                            case 1:
                                xpToApply *= 2;
                                break;
                            case 2:
                                xpToApply *= 3;
                                break;
                            case 3:
                                xpToApply *= 4;
                                break;
                        }
                    }

                    event.setAmount(xpToApply);
                    break;
                }
            }
        }
    }

}
