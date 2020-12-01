/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.team.commands;

import cc.fyre.stark.engine.command.Command;
import cc.fyre.stark.engine.command.data.parameter.Param;
import net.hcriots.hcfactions.team.Team;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ForceDisbandCommand {

    @Command(names = {"forcedisband"}, permission = "op")
    public static void forceDisband(Player sender, @Param(name = "team") Team team) {
        team.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + sender.getName() + " has force disbanded the team.");
        team.disband();
        sender.sendMessage(ChatColor.YELLOW + "Force disbanded the team " + ChatColor.LIGHT_PURPLE + team.getName() + ChatColor.YELLOW + ".");
    }

}