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

@SuppressWarnings("deprecation")
public class TeamLeaderCommand {

    @Command(names = {"team newleader", "t newleader", "f newleader", "faction newleader", "fac newleader", "team leader", "t leader", "f leader", "faction leader", "fac leader"}, permission = "")
    public static void teamLeader(Player sender, @Param(name = "player") UUID player) {
        Team team = Hulu.getInstance().getTeamHandler().getTeam(sender);

        if (team == null) {
            sender.sendMessage(ChatColor.GRAY + "You are not on a team!");
            return;
        }

        if (!team.isOwner(sender.getUniqueId())) {
            sender.sendMessage(ChatColor.DARK_AQUA + "Only the team leader can do this.");
            return;
        }

        if (!team.isMember(player)) {
            sender.sendMessage(ChatColor.RED + Stark.instance.getCore().getUuidCache().name(player) + " is not on your team.");
            return;
        }

        team.sendMessage(ChatColor.DARK_AQUA + Stark.instance.getCore().getUuidCache().name(player) + " has been given ownership of " + team.getName() + ".");
        team.setOwner(player);
        team.addCaptain(sender.getUniqueId());
    }

}