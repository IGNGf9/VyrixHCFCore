/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.team.upgrades.impl;

import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.team.event.FullTeamBypassEvent;
import net.hcriots.hcfactions.team.upgrades.TeamUpgrade;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

/**
 * Created by InspectMC
 * Date: 7/16/2020
 * Time: 9:31 PM
 */

public class ExtraSlotTeamUpgrade implements TeamUpgrade, Listener {

    @Override
    public String getUpgradeName() {
        return "Extra Team Slot";
    }

    @Override
    public String getDescription() {
        return "Increase your maximum team size by 1 slot";
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
    }

    @Override
    public int getTierLimit() {
        return 3;
    }

    @Override
    public int getPrice(int tier) {
        return 60 + ((tier - 1) * 30);
    }

    @EventHandler
    public void onFullTeamBypassEvent(FullTeamBypassEvent event) {
        event.setExtraSlots(getTier(event.getTeam()));

        if (getTier(event.getTeam()) > 0) {
            if (event.getTeam().getMembers().size() + 1 <= Hulu.getInstance().getMapHandler().getTeamSize() + getTier(event.getTeam())) {
                event.setAllowBypass(true);
            }
        }
    }

}
