/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.pvpclasses.pvpclasses.archer.commands;

import cc.fyre.stark.engine.command.Command;
import cc.fyre.stark.engine.command.data.parameter.Param;
import net.hcriots.hcfactions.Hulu;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Created by InspectMC
 * Date: 7/6/2020
 * Time: 3:50 PM
 */

public class SetArcherKillsCommand {

    @Command(names = "setarcherkills", permission = "op")
    public static void setArcherKills(Player player, @Param(name = "player") Player target, @Param(name = "kills") int kills) {
        Hulu.getInstance().getArcherKillsMap().setArcherKills(player.getUniqueId(), kills);
        player.sendMessage(ChatColor.GREEN + "You set " + target.getName() + "'s archer kills to: " + kills);
    }

}