/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.commands;

import cc.fyre.stark.engine.command.Command;
import cc.fyre.stark.engine.command.data.parameter.Param;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.listener.BorderListener;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

public class SetWorldBorderCommand {

    @Command(names = {"SetWorldBorder"}, permission = "op")
    public static void setWorldBorder(CommandSender sender, @Param(name = "border") int border) {
        BorderListener.BORDER_SIZE = border;
        sender.sendMessage(ChatColor.GRAY + "The world border is now set to " + BorderListener.BORDER_SIZE + " blocks.");

        new BukkitRunnable() {

            @Override
            public void run() {
                Hulu.getInstance().getMapHandler().saveBorder();
            }

        }.runTaskAsynchronously(Hulu.getInstance());
    }

}