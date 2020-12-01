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

import java.util.concurrent.TimeUnit;

public class PvPCreateCommand {

    @Command(names = {"pvptimer create", "timer create", "pvp create", "pvp add", "pvp give"}, permission = "worldedit.*")
    public static void pvpCreate(Player sender, @Param(name = "player", defaultValue = "self") Player player) {
        Hulu.getInstance().getPvPTimerMap().createTimer(player.getUniqueId(), (int) TimeUnit.MINUTES.toSeconds(30));
        player.sendMessage(ChatColor.YELLOW + "You have 30 minutes of PVP Timer!");

        if (sender != player) {
            sender.sendMessage(ChatColor.YELLOW + "Gave 30 minutes of PVP Timer to " + player.getName() + ".");
        }
    }

}