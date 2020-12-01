/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.persist;

import cc.fyre.stark.Stark;
import com.mongodb.DBCollection;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.team.Team;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class RedisSaveTask extends BukkitRunnable {

    public static int save(final CommandSender issuer, final boolean forceAll) {
        long startMs = System.currentTimeMillis();
        int teamsSaved = Stark.instance.getCore().getRedis().runRedisCommand(redis -> {

            DBCollection teamsCollection = Hulu.getInstance().getMongoPool().getDB(Hulu.MONGO_DB_NAME).getCollection("Teams");

            int changed = 0;

            for (Team team : Hulu.getInstance().getTeamHandler().getTeams()) {
                if (team.isNeedsSave() || forceAll) {
                    changed++;

                    redis.set("fox_teams." + team.getName().toLowerCase(), team.saveString(true));
                    teamsCollection.update(team.getJSONIdentifier(), team.toJSON(), true, false);
                }

                if (forceAll) {
                    for (UUID member : team.getMembers()) {
                        Hulu.getInstance().getTeamHandler().setTeam(member, team, true);
                    }
                }
            }

            redis.set("RostersLocked", String.valueOf(Hulu.getInstance().getTeamHandler().isRostersLocked()));
            if (issuer != null && forceAll) redis.save();
            return (changed);
        });

        int time = (int) (System.currentTimeMillis() - startMs);

        if (teamsSaved != 0) {
            System.out.println("Saved " + teamsSaved + " teams to Redis in " + time + "ms.");

            if (issuer != null) {
                issuer.sendMessage(ChatColor.DARK_PURPLE + "Saved " + teamsSaved + " teams to Redis in " + time + "ms.");
            }
        }

        return (teamsSaved);
    }

    public void run() {
        save(null, false);
    }

}