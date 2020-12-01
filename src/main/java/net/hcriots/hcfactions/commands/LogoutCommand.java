/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.commands;

import cc.fyre.stark.engine.command.Command;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.server.ServerHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class LogoutCommand {

    @Command(names = {"Logout"}, permission = "")
    public static void logout(Player sender) {
        if (sender.hasMetadata("frozen")) {
            sender.sendMessage(ChatColor.RED + "You can't log out while you're frozen!");
            return;
        }

        if (ServerHandler.getTasks().containsKey(sender.getName())) {
            sender.sendMessage(ChatColor.RED + "You are already logging out.");
            return; // dont potato and let them spam logouts
        }

        Hulu.getInstance().getServerHandler().startLogoutSequence(sender);
    }

}