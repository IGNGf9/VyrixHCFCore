/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.commands;

import cc.fyre.stark.Stark;
import cc.fyre.stark.engine.command.Command;
import net.hcriots.hcfactions.Hulu;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class PanicCommand {

    @Command(names = {"panic", "p"}, permission = "foxtrot.panic")
    public static void panic(Player sender) {
        if (sender.hasMetadata("frozen")) {
            Stark.instance.getServerHandler().unfreeze(sender.getUniqueId());
        } else {
            new BukkitRunnable() {

                public void run() {
                    if (!sender.isOnline() || !sender.hasMetadata("frozen")) {
                        cancel();
                        return;
                    }

                    for (Player player : Hulu.getInstance().getServer().getOnlinePlayers()) {
                        if (player.hasPermission("stark.staff")) {
                            player.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "Panic! " + sender.getDisplayName() + ChatColor.YELLOW + " has activated the panic feature!");
                        }
                    }
                }

            }.runTaskTimer(Hulu.getInstance(), 1L, 15 * 20L);

            Stark.instance.getServerHandler().freeze(sender);
        }
    }

}