/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.team.commands;

import cc.fyre.stark.Stark;
import cc.fyre.stark.engine.command.Command;
import cc.fyre.stark.engine.command.data.parameter.Param;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.team.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ForceKickCommand {

    @Command(names = {"forcekick"}, permission = "foxtrot.forcekick")
    public static void forceKick(Player sender, @Param(name = "player") UUID player) {
        Team team = Hulu.getInstance().getTeamHandler().getTeam(player);

        if (team == null) {
            sender.sendMessage(ChatColor.RED + Stark.instance.getCore().getUuidCache().name(player) + " is not on a team!");
            return;
        }

        if (team.getMembers().size() == 1) {
            sender.sendMessage(ChatColor.RED + Stark.instance.getCore().getUuidCache().name(player) + "'s team has one member. Please use /forcedisband to perform this action.");
            return;
        }

        team.removeMember(player);
        Hulu.getInstance().getTeamHandler().setTeam(player, null);

        Player bukkitPlayer = Bukkit.getPlayer(player);
        if (bukkitPlayer != null && bukkitPlayer.isOnline()) {
            bukkitPlayer.sendMessage(ChatColor.RED + "You were kicked from your team by a staff member.");
        }

        sender.sendMessage(ChatColor.YELLOW + "Force kicked " + ChatColor.LIGHT_PURPLE + Stark.instance.getCore().getUuidCache().name(player) + ChatColor.YELLOW + " from their team, " + ChatColor.LIGHT_PURPLE + team.getName() + ChatColor.YELLOW + ".");
    }

}