/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.elo.command;

import cc.fyre.stark.engine.command.Command;
import mkremins.fanciful.FancyMessage;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.elo.EloHandler;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

/**
 * Created by InspectMC
 * Date: 6/23/2020
 * Time: 3:35 AM
 */
public class EloTopCommand {

    @Command(names = {"topelo", "elo top", "elotop"}, permission = "")
    public static void topElo(CommandSender sender) {

        // This is sort of intensive so we run it async (cause who doesn't love async!)
        new BukkitRunnable() {

            public void run() {
                LinkedHashMap<Player, Integer> sortedPlayers = getSortedPlayers();
                int index = 0;

                sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat('-', 53));
                sender.sendMessage(ChatColor.BLUE + "Top Elo");

                for (Map.Entry<Player, Integer> playerEntry : sortedPlayers.entrySet()) {
                    if (playerEntry.getKey().getDisplayName() == null) {
                        continue;
                    }

                    index++;

                    if (10 <= index) {
                        break;
                    }

                    FancyMessage playerMessage = new FancyMessage();
                    Player player = playerEntry.getKey();
                    playerMessage.text(index + ". ").color(ChatColor.GRAY).then();
                    playerMessage.text(player.getDisplayName()).color(ChatColor.LIGHT_PURPLE).then();
                    playerMessage.text(" - ").color(ChatColor.YELLOW).then();
                    playerMessage.text(playerEntry.getValue().toString()).color(ChatColor.GRAY);
                    playerMessage.send(sender);
                }

                sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat('-', 53));
            }
        }.runTaskAsynchronously(Hulu.getInstance());
    }


    // Stole this from the /f top command - thanks to whoever wrote it!
    public static LinkedHashMap<Player, Integer> getSortedPlayers() {
        Map<Player, Integer> playersELO = new HashMap<>();

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (EloHandler.getPlayerELO(player) > 0) {
                playersELO.put(player, EloHandler.getPlayerELO(player));
            }
        }

        return sortByValues(playersELO);
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
