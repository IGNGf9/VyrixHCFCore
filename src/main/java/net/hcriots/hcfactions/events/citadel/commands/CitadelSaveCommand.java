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

public class CitadelSaveCommand {

    @Command(names = {"citadel save"}, permission = "op")
    public static void citadelSave(Player sender) {
        Hulu.getInstance().getCitadelHandler().saveCitadelInfo();
        sender.sendMessage(CitadelHandler.PREFIX + " " + ChatColor.YELLOW + "Saved Citadel info to file.");
    }

}