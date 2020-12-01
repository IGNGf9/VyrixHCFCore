/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.staffmode.command;

import cc.fyre.stark.engine.command.Command;
import net.hcriots.hcfactions.staffmode.StaffModeHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StaffModeCommand {

    @Command(names = {"staff", "h", "staffmode", "modmode", "mod"}, permission = "stark.staff")
    public static void execute(CommandSender sender) {
        Player player = (Player) sender;
        StaffModeHandler staffMode = StaffModeHandler.getStaffModeMap().get(sender);

        sender.sendMessage(ChatColor.GOLD + "Staff Mode: " + (!StaffModeHandler.hasStaffMode(player) ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled"));

        if (StaffModeHandler.hasStaffMode(player)) {
            staffMode.remove();
            return;
        }

        StaffModeHandler.getStaffModeMap().put(player, new StaffModeHandler(player));
    }
}