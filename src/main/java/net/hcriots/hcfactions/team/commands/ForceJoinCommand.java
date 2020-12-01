/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.team.commands;

import cc.fyre.stark.engine.command.Command;
import cc.fyre.stark.engine.command.data.parameter.Param;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.team.Team;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ForceJoinCommand {

    @Command(names = {"ForceJoin"}, permission = "foxtrot.forcejoin")
    public static void forceJoin(Player sender, @Param(name = "team") Team team, @Param(name = "player", defaultValue = "self") Player player) {
        if (Hulu.getInstance().getTeamHandler().getTeam(player) != null) {
            if (player == sender) {
                sender.sendMessage(ChatColor.RED + "Leave your current team before attempting to forcejoin.");
            } else {
                sender.sendMessage(ChatColor.RED + "That player needs to leave their current team first!");
            }

            return;
        }

        team.addMember(player.getUniqueId());
        Hulu.getInstance().getTeamHandler().setTeam(player.getUniqueId(), team);
        player.sendMessage(ChatColor.YELLOW + "You are now a member of " + ChatColor.LIGHT_PURPLE + team.getName() + ChatColor.YELLOW + "!");

        if (player != sender) {
            sender.sendMessage(ChatColor.LIGHT_PURPLE + player.getName() + ChatColor.YELLOW + " added to " + ChatColor.LIGHT_PURPLE + team.getName() + ChatColor.YELLOW + "!");
        }
    }

}