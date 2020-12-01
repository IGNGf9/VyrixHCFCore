/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.team.commands.team;

import cc.fyre.stark.engine.command.Command;
import cc.fyre.stark.engine.command.data.parameter.Param;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.team.Team;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TeamRenameCommand {

    @Command(names = {"team rename", "t rename", "f rename", "faction rename", "fac rename"}, permission = "")
    public static void teamRename(Player sender, @Param(name = "new name") String newName) {
        Team team = Hulu.getInstance().getTeamHandler().getTeam(sender);

        if (team == null) {
            sender.sendMessage(ChatColor.GRAY + "You are not on a team!");
            return;
        }

        if (Hulu.getInstance().getCitadelHandler().getCappers().contains(team.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + "Citadel cappers cannot change their name. Please contact an admin to rename your team.");
            return;
        }

        if (!team.isOwner(sender.getUniqueId()) && !team.isCoLeader(sender.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + "Only team owners and co-leaders can use this command!");
            return;
        }

        if (newName.length() > 16) {
            sender.sendMessage(ChatColor.RED + "Maximum team name size is 16 characters!");
            return;
        }

        if (newName.length() < 3) {
            sender.sendMessage(ChatColor.RED + "Minimum team name size is 3 characters!");
            return;
        }

        if (!TeamCreateCommand.ALPHA_NUMERIC.matcher(newName).find()) {
            if (Hulu.getInstance().getTeamHandler().getTeam(newName) == null) {
                team.rename(newName);
                sender.sendMessage(ChatColor.GREEN + "Team renamed to " + newName);
            } else {
                sender.sendMessage(ChatColor.RED + "A team with that name already exists!");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Team names must be alphanumeric!");
        }
    }

}