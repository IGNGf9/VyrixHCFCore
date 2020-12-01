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

public class CobbleCommand {

    @Command(names = {"cobble", "cobblestone"}, permission = "")
    public static void cobble(Player sender) {
        boolean val = !Hulu.getInstance().getCobblePickupMap().isCobblePickup(sender.getUniqueId());

        sender.sendMessage(ChatColor.YELLOW + "You are now " + (!val ? ChatColor.RED + "unable" : ChatColor.GREEN + "able") + ChatColor.YELLOW + " to pick up cobblestone while in Miner class!");
        Hulu.getInstance().getCobblePickupMap().setCobblePickup(sender.getUniqueId(), val);

    }
}


