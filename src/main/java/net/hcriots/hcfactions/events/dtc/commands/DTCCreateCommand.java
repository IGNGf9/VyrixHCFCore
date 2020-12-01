/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.events.dtc.commands;

import cc.fyre.stark.engine.command.Command;
import cc.fyre.stark.engine.command.data.parameter.Param;
import net.hcriots.hcfactions.events.dtc.DTC;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class DTCCreateCommand {

    @Command(names = {"DTC Create"}, permission = "foxtrot.dtc.admin")
    public static void kothCreate(Player sender, @Param(name = "dtc") String koth) {
        new DTC(koth, sender.getLocation());
        sender.sendMessage(ChatColor.GRAY + "Created a DTC named " + koth + ".");
    }

}
