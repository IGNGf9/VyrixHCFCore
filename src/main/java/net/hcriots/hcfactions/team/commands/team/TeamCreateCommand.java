/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.team.commands.team;

import cc.fyre.stark.Stark;
import cc.fyre.stark.engine.command.Command;
import cc.fyre.stark.engine.command.data.Type;
import cc.fyre.stark.engine.command.data.parameter.Param;
import cc.fyre.stark.engine.command.data.parameter.impl.filter.NormalFilter;
import cc.fyre.stark.server.chat.ServerChatSettings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.commands.EOTWCommand;
import net.hcriots.hcfactions.team.Team;
import net.hcriots.hcfactions.team.track.TeamActionTracker;
import net.hcriots.hcfactions.team.track.TeamActionType;
import org.bson.types.ObjectId;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.regex.Pattern;

public class TeamCreateCommand {

    public static final Pattern ALPHA_NUMERIC = Pattern.compile("[^a-zA-Z0-9]");
    private static final Set<String> disallowedTeamNames = ImmutableSet.of("list", "Glowstone");

    @Command(names = {"team create", "t create", "f create", "faction create", "fac create"}, permission = "")
    public static void teamCreate(Player sender, @Param(name = "team") @Type(NormalFilter.class) String team) {
        if (Hulu.getInstance().getTeamHandler().getTeam(sender) != null) {
            sender.sendMessage(ChatColor.GRAY + "You're already in a team!");
            return;
        }

        if (team.length() > 16) {
            sender.sendMessage(ChatColor.RED + "Maximum team name size is 16 characters!");
            return;
        }

        if (team.length() < 3) {
            sender.sendMessage(ChatColor.RED + "Minimum team name size is 3 characters!");
            return;
        }

//        if (TeamGeneralConfiguration.getDisallowedNames().contains(team.toLowerCase()) && !sender.isOp()) {
//            sender.sendMessage(ChatColor.RED + "That faction name is not allowed.");
//            return;
//        }

        if (Hulu.getInstance().getTeamHandler().getTeam(team) != null) {
            sender.sendMessage(ChatColor.GRAY + "That team already exists!");
            return;
        }

        if (ALPHA_NUMERIC.matcher(team).find()) {
            sender.sendMessage(ChatColor.RED + "Team names must be alphanumeric!");
            return;
        }

        if (EOTWCommand.realFFAStarted()) {
            sender.sendMessage(ChatColor.RED + "You can't create teams during FFA.");
            return;
        }

        sender.sendMessage(ChatColor.GRAY + "To learn more about teams, do /team");

        Team createdTeam = new Team(team);

        TeamActionTracker.logActionAsync(createdTeam, TeamActionType.PLAYER_CREATE_TEAM, ImmutableMap.of(
                "playerId", sender.getUniqueId(),
                "playerName", sender.getName()
        ));

        createdTeam.setUniqueId(new ObjectId());
        createdTeam.setOwner(sender.getUniqueId());
        createdTeam.setName(team);
        createdTeam.setDTR(1);

        Hulu.getInstance().getTeamHandler().setupTeam(createdTeam);

        if (!Stark.instance.getServerHandler().getFrozen() && !ServerChatSettings.INSTANCE.getMuted()) {
            Bukkit.broadcastMessage(ChatColor.YELLOW + "Team " + ChatColor.BLUE + createdTeam.getName() + ChatColor.YELLOW + " has been " + ChatColor.GREEN + "created" + ChatColor.YELLOW + " by " + sender.getDisplayName());
        }
    }
}