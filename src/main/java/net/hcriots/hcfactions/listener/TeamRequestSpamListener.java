/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.listener;

import cc.fyre.stark.modsuite.event.PlayerRequestReportEvent;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.team.Team;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.concurrent.TimeUnit;

public class TeamRequestSpamListener implements Listener {

    @EventHandler
    public void onPlayerReportRequest(PlayerRequestReportEvent event) {
        Team team = Hulu.getInstance().getTeamHandler().getTeam(event.getPlayer());

        if (team == null) {
            return;
        }

        long lastTeamRequestMsAgo = System.currentTimeMillis() - team.getLastRequestReport();

        if (lastTeamRequestMsAgo < TimeUnit.MINUTES.toMillis(1)) {
            event.setCancelled(true);
            event.setCancelMessage(ChatColor.RED + "Someone on your team has recently made a request/report.");
        } else {
            team.setLastRequestReport(System.currentTimeMillis());
        }
    }

}