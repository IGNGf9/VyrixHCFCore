/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.team.commands.team;

import cc.fyre.stark.engine.command.Command;
import cc.fyre.stark.engine.command.data.parameter.Param;
import net.hcriots.hcfactions.team.Team;
import org.bukkit.command.CommandSender;

public class TeamJSONCommand {

    @Command(names = {"team json", "t json", "f json", "faction json", "fac json"}, permission = "op")
    public static void teamJSON(CommandSender sender, @Param(name = "team", defaultValue = "self") Team team) {
        sender.sendMessage(team.toJSON().toString());
    }

}