/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.bosses.eggs.type;

import cc.fyre.stark.Stark;
import cc.fyre.stark.core.profile.Profile;
import cc.fyre.stark.core.rank.Rank;
import cc.fyre.stark.util.CC;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.bosses.eggs.BossEggAbility;
import net.hcriots.hcfactions.bosses.entity.ReaperClass;
import net.hcriots.hcfactions.bosses.particles.ParticleEffect;
import net.hcriots.hcfactions.team.dtr.DTRBitmask;
import net.hcriots.hcfactions.util.EntityTypes;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by InspectMC
 * Date: 7/20/2020
 * Time: 8:54 PM
 */

public class Reaper extends BossEggAbility {

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public ItemStack getMaterial() {
        return new ItemStack(Material.MONSTER_EGG, 1, (byte) 95);
    }

    @Override
    public String getDisplayName() {
        return ChatColor.RED.toString() + ChatColor.BOLD + "Reaper Boss";
    }

    @Override
    public List<String> getLore() {
        List<String> toReturn = new ArrayList<>();
        toReturn.add(ChatColor.translateAlternateColorCodes('&', "&c&lReaper Egg"));
        toReturn.add(ChatColor.translateAlternateColorCodes('&', "&7&oHealth: &f450"));
        toReturn.add(ChatColor.translateAlternateColorCodes('&', "&7&oType: &fWither"));
        return toReturn;
    }

    @EventHandler
    public void onItem(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Location loc = player.getLocation();
        if (Hulu.getInstance().getServerHandler().isWarzone(loc)) {
            if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (!this.isSimilar(player.getItemInHand())) {
                    return;
                }
            }

            if (DTRBitmask.SAFE_ZONE.appliesAt(player.getLocation())) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "You can't use this in spawn!");
                return;
            }

            if (this.isSimilar(player.getItemInHand())) {
                ReaperClass reaperClass = new ReaperClass(player.getWorld());
                EntityTypes.spawnEntity(
                        reaperClass,
                        player.getLocation());
                Damageable damageableAtt = (Damageable) reaperClass.getBukkitEntity();

                damageableAtt.setMaxHealth(450);
                damageableAtt.setHealth(450);
                reaperClass.setCustomName(ChatColor.RED + ChatColor.BOLD.toString() + "Reaper");
                reaperClass.setCustomNameVisible(true);

                Entity boss = reaperClass.getBukkitEntity();
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        reaperClass.getBukkitEntity().setFireTicks(0);
                    }
                }.runTaskTimer(Hulu.getInstance(), 0, 1);
                new BukkitRunnable() {
                    int count = 0;

                    @SuppressWarnings("deprecation")
                    public void run() {
                        count++;
                        if (count == 30) {
                            this.cancel();
                            try {
                                Profile profile = Stark.instance.core.getProfileHandler().getByUUID(player.getUniqueId());
                                Rank rank = profile.getRank();
                                ParticleEffect.EXPLODE.sendToPlayers(Bukkit.getOnlinePlayers(), boss.getLocation(), 2, 0, 3, 0, 70);
                                ParticleEffect.BLOCK_CRACK.sendToPlayers(Bukkit.getOnlinePlayers(), boss.getLocation(), 5, 8, 3, 0, 80);
                                Bukkit.broadcastMessage(CC.INSTANCE.translate("&6&lBoss &7» &4" + profile.getDisplayName() + " &fhas summoned in the &4&lReaper Boss"));
                                Bukkit.broadcastMessage(CC.INSTANCE.translate("&6&lCoords &7» &f" + boss.getLocation().getBlockX() + ", " + boss.getLocation().getBlockZ()));
                                Bukkit.broadcastMessage(CC.INSTANCE.translate("&6&lReward &7» &fKill him to receive &a&l20 Faction Points&f!"));
                            } catch (Exception xe1) {
                                xe1.printStackTrace();
                            }
                        } else {
                            try {
                                ParticleEffect.FIREWORKS_SPARK.sendToPlayers(Bukkit.getOnlinePlayers(), boss.getLocation(), 1, 0, 1, 0, 50);
                                ParticleEffect.RED_DUST.sendToPlayers(Bukkit.getOnlinePlayers(), boss.getLocation(), 0, 0, 0, 0, 100);
                            } catch (Exception xe) {
                                xe.printStackTrace();
                            }
                        }
                    }
                }.runTaskTimer(Hulu.getInstance(), 0, 1);

//                EntityEquipment equip = null;
//                equip.setItemInHand(new ItemStack(Material.DIAMOND_SWORD));
//                equip.getItemInHand().addEnchantment(Enchantment.KNOCKBACK, 2);
//
//                equip.setHelmet(new ItemStack(Material.DIAMOND_HELMET));
//                equip.getHelmet().addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
//
//                equip.setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
//                equip.getChestplate().addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
//
//                equip.setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
//                equip.getLeggings().addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
//
//                equip.setBoots(new ItemStack(Material.DIAMOND_BOOTS));
//                equip.getBoots().addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);

                ItemStack itemStack = player.getItemInHand();
                itemStack.setAmount(itemStack.getAmount() - 1);
                player.setItemInHand(itemStack);
            }
        }
    }
}