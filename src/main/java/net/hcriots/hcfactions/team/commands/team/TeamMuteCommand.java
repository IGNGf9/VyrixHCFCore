/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.team.commands.team;

import cc.fyre.stark.core.util.TimeUtils;
import cc.fyre.stark.engine.command.Command;
import cc.fyre.stark.engine.command.data.parameter.Param;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.team.Team;
import net.hcriots.hcfactions.team.track.TeamActionTracker;
import net.hcriots.hcfactions.team.track.TeamActionType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class TeamMuteCommand {

    @Getter
    private static final Map<UUID, String> teamMutes = new HashMap<>();

    @Command(names = {"team mute", "t mute", "f mute", "faction mute", "fac mute"}, permission = "foxtrot.mutefaction")
    public static void teamMute(Player sender, @Param(name = "team") final Team team, @Param(name = "time") int time, @Param(name = "reason", wildcard = true) String reason) {
        int timeSeconds = time * 60;

        for (UUID player : team.getMembers()) {
            teamMutes.put(player, team.getName());

            Player bukkitPlayer = Hulu.getInstance().getServer().getPlayer(player);

            if (bukkitPlayer != null) {
                bukkitPlayer.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "Your team has been muted for " + TimeUtils.formatIntoMMSS(timeSeconds) + " for " + reason + ".");
            }
        }

        TeamActionTracker.logActionAsync(team, TeamActionType.TEAM_MUTE_CREATED, ImmutableMap.of(
                "shadowMute", "false",
                "mutedById", sender.getUniqueId(),
                "mutedByName", sender.getName(),
                "duration", time
        ));

        new BukkitRunnable() {

            public void run() {
                TeamActionTracker.logActionAsync(team, TeamActionType.TEAM_MUTE_EXPIRED, ImmutableMap.of(
                        "shadowMute", "false"
                ));

                Iterator<Map.Entry<UUID, String>> mutesIterator = teamMutes.entrySet().iterator();

                while (mutesIterator.hasNext()) {
                    Map.Entry<UUID, String> mute = mutesIterator.next();

                    if (mute.getValue().equalsIgnoreCase(team.getName())) {
                        mutesIterator.remove();
                    }
                }
            }

        }.runTaskLater(Hulu.getInstance(), timeSeconds * 20L);

        sender.sendMessage(ChatColor.YELLOW + "Muted the team " + team.getName() + ChatColor.GRAY + " for " + TimeUtils.formatIntoMMSS(timeSeconds) + " for " + reason + ".");
    }

}