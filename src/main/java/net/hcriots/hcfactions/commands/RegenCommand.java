/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.commands;

import cc.fyre.stark.core.util.TimeUtils;
import cc.fyre.stark.engine.command.Command;
import cc.fyre.stark.engine.command.data.parameter.Param;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.team.Team;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class RegenCommand {

    @Command(names = {"Regen", "DTR"}, permission = "")
    public static void regen(Player sender, @Param(name = "team", defaultValue = "self") Team team) {
        if (!sender.isOp()) {
            team = Hulu.getInstance().getTeamHandler().getTeam(sender);
        }

        if (team == null) {
            sender.sendMessage(ChatColor.GRAY + "You are not on a team!");
            return;
        }

        if (team.getMaxDTR() == team.getDTR()) {
            sender.sendMessage(ChatColor.YELLOW + "Your team is currently at max DTR, which is " + ChatColor.LIGHT_PURPLE + team.getMaxDTR() + ChatColor.YELLOW + ".");
            return;
        }

        sender.sendMessage(ChatColor.YELLOW + "Your team has a max DTR of " + ChatColor.LIGHT_PURPLE + team.getMaxDTR() + ChatColor.YELLOW + ".");
        sender.sendMessage(ChatColor.YELLOW + "You are regaining DTR at a rate of " + ChatColor.LIGHT_PURPLE + Team.DTR_FORMAT.format(team.getDTRIncrement() * 60) + "/hour" + ChatColor.YELLOW + ".");
        sender.sendMessage(ChatColor.YELLOW + "At this rate, it will take you " + ChatColor.LIGHT_PURPLE + (hrsToRegain(team) == -1 ? "Infinity" : hrsToRegain(team)) + ChatColor.YELLOW + " hours to fully gain all DTR.");

        if (team.getDTRCooldown() > System.currentTimeMillis()) {
            sender.sendMessage(ChatColor.YELLOW + "Your team is on DTR cooldown for " + ChatColor.LIGHT_PURPLE + TimeUtils.formatIntoDetailedString((int) (team.getDTRCooldown() - System.currentTimeMillis()) / 1000) + ChatColor.YELLOW + ".");
        }
    }

    private static double hrsToRegain(Team team) {
        double diff = team.getMaxDTR() - team.getDTR();
        double dtrIncrement = team.getDTRIncrement();

        if (dtrIncrement == 0D) {
            return (-1);
        }

        double required = diff / dtrIncrement;
        double h = required / 60D;

        return (Math.round(10.0 * h) / 10.0);
    }

}