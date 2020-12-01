/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.map.stats;

import cc.fyre.stark.Stark;
import com.google.common.collect.Maps;
import lombok.Getter;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.map.stats.command.ChestCommand;
import net.hcriots.hcfactions.map.stats.command.KillstreaksCommand;
import net.hcriots.hcfactions.map.stats.command.StatModifyCommands;
import net.hcriots.hcfactions.map.stats.command.StatsCommand;
import net.hcriots.hcfactions.team.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

public class StatsHandler implements Listener {

    private final Map<UUID, StatsEntry> stats = Maps.newConcurrentMap();
    @Getter
    private final Map<Integer, UUID> topKills = Maps.newConcurrentMap();

    public StatsHandler() {
        Stark.instance.getCore().getRedis().runRedisCommand(redis -> {
            for (String key : redis.keys(Bukkit.getServerName() + ":" + "stats:*")) {
                UUID uuid = UUID.fromString(key.split(":")[2]);
                StatsEntry entry = Stark.getPlainGson().fromJson(redis.get(key), StatsEntry.class);

                stats.put(uuid, entry);
            }

            Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "[Kit Map] Loaded " + stats.size() + " stats.");

            return null;
        });

        Bukkit.getPluginManager().registerEvents(this, Hulu.getInstance());

        Stark.instance.getCommandHandler().registerClass(ChestCommand.class);
        Stark.instance.getCommandHandler().registerClass(KillstreaksCommand.class);
        Stark.instance.getCommandHandler().registerClass(StatModifyCommands.class);
        Stark.instance.getCommandHandler().registerClass(StatsCommand.class);

        Bukkit.getScheduler().scheduleAsyncRepeatingTask(Hulu.getInstance(), this::save, 30 * 20L, 30 * 20L);
    }

    public Map<StatsEntry, String> getLeaderboards(StatsObjective objective, int range) {
        if (objective != StatsObjective.KD) {
            Map<StatsEntry, Number> base = Maps.newHashMap();

            for (StatsEntry entry : stats.values()) {
                base.put(entry, entry.get(objective));
            }

            TreeMap<StatsEntry, Number> ordered = new TreeMap<>((first, second) -> {
                if (first.get(objective).doubleValue() >= second.get(objective).doubleValue()) {
                    return -1;
                }

                return 1;
            });
            ordered.putAll(base);

            Map<StatsEntry, String> leaderboards = Maps.newLinkedHashMap();

            int index = 0;
            for (Map.Entry<StatsEntry, Number> entry : ordered.entrySet()) {
                leaderboards.put(entry.getKey(), entry.getValue() + "");

                index++;

                if (index == range) {
                    break;
                }
            }

            return leaderboards;
        } else {
            Map<StatsEntry, Double> base = Maps.newHashMap();

            for (StatsEntry entry : stats.values()) {
                base.put(entry, entry.getKD());
            }

            TreeMap<StatsEntry, Double> ordered = new TreeMap<>((first, second) -> {
                if (first.getKD() > second.getKD()) {
                    return -1;
                }

                return 1;
            });
            ordered.putAll(base);

            Map<StatsEntry, String> leaderboards = Maps.newHashMap();

            int index = 0;
            for (Map.Entry<StatsEntry, Double> entry : ordered.entrySet()) {
                if (entry.getKey().getDeaths() < 10) {
                    continue;
                }

                String kd = Team.DTR_FORMAT.format((double) entry.getKey().getKills() / (double) entry.getKey().getDeaths());

                leaderboards.put(entry.getKey(), kd);

                index++;

                if (index == range) {
                    break;
                }
            }

            return leaderboards;
        }
    }

    public void save() {
        Stark.instance.getCore().getRedis().runRedisCommand(redis -> {
            // stats
            for (StatsEntry entry : stats.values()) {
                redis.set(Bukkit.getServerName() + ":" + "stats:" + entry.getOwner().toString(), Stark.getPlainGson().toJson(entry));
            }
            return null;
        });
    }

    public StatsEntry getStats(Player player) {
        return getStats(player.getUniqueId());
    }

    public StatsEntry getStats(String name) {
        return getStats(Stark.instance.getCore().getUuidCache().uuid(name));
    }

    public StatsEntry getStats(UUID uuid) {
        stats.putIfAbsent(uuid, new StatsEntry(uuid));
        return stats.get(uuid);
    }

    public void clearAll() {
        stats.clear();
        Bukkit.getScheduler().scheduleAsyncDelayedTask(Hulu.getInstance(), this::save);
    }

    public void clearLeaderboards() {
        Bukkit.getScheduler().scheduleAsyncDelayedTask(Hulu.getInstance(), this::save);
    }

}
