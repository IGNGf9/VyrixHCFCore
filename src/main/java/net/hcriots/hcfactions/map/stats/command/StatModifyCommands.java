/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.map.stats.command;

import cc.fyre.stark.engine.command.Command;
import cc.fyre.stark.engine.command.data.parameter.Param;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.map.stats.StatsEntry;
import net.hcriots.hcfactions.team.Team;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class StatModifyCommands {

    @Command(names = "sm setkills", permission = "op")
    public static void setKills(Player player, @Param(name = "kills") int kills) {
        StatsEntry stats = Hulu.getInstance().getMapHandler().getStatsHandler().getStats(player);
        stats.setKills(kills);

        Hulu.getInstance().getKillsMap().setKills(player.getUniqueId(), kills);

        player.sendMessage(ChatColor.GREEN + "You've set your own kills to: " + kills);
    }

    @Command(names = "sm setdeaths", permission = "op")
    public static void setDeaths(Player player, @Param(name = "deaths") int deaths) {
        StatsEntry stats = Hulu.getInstance().getMapHandler().getStatsHandler().getStats(player);
        stats.setDeaths(deaths);

        Hulu.getInstance().getDeathsMap().setDeaths(player.getUniqueId(), deaths);

        player.sendMessage(ChatColor.GREEN + "You've set your own deaths to: " + deaths);
    }

    @Command(names = "sm setteamkills", permission = "op")
    public static void setTeamKills(Player player, @Param(name = "kills") int kills) {
        Team team = Hulu.getInstance().getTeamHandler().getTeam(player);

        if (team != null) {
            team.setKills(kills);
            player.sendMessage(ChatColor.GREEN + "You've set your team's kills to: " + kills);
        }
    }

    @Command(names = "sm setteamdeaths", permission = "op")
    public static void setTeamDeaths(Player player, @Param(name = "deaths") int deaths) {
        Team team = Hulu.getInstance().getTeamHandler().getTeam(player);

        if (team != null) {
            team.setDeaths(deaths);
            player.sendMessage(ChatColor.GREEN + "You've set your team's deaths to: " + deaths);
        }
    }

}
