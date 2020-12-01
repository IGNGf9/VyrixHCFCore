/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.team.upgrades.command;

import cc.fyre.stark.engine.command.Command;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.team.Team;
import net.hcriots.hcfactions.team.upgrades.menu.TeamUpgradesMenu;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Created by InspectMC
 * Date: 7/16/2020
 * Time: 9:40 PM
 */

public class TeamUpgradesCommand {

    @Command(names = {"team upgrade", "team upgrades", "t upgrade", "t upgrades", "faction upgrade", "faction upgrades", "f upgrade", "f upgrades"}, permission = "")
    public static void teamUpgrades(Player sender) {
        Team team = Hulu.getInstance().getTeamHandler().getTeam(sender);

        if (team == null) {
            sender.sendMessage(ChatColor.GRAY + "You are not on a team!");
            return;
        }

        if (!team.isOwner(sender.getUniqueId()) && !team.isCoLeader(sender.getUniqueId())) {
            sender.sendMessage(ChatColor.DARK_AQUA + "Only team co-leaders (and above) can do this.");
            return;
        }

        new TeamUpgradesMenu().openMenu(sender);
    }

}
