/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.pvpclasses.pvpclasses;

import lombok.Getter;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.listener.FoxListener;
import net.hcriots.hcfactions.pvpclasses.PvPClass;
import net.hcriots.hcfactions.pvpclasses.PvPClassHandler;
import net.hcriots.hcfactions.pvpclasses.pvpclasses.mage.MageEffect;
import net.hcriots.hcfactions.server.SpawnTagHandler;
import net.hcriots.hcfactions.team.Team;
import net.hcriots.hcfactions.team.dtr.DTRBitmask;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by InspectMC
 * Date: 7/30/2020
 * Time: 7:54 PM
 */

public class MageClass extends PvPClass {

    public static final float MAX_ENERGY = 120;
    public static final float ENERGY_REGEN_PER_SECOND = 1;
    public static final int MAGE_RANGE = 20;
    public static final int EFFECT_COOLDOWN = 10 * 1000;
    @Getter
    private static final Map<String, Float> energy = new ConcurrentHashMap<>();
    @Getter
    private static final Map<String, Long> lastEffectUsage = new ConcurrentHashMap<>();
    public final Map<Material, MageEffect> MAGE_CLICK_EFFECTS = new HashMap<>();
    public final Map<Material, MageEffect> MAGE_PASSIVE_EFFECTS = new HashMap<>();


    public MageClass() {
        super("Mage", 15, null);
        MAGE_CLICK_EFFECTS.put(Material.GOLD_NUGGET, MageEffect.fromPotionAndEnergy(new PotionEffect(PotionEffectType.SLOW, 20 * 5, 1), 35));
        MAGE_CLICK_EFFECTS.put(Material.FERMENTED_SPIDER_EYE, MageEffect.fromPotionAndEnergy(new PotionEffect(PotionEffectType.WITHER, 20 * 6, 2), 35));
        MAGE_CLICK_EFFECTS.put(Material.INK_SACK, MageEffect.fromPotionAndEnergy(new PotionEffect(PotionEffectType.POISON, 20 * 5, 0), 45));
        MAGE_CLICK_EFFECTS.put(Material.SEEDS, MageEffect.fromPotionAndEnergy(new PotionEffect(PotionEffectType.CONFUSION, 20 * 5, 1), 40));
        MAGE_CLICK_EFFECTS.put(Material.COAL, MageEffect.fromPotionAndEnergy(new PotionEffect(PotionEffectType.WEAKNESS, 20 * 5, 1), 30));
        MAGE_CLICK_EFFECTS.put(Material.ROTTEN_FLESH, MageEffect.fromPotionAndEnergy(new PotionEffect(PotionEffectType.HUNGER, 20 * 5, 2), 25));


        MAGE_PASSIVE_EFFECTS.put(Material.GOLD_NUGGET, MageEffect.fromPotion(new PotionEffect(PotionEffectType.SLOW, 20 * 5, 1)));
        MAGE_PASSIVE_EFFECTS.put(Material.FERMENTED_SPIDER_EYE, MageEffect.fromPotion(new PotionEffect(PotionEffectType.WITHER, 20 * 6, 2)));
        MAGE_PASSIVE_EFFECTS.put(Material.INK_SACK, MageEffect.fromPotion(new PotionEffect(PotionEffectType.POISON, 20 * 5, 0)));
        MAGE_PASSIVE_EFFECTS.put(Material.SEEDS, MageEffect.fromPotion(new PotionEffect(PotionEffectType.CONFUSION, 20 * 5, 1)));
        MAGE_PASSIVE_EFFECTS.put(Material.COAL, MageEffect.fromPotion(new PotionEffect(PotionEffectType.WEAKNESS, 20 * 5, 1)));
        MAGE_PASSIVE_EFFECTS.put(Material.ROTTEN_FLESH, MageEffect.fromPotion(new PotionEffect(PotionEffectType.HUNGER, 20 * 5, 2)));
        new BukkitRunnable() {

            public void run() {
                for (Player player : Hulu.getInstance().getServer().getOnlinePlayers()) {
                    if (!PvPClassHandler.hasKitOn(player, MageClass.this) || Hulu.getInstance().getPvPTimerMap().hasTimer(player.getUniqueId())) {
                        continue;
                    }

                    if (energy.containsKey(player.getName())) {
                        if (energy.get(player.getName()) == MAX_ENERGY) {
                            continue;
                        }

                        energy.put(player.getName(), Math.min(MAX_ENERGY, energy.get(player.getName()) + ENERGY_REGEN_PER_SECOND));
                    } else {
                        energy.put(player.getName(), 0F);
                    }

                    int manaInt = energy.get(player.getName()).intValue();

                    if (manaInt % 10 == 0) {
                        player.sendMessage(ChatColor.LIGHT_PURPLE + "Mage Energy: " + ChatColor.GREEN + manaInt);
                    }
                }
            }

        }.runTaskTimer(Hulu.getInstance(), 15L, 20L);
    }

