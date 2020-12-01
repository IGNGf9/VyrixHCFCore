/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.team.commands.team;

import cc.fyre.stark.engine.command.Command;
import cc.fyre.stark.engine.command.data.parameter.Param;
import net.hcriots.hcfactions.server.SpawnTagHandler;
import net.hcriots.hcfactions.team.Team;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class TeamVaultCommand {

    @Command(names = {"team vault", "t vault", "f vault", "faction vault", "fac vault"}, permission = "")
    public static void teamVault(final Player sender, @Param(name = "team", defaultValue = "self") final Team team) {
        if (SpawnTagHandler.isTagged(sender)) {
            sender.sendMessage(ChatColor.RED + "You cannot open your faction vault while in combat!");
            return;
        }
        if (!team.ownsLocation(sender.getLocation())) {
            sender.sendMessage(ChatColor.RED + "You must be in your claim to open your faction vault!");
            return;
        }
        final Inventory inventory = team.getFactionVault();
        if (inventory == null) {
            sender.sendMessage(ChatColor.RED + "Failed to open vault!");
            return;
        }
        sender.openInventory(inventory);
        sender.sendMessage(ChatColor.GREEN + "Opening faction vault...");
    }

    @Command(names = {"team opvault", "t opvault", "f opvault", "faction opvault", "fac opvault"}, permission = "")
    public static void opteamVault(final Player sender, @Param(name = "team", defaultValue = "self") final Team team) {
        if (sender.isOp()) {
            final Inventory inventory = team.getFactionVault();
            if (inventory == null) {
                sender.sendMessage(ChatColor.RED + "Failed to open vault!");
                return;
            }
            sender.openInventory(inventory);
            sender.sendMessage(ChatColor.GREEN + "Opening faction vault...");
        }
    }
}