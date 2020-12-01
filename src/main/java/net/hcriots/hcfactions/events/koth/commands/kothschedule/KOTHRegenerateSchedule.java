/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.events.koth.commands.kothschedule;

import cc.fyre.stark.engine.command.Command;
import net.hcriots.hcfactions.Hulu;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.io.File;

public class KOTHRegenerateSchedule {

    @Command(names = {"KOTHSchedule Regenerate", "KOTHSchedule Regen"}, permission = "foxtrot.koth.admin", async = true)
    public static void kothScheduleEnable(CommandSender sender) {
        File kothSchedule = new File(Hulu.getInstance().getDataFolder(), "eventSchedule.json");

        if (kothSchedule.delete()) {
            Hulu.getInstance().getEventHandler().loadSchedules();

            sender.sendMessage(ChatColor.YELLOW + "The event schedule has been regenerated.");
        } else {
            sender.sendMessage(ChatColor.RED + "Couldn't delete event schedule file.");
        }
    }

    @Command(names = {"KOTHSchedule debug"}, permission = "op")
    public static void kothScheduleDebug(CommandSender sender) {
        Hulu.getInstance().getEventHandler().fillSchedule();
        sender.sendMessage(ChatColor.GREEN + "The event schedule has been filled.");
    }
}
