/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.server;

import com.mongodb.BasicDBObject;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public final class Deathban {

    private static final Map<String, Integer> deathban = new LinkedHashMap<>();
    private static int defaultMinutes = 240;

    static {
        deathban.put("basic", 90); //1 hour and 30m
        deathban.put("iron", 75); // 1 hour and 25m
        deathban.put("gold", 60); // 1 hour
        deathban.put("diamond", 45); //44m
        deathban.put("emerald", 30);
        deathban.put("riot", 20); // 20m
        deathban.put("riot-plus", 10);
    }

    public static void load(BasicDBObject object) {
        deathban.clear();

        for (String key : object.keySet()) {
            if (key.equals("DEFAULT")) {
                defaultMinutes = object.getInt(key);
            } else {
                deathban.put(key, object.getInt(key));
            }
        }
    }

    public static int getDeathbanSeconds(Player player) {
        int minutes = defaultMinutes;

        for (Map.Entry<String, Integer> entry : deathban.entrySet()) {
            if (player.hasPermission("inherit." + entry.getKey().toLowerCase()) && entry.getValue() < minutes) {
                minutes = entry.getValue();
            }
        }

        return (int) TimeUnit.MINUTES.toSeconds(minutes);
    }

}
