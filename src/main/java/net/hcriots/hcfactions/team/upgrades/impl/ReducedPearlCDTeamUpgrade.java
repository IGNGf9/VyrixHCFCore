/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.team.upgrades.impl;

import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.server.event.EnderpearlCooldownAppliedEvent;
import net.hcriots.hcfactions.team.Team;
import net.hcriots.hcfactions.team.upgrades.TeamUpgrade;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

/**
 * Created by InspectMC
 * Date: 7/16/2020
 * Time: 9:34 PM
 */

public class ReducedPearlCDTeamUpgrade implements TeamUpgrade, Listener {

    @Override
    public String getUpgradeName() {
        return "Reduced Pearl Cooldown";
    }

    @Override
    public String getDescription() {
        return "Reduce the Pearl Cooldown";
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Material.ENDER_PEARL);
    }

    @Override
    public int getPrice(int tier) {
        switch (tier) {
            case 1:
                return 40;
            case 2:
                return 60;
            case 3:
                return 80;
            default:
                throw new RuntimeException("This shouldn't happen");
        }
    }

    @Override
    public int getTierLimit() {
        return 3;
    }

    @EventHandler
    public void onEnderpearlCooldownAppliedEvent(EnderpearlCooldownAppliedEvent event) {
        Team team = Hulu.getInstance().getTeamHandler().getTeam(event.getPlayer());

        if (team != null) {
            if (getTier(team) > 0) {
                long timeToApply = event.getTimeToApply();

                switch (getTier(team)) {
                    case 1:
                        timeToApply = 14_000L;
                        break;
                    case 2:
                        timeToApply = 10_000L;
                        break;
                    case 3:
                        timeToApply = 8_000L;
                        break;
                }
                event.setTimeToApply(timeToApply);
            }
        }
    }

}
