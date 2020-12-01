/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.team.commands.team.subclaim;

import cc.fyre.stark.engine.command.Command;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.team.Team;
import net.hcriots.hcfactions.team.claims.VisualClaim;
import net.hcriots.hcfactions.team.claims.VisualClaimType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TeamSubclaimStartCommand {

    @Command(names = {"team subclaim start", "t subclaim start", "f subclaim start", "faction subclaim start", "fac subclaim start", "team sub start", "t sub start", "f sub start", "faction sub start", "fac sub start"}, permission = "")
    public static void teamSubclaimStart(Player sender) {
        Team team = Hulu.getInstance().getTeamHandler().getTeam(sender);

        if (team == null) {
            sender.sendMessage(ChatColor.RED + "You must be on a team to execute this command!");
            return;
        }

        if (!team.isCaptain(sender.getUniqueId()) && !team.isCoLeader(sender.getUniqueId()) && !team.isOwner(sender.getUniqueId())) {
            sender.sendMessage(ChatColor.DARK_AQUA + "Only team captains can do this.");
            return;
        }

        int slot = -1;

        for (int i = 0; i < 9; i++) {
            if (sender.getInventory().getItem(i) == null) {
                slot = i;
                break;
            }
        }

        if (slot == -1) {
            sender.sendMessage(ChatColor.RED + "You don't have space in your hotbar for the subclaim wand!");
            return;
        }

        if (!VisualClaim.getCurrentSubclaimMaps().containsKey(sender.getName())) {
            new VisualClaim(sender, VisualClaimType.SUBCLAIM_MAP, true).draw(true);
        }

        sender.getInventory().setItem(slot, TeamSubclaimCommand.SELECTION_WAND.clone());
        sender.sendMessage(ChatColor.GREEN + "Gave you a subclaim wand.");
    }

}