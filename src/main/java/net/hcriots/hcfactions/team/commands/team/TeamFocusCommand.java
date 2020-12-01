/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.team.commands.team;

import cc.fyre.stark.Stark;
import cc.fyre.stark.engine.command.Command;
import cc.fyre.stark.engine.command.data.parameter.Param;
import com.cheatbreaker.api.CheatBreakerAPI;
import com.cheatbreaker.api.object.CBWaypoint;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.team.Team;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TeamFocusCommand {

    public static CBWaypoint cbWaypoint = null;

    @Command(names = {"f focus", "t focus", "faction focus", "team focus"}, permission = "")
    public static void focus(final Player sender, @Param(name = "player") final Team targetTeam) {
        final Team senderTeam = Hulu.getInstance().getTeamHandler().getTeam(sender);
        if (senderTeam == null) {
            sender.sendMessage(ChatColor.GRAY + "You are not on a team!");
            return;
        }
        if (senderTeam == targetTeam) {
            sender.sendMessage(ChatColor.RED + "You cannot target a player on your team.");
            return;
        }
//        CBWaypoint waypoint = new CBWaypoint(targetTeam.getName() + "'s HQ (Focus)", targetTeam.getHQ(), 2, false, true);
        if (senderTeam.getFactionFocused() == targetTeam) {
            senderTeam.setFactionFocused(null);
            senderTeam.sendMessage(ChatColor.LIGHT_PURPLE + "Faction focus has been disabled.");
            CBWaypoint finalcbWaypoint = cbWaypoint;
            senderTeam.getOnlineMembers().forEach(player -> CheatBreakerAPI.getInstance().removeWaypoint(player, finalcbWaypoint));
            CheatBreakerAPI.getInstance().removeWaypoint(sender, finalcbWaypoint);
        } else {
            senderTeam.setFactionFocused(targetTeam);
            senderTeam.sendMessage(ChatColor.LIGHT_PURPLE + targetTeam.getName() + ChatColor.YELLOW + " has been focused by " + ChatColor.LIGHT_PURPLE + sender.getName() + ChatColor.YELLOW + ".");
            if (targetTeam.getHQ() != null) {
                cbWaypoint = new CBWaypoint(targetTeam.getName() + "'s HQ (Focus)", targetTeam.getHQ(), 2, false, true);
                CBWaypoint finalcbWaypoint1 = cbWaypoint;
                senderTeam.getOnlineMembers().forEach(player -> CheatBreakerAPI.getInstance().removeWaypoint(player, finalcbWaypoint1));
                CheatBreakerAPI.getInstance().removeWaypoint(sender, finalcbWaypoint1);
                senderTeam.getOnlineMembers().forEach(player -> CheatBreakerAPI.getInstance().sendWaypoint(player, finalcbWaypoint1));
                CheatBreakerAPI.getInstance().sendWaypoint(sender, finalcbWaypoint1);
            }
        }
        for (final Player onlinePlayer : Hulu.getInstance().getServer().getOnlinePlayers()) {
            if (senderTeam.isMember(onlinePlayer.getUniqueId())) {
                Stark.instance.getNametagEngine().reloadOthersFor(onlinePlayer);
            }
        }
    }
}