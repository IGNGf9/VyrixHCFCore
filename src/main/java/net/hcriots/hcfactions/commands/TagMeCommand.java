/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.commands;

import cc.fyre.stark.engine.command.Command;
import net.hcriots.hcfactions.server.SpawnTagHandler;
import org.bukkit.entity.Player;

public class TagMeCommand {

    @Command(names = {"combattagnigger"}, permission = "op")
    public static void tagMe(Player sender) {
        SpawnTagHandler.addOffensiveSeconds(sender, SpawnTagHandler.getMaxTagTime());
    }

}