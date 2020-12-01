/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.commands;

import cc.fyre.stark.engine.command.Command;
import cc.fyre.stark.engine.command.data.parameter.Param;
import net.hcriots.hcfactions.util.InventoryUtils;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CrowbarCommand {

    @Command(names = {"crowbar"}, permission = "op")
    public static void crowbar(Player sender) {
        if (sender.getGameMode() != GameMode.CREATIVE) {
            sender.sendMessage(ChatColor.RED + "This command must be ran in creative.");
            return;
        }

        sender.setItemInHand(InventoryUtils.CROWBAR);
        sender.sendMessage(ChatColor.YELLOW + "Gave you a crowbar.");
    }

    @Command(names = {"crowbar"}, permission = "op")
    public static void crowbar(CommandSender sender, @Param(name = "player") Player target) {
        target.getInventory().addItem(InventoryUtils.CROWBAR);
        target.sendMessage(ChatColor.YELLOW + "You received a crowbar from " + sender.getName() + ".");
        sender.sendMessage(ChatColor.YELLOW + "You gave a crowbar to " + target.getName() + ".");
    }

}