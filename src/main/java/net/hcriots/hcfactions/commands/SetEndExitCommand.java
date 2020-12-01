/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.commands;

import cc.fyre.stark.engine.command.Command;
import net.hcriots.hcfactions.listener.EndListener;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SetEndExitCommand {

    @Command(names = {"setendexit"}, permission = "op")
    public static void setendexit(Player sender) {
        Location previous = EndListener.getEndReturn();
        EndListener.setEndReturn(sender.getLocation());
        Location current = EndListener.getEndReturn();

        sender.sendMessage(
                ChatColor.GREEN + "End exit (" + ChatColor.WHITE + previous.getBlockX() + ":" + previous.getBlockY() + ":" + previous.getBlockZ() + ChatColor.GREEN + " -> " +
                        ChatColor.WHITE + current.getBlockX() + ":" + current.getBlockY() + ":" + current.getBlockZ() + ChatColor.GREEN + ")"
        );

        EndListener.saveEndReturn();
    }

}