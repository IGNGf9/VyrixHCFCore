/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.events.koth.commands.koth;

import cc.fyre.stark.engine.command.Command;
import cc.fyre.stark.engine.command.data.parameter.Param;
import net.hcriots.hcfactions.events.Event;
import net.hcriots.hcfactions.events.EventType;
import net.hcriots.hcfactions.events.koth.KOTH;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class KOTHDistCommand {

    @Command(names = {"KOTH Dist"}, permission = "foxtrot.koth.admin")
    public static void kothDist(Player sender, @Param(name = "koth") Event koth, @Param(name = "distance") int distance) {
        if (koth.getType() != EventType.KOTH) {
            sender.sendMessage(ChatColor.RED + "Can only set distance for KOTHs");
            return;
        }

        ((KOTH) koth).setCapDistance(distance);
        sender.sendMessage(ChatColor.GRAY + "Set max distance for the " + koth.getName() + " KOTH.");
    }

}