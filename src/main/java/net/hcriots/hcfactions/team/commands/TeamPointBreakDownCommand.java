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

public class TeamPointBreakDownCommand {

    @Command(names = {"team pointsinfo"}, permission = "op")
    public static void teamPointBreakDown(Player player, @Param(name = "team", defaultValue = "self") final Team team) {
        player.sendMessage(ChatColor.GOLD.toString() + ChatColor.BOLD + "Point Information of " + team.getName());

        for (String info : team.getPointBreakDown()) {
            player.sendMessage(info);
        }
    }

}
