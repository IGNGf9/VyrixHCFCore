/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.team.commands;

import cc.fyre.stark.engine.command.Command;
import cc.fyre.stark.engine.command.data.parameter.Param;
import net.hcriots.hcfactions.team.Team;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class StartDTRRegenCommand {

    @Command(names = {"startdtrregen"}, permission = "foxtrot.startdtrregen")
    public static void startDTRRegen(Player sender, @Param(name = "team") Team team) {
        team.setDTRCooldown(System.currentTimeMillis());
        sender.sendMessage(ChatColor.LIGHT_PURPLE + team.getName() + ChatColor.YELLOW + " is now regenerating DTR.");
    }

}