/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.team.commands.team;

import cc.fyre.stark.Stark;
import cc.fyre.stark.engine.command.Command;
import cc.fyre.stark.engine.command.data.parameter.Param;
import com.google.common.collect.ImmutableMap;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.server.SpawnTagHandler;
import net.hcriots.hcfactions.team.Team;
import net.hcriots.hcfactions.team.dtr.DTRHandler;
import net.hcriots.hcfactions.team.track.TeamActionTracker;
import net.hcriots.hcfactions.team.track.TeamActionType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

@SuppressWarnings("deprecation")
public class TeamForceKickCommand {

    @Command(names = {"team forcekick", "t forcekick", "f forcekick", "faction forcekick", "fac forcekick"}, permission = "")
    public static void teamForceKick(Player sender, @Param(name = "player") UUID player) {
        Team team = Hulu.getInstance().getTeamHandler().getTeam(sender);

        if (team == null) {
            sender.sendMessage(ChatColor.GRAY + "You are not on a team!");
            return;
        }

        if (!(team.isOwner(sender.getUniqueId()) || team.isCoLeader(sender.getUniqueId()) || team.isCaptain(sender.getUniqueId()))) {
            sender.sendMessage(ChatColor.DARK_AQUA + "Only team captains can do this.");
            return;
        }

        if (!team.isMember(player)) {
            sender.sendMessage(ChatColor.RED + Stark.instance.getCore().getUuidCache().name(player) + " isn't on your team!");
            return;
        }

        if (team.isOwner(player)) {
            sender.sendMessage(ChatColor.RED + "You cannot kick the team leader!");
            return;
        }

        if (team.isCoLeader(player) && (!team.isOwner(sender.getUniqueId()))) {
            sender.sendMessage(ChatColor.RED + "Only the owner can kick other co-leaders!");
            return;
        }

        if (team.isCaptain(player) && (!team.isOwner(sender.getUniqueId()) && !team.isCoLeader(sender.getUniqueId()))) {
            sender.sendMessage(ChatColor.RED + "Only an owner or co-leader can kick other captains!");
            return;
        }

        TeamActionTracker.logActionAsync(team, TeamActionType.MEMBER_KICKED, ImmutableMap.of(
                "playerId", player,
                "kickedById", sender.getUniqueId(),
                "kickedByName", sender.getName(),
                "usedForceKick", "true"
        ));

        if (team.removeMember(player)) {
            team.disband();
        } else {
            team.flagForSave();
        }

        Hulu.getInstance().getTeamHandler().setTeam(player, null);
        Player bukkitPlayer = Hulu.getInstance().getServer().getPlayer(player);

        if (SpawnTagHandler.isTagged(bukkitPlayer)) {
            team.setDTR(team.getDTR() - 1);
            team.sendMessage(ChatColor.RED + Stark.instance.getCore().getUuidCache().name(player) + " was force kicked by " + sender.getName() + " and your team lost 1 DTR!");
            long dtrCooldown;
            if (team.isRaidable()) {
                TeamActionTracker.logActionAsync(team, TeamActionType.TEAM_NOW_RAIDABLE, ImmutableMap.of());
                dtrCooldown = System.currentTimeMillis() + Hulu.getInstance().getMapHandler().getRegenTimeRaidable();
            } else {
                dtrCooldown = System.currentTimeMillis() + Hulu.getInstance().getMapHandler().getRegenTimeDeath();
            }

            team.setDTRCooldown(dtrCooldown);
            DTRHandler.markOnDTRCooldown(team);
        } else {
            team.sendMessage(ChatColor.RED + Stark.instance.getCore().getUuidCache().name(player) + " was force kicked by " + sender.getName() + "!");
        }

        if (bukkitPlayer != null) {
            Stark.instance.getNametagEngine().reloadPlayer(bukkitPlayer);
            Stark.instance.getNametagEngine().reloadOthersFor(bukkitPlayer);
        }
    }

}