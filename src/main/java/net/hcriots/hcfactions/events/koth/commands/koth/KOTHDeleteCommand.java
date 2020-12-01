/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.events.koth.commands.koth;

import cc.fyre.stark.engine.command.Command;
import cc.fyre.stark.engine.command.data.parameter.Param;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.events.Event;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class KOTHDeleteCommand {

    @Command(names = {"KOTH Delete", "events delete", "event delete"}, permission = "foxtrot.koth.admin")
    public static void kothDelete(Player sender, @Param(name = "koth") Event koth) {
        Hulu.getInstance().getEventHandler().getEvents().remove(koth);
        Hulu.getInstance().getEventHandler().saveEvents();
        sender.sendMessage(ChatColor.GRAY + "Deleted event " + koth.getName() + ".");
    }

}