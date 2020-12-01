/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.team.commands.team;

import cc.fyre.stark.Stark;
import cc.fyre.stark.engine.command.Command;
import cc.fyre.stark.engine.command.data.parameter.Param;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.team.Team;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TeamPromoteCommand {

    @Command(names = {"team promote", "t promote", "f promote", "faction promote", "fac promote", "team captain"}, permission = "")
    public static void teamPromote(Player sender, @Param(name = "player") UUID player) {
        Team team = Hulu.getInstance().getTeamHandler().getTeam(sender);

        if (team == null) {
            sender.sendMessage(ChatColor.GRAY + "You are not on a team!");
            return;
        }

        if (!team.isOwner(sender.getUniqueId()) && !team.isCoLeader(sender.getUniqueId())) {
            sender.sendMessage(ChatColor.DARK_AQUA + "Only team co-leaders (and above) can do this.");
            return;
        }

        if (!team.isMember(player)) {
            sender.sendMessage(ChatColor.DARK_AQUA + Stark.instance.getCore().getUuidCache().name(player) + " is not on your team.");
            return;
        }

        if (team.isOwner(player)) {
            sender.sendMessage(ChatColor.RED + Stark.instance.getCore().getUuidCache().name(player) + " is already a leader.");
        } else if (team.isCoLeader(player)) {
            if (team.isOwner(sender.getUniqueId())) {
                sender.sendMessage(ChatColor.RED + Stark.instance.getCore().getUuidCache().name(player) + " is already a co-leader! To make them a leader, use /t leader");
            } else {
                sender.sendMessage(ChatColor.RED + "Only the team leader can promote new leaders.");
            }
        } else if (team.isCaptain(player)) {
            if (team.isOwner(sender.getUniqueId())) {
                team.sendMessage(ChatColor.DARK_AQUA + Stark.instance.getCore().getUuidCache().name(player) + " has been promoted to Co-Leader!");
                team.addCoLeader(player);
                team.removeCaptain(player);
            } else {
                sender.sendMessage(ChatColor.RED + "Only the team leader can promote new Co-Leaders.");
            }
        } else {
            team.sendMessage(ChatColor.DARK_AQUA + Stark.instance.getCore().getUuidCache().name(player) + " has been promoted to Captain!");
            team.addCaptain(player);
        }
    }

}