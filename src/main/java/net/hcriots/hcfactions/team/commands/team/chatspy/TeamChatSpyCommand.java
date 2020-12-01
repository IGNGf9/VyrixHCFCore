/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.team.commands.team.chatspy;

import cc.fyre.stark.engine.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TeamChatSpyCommand {

    @Command(names = {"team chatspy", "t chatspy", "f chatspy", "faction chatspy", "fac chatspy"}, permission = "foxtrot.chatspy")
    public static void teamChatSpy(Player sender) {
        sender.sendMessage(ChatColor.RED + "/team chatspy list - views teams who you are spying on");
        sender.sendMessage(ChatColor.RED + "/team chatspy add - starts spying on a team");
        sender.sendMessage(ChatColor.RED + "/team chatspy del - stops spying on a team");
        sender.sendMessage(ChatColor.RED + "/team chatspy clear - stops spying on all teams");
    }

}