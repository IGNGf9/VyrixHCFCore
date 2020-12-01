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
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.UUID;

public class BalanceCommand {

    @Command(names = {"Balance", "Econ", "Bal", "$"})
    public static void balance(Player sender, @Param(name = "player", defaultValue = "self") UUID player) {
        if (sender.getUniqueId().equals(player)) {
            sender.sendMessage(ChatColor.GOLD + "Balance: $" + ChatColor.WHITE + NumberFormat.getNumberInstance(Locale.US).format(Hulu.getInstance().getEconomyHandler().getBalance(sender.getUniqueId())));
        } else {
            sender.sendMessage(ChatColor.GOLD + "Balance of " + Stark.instance.getCore().getUuidCache().name(player) + ": " + ChatColor.WHITE + NumberFormat.getNumberInstance(Locale.US).format(Hulu.getInstance().getEconomyHandler().getBalance(player)));
        }
    }


    @Command(names = {"bal give", "eco give", "balance give"}, permission = "hulu.balance.give")
    public static void balanceGive(CommandSender sender, @Param(name = "player") Player player, @Param(name = "amount") int amount) {
        if (amount < 0) {
            sender.sendMessage(ChatColor.RED + "Please enter a valid number.");
            return;
        }

        Hulu.getInstance().getEconomyHandler().setBalance(player.getUniqueId(), Hulu.getInstance().getEconomyHandler().getBalance(player.getUniqueId()) + amount);
        player.sendMessage(ChatColor.GOLD + "You have updated " + player.getDisplayName() + ChatColor.GOLD + " balance to " + ChatColor.WHITE + amount);
    }
}