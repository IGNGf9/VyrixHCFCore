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

public class CitadelRespawnChestsCommand {

    @Command(names = {"citadel respawnchests"}, permission = "op")
    public static void citadelRespawnChests(Player sender) {
        Hulu.getInstance().getCitadelHandler().respawnCitadelChests();
        sender.sendMessage(CitadelHandler.PREFIX + " " + ChatColor.YELLOW + "Respawned all Citadel chests.");
    }

}