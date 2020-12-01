/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.perks;

import cc.fyre.stark.engine.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

/**
 * Created by InspectMC
 * Date: 7/1/2020
 * Time: 4:22 PM
 */
public class HatCommand {

    @Command(names = {"hat", "head"}, permission = "hulu.hat")
    public static void hat(CommandSender sender) {
        Player player = (Player) sender;
        ItemStack stack = player.getItemInHand();
        if (stack == null || stack.getType() == Material.AIR) {
            sender.sendMessage(ChatColor.RED + "You are not holding anything.");
            return;
        }
        if (stack.getType().getMaxDurability() != 0) {
            sender.sendMessage(ChatColor.RED + "The item you are holding is not suitable to wear for a hat.");
            return;
        }

        PlayerInventory inventory = player.getInventory();
        ItemStack helmet = inventory.getHelmet();

        if (helmet != null && helmet.getType() != Material.AIR) {
            sender.sendMessage(ChatColor.RED + "You are already wearing something as your hat.");
            return;
        }

        int amount = stack.getAmount();
        if (amount > 1) {
            --amount;
            stack.setAmount(amount);
        } else {
            player.setItemInHand(new ItemStack(Material.AIR, 1));
        }
        helmet = stack.clone();
        helmet.setAmount(1);
        inventory.setHelmet(helmet);
    }
}