/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.map.killstreaks;

import cc.fyre.stark.Stark;
import cc.fyre.stark.engine.command.Command;
import cc.fyre.stark.engine.command.data.parameter.Param;
import com.google.common.collect.Lists;
import lombok.Getter;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.deathmessage.event.PlayerKilledEvent;
import net.hcriots.hcfactions.map.killstreaks.impl.*;
import net.hcriots.hcfactions.map.stats.StatsEntry;
import net.hcriots.hcfactions.team.Team;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class KillstreakHandler implements Listener {

    @Getter
    private final List<Killstreak> killstreaks = Lists.newArrayList();
    @Getter
    private final List<PersistentKillstreak> persistentKillstreaks = Lists.newArrayList();

    public KillstreakHandler() {
        Hulu.getInstance().getServer().getPluginManager().registerEvents(this, Hulu.getInstance());

        Stark.instance.getCommandHandler().registerClass(this.getClass());

        List<Class<?>> killstreakTypes = Arrays.asList(
                Debuffs.class,
                Gapple.class,
                GoldenApples.class,
                Invis.class,
                PermSpeed2.class,
                Speed2.class,
                Strength.class
        );

        killstreakTypes.forEach(clazz -> {
            if (Killstreak.class.isAssignableFrom(clazz)) {
                try {
                    Killstreak killstreak = (Killstreak) clazz.newInstance();

                    killstreaks.add(killstreak);
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    PersistentKillstreak killstreak = (PersistentKillstreak) clazz.newInstance();

                    persistentKillstreaks.add(killstreak);
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });

        killstreaks.sort((first, second) -> {
            int firstNumber = first.getKills()[0];
            int secondNumber = second.getKills()[0];

            if (firstNumber < secondNumber) {
                return -1;
            }
            return 1;

        });

        persistentKillstreaks.sort((first, second) -> {
            int firstNumber = first.getKillsRequired();
            int secondNumber = second.getKillsRequired();

            if (firstNumber < secondNumber) {
                return -1;
            }
            return 1;

        });
    }

    @Command(names = "setks", permission = "hcteams.setkillstreak")
    public static void setKillstreak(Player player, @Param(name = "killstreak") int killstreak) {
        StatsEntry statsEntry = Hulu.getInstance().getMapHandler().getStatsHandler().getStats(player);
        statsEntry.setKillstreak(killstreak);

        player.sendMessage(ChatColor.GREEN + "You set your killstreak to: " + killstreak);
    }

    public Killstreak check(int kills) {
        for (Killstreak killstreak : killstreaks) {
            for (int kill : killstreak.getKills()) {
                if (kills == kill) {
                    return killstreak;
                }
            }
        }

        return null;
    }

    public List<PersistentKillstreak> getPersistentKillstreaks(Player player, int count) {
        return persistentKillstreaks.stream().filter(s -> s.check(count)).collect(Collectors.toList());
    }

    private void grantTeamKillstreakReward(Player player, Team team, int killstreak, int points) {
        team.addKillstreakPoints(points);
        team.sendMessage(ChatColor.GREEN + "Your team received " + points + " points thanks to " + ChatColor.AQUA + ChatColor.BOLD + player.getName() + ChatColor.GREEN + "'s " + killstreak + " killstreak.");
    }

    @EventHandler
    public void onPlayerKilledEvent(PlayerKilledEvent event) {
        StatsEntry killerStats = Hulu.getInstance().getMapHandler().getStatsHandler().getStats(event.getKiller());
        Team killerTeam = Hulu.getInstance().getTeamHandler().getTeam(event.getKiller());

        if (killerTeam != null) {
            // Check for team killstreak points rewards
            switch (killerStats.getKillstreak()) {
                case 75:
                    grantTeamKillstreakReward(event.getKiller(), killerTeam, 75, 15);
                    break;
                case 150:
                    grantTeamKillstreakReward(event.getKiller(), killerTeam, 150, 25);
                    break;
                case 300:
                    grantTeamKillstreakReward(event.getKiller(), killerTeam, 300, 30);
                    break;
                case 400:
                    grantTeamKillstreakReward(event.getKiller(), killerTeam, 400, 40);
                    break;
                case 500:
                    grantTeamKillstreakReward(event.getKiller(), killerTeam, 500, 50);
                    break;
                case 1000:
                    grantTeamKillstreakReward(event.getKiller(), killerTeam, 1000, 100);
                    break;
            }
        }
    }

}
