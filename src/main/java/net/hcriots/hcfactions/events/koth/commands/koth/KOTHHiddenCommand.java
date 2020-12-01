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
import org.bukkit.entity.Player;

public class KOTHHiddenCommand {

    @Command(names = {"KOTH Hidden", "events hidden", "event hidden"}, permission = "foxtrot.koth.admin")
    public static void kothHidden(Player sender, @Param(name = "koth") Event koth, @Param(name = "hidden") boolean hidden) {
        koth.setHidden(hidden);
        sender.sendMessage(ChatColor.GRAY + "Set visibility for the " + koth.getName() + " event.");
    }

}