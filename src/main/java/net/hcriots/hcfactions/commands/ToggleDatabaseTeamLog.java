/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.commands;

import cc.fyre.stark.engine.command.Command;
import net.hcriots.hcfactions.team.track.TeamActionTracker;
import org.bukkit.entity.Player;

public class ToggleDatabaseTeamLog {

    @Command(names = {"toggledatabaseteamlog"}, permission = "op")
    public static void toggleDatabaseTeamLog(Player sender) {
        TeamActionTracker.setDatabaseLogEnabled(!TeamActionTracker.isDatabaseLogEnabled());
        sender.sendMessage("Enabled: " + TeamActionTracker.isDatabaseLogEnabled());
    }

}