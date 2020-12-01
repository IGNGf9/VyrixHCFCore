/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.team.commands.team.subclaim;

import cc.fyre.stark.Stark;
import cc.fyre.stark.engine.command.Command;
import cc.fyre.stark.engine.command.data.parameter.Param;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.team.Team;
import net.hcriots.hcfactions.team.claims.Subclaim;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TeamSubclaimRemovePlayerCommand {

    @Command(names = {"team subclaim removeplayer", "t subclaim removeplayer", "f subclaim removeplayer", "faction subclaim removeplayer", "fac subclaim removeplayer", "team sub removeplayer", "t sub removeplayer", "f sub removeplayer", "faction sub removeplayer", "fac sub removeplayer", "team subclaim revoke", "t subclaim revoke", "f subclaim revoke", "faction subclaim revoke", "fac subclaim revoke", "team sub revoke", "t sub revoke", "f sub revoke", "faction sub revoke", "fac sub revoke"}, permission = "")
    public static void teamSubclaimRemovePlayer(Player sender, @Param(name = "subclaim") Subclaim subclaim, @Param(name = "player") UUID player) {
        Team team = Hulu.getInstance().getTeamHandler().getTeam(sender);

        if (!team.isOwner(sender.getUniqueId()) && !team.isCoLeader(sender.getUniqueId()) && !team.isCaptain(sender.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + "Only the team captains can do this.");
            return;
        }

        if (!team.isMember(player)) {
            sender.sendMessage(ChatColor.RED + Stark.instance.getCore().getUuidCache().name(player) + " is not on your team!");
            return;
        }

        if (!subclaim.isMember(player)) {
            sender.sendMessage(ChatColor.RED + "The player already does not have access to that subclaim!");
            return;
        }

        sender.sendMessage(ChatColor.GREEN + Stark.instance.getCore().getUuidCache().name(player) + ChatColor.YELLOW + " has been removed from the subclaim " + ChatColor.GREEN + subclaim.getName() + ChatColor.YELLOW + ".");
        subclaim.removeMember(player);
        team.flagForSave();
    }

}
