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
import mkremins.fanciful.FancyMessage;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.team.Team;
import net.hcriots.hcfactions.team.track.TeamActionTracker;
import net.hcriots.hcfactions.team.track.TeamActionType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class TeamForceInviteCommand {

    @Command(names = {"team forceinvite", "t forceinvite", "f forceinvite", "faction forceinvite", "fac forceinvite"}, permission = "")
    public static void teamForceInvite(Player sender, @Param(name = "player") UUID player) {
        if (!Hulu.getInstance().getServerHandler().isForceInvitesEnabled()) {
            sender.sendMessage(ChatColor.RED + "Force-invites are not enabled on this server.");
            return;
        }

        Team team = Hulu.getInstance().getTeamHandler().getTeam(sender);

        if (Hulu.getInstance().getMapHandler().isKitMap()) {
            sender.sendMessage(ChatColor.RED + "You don't need to use this during kit maps.");
            return;
        }

        if (team == null) {
            sender.sendMessage(ChatColor.GRAY + "You are not on a team!");
            return;
        }

        if (team.getMembers().size() >= Hulu.getInstance().getMapHandler().getTeamSize()) {
            sender.sendMessage(ChatColor.RED + "The max team size is " + Hulu.getInstance().getMapHandler().getTeamSize() + "!");
            return;
        }

        if (!(team.isOwner(sender.getUniqueId()) || team.isCoLeader(sender.getUniqueId()) || team.isCaptain(sender.getUniqueId()))) {
            sender.sendMessage(ChatColor.DARK_AQUA + "Only team captains can do this.");
            return;
        }

        if (team.isMember(player)) {
            sender.sendMessage(ChatColor.DARK_AQUA + Stark.instance.getCore().getUuidCache().name(player) + " is already on your team.");
            return;
        }

        if (team.getInvitations().contains(player)) {
            sender.sendMessage(ChatColor.RED + "That player has already been invited.");
            return;
        }

        if (!team.getHistoricalMembers().contains(player)) {
            sender.sendMessage(ChatColor.RED + "That player has never been a member of your faction. Please use /f invite.");
            return;
        }

        /*if (team.isRaidable()) {
            sender.sendMessage(ChatColor.RED + "You may not invite players while your team is raidable!");
            return;
        }*/

        if (team.getForceInvites() == 0) {
            sender.sendMessage(ChatColor.RED + "You do not have any force-invites left!");
            return;
        }

        team.setForceInvites(team.getForceInvites() - 1);
        TeamActionTracker.logActionAsync(team, TeamActionType.PLAYER_INVITE_SENT, ImmutableMap.of(
                "playerId", player,
                "invitedById", sender.getUniqueId(),
                "invitedByName", sender.getName(),
                "betrayOverride", "false",
                "usedForceInvite", "true"
        ));

        // we use a runnable so this message gets displayed at the end
        new BukkitRunnable() {
            @Override
            public void run() {
                sender.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "You have used a force-invite.");

                if (team.getForceInvites() != 0) {
                    sender.sendMessage(ChatColor.YELLOW + "You have " + ChatColor.RED + team.getForceInvites() + ChatColor.YELLOW + " of those left.");
                } else {
                    sender.sendMessage(ChatColor.YELLOW + "You have " + ChatColor.RED + "none" + ChatColor.YELLOW + " of those left.");
                }
            }
        }.runTask(Hulu.getInstance());

        team.getInvitations().add(player);
        team.flagForSave();

        Player bukkitPlayer = Hulu.getInstance().getServer().getPlayer(player);

        if (bukkitPlayer != null) {
            bukkitPlayer.sendMessage(ChatColor.DARK_AQUA + sender.getName() + " invited you to join '" + ChatColor.YELLOW + team.getName() + ChatColor.DARK_AQUA + "'.");

            FancyMessage clickToJoin = new FancyMessage("Type '").color(ChatColor.DARK_AQUA).then("/team join " + team.getName()).color(ChatColor.YELLOW);
            clickToJoin.then("' or ").color(ChatColor.DARK_AQUA);
            clickToJoin.then("click here").color(ChatColor.AQUA).command("/team join " + team.getName()).tooltip("§aJoin " + team.getName());
            clickToJoin.then(" to join.").color(ChatColor.DARK_AQUA);

            clickToJoin.send(bukkitPlayer);
        }

        team.sendMessage(ChatColor.YELLOW + Stark.instance.getCore().getUuidCache().name(player) + " has been invited to the team!");
    }

}
