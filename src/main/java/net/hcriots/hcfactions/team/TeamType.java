/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.team;

import cc.fyre.stark.Stark;
import cc.fyre.stark.engine.command.data.parameter.ParameterType;
import net.hcriots.hcfactions.Hulu;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TeamType implements ParameterType<Team> {

    public Team transform(CommandSender sender, String source) {
        if (sender instanceof Player && (source.equalsIgnoreCase("self") || source.equals(""))) {
            Team team = Hulu.getInstance().getTeamHandler().getTeam(((Player) sender).getUniqueId());

            if (team == null) {
                sender.sendMessage(ChatColor.GRAY + "You're not on a team!");
                return (null);
            }

            return (team);
        }

        Team byName = Hulu.getInstance().getTeamHandler().getTeam(source);

        if (byName != null) {
            return (byName);
        }

        Player bukkitPlayer = Hulu.getInstance().getServer().getPlayer(source);

        if (bukkitPlayer != null) {
            Team byMemberBukkitPlayer = Hulu.getInstance().getTeamHandler().getTeam(bukkitPlayer.getUniqueId());

            if (byMemberBukkitPlayer != null) {
                return (byMemberBukkitPlayer);
            }
        }

        Team byMemberUUID = Hulu.getInstance().getTeamHandler().getTeam(Stark.instance.getCore().getUuidCache().uuid(source));

        if (byMemberUUID != null) {
            return (byMemberUUID);
        }

        sender.sendMessage(ChatColor.RED + "No team or member with the name " + source + " found.");
        return (null);
    }

    public List<String> tabComplete(Player sender, Set<String> flags, String source) {
        List<String> completions = new ArrayList<>();

        // Teams being included in the completion is ENABLED by default.
        if (!flags.contains("noteams")) {
            for (Team team : Hulu.getInstance().getTeamHandler().getTeams()) {
                if (StringUtils.startsWithIgnoreCase(team.getName(), source)) {
                    completions.add(team.getName());
                }
            }
        }

        // Players being included in the completion is DISABLED by default.
        if (flags.contains("players")) {
            for (Player player : Hulu.getInstance().getServer().getOnlinePlayers()) {
                if (StringUtils.startsWithIgnoreCase(player.getName(), source)) {
                    completions.add(player.getName());
                }
            }
        }

        return (completions);
    }

}