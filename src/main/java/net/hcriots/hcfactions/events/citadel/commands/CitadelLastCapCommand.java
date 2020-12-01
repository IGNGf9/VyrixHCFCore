/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.events.citadel.commands;

import cc.fyre.stark.engine.command.Command;
import net.hcriots.hcfactions.Hulu;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CitadelLastCapCommand {

    @Command(names = {"citadel lastcap"}, permission = "op")
    public static void citadelLastCap(Player sender) {
        long capTime = Hulu.getInstance().getCitadelHandler().getCitadelCapTime();
        if (capTime != 0) {
            sender.sendMessage(ChatColor.YELLOW + "Last Capped: " + getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss", new Date()));
        } else {
            sender.sendMessage(ChatColor.RED + "I do not have a last cap time for Citadel.");
        }
    }

    public static String getCurrentTimeStamp(String format, Date date) {
        return new SimpleDateFormat(format).format(date);
    }
}
