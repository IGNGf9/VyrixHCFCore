/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.team.commands.team;

import cc.fyre.stark.engine.command.Command;
import cc.fyre.stark.engine.command.data.parameter.Param;
import com.google.common.collect.ImmutableMap;
import net.hcriots.hcfactions.team.Team;
import net.hcriots.hcfactions.team.track.TeamActionTracker;
import net.hcriots.hcfactions.team.track.TeamActionType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class TeamUnShadowMuteCommand {

    @Command(names = {"team unshadowmute", "t unshadowmute", "f unshadowmute", "faction unshadowmute", "fac unshadowmute"}, permission = "foxtrot.mutefaction")
    public static void teamUnShadowMute(Player sender, @Param(name = "team") Team team) {
        TeamActionTracker.logActionAsync(team, TeamActionType.TEAM_MUTE_EXPIRED, ImmutableMap.of(
                "shadowMute", "true"
        ));

        Iterator<Map.Entry<UUID, String>> mutesIterator = TeamShadowMuteCommand.getTeamShadowMutes().entrySet().iterator();

        while (mutesIterator.hasNext()) {
            Map.Entry<UUID, String> mute = mutesIterator.next();

            if (mute.getValue().equalsIgnoreCase(team.getName())) {
                mutesIterator.remove();
            }
        }

        sender.sendMessage(ChatColor.GRAY + "Un-shadowmuted the team " + team.getName() + ChatColor.GRAY + ".");
    }

}