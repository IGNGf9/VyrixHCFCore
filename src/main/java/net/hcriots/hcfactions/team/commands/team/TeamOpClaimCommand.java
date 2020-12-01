/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.team.commands.team;

import cc.fyre.stark.engine.command.Command;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.team.Team;
import net.hcriots.hcfactions.team.claims.VisualClaim;
import net.hcriots.hcfactions.team.claims.VisualClaimType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TeamOpClaimCommand {

    @Command(names = {"team claimop", "t claimop", "f claimop", "faction claimop", "fac claimop"}, permission = "worldedit.*")
    public static void teamOpClaim(final Player sender) {
        Team team = Hulu.getInstance().getTeamHandler().getTeam(sender);

        if (team == null) {
            sender.sendMessage(ChatColor.GRAY + "You are not on a team!");
            return;
        }

        sender.getInventory().remove(TeamClaimCommand.SELECTION_WAND);

        new BukkitRunnable() {

            public void run() {
                sender.getInventory().addItem(TeamClaimCommand.SELECTION_WAND.clone());
            }

        }.runTaskLater(Hulu.getInstance(), 1L);

        new VisualClaim(sender, VisualClaimType.CREATE, true).draw(false);

        if (!VisualClaim.getCurrentMaps().containsKey(sender.getName())) {
            new VisualClaim(sender, VisualClaimType.MAP, true).draw(true);
        }
    }

}