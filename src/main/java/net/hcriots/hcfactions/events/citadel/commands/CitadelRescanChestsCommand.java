/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.events.citadel.commands;

import cc.fyre.stark.engine.command.Command;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.events.citadel.CitadelHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CitadelRescanChestsCommand {

    @Command(names = {"citadel rescanchests"}, permission = "op")
    public static void citadelRescanChests(Player sender) {
        Hulu.getInstance().getCitadelHandler().scanLoot();
        Hulu.getInstance().getCitadelHandler().saveCitadelInfo();
        sender.sendMessage(CitadelHandler.PREFIX + " " + ChatColor.YELLOW + "Rescanned all Citadel chests.");
    }

}