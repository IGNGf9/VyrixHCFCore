/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.pvpclasses.pvpclasses.archer;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by InspectMC
 * Date: 7/6/2020
 * Time: 3:43 PM
 */

public interface ArcherUpgrade {

    Map<UUID, Long> cooldown = new HashMap<>();

    static boolean canUseAbility(Player player) {
        return System.currentTimeMillis() >= cooldown.getOrDefault(player.getUniqueId(), 0L);
    }

    static void setCooldown(Player player, int seconds) {
        cooldown.put(player.getUniqueId(), System.currentTimeMillis() + (seconds * 1000L));
    }

    static long getRemainingTime(Player player) {
        return cooldown.get(player.getUniqueId()) - System.currentTimeMillis();
    }

    String getUpgradeName();

    List<String> getDescription();

    int getKillsNeeded();

    short getMaterialData();

    void onHit(Player shooter, Player victim);

    boolean applies(Player shooter);

}