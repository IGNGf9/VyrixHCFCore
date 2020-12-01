/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.pvpclasses.pvpclasses;

import cc.fyre.stark.core.util.TimeUtils;
import net.hcriots.hcfactions.pvpclasses.PvPClass;
import net.hcriots.hcfactions.server.SpawnTagHandler;
import net.hcriots.hcfactions.team.dtr.DTRBitmask;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by InspectMC
 * Date: 6/28/2020
 * Time: 11:23 PM
 */
public class ScorpionClass extends PvPClass {


    private static final Map<String, Long> lastSpeedUsage = new HashMap<>();
    private static final Map<String, Long> lastresistance = new HashMap<>();

    public ScorpionClass() {
        super("Scorpion", 15, Arrays.asList(Material.SPIDER_EYE, Material.FEATHER));
    }


    @Override
    public void apply(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2), true);
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 0), true);
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0), true);
    }

    @Override
    public void tick(Player player) {
        if (!player.hasPotionEffect(PotionEffectType.SPEED)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));
        }

        if (!player.hasPotionEffect(PotionEffectType.REGENERATION)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 0));

        }

        if (!player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0));
        }

        super.tick(player);
    }

    @Override
    public void remove(Player player) {
        removeInfiniteEffects(player);
    }

    @Override
    public boolean itemConsumed(Player player, Material material) {
        if (material == Material.SUGAR) {
            if (lastSpeedUsage.containsKey(player.getName()) && lastSpeedUsage.get(player.getName()) > System.currentTimeMillis()) {
                long millisLeft = lastSpeedUsage.get(player.getName()) - System.currentTimeMillis();
                String msg = TimeUtils.formatIntoDetailedString((int) millisLeft / 1000);

                player.sendMessage(ChatColor.RED + "You cannot use this for another §c§l" + msg + "§c.");
                return (false);
            }

            lastSpeedUsage.put(player.getName(), System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(30));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 3), true);
            return (true);
        } else {
            if (DTRBitmask.SAFE_ZONE.appliesAt(player.getLocation())) {
                player.sendMessage(ChatColor.RED + "You can't use this in spawn!");
                return (false);
            }

            if (lastresistance.containsKey(player.getName()) && lastresistance.get(player.getName()) > System.currentTimeMillis()) {
                long millisLeft = lastresistance.get(player.getName()) - System.currentTimeMillis();
                String msg = TimeUtils.formatIntoDetailedString((int) millisLeft / 1000);

                player.sendMessage(ChatColor.RED + "You cannot use this for another §c§l" + msg + "§c.");
                return (false);
            }

            lastresistance.put(player.getName(), System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 5, 3));

            SpawnTagHandler.addPassiveSeconds(player, SpawnTagHandler.getMaxTagTime());
            return (false);
        }
    }

    @Override
    public boolean qualifies(PlayerInventory armor) {
        return wearingAllArmor(armor) &&
                armor.getHelmet().getType() == Material.LEATHER_HELMET &&
                armor.getChestplate().getType() == Material.CHAINMAIL_CHESTPLATE &&
                armor.getLeggings().getType() == Material.LEATHER_LEGGINGS &&
                armor.getBoots().getType() == Material.DIAMOND_BOOTS;
    }
}
