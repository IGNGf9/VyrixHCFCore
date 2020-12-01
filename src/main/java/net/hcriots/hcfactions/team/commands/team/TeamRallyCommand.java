/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.team.commands.team;

import cc.fyre.stark.engine.command.Command;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.team.Team;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TeamRallyCommand {

    @Command(
            names = {"team rally", "t rally", "faction rally", "fac rally", "f rally"},
            permission = ""
    )
    public static void teamRally(Player sender) {
        Team team = Hulu.getInstance().getTeamHandler().getTeam(sender);
        if (team == null) {
            sender.sendMessage(ChatColor.GRAY + "You are not on a team!");
            return;
        }

        if (team.isRally()) {
            team.setRally(false);
            team.setRallyPlayer(null);
            team.sendMessage(ChatColor.RED + "Team rally has been disabled.");
        } else {
            team.setRally(true);
            team.setRallyPlayer(sender);
            team.sendMessage(ChatColor.GREEN + "Team rally has been enabled.");
        }
    }
}
