/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.events.koth.commands.koth;

import cc.fyre.stark.engine.command.Command;
import cc.fyre.stark.engine.command.data.parameter.Param;
import net.hcriots.hcfactions.events.koth.KOTH;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class KOTHCreateCommand {

    @Command(names = {"KOTH Create"}, permission = "foxtrot.koth.admin")
    public static void kothCreate(Player sender, @Param(name = "koth") String koth) {
        new KOTH(koth, sender.getLocation());
        sender.sendMessage(ChatColor.GRAY + "Created a KOTH named " + koth + ".");
    }

}