    @Override
    public void apply(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1), true);
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 0), true);

        if (Hulu.getInstance().getPvPTimerMap().hasTimer(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "You are in PvP Protection and cannot use Mage effects. Type '/pvp enable' to remove your protection.");
        }
    }

    @Override
    public void remove(Player player) {
        energy.remove(player.getName());

        for (MageEffect mageEffect : MAGE_CLICK_EFFECTS.values()) {
            mageEffect.getLastMessageSent().remove(player.getName());
        }

        for (MageEffect mageEffect : MAGE_CLICK_EFFECTS.values()) {
            mageEffect.getLastMessageSent().remove(player.getName());
        }
    }

    @Override
    public void tick(Player player) {
        if (!player.hasPotionEffect(PotionEffectType.SPEED)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
        }

        if (!player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1));
        }

        if (!player.hasPotionEffect(PotionEffectType.REGENERATION)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 0));
        }

        if (player.getItemInHand() != null && MAGE_PASSIVE_EFFECTS.containsKey(player.getItemInHand().getType()) && !DTRBitmask.SAFE_ZONE.appliesAt(player.getLocation())) {
            // CUSTOM

            if (player.getItemInHand().getType() == Material.GOLD_NUGGET || player.getItemInHand().getType() == Material.FERMENTED_SPIDER_EYE || player.getItemInHand().getType() == Material.INK_SACK || player.getItemInHand().getType() == Material.SEEDS || player.getItemInHand().getType() == Material.COAL || player.getItemInHand().getType() == Material.ROTTEN_FLESH && getLastEffectUsage().containsKey(player.getName()) && getLastEffectUsage().get(player.getName()) > System.currentTimeMillis()) {
                return;
            }

            giveMageEffect(player, MAGE_PASSIVE_EFFECTS.get(player.getItemInHand().getType()), true, false);
        }
        super.tick(player);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.getAction().name().contains("RIGHT_") || !event.hasItem() || !MAGE_CLICK_EFFECTS.containsKey(event.getItem().getType()) || !PvPClassHandler.hasKitOn(event.getPlayer(), this) || !energy.containsKey(event.getPlayer().getName())) {
            return;
        }

        if (DTRBitmask.SAFE_ZONE.appliesAt(event.getPlayer().getLocation())) {
            event.getPlayer().sendMessage(ChatColor.RED + "You cannot use Mage effects in spawn.");
            return;
        }

        if (Hulu.getInstance().getPvPTimerMap().hasTimer(event.getPlayer().getUniqueId())) {
            event.getPlayer().sendMessage(ChatColor.RED + "You are in PvP Protection and cannot use Mage effects. Type '/pvp enable' to remove your protection.");
            return;
        }

        if (getLastEffectUsage().containsKey(event.getPlayer().getName()) && getLastEffectUsage().get(event.getPlayer().getName()) > System.currentTimeMillis() && event.getPlayer().getGameMode() != GameMode.CREATIVE) {
            long millisLeft = getLastEffectUsage().get(event.getPlayer().getName()) - System.currentTimeMillis();

            double value = (millisLeft / 1000D);
            double sec = Math.round(10.0 * value) / 10.0;

            event.getPlayer().sendMessage(ChatColor.RED + "You cannot use this for another " + ChatColor.BOLD + sec + ChatColor.RED + " seconds!");
            return;
        }

        MageEffect mageEffect = MAGE_CLICK_EFFECTS.get(event.getItem().getType());

        if (mageEffect.getEnergy() > energy.get(event.getPlayer().getName())) {
            event.getPlayer().sendMessage(ChatColor.RED + "You need " + mageEffect.getEnergy() + " energy, but you only have " + energy.get(event.getPlayer().getName()).intValue());
            return;
        }

        energy.put(event.getPlayer().getName(), energy.get(event.getPlayer().getName()) - mageEffect.getEnergy());

        boolean negative = mageEffect.getPotionEffect() != null && FoxListener.DEBUFFS.contains(mageEffect.getPotionEffect().getType());

        getLastEffectUsage().put(event.getPlayer().getName(), System.currentTimeMillis() + EFFECT_COOLDOWN);
        SpawnTagHandler.addOffensiveSeconds(event.getPlayer(), SpawnTagHandler.getMaxTagTime());
        giveMageEffect(event.getPlayer(), mageEffect, !negative, true);

        if (event.getPlayer().getItemInHand().getAmount() == 1) {
            event.getPlayer().setItemInHand(new ItemStack(Material.AIR));
            event.getPlayer().updateInventory();
        } else {
            event.getPlayer().getItemInHand().setAmount(event.getPlayer().getItemInHand().getAmount() - 1);
        }
    }

    public void giveMageEffect(Player source, MageEffect mageEffect, boolean friendly, boolean persistOldValues) {
        for (Player player : getNearbyPlayers(source, friendly)) {
            if (DTRBitmask.SAFE_ZONE.appliesAt(player.getLocation())) {
                continue;
            }

            if (mageEffect.getPotionEffect() != null) {
                smartAddPotion(player, mageEffect.getPotionEffect(), persistOldValues, this);
            } else {
                Material material = source.getItemInHand().getType();
                giveCustomMageEffect(player, material);
            }
        }
    }

    public void giveCustomMageEffect(Player player, Material material) {
        switch (material) {
            case WHEAT:
                for (Player nearbyPlayer : getNearbyPlayers(player, true)) {
                    nearbyPlayer.setFoodLevel(20);
                    nearbyPlayer.setSaturation(10F);
                }

                break;
            case FERMENTED_SPIDER_EYE:


                break;
            default:
                Hulu.getInstance().getLogger().warning("No custom Mage effect defined for " + material + ".");
        }
    }

    public List<Player> getNearbyPlayers(Player player, boolean friendly) {
        List<Player> valid = new ArrayList<>();
        Team sourceTeam = Hulu.getInstance().getTeamHandler().getTeam(player);

        // We divide by 2 so that the range isn't as much on the Y level (and can't be abused by standing on top of / under events)
        for (Entity entity : player.getNearbyEntities(MAGE_RANGE, MAGE_RANGE / 2, MAGE_RANGE)) {
            if (entity instanceof Player) {
                Player nearbyPlayer = (Player) entity;

                if (Hulu.getInstance().getPvPTimerMap().hasTimer(nearbyPlayer.getUniqueId())) {
                    continue;
                }

                if (sourceTeam == null) {
                    if (!friendly) {
                        valid.add(nearbyPlayer);
                    }

                    continue;
                }

                boolean isFriendly = sourceTeam.isMember(nearbyPlayer.getUniqueId());
                boolean isAlly = sourceTeam.isAlly(nearbyPlayer.getUniqueId());

                if (friendly && isFriendly) {
                    valid.add(nearbyPlayer);
                } else if (!friendly && !isFriendly && !isAlly) { // the isAlly is here so you can't give your allies negative effects, but so you also can't give them positive effects.
                    valid.add(nearbyPlayer);
                }
            }
        }

        valid.add(player);
        return (valid);
    }

    @Override
    public boolean qualifies(PlayerInventory armor) {
        return wearingAllArmor(armor) &&
                armor.getHelmet().getType() == Material.GOLD_HELMET &&
                armor.getChestplate().getType() == Material.CHAINMAIL_CHESTPLATE &&
                armor.getLeggings().getType() == Material.CHAINMAIL_LEGGINGS &&
                armor.getBoots().getType() == Material.GOLD_BOOTS;
    }
}