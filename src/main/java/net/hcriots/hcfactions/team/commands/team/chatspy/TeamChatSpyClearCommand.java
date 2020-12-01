/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.team.commands.team.chatspy;

import cc.fyre.stark.engine.command.Command;
import net.hcriots.hcfactions.Hulu;
import org.bson.types.ObjectId;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class TeamChatSpyClearCommand {

    @Command(names = {"team chatspy clear", "t chatspy clear", "f chatspy clear", "faction chatspy clear", "fac chatspy clear"}, permission = "foxtrot.chatspy")
    public static void teamChatSpyClear(Player sender) {
        Hulu.getInstance().getChatSpyMap().setChatSpy(sender.getUniqueId(), new ArrayList<ObjectId>());
        sender.sendMessage(ChatColor.GREEN + "You are no longer spying on any teams.");
    }

}