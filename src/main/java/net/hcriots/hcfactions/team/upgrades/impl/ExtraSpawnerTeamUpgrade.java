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
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by InspectMC
 * Date: 7/16/2020
 * Time: 9:31 PM
 */

public class ExtraSpawnerTeamUpgrade implements TeamUpgrade, Listener {

    @Override
    public String getUpgradeName() {
        return "Extra Spawner";
    }

    @Override
    public String getDescription() {
        return "Increase the maximum amount of spawners you can have in your claim";
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Material.MOB_SPAWNER);
    }

    @Override
    public int getPrice(int tier) {
        return 15 + ((tier - 1) * 15);
    }

    @Override
    public int getTierLimit() {
        return 5;
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onBlockPlaceEvent(BlockPlaceEvent event) {
        if (event.getBlockPlaced().getType() == Material.MOB_SPAWNER) {
            Team team = Hulu.getInstance().getTeamHandler().getTeam(event.getPlayer());

            if (team != null) {
                for (Claim claim : team.getClaims()) {
                    if (claim.contains(event.getBlockPlaced().getLocation())) {
                        int spawnerLimit = 5 + getTier(team);

                        if (team.getSpawnersInClaim() >= spawnerLimit) {
                            event.setCancelled(true);
                            event.getPlayer().sendMessage(ChatColor.RED + "You can't place more than " + spawnerLimit + " spawners in your claim.");
                            event.getPlayer().sendMessage(ChatColor.RED + "To bypass this limit, you can purchase team upgrades via /t upgrades.");
                        }

                        break;
                    }
                }
            }
        }
    }

}
