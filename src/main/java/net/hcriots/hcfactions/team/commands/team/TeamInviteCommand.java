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
import net.hcriots.hcfactions.team.event.FullTeamBypassEvent;
import net.hcriots.hcfactions.team.track.TeamActionTracker;
import net.hcriots.hcfactions.team.track.TeamActionType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TeamInviteCommand {

    @Command(names = {"team invite", "t invite", "f invite", "faction invite", "fac invite", "team inv", "t inv", "f inv", "faction inv", "fac inv"}, permission = "")
    public static void teamInvite(Player sender, @Param(name = "player") UUID player, @Param(name = "override?", defaultValue = "something-not-override") String override) {
        Team team = Hulu.getInstance().getTeamHandler().getTeam(sender);

        if (team == null) {
            sender.sendMessage(ChatColor.GRAY + "You are not on a team!");
            return;
        }

        if (team.getMembers().size() >= Hulu.getInstance().getMapHandler().getTeamSize()) {
            FullTeamBypassEvent bypassEvent = new FullTeamBypassEvent(sender, team);
            Hulu.getInstance().getServer().getPluginManager().callEvent(bypassEvent);

            if (!bypassEvent.isAllowBypass()) {
                sender.sendMessage(ChatColor.RED + "The max team size is " + Hulu.getInstance().getMapHandler().getTeamSize() + (bypassEvent.getExtraSlots() == 0 ? "" : " (+" + bypassEvent.getExtraSlots() + ")") + "!");
                return;
            }
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

        /*if (team.isRaidable()) {
            sender.sendMessage(ChatColor.RED + "You may not invite players while your team is raidable!");
            return;
        }*/

        if (Hulu.getInstance().getServerHandler().isForceInvitesEnabled() && !Hulu.getInstance().getServerHandler().isPreEOTW()) {
            /* if we just check team.getSize() players can make a team with 10 players,
            send out 20 invites, and then have them all accepted (instead of 1 invite,
            1 join, 1 invite, etc) To solve this we treat their size as their actual
            size + number of open invites. */
            int possibleTeamSize = team.getSize() + team.getInvitations().size();

            if (!Hulu.getInstance().getMapHandler().isKitMap() && team.getHistoricalMembers().contains(player) && possibleTeamSize > Hulu.getInstance().getMapHandler().getMinForceInviteMembers()) {
                sender.sendMessage(ChatColor.RED + "This player has previously joined your faction. You must use a force-invite to re-invite " + Stark.instance.getCore().getUuidCache().name(player) + ". Type "
                        + ChatColor.YELLOW + "'/f forceinvite " + Stark.instance.getCore().getUuidCache().name(player) + "'" + ChatColor.RED + " to use a force-invite."
                );

                return;
            }
        }

        TeamActionTracker.logActionAsync(team, TeamActionType.PLAYER_INVITE_SENT, ImmutableMap.of(
                "playerId", player,
                "invitedById", sender.getUniqueId(),
                "invitedByName", sender.getName(),
                "betrayOverride", override.equalsIgnoreCase("override"),
                "usedForceInvite", "false"
        ));

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