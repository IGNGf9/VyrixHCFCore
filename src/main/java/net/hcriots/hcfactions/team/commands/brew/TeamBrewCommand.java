/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.team.commands.brew;

import cc.fyre.stark.engine.command.Command;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.team.Team;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class TeamBrewCommand {

    @Command(names = {"team brew", "t brew", "faction brew", "f brew", "fac brew"}, permission = "")
    public static void teamBrew(Player sender) {
        Team team = Hulu.getInstance().getTeamHandler().getTeam(sender);

        if (team == null) {
            sender.sendMessage(ChatColor.GRAY + "You are not on a team!");
            return;
        }
        sender.sendMessage(ChatColor.RED + "This feature is currently disabled.");
//        new TeamBrewMenu(team).openMenu(sender);
    }

    @Command(names = {"team brewinv", "t brewinv", "faction brewinv", "f brewinv", "fac brewinv"}, permission = "")
    public static void teamBrewInv(Player sender) {
        Team team = Hulu.getInstance().getTeamHandler().getTeam(sender);

        if (team == null) {
            sender.sendMessage(ChatColor.GRAY + "You are not on a team!");
            return;
        }
        final Inventory inventory = team.getBrewingVault();
        if (inventory == null) {
            sender.sendMessage(ChatColor.RED + "Failed to open brewing menu!");
            return;
        }
        sender.sendMessage(ChatColor.RED + "This feature is currently disabled.");
//        sender.openInventory(inventory);
//        sender.sendMessage(ChatColor.GREEN + "Opening faction brewing menu...");
    }
}
