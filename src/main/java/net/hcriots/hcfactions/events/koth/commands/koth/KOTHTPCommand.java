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
import net.hcriots.hcfactions.events.EventType;
import net.hcriots.hcfactions.events.koth.KOTH;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class KOTHTPCommand {

    @Command(names = {"KOTH TP", "KOTHTP", "events tp", "event tp"}, permission = "foxtrot.koth")
    public static void kothTP(Player sender, @Param(name = "koth", defaultValue = "active") Event koth) {
        if (koth.getType() == EventType.KOTH) {
            sender.teleport(((KOTH) koth).getCapLocation().toLocation(Hulu.getInstance().getServer().getWorld(((KOTH) koth).getWorld())));
            sender.sendMessage(ChatColor.GRAY + "Teleported to the " + koth.getName() + " KOTH.");
        } else if (koth.getType() == EventType.DTC) {
            sender.teleport(((KOTH) koth).getCapLocation().toLocation(Hulu.getInstance().getServer().getWorld(((KOTH) koth).getWorld())));
            sender.sendMessage(ChatColor.GRAY + "Teleported to the " + koth.getName() + " DTC.");
        }

        sender.sendMessage(ChatColor.RED + "You can't TP to an event that doesn't have a location.");
    }

}