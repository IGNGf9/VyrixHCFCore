/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.tab;

import cc.fyre.stark.core.util.TimeUtils;
import cc.fyre.stark.engine.tab.LayoutProvider;
import cc.fyre.stark.engine.tab.TabLayout;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.elo.EloHandler;
import net.hcriots.hcfactions.events.Event;
import net.hcriots.hcfactions.events.EventScheduledTime;
import net.hcriots.hcfactions.events.koth.KOTH;
import net.hcriots.hcfactions.map.stats.StatsEntry;
import net.hcriots.hcfactions.team.Team;
import net.hcriots.hcfactions.team.claims.LandBoard;
import net.hcriots.hcfactions.team.commands.team.TeamListCommand;
import net.hcriots.hcfactions.util.PlayerDirection;
import net.minecraft.util.com.google.common.collect.Maps;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.text.NumberFormat;
import java.util.*;

public class HuluTabLayoutProvider implements LayoutProvider {

    long cacheLastUpdated;
    private LinkedHashMap<Team, Integer> cachedTeamList = Maps.newLinkedHashMap();

    @Override
    public TabLayout provide(Player player) {
        TabLayout layout = TabLayout.create(player);

        Team team = Hulu.getInstance().getTeamHandler().getTeam(player);
        TabListMode mode = Hulu.getInstance().getTabListModeMap().getTabListMode(player.getUniqueId());

        String serverName = Hulu.getInstance().getServerHandler().getTabServerName();
        String titleColor = Hulu.getInstance().getServerHandler().getTabSectionColor();
        String infoColor = Hulu.getInstance().getServerHandler().getTabInfoColor();

        layout.set(1, 1, serverName);
        layout.set(1, 2, ChatColor.WHITE + Hulu.getInstance().getServerHandler().getNetworkWebsite());

        //Left Side
        layout.set(0, 4, titleColor + "Stats");

        // Stats of Player
        boolean isKitMap = Hulu.getInstance().getMapHandler().isKitMap();
        if (isKitMap) {
            StatsEntry stats = Hulu.getInstance().getMapHandler().getStatsHandler().getStats(player.getUniqueId());
            layout.set(0, 5, infoColor + "Kills: " + ChatColor.WHITE + stats.getKills());
            layout.set(0, 6, infoColor + "Deaths: " + ChatColor.WHITE + stats.getDeaths());
        } else {
            layout.set(0, 5, infoColor + "Kills: " + ChatColor.WHITE + Hulu.getInstance().getKillsMap().getKills(player.getUniqueId()));
            layout.set(0, 6, infoColor + "Deaths: " + ChatColor.WHITE + Hulu.getInstance().getDeathsMap().getDeaths(player.getUniqueId()));
        }
        layout.set(0, 7, infoColor + "Balance: " + ChatColor.GREEN + "$" + ChatColor.WHITE + NumberFormat.getNumberInstance(Locale.US).format(Hulu.getInstance().getEconomyHandler().getBalance(player.getUniqueId())));
        layout.set(0, 8, infoColor + "ELO: " + ChatColor.WHITE + EloHandler.getPlayerELO(player));

        // Next Koth
        KOTH activeKOTH = null;
        for (final Event event : Hulu.getInstance().getEventHandler().getEvents()) {
            if (!(event instanceof KOTH)) {
                continue;
            }
            final KOTH koth = (KOTH) event;
            if (koth.isActive() && !koth.isHidden()) {
                activeKOTH = koth;
                break;
            }
        }
        layout.set(0, 12, titleColor + (activeKOTH == null ? "Next Event" : "Active Event"));
        if (activeKOTH == null) {
            final Date now = new Date();
            String nextKothName = null;
            Date nextKothDate = null;
            for (final Map.Entry<EventScheduledTime, String> entry : Hulu.getInstance().getEventHandler().getEventSchedule().entrySet()) {
                if (entry.getKey().toDate().after(now) && (nextKothDate == null || nextKothDate.getTime() > entry.getKey().toDate().getTime())) {
                    nextKothName = entry.getValue();
                    nextKothDate = entry.getKey().toDate();
                }
            }
            if (nextKothName != null) {
                layout.set(0, 13, infoColor + nextKothName);
                final Event nextEvent = Hulu.getInstance().getEventHandler().getEvent(nextKothName);
                if (nextEvent instanceof KOTH) {
                    final KOTH koth2 = (KOTH) nextEvent;
                    layout.set(0, 14, infoColor + koth2.getCapLocation().getBlockX() + ", " + koth2.getCapLocation().getBlockY() + ", " + koth2.getCapLocation().getBlockZ());
                    final int seconds = (int) ((nextKothDate.getTime() - System.currentTimeMillis()) / 1000L);
                    layout.set(0, 15, titleColor + "Goes active in:");
                    final String time = TimeUtils.formatIntoDetailedString(seconds).replace("minutes", "min").replace("minute", "min").replace("seconds", "sec").replace("second", "sec");
                    layout.set(0, 16, infoColor + time);
                }
            }
        } else {
            layout.set(0, 13, ChatColor.BLUE + ChatColor.BOLD.toString() + activeKOTH.getName());
            layout.set(0, 14, infoColor + TimeUtils.formatIntoHHMMSS(activeKOTH.getRemainingCapTime()));
            layout.set(0, 15, infoColor + activeKOTH.getCapLocation().getBlockX() + ", " + activeKOTH.getCapLocation().getBlockY() + ", " + activeKOTH.getCapLocation().getBlockZ());
        }

        // Middle
        layout.set(1, 4, titleColor + "Faction Info");
        if (team == null) {
            layout.set(1, 5, ChatColor.WHITE + "/f create <faction>");
        } else {
            layout.set(1, 5, infoColor + "Name: " + ChatColor.WHITE + team.getName());
            layout.set(1, 6, infoColor + "DTR: " + team.getDTRWithColor());
            layout.set(1, 7, infoColor + "Points: " + ChatColor.WHITE + team.getPoints());
            if (team.getHQ() != null) {
                String homeLocation = team.getHQ().getBlockX() + ", " + team.getHQ().getBlockY() + ", " + team.getHQ().getBlockZ();
                layout.set(1, 8, infoColor + "HQ: " + ChatColor.WHITE + homeLocation);
            } else {
                layout.set(1, 8, infoColor + "HQ: " + ChatColor.RED + "Not Set");
            }
        }

        if (mode == TabListMode.DETAILED) {
            layout.set(2, 4, titleColor + "Map Info");
            layout.set(2, 5, infoColor + "Map Kit: " + ChatColor.WHITE + "P" + 1 + ", S" + 1);
            layout.set(2, 6, infoColor + "Factions: " + ChatColor.WHITE + Hulu.getInstance().getMapHandler().getTeamSize() + " Man");
            layout.set(2, 7, infoColor + "Allies: " + ChatColor.WHITE + (Hulu.getInstance().getMapHandler().getAllyLimit() == 0 ? "No Allies" : Hulu.getInstance().getMapHandler().getAllyLimit()));

            // Player Location
            layout.set(2, 12, titleColor + "Your Location:");
            Location loc = player.getLocation();
            Team ownerTeam = LandBoard.getInstance().getTeam(loc);
            String location;
            if (ownerTeam != null) {
                location = ownerTeam.getName(player.getPlayer());
            } else if (!Hulu.getInstance().getServerHandler().isWarzone(loc)) {
                location = ChatColor.DARK_GREEN + "Wilderness";
            } else if (LandBoard.getInstance().getTeam(loc) != null && LandBoard.getInstance().getTeam(loc).getName().equalsIgnoreCase("citadel")) {
                location = titleColor + "Citadel";
            } else {
                location = ChatColor.DARK_RED + "Warzone";
            }

            String direction = PlayerDirection.getCardinalDirection(player);
            if (direction != null) {
                layout.set(2, 14, ChatColor.GRAY + "(" + loc.getBlockX() + ", " + loc.getBlockZ() + ") [" + direction + "]");
            } else {
                layout.set(2, 14, ChatColor.GRAY + "(" + loc.getBlockX() + ", " + loc.getBlockZ() + ")");
            }
            layout.set(2, 13, location);

        } else if (mode == TabListMode.DETAILED_WITH_FACTION_INFO) {
            boolean shouldReloadCache = cachedTeamList == null || (System.currentTimeMillis() - cacheLastUpdated > 2000);

            int y = 1;
            y = 5;

            Map<Team, Integer> teamPlayerCount = new HashMap<>();

            if (shouldReloadCache) {
                // Sort of weird way of getting player counts, but it does it in the least iterations (1), which is what matters!
                for (Player other : Hulu.getInstance().getServer().getOnlinePlayers()) {
                    if (other.hasMetadata("invisible")) {
                        continue;
                    }

                    Team playerTeam = Hulu.getInstance().getTeamHandler().getTeam(other);

                    if (playerTeam != null) {
                        if (teamPlayerCount.containsKey(playerTeam)) {
                            teamPlayerCount.put(playerTeam, teamPlayerCount.get(playerTeam) + 1);
                        } else {
                            teamPlayerCount.put(playerTeam, 1);
                        }
                    }
                }
            }

            LinkedHashMap<Team, Integer> sortedTeamPlayerCount;

            if (shouldReloadCache) {
                sortedTeamPlayerCount = TeamListCommand.sortByValues(teamPlayerCount);
                cachedTeamList = sortedTeamPlayerCount;
                cacheLastUpdated = System.currentTimeMillis();
            } else {
                sortedTeamPlayerCount = cachedTeamList;
            }

            int index = 0;
            boolean title = false;

            for (Map.Entry<Team, Integer> teamEntry : sortedTeamPlayerCount.entrySet()) {
                index++;

                if (index > 12) {
                    break;
                }

                if (!title) {
                    title = true;
                    layout.set(2, 4, titleColor + "Team List:");
                }
                String teamName = teamEntry.getKey().getName();
                String teamColor = teamEntry.getKey().isMember(player.getUniqueId()) ? ChatColor.GREEN.toString() : infoColor;

                if (teamName.length() > 10) teamName = teamName.substring(0, 10);

                layout.set(2, y++, teamColor + teamName + ChatColor.GRAY + " (" + teamEntry.getValue() + ")");
            }
        }
        return layout;
    }
}
