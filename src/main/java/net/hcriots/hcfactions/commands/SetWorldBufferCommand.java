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
import org.bukkit.scheduler.BukkitRunnable;

public class SetWorldBufferCommand {

    @Command(names = {"SetWorldBuffer"}, permission = "op")
    public static void setWorldBuffer(Player sender, @Param(name = "worldBuffer") int newBuffer) {
        Hulu.getInstance().getMapHandler().setWorldBuffer(newBuffer);
        sender.sendMessage(ChatColor.GRAY + "The world buffer is now set to " + newBuffer + " blocks.");

        new BukkitRunnable() {

            @Override
            public void run() {
                Hulu.getInstance().getMapHandler().saveWorldBuffer();
            }

        }.runTaskAsynchronously(Hulu.getInstance());
    }

}
