/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.team.commands.pvp;

import cc.fyre.stark.Stark;
import cc.fyre.stark.engine.command.Command;
import cc.fyre.stark.engine.command.data.parameter.Param;
import net.hcriots.hcfactions.Hulu;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.UUID;

public class PvPLivesCommand {

    @Command(names = {"pvptimer lives", "timer lives", "pvp lives"}, permission = "")
    public static void pvpLives(CommandSender sender, @Param(name = "player", defaultValue = "self") UUID player) {
        String name = Stark.instance.getCore().getUuidCache().name(player);

        sender.sendMessage(ChatColor.GOLD + name + "'s Friend Lives: " + ChatColor.WHITE + Hulu.getInstance().getFriendLivesMap().getLives(player));
    }

}