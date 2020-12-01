/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.commands;

import cc.fyre.stark.engine.command.Command;
import net.hcriots.hcfactions.team.claims.LandBoard;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ToggleClaimsCommand {

    @Command(names = {"ToggleClaims"}, permission = "op")
    public static void toggleClaims(Player sender) {
        LandBoard.getInstance().setClaimsEnabled(!LandBoard.getInstance().isClaimsEnabled());
        sender.sendMessage(ChatColor.YELLOW + "Claims enabled? " + ChatColor.LIGHT_PURPLE + (LandBoard.getInstance().isClaimsEnabled() ? "Yes" : "No"));
    }

}