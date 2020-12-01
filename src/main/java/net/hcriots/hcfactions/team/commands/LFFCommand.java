/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.team.commands;

import cc.fyre.stark.engine.command.Command;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.team.Team;
import net.hcriots.hcfactions.util.Cooldowns;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class LFFCommand {

    @Command(names = {"lff", "lookingforfaction"}, permission = "")
    public static void lff(Player player) {
        Team playerTeam = Hulu.getInstance().getTeamHandler().getTeam(player);
        if (playerTeam != null) {
            player.sendMessage(ChatColor.RED + "You are already in a faction!");
            return;
        }
        if (Cooldowns.isOnCooldown("lff", player)) {
            player.sendMessage(ChatColor.RED + "You can do this again in " + ChatColor.BOLD + Cooldowns.getCooldownForPlayerInt("lff", player) + ChatColor.RED + "s!");
        } else {
            Bukkit.broadcastMessage(player.getDisplayName() + ChatColor.GRAY + " is looking for a " + ChatColor.BLUE + "team");
            Cooldowns.addCooldown("lff", player, 60);
        }
    }
}