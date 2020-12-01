/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.team.commands;

import cc.fyre.stark.engine.command.Command;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.team.Team;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class ForceDisbandAllCommand {

    private static Runnable confirmRunnable;

    @Command(names = {"forcedisbandall"}, permission = "op")
    public static void forceDisbandAll(CommandSender sender) {
        confirmRunnable = () -> {
            List<Team> teams = new ArrayList<>();

            for (Team team : Hulu.getInstance().getTeamHandler().getTeams()) {
                teams.add(team);
            }

            for (Team team : teams) {
                team.disband();
            }

            Hulu.getInstance().getServer().broadcastMessage(ChatColor.RED.toString() + ChatColor.BOLD + "All teams have been forcibly disbanded!");
        };

        sender.sendMessage(ChatColor.RED + "Are you sure you want to disband all factions? Type " + ChatColor.DARK_RED + "/forcedisbandall confirm" + ChatColor.RED + " to confirm or " + ChatColor.GREEN + "/forcedisbandall cancel" + ChatColor.RED + " to cancel.");
    }

    @Command(names = {"forcedisbandall confirm"}, permission = "op")
    public static void confirm(CommandSender sender) {
        if (confirmRunnable == null) {
            sender.sendMessage(ChatColor.RED + "Nothing to confirm.");
            return;
        }

        sender.sendMessage(ChatColor.GREEN + "If you're sure...");
        confirmRunnable.run();
    }

    @Command(names = {"forcedisbandall cancel"}, permission = "op")
    public static void cancel(CommandSender sender) {
        if (confirmRunnable == null) {
            sender.sendMessage(ChatColor.RED + "Nothing to cancel.");
            return;
        }

        sender.sendMessage(ChatColor.GREEN + "Cancelled.");
        confirmRunnable = null;
    }

}