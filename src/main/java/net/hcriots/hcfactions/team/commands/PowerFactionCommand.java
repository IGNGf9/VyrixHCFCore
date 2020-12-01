/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.team.commands;

import cc.fyre.stark.engine.command.Command;
import cc.fyre.stark.engine.command.data.parameter.Param;
import net.hcriots.hcfactions.team.Team;
import net.hcriots.hcfactions.team.TeamHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PowerFactionCommand {

    @Command(names = {"powerfaction add", "team powerfaction add", "pf add", "powerfac add"}, permission = "foxtrot.powerfactions")
    public static void powerFactionAdd(Player sender, @Param(name = "team") Team team) {
        team.setPowerFaction(true);
        sender.sendMessage(ChatColor.LIGHT_PURPLE + team.getName() + ChatColor.YELLOW + " is now a power faction!");
    }

    @Command(names = {"powerfaction remove", "team powerfaction remove", "pf remove", "powerfac remove"}, permission = "foxtrot.powerfactions")
    public static void powerFactionRemove(Player sender, @Param(name = "team") Team team) {
        team.setPowerFaction(false);
        sender.sendMessage(ChatColor.LIGHT_PURPLE + team.getName() + ChatColor.YELLOW + " is no longer a power faction!");
    }

    @Command(names = {"powerfaction list", "team powerfaction list", "pf list", "powerfac list"}, permission = "foxtrot.powerfactions")
    public static void powerFactionList(Player sender) {
        sender.sendMessage(ChatColor.YELLOW + "Found " + ChatColor.RED + TeamHandler.getPowerFactions().size() + ChatColor.YELLOW + " Power Factions.");
        int i = 1;
        for (Team t : TeamHandler.getPowerFactions()) {
            sender.sendMessage(ChatColor.YELLOW + "" + i + ". " + ChatColor.RED + t.getName());
            i++;
        }
    }
}