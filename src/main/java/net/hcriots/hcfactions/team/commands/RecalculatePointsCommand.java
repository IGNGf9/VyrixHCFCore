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

public class RecalculatePointsCommand {

    @Command(names = {"team recalculatepoints", "f recalculatepoints", "team recalcpoints"}, permission = "op")
    public static void recalculate(CommandSender sender) {
        int changed = 0;

        for (Team team : Hulu.getInstance().getTeamHandler().getTeams()) {
            int oldPoints = team.getPoints();
            team.recalculatePoints();
            if (team.getPoints() != oldPoints) {
                team.flagForSave();
                sender.sendMessage(ChatColor.YELLOW + "Changed " + team.getName() + "'s points from " + oldPoints + " to " + team.getPoints());
                changed++;
            }
        }
        sender.sendMessage(ChatColor.YELLOW + "Changed a total of " + changed + " teams points.");
    }
}
