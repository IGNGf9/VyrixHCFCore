/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.team.commands.team;

import cc.fyre.stark.engine.command.Command;
import org.bukkit.entity.Player;

public class TeamHelpCommand {

    @Command(names = {"team help", "t help", "f help", "faction help", "fac help"}, permission = "")
    public static void teamHelp(Player player) {
        TeamCommand.team(player);
    }
}