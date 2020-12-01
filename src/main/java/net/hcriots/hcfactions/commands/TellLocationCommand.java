/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.commands;

import cc.fyre.stark.engine.command.Command;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.HuluConstants;
import net.hcriots.hcfactions.team.Team;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public final class TellLocationCommand {

    @Command(names = {"telllocation", "tl"}, permission = "")
    public static void tellLocation(Player sender) {
        Team team = Hulu.getInstance().getTeamHandler().getTeam(sender);

        if (team == null) {
            sender.sendMessage(ChatColor.RED + "You're not on a team!");
            return;
        }

        Location l = sender.getLocation();
        team.sendMessage(HuluConstants.teamChatFormat(sender, String.format("[%.1f, %.1f, %.1f]", l.getX(), l.getY(), l.getZ())));
    }

}
