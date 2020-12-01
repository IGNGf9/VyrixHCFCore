/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.commands;

import cc.fyre.stark.engine.command.Command;
import net.hcriots.hcfactions.listener.EndListener;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ToggleEndCommand {

    @Command(names = {"ToggleEnd"}, permission = "foxtrot.toggleend")
    public static void toggleEnd(Player sender) {
        EndListener.endActive = !EndListener.endActive;
        sender.sendMessage(ChatColor.YELLOW + "End enabled? " + ChatColor.LIGHT_PURPLE + (EndListener.endActive ? "Yes" : "No"));
    }

}