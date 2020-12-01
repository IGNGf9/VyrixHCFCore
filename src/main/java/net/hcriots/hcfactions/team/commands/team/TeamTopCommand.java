/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.team.commands.team;

import cc.fyre.stark.engine.command.Command;
import mkremins.fanciful.FancyMessage;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.team.Team;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class TeamTopCommand {

    @Command(names = {"team top", "t top", "f top", "faction top", "fac top"}, permission = "")
    public static void teamList(final CommandSender sender) {
        // This is sort of intensive so we run it async (cause who doesn't love async!)
        new BukkitRunnable() {

            public void run() {
                LinkedHashMap<Team, Integer> sortedTeamPlayerCount = getSortedTeams();

                int index = 0;

                sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat('-', 53));
                sender.sendMessage(ChatColor.BLUE + "Top Teams");

                for (Map.Entry<Team, Integer> teamEntry : sortedTeamPlayerCount.entrySet()) {

                    index++;

                    if (10 <= index) {
                        break;
                    }

                    FancyMessage teamMessage = new FancyMessage();

                    Team team = teamEntry.getKey();

                    teamMessage.text(index + ". ").color(ChatColor.GRAY).then();
                    teamMessage.text(teamEntry.getKey().getName()).color(sender instanceof Player && teamEntry.getKey().isMember(((Player) sender).getUniqueId()) ? ChatColor.GREEN : ChatColor.RED)
                            .tooltip((sender instanceof Player && teamEntry.getKey().isMember(((Player) sender).getUniqueId()) ? ChatColor.GREEN : ChatColor.RED).toString() + teamEntry.getKey().getName() + "\n" +
                                    ChatColor.WHITE + "Balance: " + ChatColor.GOLD + "$" + team.getBalance() + "\n" +
                                    ChatColor.WHITE + "Kills: " + ChatColor.GOLD.toString() + team.getKills() + "\n" +
                                    ChatColor.WHITE + "Deaths: " + ChatColor.GOLD.toString() + team.getDeaths() + "\n\n" +
                                    ChatColor.WHITE + "KOTH Captures: " + ChatColor.GOLD.toString() + team.getKothCaptures() + "\n" +
                                    ChatColor.GREEN + "Click to view team info").command("/t who " + teamEntry.getKey().getName()).then();
                    teamMessage.text(" - ").color(ChatColor.YELLOW).then();
                    teamMessage.text(teamEntry.getValue().toString()).color(ChatColor.GRAY);

                    teamMessage.send(sender);
                }

                sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat('-', 53));
            }

        }.runTaskAsynchronously(Hulu.getInstance());
    }


    public static LinkedHashMap<Team, Integer> getSortedTeams() {
        Map<Team, Integer> teamPointsCount = new HashMap<>();

        // Sort of weird way of getting player counts, but it does it in the least iterations (1), which is what matters!
        for (Team team : Hulu.getInstance().getTeamHandler().getTeams()) {
            teamPointsCount.put(team, team.getPoints());
        }

        return sortByValues(teamPointsCount);
    }

    public static LinkedHashMap<Team, Integer> sortByValues(Map<Team, Integer> map) {
        LinkedList<Map.Entry<Team, Integer>> list = new LinkedList<>(map.entrySet());

        Collections.sort(list, (o1, o2) -> (o2.getValue().compareTo(o1.getValue())));

        LinkedHashMap<Team, Integer> sortedHashMap = new LinkedHashMap<>();

        for (Map.Entry<Team, Integer> entry : list) {
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }

        return (sortedHashMap);
    }

}