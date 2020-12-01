/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.team.commands.team;

import cc.fyre.stark.engine.command.Command;
import cc.fyre.stark.engine.command.data.parameter.Param;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.team.Team;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Created by InspectMC
 * Date: 8/1/2020
 * Time: 3:50 PM
 */
public class TeamPointsCommand {


    @Command(names = {"team points", "t points", "f points", "faction points", "fac points"}, permission = "")
    public static void tpoints(Player player) {
        Team team = Hulu.getInstance().getTeamHandler().getTeam(player);

        if (team == null) {
            player.sendMessage(ChatColor.GRAY + "You are not on a team!");
            return;
        }

        player.sendMessage(ChatColor.YELLOW + "Your team has " + ChatColor.BLUE + team.getPoints() + ChatColor.YELLOW + " points");
    }

    @Command(names = {"team addpoints", "t addpoints", "f addpoints", "faction addpoints", "fac addpoints"}, permission = "hulu.points")
    public static void tPointsAdd(Player player, @Param(name = "team") Team team, @Param(name = "points") int points) {
        if (team == null) {
            player.sendMessage(ChatColor.GRAY + "Team not found!");
            return;
        }

        if (points <= 0) {
            player.sendMessage(net.md_5.bungee.api.ChatColor.RED + "Please enter a number greater than 0");
            return;
        }

        int previousPoints = team.getPoints();
        points = previousPoints + points;
        team.setPoints(points);
        player.sendMessage(ChatColor.YELLOW + "You haved updated " + ChatColor.BLUE + team.getName() + ChatColor.YELLOW + " points to " + ChatColor.LIGHT_PURPLE + points);
    }
}
