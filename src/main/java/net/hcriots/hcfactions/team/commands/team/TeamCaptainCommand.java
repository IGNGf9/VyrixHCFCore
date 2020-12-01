/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.team.commands.team;

import cc.fyre.stark.Stark;
import cc.fyre.stark.engine.command.Command;
import cc.fyre.stark.engine.command.data.parameter.Param;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.team.Team;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TeamCaptainCommand {

    @Command(names = {"team captain add", "t captain add", "t mod add", "team mod add", "f mod add", "fac mod add", "faction mod add", "f captain add", "fac captain add", "faction captain add"}, permission = "")
    public static void captainAdd(Player sender, @Param(name = "player") UUID promote) {
        Team team = Hulu.getInstance().getTeamHandler().getTeam(sender.getUniqueId());
        if (team == null) {
            sender.sendMessage(ChatColor.RED + "You must be in a team to execute this command.");
            return;
        }

        if (!team.isOwner(sender.getUniqueId()) && !team.isCoLeader(sender.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + "Only team co-leaders can execute this command.");
            return;
        }

        if (!team.isMember(promote)) {
            sender.sendMessage(ChatColor.RED + "This player must be a member of your team.");
            return;
        }

        if (team.isOwner(promote) || team.isCaptain(promote) || team.isCoLeader(promote)) {
            sender.sendMessage(ChatColor.RED + "This player is already a captain (or above) of your team.");
            return;
        }

        team.removeCoLeader(promote);
        team.addCaptain(promote);
        team.sendMessage(org.bukkit.ChatColor.DARK_AQUA + Stark.instance.getCore().getUuidCache().name(promote) + " has been promoted to Captain!");
    }

    @Command(names = {"team captain remove", "t captain remove", "t mod remove", "team mod remove", "f mod remove", "fac mod remove", "faction mod remove", "f captain remove", "fac captain remove", "faction captain remove"}, permission = "")
    public static void captainRemove(Player sender, @Param(name = "player") UUID demote) {
        Team team = Hulu.getInstance().getTeamHandler().getTeam(sender.getUniqueId());
        if (team == null) {
            sender.sendMessage(ChatColor.RED + "You must be in a team to execute this command.");
            return;
        }

        if (!team.isOwner(sender.getUniqueId()) && !team.isCoLeader(sender.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + "Only team co-leaders can execute this command.");
            return;
        }

        if (!team.isMember(demote)) {
            sender.sendMessage(ChatColor.RED + "This player must be a member of your team.");
            return;
        }

        if (!team.isCaptain(demote)) {
            sender.sendMessage(ChatColor.RED + "This player is not a captain of your team.");
            return;
        }

        team.removeCoLeader(demote);
        team.removeCaptain(demote);
        team.sendMessage(org.bukkit.ChatColor.DARK_AQUA + Stark.instance.getCore().getUuidCache().name(demote) + " has been demoted to a member!");
    }

}
