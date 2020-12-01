/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.bosses.listener;

import cc.fyre.stark.util.ItemBuilder;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.team.Team;
import net.hcriots.hcfactions.util.MathUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

/**
 * Created by InspectMC
 * Date: 7/20/2020
 * Time: 6:20 PM
 */

public class BossDeathListener implements Listener {

    @EventHandler
    public void onDeath(EntityDeathEvent e) {
        if (e.getEntity().getCustomName() != null) {
            if (e.getEntity().getCustomName().equals(ChatColor.GOLD + ChatColor.BOLD.toString() + "Goliath")) {
                Player p = e.getEntity().getKiller();
                Team team = Hulu.getInstance().getTeamHandler().getTeam(p);
                Bukkit.broadcastMessage(ChatColor.GOLD + "The Goliath has been killed by " + e.getEntity().getKiller().getDisplayName());
                if (e.getEntity().getKiller() != null) {
                    e.getDrops().clear();
                    if (team != null) {
                        team.sendMessage("§fYour faction has gained §610 points §ffor killing the boss!");
                        team.addPlaytimePoints(10);
                    }
                    p.sendMessage("§fYou have killed §6§lThe Goliath");

                    List<ItemStack> randItem = Arrays.asList(
                            new ItemBuilder(Material.DIAMOND_SWORD)
                                    .name("&6&lGoliath's Slayer")
                                    .enchant(Enchantment.DAMAGE_ALL, 3)
                                    .enchant(Enchantment.DURABILITY, 3)
                                    .enchant(Enchantment.FIRE_ASPECT, 2)
                                    .build()
                    );

                    List<String> randCmd = Arrays.asList(
                            "partnerpackage " + p.getName() + " 3",
                            "airdrops give " + p.getName() + " 5",
                            "monthlycrate give " + p.getName() + " Pandora 3",
                            "cr givekey " + p.getName() + " Halloween 3",
                            "cr givekey " + p.getName() + " KOTH 1",
                            "ability NinjaStar 1 " + p.getName()
                    );

                    int rand = MathUtils.getBetween(1, 10);
                    if (rand <= 5) {
                        Object toDrop = MathUtils.getRandomObject(randItem);
                        e.getEntity().getLocation().getWorld().dropItemNaturally(e.getEntity().getLocation(), (ItemStack) toDrop);
                    } else {
                        Object toRun = MathUtils.getRandomObject(randCmd);
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), (String) toRun);
                    }
                }
            } else if (e.getEntity().getCustomName().equals(ChatColor.RED + ChatColor.BOLD.toString() + "Reaper")) {
                Player p = e.getEntity().getKiller();
                Team team = Hulu.getInstance().getTeamHandler().getTeam(p);
                Bukkit.broadcastMessage(ChatColor.GOLD + "The Reaper has been killed by " + e.getEntity().getKiller().getDisplayName());
                if (e.getEntity().getKiller() != null) {
                    e.getDrops().clear();
                    if (team != null) {
                        team.sendMessage("§fYour faction has gained §615 points §ffor killing the boss!");
                        team.addPlaytimePoints(15);
                    }
                    p.sendMessage("§fYou have killed §c§lThe Reaper");

                    List<ItemStack> randItem = Arrays.asList(
                            new ItemBuilder(Material.DIAMOND_SWORD)
                                    .name("&c&lReaper's Slayer")
                                    .enchant(Enchantment.DAMAGE_ALL, 3)
                                    .enchant(Enchantment.DURABILITY, 3)
                                    .enchant(Enchantment.FIRE_ASPECT, 2)
                                    .build()
                    );

                    List<String> randCmd = Arrays.asList(
                            "partnerpackage " + p.getName() + " 3",
                            "airdrops give " + p.getName() + " 5",
                            "monthlycrate give " + p.getName() + " Pandora 3",
                            "cr givekey " + p.getName() + " Halloween 3",
                            "cr givekey " + p.getName() + " KOTH 1",
                            "ability NinjaStar 1 " + p.getName()
                    );

                    int rand = MathUtils.getBetween(1, 10);
                    if (rand <= 5) {
                        Object toDrop = MathUtils.getRandomObject(randItem);
                        e.getEntity().getLocation().getWorld().dropItemNaturally(e.getEntity().getLocation(), (ItemStack) toDrop);
                    } else {
                        Object toRun = MathUtils.getRandomObject(randCmd);
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), (String) toRun);
                    }
                }
            } else if (e.getEntity().getCustomName().equals(ChatColor.AQUA + ChatColor.BOLD.toString() + "Dagger")) {
                Player p = e.getEntity().getKiller();
                Team team = Hulu.getInstance().getTeamHandler().getTeam(p);
                Bukkit.broadcastMessage(ChatColor.GOLD + "The Dagger has been killed by " + e.getEntity().getKiller().getDisplayName());
                if (e.getEntity().getKiller() != null) {
                    e.getDrops().clear();
                    if (team != null) {
                        team.sendMessage("§fYour faction has gained §65 points §ffor killing the boss!");
                        team.addPlaytimePoints(5);
                    }
                    p.sendMessage("§fYou have killed §b§lThe Dagger");

                    List<ItemStack> randItem = Arrays.asList(
                            new ItemBuilder(Material.DIAMOND_SWORD)
                                    .name("&b&lDagger's Slayer")
                                    .enchant(Enchantment.DAMAGE_ALL, 3)
                                    .enchant(Enchantment.DURABILITY, 3)
                                    .enchant(Enchantment.FIRE_ASPECT, 2)
                                    .build()
                    );

                    List<String> randCmd = Arrays.asList(
                            "partnerpackage " + p.getName() + " 3",
                            "airdrops give " + p.getName() + " 5",
                            "monthlycrate give " + p.getName() + " Pandora 3",
                            "cr givekey " + p.getName() + " Halloween 3",
                            "cr givekey " + p.getName() + " KOTH 1",
                            "ability NinjaStar 1 " + p.getName()
                    );

                    int rand = MathUtils.getBetween(1, 10);
                    if (rand <= 5) {
                        Object toDrop = MathUtils.getRandomObject(randItem);
                        e.getEntity().getLocation().getWorld().dropItemNaturally(e.getEntity().getLocation(), (ItemStack) toDrop);
                    } else {
                        Object toRun = MathUtils.getRandomObject(randCmd);
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), (String) toRun);
                    }
                }
            }
        }
    }
}