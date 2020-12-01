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

public class TeamDemoteCommand {

    @Command(names = {"team demote", "t demote", "f demote", "faction demote", "fac demote"}, permission = "")
    public static void teamDemote(Player sender, @Param(name = "player") UUID player) {
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
            sender.sendMessage(ChatColor.RED + Stark.instance.getCore().getUuidCache().name(player) + " is the leader. To change leaders, the team leader must use /t leader <name>");
        } else if (team.isCoLeader(player)) {
            if (team.isOwner(sender.getUniqueId())) {
                team.removeCoLeader(player);
                team.addCaptain(player);
                team.sendMessage(ChatColor.DARK_AQUA + Stark.instance.getCore().getUuidCache().name(player) + " has been demoted to Captain!");
            } else {
                sender.sendMessage(ChatColor.RED + "Only the team leader can demote Co-Leaders.");
            }
        } else if (team.isCaptain(player)) {
            team.removeCaptain(player);
            team.sendMessage(ChatColor.DARK_AQUA + Stark.instance.getCore().getUuidCache().name(player) + " has been demoted to a member!");
        } else {
            sender.sendMessage(ChatColor.RED + Stark.instance.getCore().getUuidCache().name(player) + " is currently a member. To kick them, use /t kick");
        }
    }

}