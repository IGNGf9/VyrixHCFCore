/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.commands;

import cc.fyre.stark.engine.command.Command;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.team.Team;
import net.hcriots.hcfactions.team.claims.LandBoard;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class LocationCommand {

    @Command(names = {"Location", "Loc"}, permission = "")
    public static void location(Player sender) {
        Location loc = sender.getLocation();
        Team owner = LandBoard.getInstance().getTeam(loc);

        if (owner != null) {
            sender.sendMessage(ChatColor.YELLOW + "You are in " + owner.getName(sender.getPlayer()) + ChatColor.YELLOW + "'s territory.");
            return;
        }

        if (!Hulu.getInstance().getServerHandler().isWarzone(loc)) {
            sender.sendMessage(ChatColor.YELLOW + "You are in " + ChatColor.DARK_GREEN + "Wilderness" + ChatColor.YELLOW + "!");
        } else {
            sender.sendMessage(ChatColor.YELLOW + "You are in the " + ChatColor.DARK_RED + "Warzone" + ChatColor.YELLOW + "!");
        }
    }

}