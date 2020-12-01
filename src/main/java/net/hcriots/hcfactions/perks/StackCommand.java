/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.perks;

import cc.fyre.stark.engine.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

/**
 * Created by InspectMC
 * Date: 7/1/2020
 * Time: 4:24 PM
 */
public class StackCommand {

    @Command(names = {"stack"}, permission = "hulu.command.stack")
    public static void stack(CommandSender sender) {
        Player player = ((Player) sender);
        PlayerInventory inventory = player.getInventory();
        ItemStack[] contents = inventory.getContents();
        int done = 0;
        for (int i = 0; i < contents.length; i++) {
            ItemStack current = contents[i];
            if (current != null) {
                for (int i2 = i + 1; i2 < contents.length; i2++) {
                    ItemStack current2 = contents[i2];
                    if (current.isSimilar(current2)) {
                        int allowed = current.getMaxStackSize() - current.getAmount();
                        if (allowed > 0) {
                            int left = current2.getAmount() - allowed;
                            if (left > 0) {
                                current2.setAmount(left);
                                current.setAmount(current.getMaxStackSize());
                            } else {
                                done++;
                                current.setAmount(current.getAmount() + current2.getAmount());
                                contents[i2] = null;
                            }
                        }
                    }
                }
            }
        }
        inventory.setContents(contents);
        player.updateInventory();
        sender.sendMessage(done == 0 ? ChatColor.RED + "You don't have any items to stack." : ChatColor.GOLD + "You've stacked " + ChatColor.GREEN + done + ChatColor.GOLD + " item" + (done != 1 ? "s" : ""));
    }
}