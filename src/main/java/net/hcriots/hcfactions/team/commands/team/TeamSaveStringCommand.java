/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.team.commands.team;

import cc.fyre.stark.engine.command.Command;
import cc.fyre.stark.engine.command.data.parameter.Param;
import net.hcriots.hcfactions.team.Team;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class TeamSaveStringCommand {

    @Command(names = {"team savestring", "t savestring", "f savestring", "faction savestring", "fac savestring"}, permission = "op")
    public static void teamSaveString(CommandSender sender, @Param(name = "team", defaultValue = "self") Team team) {
        String saveString = team.saveString(false);

        sender.sendMessage(ChatColor.BLUE.toString() + ChatColor.UNDERLINE + "Save String (" + team.getName() + ")");
        sender.sendMessage("");

        for (String line : saveString.split("\n")) {
            sender.sendMessage(ChatColor.BLUE + line.substring(0, line.indexOf(":")) + ": " + ChatColor.YELLOW + line.substring(line.indexOf(":") + 1).replace(",", ChatColor.BLUE + "," + ChatColor.YELLOW).replace(":", ChatColor.BLUE + ":" + ChatColor.YELLOW));
        }
    }

}