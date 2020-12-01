/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.events.blackmarket.commands;

import cc.fyre.stark.engine.command.Command;
import cc.fyre.stark.engine.command.CommandHandler;
import cc.fyre.stark.engine.command.CommandNode;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.events.EventScheduledTime;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * @author Missionary (missionarymc@gmail.com)
 * @since 8/4/2020
 */
public class BlackMarketCommands {

    public static final SimpleDateFormat TIME_FORMATTER = new SimpleDateFormat("MM/dd 'at' hh:mm:a 'ET'");

    static {
        TIME_FORMATTER.setTimeZone(TimeZone.getTimeZone("America/New_York"));
    }

    @Command(names = {"blackmarket"}, description = "Root node for blackmarket commands")
    public static void blackmarket(Player player) {
        // Show command help for ops and the schedule for all others
        if (player.isOp()) {
            CommandNode node = CommandHandler.Companion.getRootNode().getCommand("blackmarket");
            if (node != null) {
                node.getSubCommands(player, true);
                player.sendMessage(ChatColor.YELLOW + "To view the schedule type " + ChatColor.RED + "/blackmarket schedule" + ChatColor.YELLOW + '.');
            } else {
                schedule(player);
            }
        } else {
            schedule(player);
        }
    }

    @Command(names = "blackmarket start", permission = "op", description = "Forcefully starts the blackmarket event")
    public static void start(Player player) {
        Hulu.getInstance().getBlackMarketHandler().startBlackMarket();
        Hulu.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(Hulu.getInstance(), () -> Hulu.getInstance().getBlackMarketHandler().endBlackMarket(), 20L * TimeUnit.MINUTES.toSeconds(30L));
        player.sendMessage(ChatColor.YELLOW + "Forcefully started the blackmarket event. To cancel type " + ChatColor.RED + "/blackmarket end" + ChatColor.YELLOW + '.');
    }

    @Command(names = {"blackmarket end"}, permission = "op", description = "Forcefully ends the blackmarket event")
    public static void end(Player player) {
        Hulu.getInstance().getBlackMarketHandler().endBlackMarket();
        player.sendMessage(ChatColor.YELLOW + "Forcefully ended the blackmarket event.");
    }

    @Command(names = {"blackmarket regenerateschedule"}, permission = "op")
    public static void regenerateSchedule(Player player) {
        Hulu.getInstance().getBlackMarketHandler().regenerateSchedules();
        player.sendMessage(ChatColor.YELLOW + "Regenerated the schedule for blackmarket.");
        schedule(player);
    }

    @Command(names = {"blackmarket schedule"})
    public static void schedule(Player player) {
        player.sendMessage(ChatColor.RED + "[Blackmarket] " + ChatColor.YELLOW + "The event will be active at:");
        for (EventScheduledTime time : Hulu.getInstance().getBlackMarketHandler().getScheduledTimeList()) {
            if (!time.toDate().after(new Date())) {
                continue;
            }
            Date date = time.toDate();
            player.sendMessage(ChatColor.GRAY + "  -  " + ChatColor.GREEN + TIME_FORMATTER.format(date));
        }
        player.sendMessage(ChatColor.YELLOW + "All times are in EST (UTC-5)");
    }
}
