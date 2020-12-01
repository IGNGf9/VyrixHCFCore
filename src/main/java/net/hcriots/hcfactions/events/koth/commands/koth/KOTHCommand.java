/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.events.koth.commands.koth;

import cc.fyre.stark.engine.command.Command;
import mkremins.fanciful.FancyMessage;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.events.Event;
import net.hcriots.hcfactions.events.EventScheduledTime;
import net.hcriots.hcfactions.events.koth.KOTH;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.Map;

import static org.bukkit.ChatColor.*;

public class KOTHCommand {

    // Make this pretty.
    @Command(names = {"Event", "Event Next", "Event Info", "Event", "koth", "koth next", "koth info"}, permission = "")
    public static void koth(Player sender) {
        for (Event koth : Hulu.getInstance().getEventHandler().getEvents()) {
            if (!koth.isHidden() && koth.isActive()) {
                FancyMessage fm = new FancyMessage("[Events] ")
                        .color(GOLD)
                        .then(koth.getName())
                        .color(YELLOW) // koth name should be yellow
                        .style(UNDERLINE);
                if (koth instanceof KOTH) {
                    fm.tooltip(YELLOW.toString() + ((KOTH) koth).getCapLocation().getBlockX() + ", " + ((KOTH) koth).getCapLocation().getBlockZ());
                }
                fm.color(YELLOW) // should color Event coords gray
                        .then(" can be contested now.")
                        .color(GOLD);
                fm.send(sender);
                return;
            }
        }

        Date now = new Date();

        for (Map.Entry<EventScheduledTime, String> entry : Hulu.getInstance().getEventHandler().getEventSchedule().entrySet()) {
            if (entry.getKey().toDate().after(now)) {
                sender.sendMessage(GOLD + "[KingOfTheHill] " + YELLOW + entry.getValue() + GOLD + " can be captured at " + BLUE + KOTHScheduleCommand.KOTH_DATE_FORMAT.format(entry.getKey().toDate()) + GOLD + ".");
                sender.sendMessage(GOLD + "[KingOfTheHill] " + YELLOW + "It is currently " + BLUE + KOTHScheduleCommand.KOTH_DATE_FORMAT.format(now) + GOLD + ".");
                sender.sendMessage(YELLOW + "Type '/koth schedule' to see more upcoming Events.");
                return;
            }
        }

        sender.sendMessage(GOLD + "[KingOfTheHill] " + RED + "Next Event: " + YELLOW + "Undefined");
    }

}