/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.commands;

import cc.fyre.stark.engine.command.Command;
import cc.fyre.stark.engine.command.data.parameter.Param;
import net.hcriots.hcfactions.Hulu;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class GoppleResetCommand {

    @Command(names = {"GoppleReset"}, permission = "foxtrot.gopplereset")
    public static void goppleReset(Player sender, @Param(name = "player") UUID player) {
        Hulu.getInstance().getOppleMap().resetCooldown(player);
        sender.sendMessage(ChatColor.RED + "Cooldown reset!");
    }

}