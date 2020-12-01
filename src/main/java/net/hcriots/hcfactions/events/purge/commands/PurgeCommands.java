/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.events.purge.commands;

import cc.fyre.stark.core.util.TimeUtils;
import cc.fyre.stark.engine.command.Command;
import cc.fyre.stark.engine.command.data.parameter.Param;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by InspectMC
 * Date: 8/13/2020
 * Time: 3:29 PM
 */
public class PurgeCommands {

    @Getter
    private static final Map<String, Long> customTimers = new HashMap<>();


    @Command(names = "purge start", permission = "hulu.purge")
    public static void sotwStart(CommandSender sender, @Param(name = "time") String time) {
        int seconds = TimeUtils.parseTime(time);
        if (seconds < 0) {
            sender.sendMessage(ChatColor.RED + "Invalid time!");
            return;
        }


        customTimers.put("&4&lPurge", System.currentTimeMillis() + (seconds * 1000));
        sender.sendMessage(ChatColor.GREEN + "Started the Purge Event for " + time);
    }

    @Command(names = {"purge cancel"}, permission = "hulu.purge")
    public static void purgeCancel(CommandSender sender) {
        Long removed = customTimers.remove("&4&lPurge");
        if (removed != null && System.currentTimeMillis() < removed) {
            sender.sendMessage(ChatColor.GREEN + "Deactivated the Purge Event.");
            return;
        }

        sender.sendMessage(ChatColor.RED + "Pure Event is not active.");
    }


    public static boolean isPurgeTimer() {
        return customTimers.containsKey("&4&lPurge");
    }
}
