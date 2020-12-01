/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.events.koth.commands.koth;

import cc.fyre.stark.engine.command.Command;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.events.Event;
import net.hcriots.hcfactions.events.EventScheduledTime;
import net.hcriots.hcfactions.events.EventType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class KOTHScheduleCommand {

    public static final DateFormat KOTH_DATE_FORMAT = new SimpleDateFormat("EEE h:mm a");

    // Make this pretty.
    @Command(names = {"KOTH Schedule"}, permission = "")
    public static void kothSchedule(Player sender) {
        int sent = 0;
        Date now = new Date();

        for (Map.Entry<EventScheduledTime, String> entry : Hulu.getInstance().getEventHandler().getEventSchedule().entrySet()) {
            Event resolved = Hulu.getInstance().getEventHandler().getEvent(entry.getValue());

            if (resolved == null || resolved.isHidden() || !entry.getKey().toDate().after(now) || resolved.getType() != EventType.KOTH) {
                continue;
            }

            if (sent > 5) {
                break;
            }

            sent++;
            sender.sendMessage(ChatColor.GOLD + "[KingOfTheHill] " + ChatColor.YELLOW + entry.getValue() + ChatColor.GOLD + " can be captured at " + ChatColor.BLUE + KOTH_DATE_FORMAT.format(entry.getKey().toDate()) + ChatColor.GOLD + ".");
        }

        if (sent == 0) {
            sender.sendMessage(ChatColor.GOLD + "[KingOfTheHill] " + ChatColor.RED + "KOTH Schedule: " + ChatColor.YELLOW + "Undefined");
        } else {
            sender.sendMessage(ChatColor.GOLD + "[KingOfTheHill] " + ChatColor.YELLOW + "It is currently " + ChatColor.BLUE + KOTH_DATE_FORMAT.format(new Date()) + ChatColor.GOLD + ".");
        }
    }

}