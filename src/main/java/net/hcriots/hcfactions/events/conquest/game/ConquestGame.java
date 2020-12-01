/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.events.conquest.game;

import cc.fyre.stark.Stark;
import lombok.Getter;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.events.Event;
import net.hcriots.hcfactions.events.EventType;
import net.hcriots.hcfactions.events.conquest.ConquestHandler;
import net.hcriots.hcfactions.events.conquest.enums.ConquestCapzone;
import net.hcriots.hcfactions.events.events.EventCapturedEvent;
import net.hcriots.hcfactions.events.koth.KOTH;
import net.hcriots.hcfactions.events.koth.events.EventControlTickEvent;
import net.hcriots.hcfactions.events.koth.events.KOTHControlLostEvent;
import net.hcriots.hcfactions.team.Team;
import org.bson.types.ObjectId;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class ConquestGame implements Listener {

    @Getter
    private LinkedHashMap<ObjectId, Integer> teamPoints = new LinkedHashMap<>();

    public ConquestGame() {
        Hulu.getInstance().getServer().getPluginManager().registerEvents(this, Hulu.getInstance());

        for (Event event : Hulu.getInstance().getEventHandler().getEvents()) {
            if (event.getType() != EventType.KOTH) continue;
            KOTH koth = (KOTH) event;
            if (koth.getName().startsWith(ConquestHandler.KOTH_NAME_PREFIX)) {
                if (!koth.isHidden()) {
                    koth.setHidden(true);
                }

                if (koth.getCapTime() != ConquestHandler.TIME_TO_CAP) {
                    koth.setCapTime(ConquestHandler.TIME_TO_CAP);
                }

                koth.activate();
            }
        }

        Hulu.getInstance().getServer().broadcastMessage(ConquestHandler.PREFIX + " " + ChatColor.GOLD + "Conquest has started! Use /conquest for more information.");
        Hulu.getInstance().getConquestHandler().setGame(this);
    }

    private static LinkedHashMap<ObjectId, Integer> sortByValues(Map<ObjectId, Integer> map) {
        LinkedList<Map.Entry<ObjectId, Integer>> list = new LinkedList<>(map.entrySet());
        Collections.sort(list, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        LinkedHashMap<ObjectId, Integer> sortedHashMap = new LinkedHashMap<>();
        Iterator<Map.Entry<ObjectId, Integer>> iterator = list.iterator();

        while (iterator.hasNext()) {
            Map.Entry<ObjectId, Integer> entry = iterator.next();
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }

        return sortedHashMap;
    }

    public void endGame(final Team winner) {
        if (winner == null) {
            Hulu.getInstance().getServer().broadcastMessage(ConquestHandler.PREFIX + " " + ChatColor.GOLD + "Conquest has ended.");
        } else {
            Hulu.getInstance().getServer().broadcastMessage(ConquestHandler.PREFIX + " " + ChatColor.GOLD.toString() + ChatColor.BOLD + winner.getName() + ChatColor.GOLD + " has won Conquest!");
        }

        for (Event koth : Hulu.getInstance().getEventHandler().getEvents()) {
            if (koth.getName().startsWith(ConquestHandler.KOTH_NAME_PREFIX)) {
                koth.deactivate();
            }
        }

        HandlerList.unregisterAll(this);
        Hulu.getInstance().getConquestHandler().setGame(null);
    }

    @EventHandler
    public void onKOTHCaptured(final EventCapturedEvent event) {
        if (!event.getEvent().getName().startsWith(ConquestHandler.KOTH_NAME_PREFIX)) {
            return;
        }

        Team team = Hulu.getInstance().getTeamHandler().getTeam(event.getPlayer());
        ConquestCapzone capzone = ConquestCapzone.valueOf(event.getEvent().getName().replace(ConquestHandler.KOTH_NAME_PREFIX, "").toUpperCase());

        if (team == null) {
            return;
        }

        if (teamPoints.containsKey(team.getUniqueId())) {
            if (capzone.getName().equalsIgnoreCase("Main")) {
                teamPoints.put(team.getUniqueId(), teamPoints.get(team.getUniqueId()) + 2);
            } else {
                teamPoints.put(team.getUniqueId(), teamPoints.get(team.getUniqueId()) + 1);
            }
        } else {
            if (capzone.getName().equalsIgnoreCase("Main")) {
                teamPoints.put(team.getUniqueId(), 2);
            } else {
                teamPoints.put(team.getUniqueId(), teamPoints.get(team.getUniqueId()) + 1);
            }
        }

        teamPoints = sortByValues(teamPoints);
        Hulu.getInstance().getServer().broadcastMessage(ConquestHandler.PREFIX + " " + ChatColor.GOLD + team.getName() + ChatColor.GOLD + " captured " + capzone.getColor() + capzone.getName() + ChatColor.GOLD + " and earned a point!" + ChatColor.AQUA + " (" + teamPoints.get(team.getUniqueId()) +
                "/" + ConquestHandler.getPointsToWin() + ")");

        if (teamPoints.get(team.getUniqueId()) >= ConquestHandler.getPointsToWin()) {
            endGame(team);


            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "cr givekey " + event.getPlayer().getName() + " Koth 5");
            team.addPlaytimePoints(50);
        } else {
            new BukkitRunnable() {

                public void run() {
                    if (Hulu.getInstance().getConquestHandler().getGame() != null) {
                        event.getEvent().activate();
                    }
                }

            }.runTaskLater(Hulu.getInstance(), 10L);
        }
    }

    @EventHandler
    public void onKOTHControlLost(KOTHControlLostEvent event) {
        if (!event.getKOTH().getName().startsWith(ConquestHandler.KOTH_NAME_PREFIX)) {
            return;
        }

        Team team = Hulu.getInstance().getTeamHandler().getTeam(Stark.instance.getCore().getUuidCache().uuid(event.getKOTH().getCurrentCapper()));
        ConquestCapzone capzone = ConquestCapzone.valueOf(event.getKOTH().getName().replace(ConquestHandler.KOTH_NAME_PREFIX, "").toUpperCase());

        if (team == null) {
            return;
        }

        team.sendMessage(ConquestHandler.PREFIX + ChatColor.GOLD + " " + event.getKOTH().getCurrentCapper() + " was knocked off of " + capzone.getColor() + capzone.getName() + ChatColor.GOLD + "!");
    }

    @EventHandler
    public void onKOTHControlTick(EventControlTickEvent event) {

        if (!event.getKOTH().getName().startsWith(ConquestHandler.KOTH_NAME_PREFIX) || event.getKOTH().getRemainingCapTime() % 5 != 0) {
            return;
        }

        ConquestCapzone capzone = ConquestCapzone.valueOf(event.getKOTH().getName().replace(ConquestHandler.KOTH_NAME_PREFIX, "").toUpperCase());
        Player capper = Hulu.getInstance().getServer().getPlayerExact(event.getKOTH().getCurrentCapper());

        if (capper != null) {
            capper.sendMessage(ConquestHandler.PREFIX + " " + ChatColor.GOLD + "Attempting to capture " + capzone.getColor() + capzone.getName() + ChatColor.GOLD + "!" + ChatColor.AQUA + " (" + event.getKOTH().getRemainingCapTime() + "s)");
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Team team = Hulu.getInstance().getTeamHandler().getTeam(event.getEntity());

        if (team == null || !teamPoints.containsKey(team.getUniqueId())) {
            return;
        }

        teamPoints.put(team.getUniqueId(), Math.max(0, teamPoints.get(team.getUniqueId()) - ConquestHandler.POINTS_DEATH_PENALTY));
        teamPoints = sortByValues(teamPoints);
        team.sendMessage(ConquestHandler.PREFIX + ChatColor.GOLD + " Your team has lost " + ConquestHandler.POINTS_DEATH_PENALTY + " points because of " + event.getEntity().getName() + "'s death!" + ChatColor.AQUA + " (" + teamPoints.get(team.getUniqueId()) + "/" + ConquestHandler.getPointsToWin() + ")");
    }

}