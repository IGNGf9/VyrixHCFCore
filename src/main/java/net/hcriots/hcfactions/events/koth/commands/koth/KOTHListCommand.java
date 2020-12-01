/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.events.koth.commands.koth;

import cc.fyre.stark.core.util.TimeUtils;
import cc.fyre.stark.engine.command.Command;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.events.Event;
import net.hcriots.hcfactions.events.EventType;
import net.hcriots.hcfactions.events.dtc.DTC;
import net.hcriots.hcfactions.events.koth.KOTH;
import org.bukkit.entity.Player;

import static org.bukkit.ChatColor.*;

public class KOTHListCommand {

    @Command(names = {"KOTH List", "events list", "event list"}, permission = "foxtrot.koth")
    public static void kothList(Player sender) {
        if (Hulu.getInstance().getEventHandler().getEvents().isEmpty()) {
            sender.sendMessage(RED + "There aren't any events set.");
            return;
        }

        for (Event event : Hulu.getInstance().getEventHandler().getEvents()) {
            if (event.getType() == EventType.KOTH) {
                KOTH koth = (KOTH) event;
                sender.sendMessage((koth.isHidden() ? DARK_GRAY + "[H] " : "") + (koth.isActive() ? GREEN : RED) + koth.getName() + WHITE + " - " + GRAY + TimeUtils.formatIntoMMSS(koth.getRemainingCapTime()) + DARK_GRAY + "/" + GRAY + TimeUtils.formatIntoMMSS(koth.getCapTime()) + " " + WHITE + "- " + GRAY + (koth.getCurrentCapper() == null ? "None" : koth.getCurrentCapper()));
            } else if (event.getType() == EventType.DTC) {
                DTC dtc = (DTC) event;
                sender.sendMessage((dtc.isHidden() ? DARK_GRAY + "[H] " : "") + (dtc.isActive() ? GREEN : RED) + dtc.getName() + WHITE + " - " + GRAY + "P: " + dtc.getCurrentPoints());
            }
        }
    }

}