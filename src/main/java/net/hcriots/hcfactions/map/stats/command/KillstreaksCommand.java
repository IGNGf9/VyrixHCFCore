/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.map.stats.command;

import cc.fyre.stark.engine.command.Command;
import com.google.common.collect.Lists;
import mkremins.fanciful.FancyMessage;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.HuluLang;
import net.hcriots.hcfactions.map.killstreaks.Killstreak;
import net.hcriots.hcfactions.map.killstreaks.PersistentKillstreak;
import net.hcriots.hcfactions.map.stats.StatsEntry;
import net.hcriots.hcfactions.team.Team;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class KillstreaksCommand {

    @Command(names = {"killstreaks", "ks", "killstreak"}, permission = "")
    public static void killstreaks(CommandSender sender) {
        if (!Hulu.getInstance().getMapHandler().isKitMap()) {
            sender.sendMessage(HuluLang.KIT_MAP_ONLY);
        } else {
            sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat('-', 53));

            List<Object> streaks = Lists.newArrayList(Hulu.getInstance().getMapHandler().getKillstreakHandler().getKillstreaks());
            streaks.addAll(Hulu.getInstance().getMapHandler().getKillstreakHandler().getPersistentKillstreaks());

            streaks.sort((first, second) -> {

                int firstNumber = first instanceof Killstreak ? ((Killstreak) first).getKills()[0] : ((PersistentKillstreak) first).getKillsRequired();
                int secondNumber = second instanceof Killstreak ? ((Killstreak) second).getKills()[0] : ((PersistentKillstreak) second).getKillsRequired();

                if (firstNumber < secondNumber) {
                    return -1;
                }
                return 1;

            });

            for (Object ks : streaks) {
                String name = ks instanceof Killstreak ? ((Killstreak) ks).getName() : ((PersistentKillstreak) ks).getName();
                int kills = ks instanceof Killstreak ? ((Killstreak) ks).getKills()[0] : ((PersistentKillstreak) ks).getKillsRequired();


                sender.sendMessage(ChatColor.YELLOW + name + ChatColor.GRAY + " - " + ChatColor.WHITE + kills);
            }

            sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat('-', 53));
        }
    }

    @Command(names = {"killstreaks active", "ks active", "killstreak active"}, permission = "")
    public static void killstreaksActive(CommandSender sender) {
        if (!Hulu.getInstance().getMapHandler().isKitMap()) {
            sender.sendMessage(HuluLang.KIT_MAP_ONLY);
        } else {
            Map<Player, Integer> sorted = getSortedPlayers();

            if (sorted.isEmpty()) {
                sender.sendMessage(ChatColor.GRAY + "There is no data to display.");
            } else {
                sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat('-', 53));

                int index = 0;

                for (Map.Entry<Player, Integer> entry : getSortedPlayers().entrySet()) {
                    if (index > 10) {
                        break;
                    }

                    index++;

                    Team team = Hulu.getInstance().getTeamHandler().getTeam(entry.getKey());

                    FancyMessage playerMessage = new FancyMessage();
                    playerMessage.text(index + ". ").color(ChatColor.GRAY).then();
                    playerMessage.text(entry.getKey().getName()).color(sender instanceof Player && team != null && team.isMember(((Player) sender).getUniqueId()) ? ChatColor.GREEN : ChatColor.RED).then();
                    playerMessage.text(" - ").color(ChatColor.YELLOW).then();
                    playerMessage.text(entry.getValue() + " killstreak").color(ChatColor.GRAY);
                    playerMessage.send(sender);
                }

                sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat('-', 53));
            }
        }
    }

    // Stole this from the /f top command - thanks to whoever wrote it!
    public static LinkedHashMap<Player, Integer> getSortedPlayers() {
        Map<Player, Integer> playerKillstreaks = new HashMap<>();

        for (Player player : Bukkit.getOnlinePlayers()) {
            StatsEntry stats = Hulu.getInstance().getMapHandler().getStatsHandler().getStats(player.getUniqueId());

            if (stats.getKillstreak() > 0) {
                playerKillstreaks.put(player, stats.getKillstreak());
            }
        }

        return sortByValues(playerKillstreaks);
    }

    public static LinkedHashMap<Player, Integer> sortByValues(Map<Player, Integer> map) {
        LinkedList<Map.Entry<Player, Integer>> list = new LinkedList<>(map.entrySet());

        Collections.sort(list, (o1, o2) -> (o2.getValue().compareTo(o1.getValue())));

        LinkedHashMap<Player, Integer> sortedHashMap = new LinkedHashMap<>();

        for (Map.Entry<Player, Integer> entry : list) {
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }

        return (sortedHashMap);
    }

}
