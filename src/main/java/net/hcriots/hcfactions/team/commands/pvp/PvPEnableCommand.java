/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.team.commands.pvp;

import cc.fyre.stark.engine.command.Command;
import cc.fyre.stark.engine.command.data.parameter.Param;
import net.hcriots.hcfactions.Hulu;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PvPEnableCommand {

    @Command(names = {"pvptimer enable", "timer enable", "pvp enable", "pvptimer remove", "timer remove", "pvp remove"}, permission = "")
    public static void pvpEnable(Player sender, @Param(name = "target", defaultValue = "self") Player target) {
        if (target != sender && !sender.isOp()) {
            sender.sendMessage(ChatColor.RED + "No permission.");
            return;
        }

        if (Hulu.getInstance().getPvPTimerMap().hasTimer(target.getUniqueId())) {
            Hulu.getInstance().getPvPTimerMap().removeTimer(target.getUniqueId());

            if (target == sender) {
                sender.sendMessage(ChatColor.RED + "Your PvP Timer has been removed!");
            } else {
                sender.sendMessage(ChatColor.RED + target.getName() + "'s PvP Timer has been removed!");
            }
        } else {
            if (target == sender) {
                sender.sendMessage(ChatColor.RED + "You do not have a PvP Timer!");
            } else {
                sender.sendMessage(ChatColor.RED + target.getName() + " does not have a PvP Timer.");
            }
        }
    }

}