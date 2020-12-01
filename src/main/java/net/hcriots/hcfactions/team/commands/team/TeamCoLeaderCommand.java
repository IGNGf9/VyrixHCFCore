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

public class TeamCoLeaderCommand {

    @Command(names = {"team coleader add", "t coleader add", "t co-leader add", "team co-leader add", "f co-leader add", "fac co-leader add", "faction co-leader add", "f coleader add", "fac coleader add", "faction coleader add"}, permission = "")
    public static void coleaderAdd(Player sender, @Param(name = "player") UUID promote) {
        Team team = Hulu.getInstance().getTeamHandler().getTeam(sender.getUniqueId());
        if (team == null) {
            sender.sendMessage(ChatColor.RED + "You must be in a team to execute this command.");
            return;
        }

        if (!team.isOwner(sender.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + "Only the team owner can execute this command.");
            return;
        }

        if (!team.isMember(promote)) {
            sender.sendMessage(ChatColor.RED + "This player must be a member of your team.");
            return;
        }

        if (team.isOwner(promote) || team.isCoLeader(promote)) {
            sender.sendMessage(ChatColor.RED + "This player is already a co-leader (or above) of your team.");
            return;
        }

        team.addCoLeader(promote);
        team.removeCaptain(promote);
        team.sendMessage(org.bukkit.ChatColor.DARK_AQUA + Stark.instance.getCore().getUuidCache().name(promote) + " has been promoted to Co-Leader!");
    }

    @Command(names = {"team coleader remove", "t coleader remove", "t co-leader remove", "team co-leader remove", "f co-leader remove", "fac co-leader remove", "faction co-leader remove", "f coleader remove", "fac coleader remove", "faction coleader remove"}, permission = "")
    public static void coleaderRemove(Player sender, @Param(name = "player") UUID demote) {
        Team team = Hulu.getInstance().getTeamHandler().getTeam(sender.getUniqueId());
        if (team == null) {
            sender.sendMessage(ChatColor.RED + "You must be in a team to execute this command.");
            return;
        }

        if (!team.isOwner(sender.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + "Only the team owner can execute this command.");
            return;
        }

        if (!team.isMember(demote)) {
            sender.sendMessage(ChatColor.RED + "This player must be a member of your team.");
            return;
        }

        if (!team.isCoLeader(demote)) {
            sender.sendMessage(ChatColor.RED + "This player is not a co-leader of your team.");
            return;
        }

        team.removeCoLeader(demote);
        team.removeCaptain(demote);
        team.sendMessage(org.bukkit.ChatColor.DARK_AQUA + Stark.instance.getCore().getUuidCache().name(demote) + " has been demoted to a member!");
    }
}
