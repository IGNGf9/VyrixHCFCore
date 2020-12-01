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
import net.hcriots.hcfactions.util.EffectSnapshot;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

/**
 * Created by InspectMC
 * Date: 7/16/2020
 * Time: 9:28 PM
 */

public class ClaimEffectsCategoryTeamUpgrade implements TeamUpgrade, Listener {

    private static final List<ClaimEffectTeamUpgrade> claimEffectUpgrades = Arrays.asList(
            new ClaimEffectTeamUpgrade("Saturation", 10, 0, 1, new ItemStack(Material.MELON), PotionEffectType.SATURATION),
            new ClaimEffectTeamUpgrade("Speed", 25, 25, 3, new ItemStack(Material.SUGAR), PotionEffectType.SPEED),
            new ClaimEffectTeamUpgrade("Strength", 35, 0, 1, new ItemStack(Material.BLAZE_POWDER), PotionEffectType.INCREASE_DAMAGE),
            new ClaimEffectTeamUpgrade("Fire Resistance", 20, 0, 1, new ItemStack(Material.MAGMA_CREAM), PotionEffectType.FIRE_RESISTANCE),
            new ClaimEffectTeamUpgrade("Invisibility", 30, 0, 1, new ItemStack(Material.WHEAT), PotionEffectType.INVISIBILITY)
    );

    private static final Map<UUID, Boolean> receivingEffects = new HashMap<>();
    private static final Map<UUID, List<EffectSnapshot>> effectsToRestore = new HashMap<>();

    public ClaimEffectsCategoryTeamUpgrade() {
        // Start task to give effects to players in teams that have team upgrades
        Bukkit.getScheduler().runTaskTimerAsynchronously(Hulu.getInstance(), () -> {
            playerLoop:
            for (Player player : Bukkit.getOnlinePlayers()) {
                Team team = Hulu.getInstance().getTeamHandler().getTeam(player);

                if (team != null) {
                    for (Claim claim : team.getClaims()) {
                        if (claim.contains(player)) {
                            receivingEffects.put(player.getUniqueId(), true);

                            List<EffectSnapshot> effects = new ArrayList<>();

                            for (ClaimEffectTeamUpgrade upgrade : claimEffectUpgrades) {
                                if (upgrade.getTier(team) > 0) {
                                    // Store their previous matching effects
                                    for (PotionEffect activeEffect : player.getActivePotionEffects()) {
                                        // Make sure effectType matches and that the duration isn't over 15 minutes
                                        // I tried checking if duration == Integer.MAX_VALUE but it doesn't seem to work
                                        if (activeEffect.getType().equals(upgrade.getPotionEffectType()) && activeEffect.getDuration() <= 18_000L) {
                                            effects.add(new EffectSnapshot(activeEffect.getType(), activeEffect.getAmplifier(), activeEffect.getDuration()));
                                            player.removePotionEffect(activeEffect.getType());
                                            break;
                                        }
                                    }

                                    player.addPotionEffect(new PotionEffect(upgrade.getPotionEffectType(), Integer.MAX_VALUE, upgrade.getTier(team) - 1));
                                }
                            }

                            effectsToRestore.putIfAbsent(player.getUniqueId(), effects);

                            continue playerLoop;
                        }
                    }

                    if (receivingEffects.getOrDefault(player.getUniqueId(), false)) {
                        receivingEffects.put(player.getUniqueId(), false);

                        for (ClaimEffectTeamUpgrade upgrade : claimEffectUpgrades) {
                            if (upgrade.getTier(team) > 0) {
                                player.removePotionEffect(upgrade.getPotionEffectType());
                            }
                        }

                        for (EffectSnapshot effectSnapshot : effectsToRestore.get(player.getUniqueId())) {
                            player.addPotionEffect(new PotionEffect(effectSnapshot.getEffectType(), effectSnapshot.getDuration(), effectSnapshot.getAmplifier()));
                        }

                        effectsToRestore.remove(player.getUniqueId());
                    }
                } else {
                    if (receivingEffects.getOrDefault(player.getUniqueId(), false)) {
                        receivingEffects.put(player.getUniqueId(), false);

                        for (PotionEffect effect : player.getActivePotionEffects()) {
                            player.removePotionEffect(effect.getType());
                        }

                        if (effectsToRestore.containsKey(player.getUniqueId())) {
                            for (EffectSnapshot effectSnapshot : effectsToRestore.get(player.getUniqueId())) {
                                player.addPotionEffect(new PotionEffect(effectSnapshot.getEffectType(), effectSnapshot.getDuration(), effectSnapshot.getAmplifier()));
                            }

                            effectsToRestore.remove(player.getUniqueId());
                        }
                    }
                }
            }
        }, 0L, 20L);
    }

    @Override
    public String getUpgradeName() {
        return "Claim Effects";
    }

    @Override
    public String getDescription() {
        return "Receive passive effects while in your claim";
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Material.BEACON);
    }

    @Override
    public int getPrice(int tier) {
        return -1;
    }

    @Override
    public int getTier(Team team) {
        return -1;
    }

    @Override
    public int getTierLimit() {
        return -1;
    }

    @Override
    public boolean isCategory() {
        return true;
    }

    @Override
    public List<ClaimEffectTeamUpgrade> getCategoryElements() {
        return claimEffectUpgrades;
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        receivingEffects.remove(event.getPlayer().getUniqueId());
        effectsToRestore.remove(event.getPlayer().getUniqueId());
    }

}
