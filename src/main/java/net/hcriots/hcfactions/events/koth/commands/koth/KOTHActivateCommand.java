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

public class KOTHActivateCommand {

    @Command(names = {"KOTH Activate", "KOTH Active", "events activate"}, permission = "foxtrot.activatekoth")
    public static void kothActivate(Player sender, @Param(name = "event") Event koth) {
        // Don't start a KOTH if another one is active.
        for (Event otherKoth : Hulu.getInstance().getEventHandler().getEvents()) {
            if (otherKoth.isActive()) {
                sender.sendMessage(ChatColor.RED + otherKoth.getName() + " is currently active.");
                return;
            }
        }

        if ((koth.getName().equalsIgnoreCase("citadel") || koth.getName().toLowerCase().contains("conquest")) && !sender.isOp()) {
            sender.sendMessage(ChatColor.RED + "Only ops can use the activate command for weekend events.");
            return;
        }

        koth.activate();
        sender.sendMessage(ChatColor.GRAY + "Activated " + koth.getName() + ".");
    }

}
