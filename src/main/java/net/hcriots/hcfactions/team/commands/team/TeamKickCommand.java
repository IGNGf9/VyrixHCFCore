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
import net.hcriots.hcfactions.team.track.TeamActionTracker;
import net.hcriots.hcfactions.team.track.TeamActionType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

@SuppressWarnings("deprecation")
public class TeamKickCommand {

    @Command(names = {"team kick", "t kick", "f kick", "faction kick", "fac kick"}, permission = "")
    public static void teamKick(Player sender, @Param(name = "player") UUID player) {
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

        if (team.isCaptain(player) && !team.isOwner(sender.getUniqueId()) && !team.isCoLeader(sender.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + "Only an owner or co-leader can kick other captains!");
            return;
        }

        Player bukkitPlayer = Hulu.getInstance().getServer().getPlayer(player);

        if (bukkitPlayer != null && SpawnTagHandler.isTagged(bukkitPlayer)) {
            sender.sendMessage(ChatColor.RED + bukkitPlayer.getName() + " is currently combat-tagged! You can forcibly kick " + bukkitPlayer.getName() + " by using '"
                    + ChatColor.YELLOW + "/f forcekick " + bukkitPlayer.getName() + ChatColor.RED + "' which will cost your team 1 DTR.");
            return;
        }

        team.sendMessage(ChatColor.DARK_AQUA + Stark.instance.getCore().getUuidCache().name(player) + " was kicked by " + sender.getName() + "!");

        TeamActionTracker.logActionAsync(team, TeamActionType.MEMBER_KICKED, ImmutableMap.of(
                "playerId", player,
                "kickedById", sender.getUniqueId(),
                "kickedByName", sender.getName(),
                "usedForceKick", "false"
        ));

        if (team.removeMember(player)) {
            team.disband();
        } else {
            team.flagForSave();
        }

        Hulu.getInstance().getTeamHandler().setTeam(player, null);

        if (bukkitPlayer != null) {
            Stark.instance.getNametagEngine().reloadPlayer(bukkitPlayer);
            Stark.instance.getNametagEngine().reloadOthersFor(bukkitPlayer);
        }
    }

}