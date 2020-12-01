/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.commands;

import cc.fyre.stark.engine.command.Command;
import net.hcriots.hcfactions.Hulu;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ReloadMapConfigCommand {

    @Command(names = {"reloadMapConfig"}, permission = "op")
    public static void reloadMapConfig(Player sender) {
        Hulu.getInstance().getMapHandler().reloadConfig();
        sender.sendMessage(ChatColor.DARK_PURPLE + "Reloaded mapInfo.json from file.");
    }

}