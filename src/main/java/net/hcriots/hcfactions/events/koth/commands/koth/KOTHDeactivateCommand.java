/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.events.koth.commands.koth;

import cc.fyre.stark.engine.command.Command;
import cc.fyre.stark.engine.command.data.parameter.Param;
import net.hcriots.hcfactions.events.Event;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class KOTHDeactivateCommand {

    @Command(names = {"KOTH Deactivate", "KOTH Inactive", "event deactivate"}, permission = "foxtrot.koth.admin")
    public static void kothDectivate(CommandSender sender, @Param(name = "koth") Event koth) {
        koth.deactivate();
        sender.sendMessage(ChatColor.GRAY + "Deactivated " + koth.getName() + " event.");
    }

}
