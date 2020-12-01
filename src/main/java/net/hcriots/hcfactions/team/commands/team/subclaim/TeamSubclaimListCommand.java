/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.team.commands.team.subclaim;

import cc.fyre.stark.engine.command.Command;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.team.Team;
import net.hcriots.hcfactions.team.claims.Subclaim;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TeamSubclaimListCommand {

    @Command(names = {"team subclaim list", "t subclaim list", "f subclaim list", "faction subclaim list", "fac subclaim list", "team sub list", "t sub list", "f sub list", "faction sub list", "fac sub list"}, permission = "")
    public static void teamSubclaimList(Player sender) {
        Team team = Hulu.getInstance().getTeamHandler().getTeam(sender);

        if (team == null) {
            sender.sendMessage(ChatColor.RED + "You must be on a team to execute this command!");
            return;
        }

        StringBuilder access = new StringBuilder();
        StringBuilder other = new StringBuilder();

        for (Subclaim subclaim : team.getSubclaims()) {
            if (subclaim.isMember(sender.getUniqueId()) || team.isOwner(sender.getUniqueId()) || team.isCoLeader(sender.getUniqueId()) || team.isCaptain(sender.getUniqueId())) {
                access.append(subclaim.getName()).append(", ");
                continue;
            }

            other.append(subclaim).append(", ");
        }

        if (access.length() > 2) {
            access.setLength(access.length() - 2);
        }

        if (other.length() > 2) {
            other.setLength(other.length() - 2);
        }

        sender.sendMessage(ChatColor.BLUE + team.getName() + ChatColor.YELLOW + " Subclaim List");
        sender.sendMessage(ChatColor.YELLOW + "Subclaims you can access: " + ChatColor.WHITE + access.toString());
        sender.sendMessage(ChatColor.YELLOW + "Other Subclaims: " + ChatColor.WHITE + other.toString());
    }

}