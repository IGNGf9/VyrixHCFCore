/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.credit.commands;

import cc.fyre.stark.core.util.TimeUtils;
import cc.fyre.stark.engine.command.Command;
import cc.fyre.stark.engine.command.data.parameter.Param;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.credit.CreditHandler;
import net.hcriots.hcfactions.credit.menu.CreditMenu;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by InspectMC
 * Date: 6/28/2020
 * Time: 7:46 PM
 */
public class CreditsCommand {

    @Command(names = {"charm", "charms"}, permission = "")
    public static void credits(Player sender) {
        int nextCreditsSeconds = (int) ((CreditHandler.getPendingCredits().get(sender.getUniqueId()) - System.currentTimeMillis()) / 1_000L);
//        sender.sendMessage(ChatColor.WHITE + "You have " + ChatColor.GOLD + Hulu.getInstance().getCreditsMap().getCredits(sender.getUniqueId()) + ChatColor.WHITE + " charms..");
//        sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.ITALIC + "You will receive another playtime charm in " + ChatColor.GOLD +
//                TimeUtils.formatIntoDetailedString(nextCreditsSeconds) + ChatColor.YELLOW + ".");
    }

    @Command(names = {"charm check", "charms check"}, permission = "")
    public static void creditsCheck(CommandSender sender, @Param(name = "player") Player player) {
        sender.sendMessage(ChatColor.WHITE + player.getName() + " " + ChatColor.GOLD + "has " + ChatColor.WHITE + Hulu.getInstance().getCreditsMap().getCredits(player.getUniqueId()) + ChatColor.GOLD + " charms.");
    }

    @Command(names = {"charm check", "charms check"}, permission = "")
    public static void creditsCheckOffline(CommandSender sender, @Param(name = "player") OfflinePlayer player) {
        sender.sendMessage(ChatColor.WHITE + player.getName() + " " + ChatColor.GOLD + "has " + ChatColor.WHITE + Hulu.getInstance().getCreditsMap().getCredits(player.getUniqueId()) + ChatColor.GOLD + " charms.");
    }

    @Command(names = {"charms pay", "charm pay"})
    public static void creditsPay(CommandSender sender, @Param(name = "player") Player player, @Param(name = "amount") int amount) {
        if (amount < 0) {
            sender.sendMessage(ChatColor.RED + "Please enter a valid number.");
            return;
        }
        if (amount > CreditHandler.getPlayerCredits(sender.getServer().getPlayer(sender.getName()))) {
            sender.sendMessage(ChatColor.RED + "You do not have enough charms.");
            return;
        }
        Player payer = sender.getServer().getPlayer(sender.getName());

        Hulu.getInstance().getCreditsMap().setCredits(player.getUniqueId(), CreditHandler.getPlayerCredits(player) + amount);
        Hulu.getInstance().getCreditsMap().setCredits(payer.getUniqueId(), CreditHandler.getPlayerCredits(payer) - amount);
        player.sendMessage(ChatColor.GOLD + "You have been given " + ChatColor.WHITE + amount + " charms" + ChatColor.GOLD + " from " + sender.getName());

        sender.sendMessage(ChatColor.GOLD + "You have given " + ChatColor.WHITE + amount + " charms" + ChatColor.GOLD + " to " + player.getName());
    }

    @Command(names = {"charms set"}, permission = "hulu.charms")
    public static void creditsSet(CommandSender sender, @Param(name = "player") Player player, @Param(name = "amount") int amount) {
        if (amount < 0) {
            sender.sendMessage(ChatColor.RED + "Please enter a valid number.");
            return;
        }

        Hulu.getInstance().getCreditsMap().setCredits(player.getUniqueId(), amount);
        sender.sendMessage(ChatColor.YELLOW + "You have updated " + player.getDisplayName() + ChatColor.YELLOW + " " + amount + " charms!");
    }

    @Command(names = {"charms set"}, permission = "hulu.charms")
    public static void creditsSetOffline(CommandSender sender, @Param(name = "player") OfflinePlayer player, @Param(name = "amount") int amount) {
        if (amount < 0) {
            sender.sendMessage(ChatColor.RED + "Please enter a valid number.");
            return;
        }
        Hulu.getInstance().getCreditsMap().setCredits(player.getUniqueId(), amount);
        sender.sendMessage(ChatColor.YELLOW + "You have updated " + player.getName() + ChatColor.YELLOW + " " + amount + " charms!");
    }

    @Command(names = {"charms give"}, permission = "hulu.charms")
    public static void creditsGive(CommandSender sender, @Param(name = "player") Player player, @Param(name = "amount") int amount) {
        if (amount < 0) {
            sender.sendMessage(ChatColor.RED + "Please enter a valid number.");
            return;
        }

        Hulu.getInstance().getCreditsMap().setCredits(player.getUniqueId(), Hulu.getInstance().getCreditsMap().getCredits(player.getUniqueId()) + amount);
        player.sendMessage(ChatColor.GOLD + "You have been given " + ChatColor.WHITE + amount + " charms" + ChatColor.GOLD + ".");
    }


    @Command(names = {"charms shop", "charm shop"}, permission = "")
    public static void creditsShop(Player sender) {
        new CreditMenu().openMenu(sender);
    }
}