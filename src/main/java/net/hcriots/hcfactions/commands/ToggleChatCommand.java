/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.commands;

import cc.fyre.stark.engine.command.Command;
import net.hcriots.hcfactions.Hulu;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ToggleChatCommand {

    @Command(names = {"ToggleChat", "ToggleGlobalChat", "TGC"}, permission = "")
    public static void toggleChat(Player sender) {
        boolean val = !Hulu.getInstance().getToggleGlobalChatMap().isGlobalChatToggled(sender.getUniqueId());

        sender.sendMessage(ChatColor.YELLOW + "You are now " + (!val ? ChatColor.RED + "unable" : ChatColor.GREEN + "able") + ChatColor.YELLOW + " to see global chat!");
        Hulu.getInstance().getToggleGlobalChatMap().setGlobalChatToggled(sender.getUniqueId(), val);
    }

}