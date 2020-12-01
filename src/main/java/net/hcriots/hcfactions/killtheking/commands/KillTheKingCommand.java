/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.killtheking.commands;

import cc.fyre.stark.engine.command.Command;
import cc.fyre.stark.engine.command.data.parameter.Param;
import cc.fyre.stark.util.ItemBuilder;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.killtheking.KillTheKing;
import net.hcriots.hcfactions.killtheking.menu.HostMenu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by InspectMC
 * Date: 6/23/2020
 * Time: 4:53 AM
 */
public class KillTheKingCommand {

    @Command(names = {"host"}, permission = "hulu.command.host")
    public static void host(Player player) {
        if (Hulu.getInstance().getMapHandler().isKitMap()) {
            new HostMenu().openMenu(player);
        } else {
            player.sendMessage(ChatColor.RED + "You can't use that command on this server.");
        }
    }

    @Command(names = {"killtheking adminstop", "ktk adminstop"}, permission = "event.killtheking")
    public static void killthekingstop(CommandSender sender, @Param(name = "player", defaultValue = "self") Player target) {
        target.getInventory().clear();
        target.removePotionEffect(PotionEffectType.SPEED);
        target.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
        target.getInventory().setArmorContents(null);
        target.sendMessage(ChatColor.RED + "KillTheKing has ended.");
        Hulu.getInstance().setKillTheKing(null);
    }

    @Command(names = {"killtheking adminstart", "ktk adminstart"}, permission = "event.killtheking")
    public static void killthekingset(CommandSender sender, @Param(name = "player") Player target) {
        target.getInventory().clear();
        target.getInventory().setArmorContents(null);
        Hulu.getInstance().setKillTheKing(new KillTheKing(target, true));

        target.getInventory().setBoots(new ItemBuilder(Material.DIAMOND_BOOTS).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 10).build());
        target.getInventory().setLeggings(new ItemBuilder(Material.DIAMOND_LEGGINGS).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 10).build());
        target.getInventory().setChestplate(new ItemBuilder(Material.DIAMOND_CHESTPLATE).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 10).build());
        target.getInventory().setHelmet(new ItemBuilder(Material.DIAMOND_HELMET).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 10).build());


        target.getInventory().setItem(0, new ItemBuilder(Material.DIAMOND_SWORD).enchant(Enchantment.DAMAGE_ALL, 10).build());
        target.getInventory().setItem(1, new ItemBuilder(Material.ENDER_PEARL, 64).build());
        target.getInventory().setItem(8, new ItemBuilder(Material.GOLDEN_APPLE, 64).build());
        for (int i = 2; i < 5; i++) {
            ItemStack itemStack = target.getInventory().getItem(i);
            if (itemStack != null) {
                continue;
            }
            ItemStack HP = new ItemStack(Material.POTION, 64, (short) 16421);
            target.getInventory().setItem(i, HP);

        }

        target.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));
        target.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 5));
        String[] messages;

        messages = new String[]{
                ChatColor.RED + "███████",
                ChatColor.RED + "█" + ChatColor.GOLD + "█████" + ChatColor.RED + "█" + " " + ChatColor.GOLD + "[Event]",
                ChatColor.RED + "█" + ChatColor.GOLD + "█" + ChatColor.RED + "█████" + " " + ChatColor.YELLOW + "KillTheKing",
                ChatColor.RED + "█" + ChatColor.GOLD + "████" + ChatColor.RED + "██" + " " + ChatColor.GREEN + "King: " + ChatColor.GRAY + Hulu.getInstance().getKillTheKing().getActiveKing().getName(),
                ChatColor.RED + "█" + ChatColor.GOLD + "█" + ChatColor.RED + "█████",
                ChatColor.RED + "█" + ChatColor.GOLD + "█████" + ChatColor.RED + "█",
                ChatColor.RED + "███████"
        };

        final String[] messagesFinal = messages;

        new BukkitRunnable() {

            public void run() {
                for (Player player : Hulu.getInstance().getServer().getOnlinePlayers()) {
                    player.sendMessage(messagesFinal);
                }
            }

        }.runTaskAsynchronously(Hulu.getInstance());
    }
}