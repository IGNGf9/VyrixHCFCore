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

public class ForceLeaderCommand {

    @Command(names = {"ForceLeader"}, permission = "foxtrot.forceleader")
    public static void forceLeader(Player sender, @Param(name = "player", defaultValue = "self") UUID player) {
        Team playerTeam = Hulu.getInstance().getTeamHandler().getTeam(player);

        if (playerTeam == null) {
            sender.sendMessage(ChatColor.GRAY + "That player is not on a team.");
            return;
        }

        Player bukkitPlayer = Bukkit.getPlayer(player);

        if (bukkitPlayer != null && bukkitPlayer.isOnline()) {
            bukkitPlayer.sendMessage(ChatColor.YELLOW + "A staff member has made you leader of §b" + playerTeam.getName() + "§e.");
        }

        playerTeam.setOwner(player);
        sender.sendMessage(ChatColor.LIGHT_PURPLE + Stark.instance.getCore().getUuidCache().name(player) + ChatColor.YELLOW + " is now the owner of " + ChatColor.LIGHT_PURPLE + playerTeam.getName() + ChatColor.YELLOW + ".");
    }

}