package net.hcriots.hcfactions;

import com.cheatbreaker.api.CheatBreakerAPI;
import com.cheatbreaker.api.object.CBWaypoint;
import net.hcriots.hcfactions.team.Team;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.TimeUnit;

public class RallyTasks {

    public RallyTasks() {
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(Hulu.getInstance(), new BukkitRunnable() {
            @Override
            public void run() {
                for (Team teams : Hulu.getInstance().getTeamHandler().getTeams()) {
                    if (teams.isRally()) {
                        if (teams.isMember(teams.getRallyPlayer().getUniqueId())) {
                            CBWaypoint waypoint = new CBWaypoint(
                                    "Faction Rally",
                                    teams.getRallyPlayer().getLocation(),
                                    2,
                                    true,
                                    true
                            );
                            for (Player online : teams.getOnlineMembers()) {
                                CheatBreakerAPI.getInstance().sendWaypoint(
                                        online,
                                        waypoint
                                );
                            }

                            try {
                                Thread.sleep(TimeUnit.SECONDS.toMillis(5)); // this is the update timer (every x seconds)

                                for (Player online : teams.getOnlineMembers()) {
                                    CheatBreakerAPI.getInstance().removeWaypoint(
                                            online,
                                            waypoint
                                    );
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }, 0, 20L);
    }
}