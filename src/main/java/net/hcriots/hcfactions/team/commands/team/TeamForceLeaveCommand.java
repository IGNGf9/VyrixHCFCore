/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.team.commands.team;

import cc.fyre.stark.Stark;
import cc.fyre.stark.engine.command.Command;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.server.SpawnTagHandler;
import net.hcriots.hcfactions.team.Team;
import net.hcriots.hcfactions.team.claims.LandBoard;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TeamForceLeaveCommand {

    @Command(names = {"team forceleave", "t forceleave", "f forceleave", "faction forceleave", "fac forceleave", "t fl", "team fl"}, permission = "")
    public static void forceLeave(Player sender) {
        Team team = Hulu.getInstance().getTeamHandler().getTeam(sender);

        if (team == null) {
            sender.sendMessage(ChatColor.GRAY + "You are not on a team!");
            return;
        }

        if (team.isOwner(sender.getUniqueId()) && team.getSize() > 1) {
            sender.sendMessage(ChatColor.RED + "Please choose a new leader before leaving your team!");
            return;
        }

        if (LandBoard.getInstance().getTeam(sender.getLocation()) == team) {
            sender.sendMessage(ChatColor.RED + "You cannot leave your team while on team territory.");
            return;
        }

        if (team.removeMember(sender.getUniqueId())) {
            team.disband();
            Hulu.getInstance().getTeamHandler().setTeam(sender.getUniqueId(), null);
            sender.sendMessage(ChatColor.DARK_AQUA + "Successfully left and disbanded team!");
        } else {
            Hulu.getInstance().getTeamHandler().setTeam(sender.getUniqueId(), null);
            team.flagForSave();

            if (SpawnTagHandler.isTagged(sender)) {
                team.setDTR(team.getDTR() - 1);
                team.sendMessage(ChatColor.RED + sender.getName() + " forcibly left the team. Your team has lost 1 DTR.");

                sender.sendMessage(ChatColor.RED + "You have forcibly left your team. Your team lost 1 DTR.");
            } else {
                team.sendMessage(ChatColor.YELLOW + sender.getName() + " has left the team.");

                sender.sendMessage(ChatColor.DARK_AQUA + "Successfully left the team!");
            }
        }

        Stark.instance.getNametagEngine().reloadPlayer(sender);
        Stark.instance.getNametagEngine().reloadOthersFor(sender);
    }
}
