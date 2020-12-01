/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.team.commands.team;

import cc.fyre.stark.engine.command.Command;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.team.Team;
import net.hcriots.hcfactions.team.claims.LandBoard;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@SuppressWarnings("deprecation")
public class TeamSetHQCommand {

    @Command(names = {"team sethq", "t sethq", "f sethq", "faction sethq", "fac sethq", "team sethome", "t sethome", "f sethome", "faction sethome", "fac sethome", "sethome", "sethq"}, permission = "")
    public static void teamSetHQ(Player sender) {
        Team team = Hulu.getInstance().getTeamHandler().getTeam(sender);

        if (team == null) {
            sender.sendMessage(ChatColor.GRAY + "You are not on a team!");
            return;
        }

        if (team.isOwner(sender.getUniqueId()) || team.isCoLeader(sender.getUniqueId()) || team.isCaptain(sender.getUniqueId())) {
            if (LandBoard.getInstance().getTeam(sender.getLocation()) != team) {
                if (!sender.isOp()) {
                    sender.sendMessage(ChatColor.RED + "You can only set HQ in your team's territory.");
                    return;
                } else {
                    sender.sendMessage(ChatColor.RED.toString() + ChatColor.ITALIC + "Setting HQ outside of your team's territory would normally be disallowed, but this check is being bypassed due to your rank.");
                }
            }

            /*
             * Removed at Jon's request.
             * https://github.com/FrozenOrb/HCTeams/issues/59
             */

//            if (sender.getLocation().getBlockY() > 100) {
//                if (!sender.isOp()) {
//                    sender.sendMessage(ChatColor.RED + "You can't set your HQ above  Y 100.");
//                    return;
//                } else {
//                    sender.sendMessage(ChatColor.RED.toString() + ChatColor.ITALIC + "Claiming above Y 100 would normally be disallowed, but this check is being bypassed due to your rank.");
//                }
//            }


            team.setHQ(sender.getLocation());
            team.sendMessage(ChatColor.DARK_AQUA + sender.getName() + " has updated the team's HQ point!");
        } else {
            sender.sendMessage(ChatColor.DARK_AQUA + "Only team captains can do this.");
        }
    }

}