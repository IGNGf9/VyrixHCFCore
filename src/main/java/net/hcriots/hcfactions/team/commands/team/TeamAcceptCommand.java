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
import net.hcriots.hcfactions.server.SpawnTagHandler;
import net.hcriots.hcfactions.team.Team;
import net.hcriots.hcfactions.team.dtr.DTRHandler;
import net.hcriots.hcfactions.team.event.FullTeamBypassEvent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TeamAcceptCommand {

    @Command(names = {"team accept", "t accept", "f accept", "faction accept", "fac accept", "team a", "t a", "f a", "faction a", "fac a", "team join", "t join", "f join", "faction join", "fac join", "team j", "t j", "f j", "faction j", "fac j"}, permission = "")
    public static void teamAccept(Player sender, @Param(name = "team") Team team) {
        if (team.getInvitations().contains(sender.getUniqueId())) {
            if (Hulu.getInstance().getTeamHandler().getTeam(sender) != null) {
                sender.sendMessage(ChatColor.RED + "You are already on a team!");
                return;
            }

            if (team.getMembers().size() >= Hulu.getInstance().getMapHandler().getTeamSize()) {
                FullTeamBypassEvent attemptEvent = new FullTeamBypassEvent(sender, team);
                Hulu.getInstance().getServer().getPluginManager().callEvent(attemptEvent);

                if (!attemptEvent.isAllowBypass()) {
                    sender.sendMessage(ChatColor.RED + team.getName() + " cannot be joined: Team is full!");
                    return;
                }
            }

            if (DTRHandler.isOnCooldown(team) && !Hulu.getInstance().getServerHandler().isPreEOTW() && !Hulu.getInstance().getMapHandler().isKitMap()) {
                sender.sendMessage(ChatColor.RED + team.getName() + " cannot be joined: Team not regenerating DTR!");
                return;
            }

            if (team.getMembers().size() >= 15 && Hulu.getInstance().getTeamHandler().isRostersLocked()) {
                sender.sendMessage(ChatColor.RED + team.getName() + " cannot be joined: Team rosters are locked server-wide!");
                return;
            }

            if (SpawnTagHandler.isTagged(sender)) {
                sender.sendMessage(ChatColor.RED + team.getName() + " cannot be joined: You are combat tagged!");
                return;
            }

            team.getInvitations().remove(sender.getUniqueId());
            team.addMember(sender.getUniqueId());
            Hulu.getInstance().getTeamHandler().setTeam(sender.getUniqueId(), team);

            team.sendMessage(ChatColor.YELLOW + sender.getName() + " has joined the team!");

            Stark.instance.getNametagEngine().reloadPlayer(sender);
            Stark.instance.getNametagEngine().reloadOthersFor(sender);
        } else {
            sender.sendMessage(ChatColor.RED + "This team has not invited you!");
        }
    }

}