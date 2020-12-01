/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.team.commands.team;

import cc.fyre.stark.Stark;
import cc.fyre.stark.engine.command.Command;
import cc.fyre.stark.engine.command.data.parameter.Param;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.team.Team;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TeamAllyCommand {

    @Command(names = {"team ally", "t ally", "f ally", "faction ally", "fac ally"}, permission = "")
    public static void teamAlly(Player sender, @Param(name = "team") Team team) {
        Team senderTeam = Hulu.getInstance().getTeamHandler().getTeam(sender);

        if (senderTeam == null) {
            sender.sendMessage(ChatColor.GRAY + "You are not on a team!");
            return;
        }

        if (!(senderTeam.isOwner(sender.getUniqueId()) || senderTeam.isCaptain(sender.getUniqueId()) || senderTeam.isCoLeader(sender.getUniqueId()))) {
            sender.sendMessage(ChatColor.DARK_AQUA + "Only team captains can do this.");
            return;
        }

        if (senderTeam.equals(team)) {
            sender.sendMessage(ChatColor.YELLOW + "You cannot ally your own team!");
            return;
        }

        if (senderTeam.getAllies().size() >= Hulu.getInstance().getMapHandler().getAllyLimit()) {
            sender.sendMessage(ChatColor.YELLOW + "Your team already has the max number of allies, which is " + Hulu.getInstance().getMapHandler().getAllyLimit() + ".");
            return;
        }

        if (team.getAllies().size() >= Hulu.getInstance().getMapHandler().getAllyLimit()) {
            sender.sendMessage(ChatColor.YELLOW + "The team you're trying to ally already has the max number of allies, which is " + Hulu.getInstance().getMapHandler().getAllyLimit() + ".");
            return;
        }

        if (senderTeam.isAlly(team)) {
            sender.sendMessage(ChatColor.YELLOW + "You're already allied to " + team.getName(sender) + ChatColor.YELLOW + ".");
            return;
        }

        if (senderTeam.getRequestedAllies().contains(team.getUniqueId())) {
            senderTeam.getRequestedAllies().remove(team.getUniqueId());

            team.getAllies().add(senderTeam.getUniqueId());
            senderTeam.getAllies().add(team.getUniqueId());

            team.flagForSave();
            senderTeam.flagForSave();

            for (Player player : Hulu.getInstance().getServer().getOnlinePlayers()) {
                if (team.isMember(player.getUniqueId())) {
                    player.sendMessage(senderTeam.getName(player) + ChatColor.YELLOW + " has accepted your request to ally. You now have " + Team.ALLY_COLOR + team.getAllies().size() + "/" + Hulu.getInstance().getMapHandler().getAllyLimit() + " allies" + ChatColor.YELLOW + ".");
                } else if (senderTeam.isMember(player.getUniqueId())) {
                    player.sendMessage(ChatColor.YELLOW + "Your team has allied " + team.getName(sender) + ChatColor.YELLOW + ". You now have " + Team.ALLY_COLOR + senderTeam.getAllies().size() + "/" + Hulu.getInstance().getMapHandler().getAllyLimit() + " allies" + ChatColor.YELLOW + ".");
                }

                if (team.isMember(player.getUniqueId()) || senderTeam.isMember(player.getUniqueId())) {
                    Stark.instance.getNametagEngine().reloadPlayer(sender);
                    Stark.instance.getNametagEngine().reloadOthersFor(sender);
                }
            }
        } else {
            if (team.getRequestedAllies().contains(senderTeam.getUniqueId())) {
                sender.sendMessage(ChatColor.YELLOW + "You have already requested to ally " + team.getName(sender) + ChatColor.YELLOW + ".");
                return;
            }

            team.getRequestedAllies().add(senderTeam.getUniqueId());
            team.flagForSave();

            for (Player player : Hulu.getInstance().getServer().getOnlinePlayers()) {
                if (team.isMember(player.getUniqueId())) {
                    player.sendMessage(senderTeam.getName(player.getPlayer()) + ChatColor.YELLOW + " has requested to be your ally. Type " + Team.ALLY_COLOR + "/team ally " + senderTeam.getName() + ChatColor.YELLOW + " to accept.");
                } else if (senderTeam.isMember(player.getUniqueId())) {
                    player.sendMessage(ChatColor.YELLOW + "Your team has requested to ally " + team.getName(player) + ChatColor.YELLOW + ".");
                }
            }
        }
    }

}