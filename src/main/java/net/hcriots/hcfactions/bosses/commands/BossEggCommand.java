/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.bosses.commands;

import cc.fyre.stark.engine.command.Command;
import net.hcriots.hcfactions.bosses.menu.BossEggsMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by InspectMC
 * Date: 7/21/2020
 * Time: 2:36 PM
 */
public class BossEggCommand {

    @Command(names = {"boss"}, permission = "")
    public static void bossEgg(CommandSender sender) {
        Player player = (Player) sender;
        new BossEggsMenu().openMenu(player);
    }
}
