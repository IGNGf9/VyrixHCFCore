/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.server;

import com.cheatbreaker.api.CheatBreakerAPI;
import com.cheatbreaker.api.object.CBCooldown;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.team.dtr.DTRBitmask;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class SpawnTagHandler {

    @Getter
    private static final Map<String, Long> spawnTags = new ConcurrentHashMap<>();

    public static void removeTag(Player player) {
        spawnTags.remove(player.getName());
    }

    public static void addOffensiveSeconds(Player player, int seconds) {
        addSeconds(player, seconds);
    }

    public static void addPassiveSeconds(Player player, int seconds) {
        if (!Hulu.getInstance().getServerHandler().isPassiveTagEnabled()) {
            return;
        }

        addSeconds(player, seconds);
    }

    private static void addSeconds(Player player, int seconds) {
        if (DTRBitmask.SAFE_ZONE.appliesAt(player.getLocation())) {
            return;
        }

        if (isTagged(player)) {
            int secondsTaggedFor = (int) ((spawnTags.get(player.getName()) - System.currentTimeMillis()) / 1000L);
            int newSeconds = Math.min(secondsTaggedFor + seconds, getMaxTagTime());

            spawnTags.put(player.getName(), System.currentTimeMillis() + (newSeconds * 1000L));
            CheatBreakerAPI.getInstance().sendCooldown(player, new CBCooldown("Combat", 30, TimeUnit.SECONDS, Material.DIAMOND_SWORD));
        } else {
            player.sendMessage(ChatColor.YELLOW + "You have been spawn-tagged for §c" + seconds + " §eseconds!");
            spawnTags.put(player.getName(), System.currentTimeMillis() + (seconds * 1000L));
            CheatBreakerAPI.getInstance().sendCooldown(player, new CBCooldown("Combat", 30, TimeUnit.SECONDS, Material.DIAMOND_SWORD));
        }

    }

    public static long getTag(Player player) {
        return (spawnTags.get(player.getName()) - System.currentTimeMillis());
    }

    public static boolean isTagged(Player player) {
        if (player != null) {
            return spawnTags.containsKey(player.getName()) && spawnTags.get(player.getName()) > System.currentTimeMillis();
        } else {
            return false;
        }
    }

    public static int getMaxTagTime() {
        if (Hulu.getInstance().getServerHandler().isHardcore()) {
            return 45;
        }

        return Hulu.getInstance().getServerHandler().isPassiveTagEnabled() ? 30 : 60;
    }

}