/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.events.blackmarket;

import cc.fyre.stark.util.Cuboid;
import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import lombok.Getter;
import lombok.Setter;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.commands.CustomTimerCreateCommand;
import net.hcriots.hcfactions.events.EventScheduledTime;
import net.hcriots.hcfactions.events.blackmarket.npc.BlackMarketNPCHandler;
import net.hcriots.hcfactions.util.Pair;
import net.minecraft.server.v1_7_R4.AxisAlignedBB;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Missionary (missionarymc@gmail.com)
 * @since 8/1/2020
 */
public class BlackMarketHandler {

    private final Hulu plugin;

    @Getter
    private final BlackMarketNPCHandler npcHandler;
    @Getter
    private final List<EventScheduledTime> scheduledTimeList;
    private final BukkitTask scheduleTask;
    @Getter
    @Setter
    private boolean active, disabled;
    @Getter
    private Pair<EditSession, CuboidClipboard> editSession;
    @Getter
    @Setter
    private Location location;
    @Getter
    private Cuboid blackMarketRegion;

    // TODO: 8/10/2020 overhaul scheduling system
    public BlackMarketHandler(Hulu plugin) {
        this.plugin = plugin;
        this.scheduledTimeList = new ArrayList<>();
        this.npcHandler = new BlackMarketNPCHandler(plugin);
        calculateScheduledTimes();
        this.scheduleTask = plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
            if (active) {
                return;
            }
            EventScheduledTime time = EventScheduledTime.parse(new Date());
            if (scheduledTimeList.contains(time)) {
                if (!CustomTimerCreateCommand.isSOTWTimer()) {
                    startBlackMarket();
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, this::endBlackMarket, 20L * TimeUnit.MINUTES.toSeconds(30L));
                }
                scheduledTimeList.remove(time);
            }

            if (scheduledTimeList.isEmpty()) {
                calculateScheduledTimes();
            }
        }, 20L, 20L);
    }

    public void regenerateSchedules() {
        scheduledTimeList.clear();
        calculateScheduledTimes();
    }

    private void calculateScheduledTimes() {
        for (int hour = 0; hour < 24; hour++) {
            if (hour % 6 == 0) {
                scheduledTimeList.add(new EventScheduledTime(Calendar.getInstance().get(Calendar.DAY_OF_YEAR), hour, 0));
            }
        }
    }


    public void pasteBlackMarketShop(Location location) throws Exception {
        System.out.println(plugin.getDataFolder().getAbsolutePath());
        File schematicFile = new File(plugin.getDataFolder().getAbsolutePath() + "/blackmarket.schematic");
        if (!schematicFile.exists()) {
            this.disabled = true;
            throw new RuntimeException("Blackmarket schematic not found!");
        }
        EditSession editSession = new EditSession(new BukkitWorld(location.getWorld()), 999999999);
        CuboidClipboard cuboidClipboard = CuboidClipboard.loadSchematic(schematicFile);
        Vector vector = new Vector(location.getX(), location.getY(), location.getZ());
        cuboidClipboard.paste(editSession, vector, false);
        this.editSession = new Pair<>(editSession, cuboidClipboard);
        // TODO: 8/5/2020 config radius
        AxisAlignedBB alignedBB = AxisAlignedBB.a(location.getBlockX() - 30, Math.min(location.getBlockY() - 30, 0), location.getBlockZ() - 30, location.getBlockX() + 30, Math.max(location.getBlockY() + 30, 256), location.getBlockZ() + 30);
        this.blackMarketRegion = new Cuboid(location.getWorld(), (int) alignedBB.a, (int) alignedBB.b, (int) alignedBB.c, (int) alignedBB.d, (int) alignedBB.e, (int) alignedBB.f);
    }

    public void startBlackMarket() {
        try {
            pasteBlackMarketShop(location);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setActive(true);
        npcHandler.spawnShopKeeper(location);
        Bukkit.broadcastMessage(ChatColor.RED + "[Blackmarket] " + ChatColor.YELLOW + "The market is now " + ChatColor.GREEN + "active" +
                ChatColor.YELLOW + " at " + ChatColor.LIGHT_PURPLE + location.getX() + ',' + location.getZ() + ChatColor.YELLOW + " and it will end in 30 minutes");
    }

    public void endBlackMarket() {
        // TODO: 8/6/2020 undo the build
        setActive(false);
        npcHandler.despawnShopKeeper();
        editSession.first.undo(editSession.first);
        Bukkit.broadcastMessage(ChatColor.RED + "[Blackmarket] " + ChatColor.YELLOW + "The market has closed. " +
                "Check " + ChatColor.RED + "/blackmarket schedule" + ChatColor.YELLOW + " to see when it will open next.");
    }

    public void disable() {
        if (active) {
            endBlackMarket();
            active = false;
        }
    }
}
