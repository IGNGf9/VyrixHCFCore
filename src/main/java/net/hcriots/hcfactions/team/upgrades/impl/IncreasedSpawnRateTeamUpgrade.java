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
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Created by InspectMC
 * Date: 7/16/2020
 * Time: 9:33 PM
 */

public class IncreasedSpawnRateTeamUpgrade implements TeamUpgrade, Listener {

    @Override
    public String getUpgradeName() {
        return "Increase Mob Spawn Rate";
    }

    @Override
    public String getDescription() {
        return "Increase the rate at which mobs spawn in your claim";
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Material.BONE);
    }

    @Override
    public int getPrice(int tier) {
        switch (tier) {
            case 1:
                return 15;
            case 2:
                return 35;
            case 3:
                return 50;
            default:
                throw new RuntimeException("This shouldn't happen");
        }
    }

    @Override
    public int getTierLimit() {
        return 3;
    }

    @Override
    public void onPurchase(Player player, Team team, int tier, int price) {
        Bukkit.getScheduler().runTaskAsynchronously(Hulu.getInstance(), () -> {
            List<CreatureSpawner> list = team.findSpawners();

            Bukkit.getScheduler().runTask(Hulu.getInstance(), () -> {
                for (CreatureSpawner creatureSpawner : list) {
                    creatureSpawner.setDelay(creatureSpawner.getDelay() / tier);
                    creatureSpawner.update();
                }
            });
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockPlaceEvent(BlockPlaceEvent event) {
        if (event.getBlockPlaced().getType() == Material.MOB_SPAWNER) {
            Team team = Hulu.getInstance().getTeamHandler().getTeam(event.getPlayer());

            if (team != null) {
                for (Claim claim : team.getClaims()) {
                    if (claim.contains(event.getBlockPlaced().getLocation())) {
                        if (getTier(team) > 0) {
                            CreatureSpawner creatureSpawner = (CreatureSpawner) event.getBlockPlaced().getState();
                            creatureSpawner.setDelay(creatureSpawner.getDelay() / getTier(team));
                        }

                        break;
                    }
                }
            }
        }
    }

}
