/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.commands;

import cc.fyre.stark.core.util.TimeUtils;
import cc.fyre.stark.engine.command.Command;
import cc.fyre.stark.engine.command.data.parameter.Param;
import com.google.common.collect.Sets;
import lombok.Getter;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.server.SpawnTagHandler;
import net.hcriots.hcfactions.team.Team;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class CustomTimerCreateCommand {

    @Getter
    private static final Map<String, Long> customTimers = new HashMap<>();
    private static final Set<UUID> sotwEnabled = Sets.newHashSet();

    @Command(names = {"customtimer create"}, permission = "foxtrot.customtimer")
    public static void customTimerCreate(CommandSender sender, @Param(name = "time") int time, @Param(name = "title", wildcard = true) String title) {
        if (time == 0) {
            customTimers.remove(title);
        } else {
            customTimers.put(title, System.currentTimeMillis() + (time * 1000));
        }
    }

    @Command(names = {"sotw enable"}, permission = "")
    public static void sotwEnable(Player sender) {
        if (!isSOTWTimer()) {
            sender.sendMessage(ChatColor.RED + "You can't /sotw enable when there is no SOTW timer...");
            return;
        }

        if (sotwEnabled.add(sender.getUniqueId())) {
            sender.sendMessage(ChatColor.GREEN + "Successfully disabled your SOTW timer.");
        } else {
            sender.sendMessage(ChatColor.RED + "Your SOTW timer was already disabled...");
        }
    }

    @Command(names = {"sotw spawn"}, permission = "")
    public static void sotwSpawn(Player sender) {
        if (!isSOTWTimer()) {
            sender.sendMessage(ChatColor.RED + "SOTW Timer is not active");
            return;
        }

        if (SpawnTagHandler.isTagged(sender)) {
            sender.sendMessage(ChatColor.RED + "You cannot teleport to spawn since you're in combat.");
            return;
        }

        Team team = Hulu.getInstance().getTeamHandler().getTeam(sender.getUniqueId());
        if (!team.ownsLocation(sender.getLocation())) {
            sender.sendMessage(ChatColor.RED + "You must be in your claim to execute this command!");
            return;
        }

        sender.teleport(Hulu.getInstance().getServerHandler().getSpawnLocation());
    }

    @Command(names = {"sotw cancel"}, permission = "foxtrot.sotw")
    public static void sotwCancel(CommandSender sender) {
        Long removed = customTimers.remove("&e&lSOTW");
        if (removed != null && System.currentTimeMillis() < removed) {
            sender.sendMessage(ChatColor.GREEN + "Deactivated the SOTW timer.");
            return;
        }

        sender.sendMessage(ChatColor.RED + "SOTW timer is not active.");
    }

    @Command(names = "sotw start", permission = "foxtrot.sotw")
    public static void sotwStart(CommandSender sender, @Param(name = "time") String time) {
        int seconds = TimeUtils.parseTime(time);
        if (seconds < 0) {
            sender.sendMessage(ChatColor.RED + "Invalid time!");
            return;
        }

        customTimers.put("&e&lSOTW", System.currentTimeMillis() + (seconds * 1000));
        sender.sendMessage(ChatColor.GREEN + "Started the SOTW timer for " + time);
    }

    @Command(names = "sotw extend", permission = "foxtrot.sotw")
    public static void sotwExtend(CommandSender sender, @Param(name = "time") String time) {
        int seconds;
        try {
            seconds = TimeUtils.parseTime(time);
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "Invalid time!");
            return;
        }

        if (seconds < 0) {
            sender.sendMessage(ChatColor.RED + "Invalid time!");
            return;
        }

        if (!customTimers.containsKey("&e&lSOTW")) {
            sender.sendMessage(ChatColor.RED + "There is currently no active SOTW timer.");
            return;
        }

        customTimers.put("&e&lSOTW", customTimers.get("&e&lSOTW") + (seconds * 1000));
        sender.sendMessage(ChatColor.GREEN + "Extended the SOTW timer by " + time);
    }

    public static boolean isSOTWTimer() {
        return customTimers.containsKey("&e&lSOTW");
    }

    public static boolean hasSOTWEnabled(UUID uuid) {
        return sotwEnabled.contains(uuid);
    }
}