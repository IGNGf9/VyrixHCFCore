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
import org.bukkit.entity.Player;

public class ForceLeaveCommand {

    @Command(names = {"forceleave"}, permission = "foxtrot.forceleave")
    public static void forceLeave(Player player) {
        Team team = Hulu.getInstance().getTeamHandler().getTeam(player);

        if (team == null) {
            player.sendMessage(ChatColor.RED + "You are not on a team!");
            return;
        }

        team.removeMember(player.getUniqueId());
        Hulu.getInstance().getTeamHandler().setTeam(player.getUniqueId(), null);
        player.sendMessage(ChatColor.YELLOW + "Force left your team.");
    }

}