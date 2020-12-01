/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.team.commands.team;

import cc.fyre.stark.Stark;
import cc.fyre.stark.engine.command.Command;
import cc.fyre.stark.engine.command.data.parameter.Param;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.team.Team;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TeamInfoCommand {

    @Command(names = {"team info", "t info", "f info", "faction info", "fac info", "team who", "t who", "f who", "faction who", "fac who", "team show", "t show", "f show", "faction show", "fac show", "team i", "t i", "f i", "faction i", "fac i"}, permission = "")
    public static void teamInfo(final Player sender, @Param(name = "team", defaultValue = "self", tabCompleteFlags = {"noteams", "players"}) final Team team) {
        new BukkitRunnable() {

            public void run() {
                Team exactPlayerTeam = Hulu.getInstance().getTeamHandler().getTeam(Stark.instance.getCore().getUuidCache().uuid(team.getName()));

                if (exactPlayerTeam != null && exactPlayerTeam != team) {
                    exactPlayerTeam.sendTeamInfo(sender);
                }

                team.sendTeamInfo(sender);
            }

        }.runTaskAsynchronously(Hulu.getInstance());
    }

}