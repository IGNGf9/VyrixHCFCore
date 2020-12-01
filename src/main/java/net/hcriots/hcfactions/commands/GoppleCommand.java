/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.commands;

import cc.fyre.stark.core.util.TimeUtils;
import cc.fyre.stark.engine.command.Command;
import net.hcriots.hcfactions.Hulu;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class GoppleCommand {

    @Command(names = {"Gopple", "Opple", "GoppleTime", "OppleTime", "GoppleTimer", "OppleTimer"}, permission = "")
    public static void gopple(Player sender) {
        if (Hulu.getInstance().getOppleMap().isOnCooldown(sender.getUniqueId())) {
            long millisLeft = Hulu.getInstance().getOppleMap().getCooldown(sender.getUniqueId()) - System.currentTimeMillis();
            sender.sendMessage(ChatColor.GOLD + "Gopple cooldown: " + ChatColor.WHITE + TimeUtils.formatIntoDetailedString((int) millisLeft / 1000));
        } else {
            sender.sendMessage(ChatColor.RED + "No current gopple cooldown!");
        }
    }

}