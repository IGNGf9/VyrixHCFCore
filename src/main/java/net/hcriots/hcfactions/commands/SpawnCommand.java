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

public class SpawnCommand {

    @Command(names = {"spawn"}, permission = "")
    public static void spawn(Player sender) {
        if (sender.hasPermission("foxtrot.spawn")) {
            sender.teleport(Hulu.getInstance().getServerHandler().getSpawnLocation());
        } else {
            // Make this pretty.
            sender.sendMessage(ChatColor.RED + "You must walk to the server spawn location!");
            sender.sendMessage(ChatColor.RED + "Spawn is located at 0,0.");
        }
    }

}
