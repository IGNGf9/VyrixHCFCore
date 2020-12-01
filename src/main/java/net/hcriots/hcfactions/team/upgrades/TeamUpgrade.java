/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.team.upgrades;

import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.team.Team;
import net.hcriots.hcfactions.team.upgrades.impl.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by InspectMC
 * Date: 7/16/2020
 * Time: 9:21 PM
 */

public interface TeamUpgrade {

    Map<String, TeamUpgrade> upgrades = new LinkedHashMap<>();

    static void register() {
        List<TeamUpgrade> list = Arrays.asList(
                new ExtraSlotTeamUpgrade(),
                new AbilityCooldownTeamUpgrade(),
                new ReduceDTRTeamUpgrade(),
//                new ExtraSpawnerTeamUpgrade(),
//                new IncreasedSpawnRateTeamUpgrade(),
//                new IncreasedXPRateTeamUpgrade(),
                new ReducedPearlCDTeamUpgrade(),
                new TeamVaultUpgrade(),
                new ClaimEffectsCategoryTeamUpgrade()
        );

        for (TeamUpgrade upgrade : list) {
            System.out.println("Registered team upgrade: \"" + upgrade.getUpgradeName() + "\"");
            upgrades.put(upgrade.getUpgradeName(), upgrade);

            if (upgrade instanceof Listener) {
                Hulu.getInstance().getServer().getPluginManager().registerEvents((Listener) upgrade, Hulu.getInstance());
            }
        }
    }

    String getUpgradeName();

    String getDescription();

    ItemStack getIcon();

    int getTierLimit();

    int getPrice(int tier);

    default int getTier(Team team) {
        return team.getUpgradeToTier().getOrDefault(getUpgradeName(), 0);
    }

    default boolean isCategory() {
        return false;
    }

    default List<ClaimEffectTeamUpgrade> getCategoryElements() {
        return null;
    }

    default void onPurchase(Player player, Team team, int tier, int price) {
    }
}