/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.events.koth.commands.kothschedule;

import cc.fyre.stark.engine.command.Command;
import net.hcriots.hcfactions.Hulu;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class KOTHReloadSchedule {

    @Command(names = {"KOTHSchedule Reload"}, permission = "foxtrot.koth.admin")
    public static void kothScheduleReload(Player sender) {
        Hulu.getInstance().getEventHandler().loadSchedules();
        sender.sendMessage(ChatColor.GOLD + "[KingOfTheHill] " + ChatColor.YELLOW + "Reloaded the KOTH schedule.");
    }

}