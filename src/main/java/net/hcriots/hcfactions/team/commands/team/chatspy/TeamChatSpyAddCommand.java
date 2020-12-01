/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.team.commands.team.chatspy;

import cc.fyre.stark.engine.command.Command;
import cc.fyre.stark.engine.command.data.parameter.Param;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.team.Team;
import org.bson.types.ObjectId;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TeamChatSpyAddCommand {

    @Command(names = {"team chatspy add", "t chatspy add", "f chatspy add", "faction chatspy add", "fac chatspy add"}, permission = "foxtrot.chatspy")
    public static void teamChatSpyAdd(Player sender, @Param(name = "team") Team team) {
        if (Hulu.getInstance().getChatSpyMap().getChatSpy(sender.getUniqueId()).contains(team.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + "You are already spying on " + team.getName() + ".");
            return;
        }

        List<ObjectId> teams = new ArrayList<>(Hulu.getInstance().getChatSpyMap().getChatSpy(sender.getUniqueId()));

        teams.add(team.getUniqueId());

        Hulu.getInstance().getChatSpyMap().setChatSpy(sender.getUniqueId(), teams);
        sender.sendMessage(ChatColor.GREEN + "You are now spying on the chat of " + ChatColor.YELLOW + team.getName() + ChatColor.GREEN + ".");
    }

}