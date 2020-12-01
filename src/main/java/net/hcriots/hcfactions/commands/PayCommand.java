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
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.NumberFormat;
import java.util.UUID;

public class PayCommand {

    @Command(names = {"Pay", "P2P"}, permission = "")
    public static void pay(Player sender, @Param(name = "player") UUID player, @Param(name = "amount") float amount) {
        double balance = Hulu.getInstance().getEconomyHandler().getBalance(sender.getUniqueId());
        Player bukkitPlayer = Hulu.getInstance().getServer().getPlayer(player);

        if (bukkitPlayer == null || !bukkitPlayer.isOnline()) {
            sender.sendMessage(ChatColor.RED + "That player is not online.");
            return;
        }

        if (sender.equals(bukkitPlayer)) {
            sender.sendMessage(ChatColor.RED + "You cannot send money to yourself!");
            return;
        }

        if (amount < 5) {
            sender.sendMessage(ChatColor.RED + "You must send at least $5!");
            return;
        }

        if (balance > 600000) {
            sender.sendMessage("§cYour balance is too high to send money. Please contact an admin to transfer money.");
            Bukkit.getLogger().severe("[ECONOMY] " + sender.getName() + " tried to send " + amount);
            return;
        }

        if (Double.isNaN(balance)) {
            sender.sendMessage("§cYou can't send money because your balance is fucked.");
            return;
        }

        if (Float.isNaN(amount)) {
            sender.sendMessage(ChatColor.RED + "Nope.");
            return;
        }

        if (balance < amount) {
            sender.sendMessage(ChatColor.RED + "You do not have $" + amount + "!");
            return;
        }

        Hulu.getInstance().getEconomyHandler().deposit(player, amount);
        Hulu.getInstance().getEconomyHandler().withdraw(sender.getUniqueId(), amount);

        Hulu.getInstance().getWrappedBalanceMap().setBalance(player, Hulu.getInstance().getEconomyHandler().getBalance(player));
        Hulu.getInstance().getWrappedBalanceMap().setBalance(sender.getUniqueId(), Hulu.getInstance().getEconomyHandler().getBalance(sender.getUniqueId()));

        sender.sendMessage(ChatColor.YELLOW + "You sent " + ChatColor.LIGHT_PURPLE + NumberFormat.getCurrencyInstance().format(amount) + ChatColor.YELLOW + " to " + ChatColor.LIGHT_PURPLE + Stark.instance.getCore().getUuidCache().name(player) +
                ChatColor.YELLOW + ".");

        bukkitPlayer.sendMessage(ChatColor.LIGHT_PURPLE + sender.getName() + ChatColor.YELLOW + " sent you " + ChatColor.LIGHT_PURPLE + NumberFormat.getCurrencyInstance().format(amount) + ChatColor.YELLOW + ".");
    }

}