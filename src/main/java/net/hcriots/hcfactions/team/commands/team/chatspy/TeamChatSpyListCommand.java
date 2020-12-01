/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.team.commands.team.chatspy;

import cc.fyre.stark.engine.command.Command;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.team.Team;
import org.bson.types.ObjectId;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TeamChatSpyListCommand {

    @Command(names = {"team chatspy list", "t chatspy list", "f chatspy list", "faction chatspy list", "fac chatspy list"}, permission = "foxtrot.chatspy")
    public static void teamChatSpyList(Player sender) {
        StringBuilder stringBuilder = new StringBuilder();

        for (ObjectId team : Hulu.getInstance().getChatSpyMap().getChatSpy(sender.getUniqueId())) {
            Team teamObj = Hulu.getInstance().getTeamHandler().getTeam(team);

            if (teamObj != null) {
                stringBuilder.append(ChatColor.YELLOW).append(teamObj.getName()).append(ChatColor.GOLD).append(", ");
            }
        }

        if (stringBuilder.length() > 2) {
            stringBuilder.setLength(stringBuilder.length() - 2);
        }

        sender.sendMessage(ChatColor.GOLD + "You are currently spying on the team chat of: " + stringBuilder.toString());
    }

}