/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.commands;

import cc.fyre.stark.Stark;
import cc.fyre.stark.engine.command.Command;
import cc.fyre.stark.engine.command.data.parameter.Param;
import net.hcriots.hcfactions.Hulu;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.UUID;

public class LastInvCommand {

    @Command(names = {"lastinv"}, permission = "foxtrot.lastinv")
    public static void lastInv(Player sender, @Param(name = "player") UUID player) {
        Hulu.getInstance().getServer().getScheduler().runTaskAsynchronously(Hulu.getInstance(), () -> {
            Stark.instance.getCore().getRedis().runRedisCommand((redis) -> {
                if (!redis.exists("lastInv:contents:" + player.toString())) {
                    sender.sendMessage(ChatColor.RED + "No last inventory recorded for " + Stark.instance.getCore().getUuidCache().name(player));
                    return null;
                }

                ItemStack[] contents = Stark.getPlainGson().fromJson(redis.get("lastInv:contents:" + player.toString()), ItemStack[].class);
                ItemStack[] armor = Stark.getPlainGson().fromJson(redis.get("lastInv:armorContents:" + player.toString()), ItemStack[].class);

                cleanLoot(contents);
                cleanLoot(armor);

                Hulu.getInstance().getServer().getScheduler().runTask(Hulu.getInstance(), () -> {
                    sender.getInventory().setContents(contents);
                    sender.getInventory().setArmorContents(armor);
                    sender.updateInventory();

                    sender.sendMessage(ChatColor.GREEN + "Loaded " + Stark.instance.getCore().getUuidCache().name(player) + "'s last inventory.");
                });

                return null;
            });
        });
    }

    public static void cleanLoot(ItemStack[] stack) {
        for (ItemStack item : stack) {
            if (item != null && item.hasItemMeta() && item.getItemMeta().hasLore()) {
                ItemMeta meta = item.getItemMeta();

                List<String> lore = item.getItemMeta().getLore();
                lore.remove(ChatColor.DARK_GRAY + "PVP Loot");
                meta.setLore(lore);

                item.setItemMeta(meta);
            }
        }
    }

    public static void recordInventory(Player player) {
        recordInventory(player.getUniqueId(), player.getInventory().getContents(), player.getInventory().getArmorContents());
    }

    public static void recordInventory(UUID player, ItemStack[] contents, ItemStack[] armor) {
        Hulu.getInstance().getServer().getScheduler().runTaskAsynchronously(Hulu.getInstance(), () -> {
            Stark.instance.getCore().getRedis().runRedisCommand((redis) -> {
                redis.set("lastInv:contents:" + player.toString(), Stark.getPlainGson().toJson(contents));
                redis.set("lastInv:armorContents:" + player.toString(), Stark.getPlainGson().toJson(armor));
                return null;
            });
        });
    }

}