/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.events.citadel.commands;

import cc.fyre.stark.engine.command.Command;
import cc.fyre.stark.engine.command.data.parameter.Param;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.events.citadel.CitadelHandler;
import net.hcriots.hcfactions.team.Team;
import org.bson.types.ObjectId;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CitadelSetCapperCommand {

    @Command(names = {"citadel setcapper"}, permission = "op")
    public static void citadelSetCapper(Player sender, @Param(name = "cappers") String cappers) {
        if (cappers.equals("null")) {
            Hulu.getInstance().getCitadelHandler().resetCappers();
            sender.sendMessage(CitadelHandler.PREFIX + " " + ChatColor.YELLOW + "Reset Citadel cappers.");
        } else {
            String[] teamNames = cappers.split(",");
            List<ObjectId> teams = new ArrayList<>();

            for (String teamName : teamNames) {
                Team team = Hulu.getInstance().getTeamHandler().getTeam(teamName);

                if (team != null) {
                    teams.add(team.getUniqueId());
                } else {
                    sender.sendMessage(ChatColor.RED + "Team '" + teamName + "' cannot be found.");
                    return;
                }
            }

            Hulu.getInstance().getCitadelHandler().getCappers().clear();
            Hulu.getInstance().getCitadelHandler().getCappers().addAll(teams);
            sender.sendMessage(CitadelHandler.PREFIX + " " + ChatColor.YELLOW + "Updated Citadel cappers.");
        }
    }

}