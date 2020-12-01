/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.events;

import cc.fyre.stark.Stark;
import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;
import lombok.Getter;
import lombok.Setter;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.events.dtc.DTC;
import net.hcriots.hcfactions.events.dtc.DTCListener;
import net.hcriots.hcfactions.events.koth.KOTH;
import net.hcriots.hcfactions.events.koth.listeners.KOTHListener;
import net.minecraft.util.org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.libs.com.google.gson.JsonParser;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class EventHandler {

    @Getter
    private final Set<Event> events = new HashSet<>();
    @Getter
    private final Map<EventScheduledTime, String> EventSchedule = new TreeMap<>();

    @Getter
    @Setter
    private boolean scheduleEnabled;

    public EventHandler() {
        loadEvents();
        loadSchedules();

        Hulu.getInstance().getServer().getPluginManager().registerEvents(new KOTHListener(), Hulu.getInstance());
        Hulu.getInstance().getServer().getPluginManager().registerEvents(new DTCListener(), Hulu.getInstance());
        Hulu.getInstance().getServer().getPluginManager().registerEvents(new EventListener(), Hulu.getInstance());

        Stark.instance.getCommandHandler().registerParameterType(Event.class, new EventParameterType());

        new BukkitRunnable() {
            public void run() {
                if (Stark.instance.getServerHandler().getFrozen()) {
                    return;
                }

                for (Event event : events) {
                    if (event.isActive()) {
                        event.tick();
                    }
                }
            }

        }.runTaskTimer(Hulu.getInstance(), 5L, 20L);

        Hulu.getInstance().getServer().getScheduler().runTaskTimer(Hulu.getInstance(), () -> {
            terminateKOTHs();
            activateKOTHs();
        }, 20L, 20L);
        // The initial delay of 5 ticks is to 'offset' us with the scoreboard handler.
    }

    public void loadEvents() {
        try {
            File eventsBase = new File(Hulu.getInstance().getDataFolder(), "events");

            if (!eventsBase.exists()) {
                eventsBase.mkdir();
            }

            for (EventType eventType : EventType.values()) {
                File subEventsBase = new File(eventsBase, eventType.name().toLowerCase());

                if (!subEventsBase.exists()) {
                    subEventsBase.mkdir();
                }

                for (File eventFile : subEventsBase.listFiles()) {
                    if (eventFile.getName().endsWith(".json")) {
                        events.add(Stark.getGson().fromJson(FileUtils.readFileToString(eventFile), eventType == EventType.KOTH ? KOTH.class : DTC.class));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // look for a previously active Event, if present deactivate and start it after 15 seconds
        events.stream().filter(Event::isActive).findFirst().ifPresent((event) -> {
            event.setActive(false);
            Bukkit.getScheduler().runTaskLater(Hulu.getInstance(), () -> {
                // if anyone had started a Event within the last 15 seconds,
                // don't activate previously active one
                if (events.stream().noneMatch(Event::isActive)) {
                    event.activate();
                }
            }, 300L);
        });
    }

    public void fillSchedule() {
        List<String> allevents = new ArrayList<>();

        for (Event event : getEvents()) {
            if (event.isHidden() || event.getName().equalsIgnoreCase("EOTW") || event.getName().equalsIgnoreCase("Citadel")) {
                continue;
            }

            allevents.add(event.getName());
        }

        for (int minute = 0; minute < 60; minute++) {
            for (int hour = 0; hour < 24; hour++) {
                this.EventSchedule.put(new EventScheduledTime(Calendar.getInstance().get(Calendar.DAY_OF_YEAR), (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + hour) % 24, minute), allevents.get(0));
            }
        }
    }

    public void loadSchedules() {
        EventSchedule.clear();

        try {
            File eventSchedule = new File(Hulu.getInstance().getDataFolder(), "eventSchedule.json");

            if (!eventSchedule.exists()) {
                eventSchedule.createNewFile();
                BasicDBObject schedule = new BasicDBObject();
                int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
                List<String> allevents = new ArrayList<>();

                for (Event event : getEvents()) {
                    if (event.isHidden() || event.getName().equalsIgnoreCase("EOTW") || event.getName().equalsIgnoreCase("Citadel")) {
                        continue;
                    }

                    allevents.add(event.getName());
                }

                for (int dayOffset = 0; dayOffset < 21; dayOffset++) {
                    int day = (currentDay + dayOffset) % 365;
                    EventScheduledTime[] times = new EventScheduledTime[]{

                            new EventScheduledTime(day, 0, 30), // 00:30am EST
                            new EventScheduledTime(day, 3, 30), // 03:30am EST
                            new EventScheduledTime(day, 6, 30), // 06:30am EST
                            new EventScheduledTime(day, 9, 30), // 09:30am EST
                            new EventScheduledTime(day, 12, 30), // 12:30pm EST
                            new EventScheduledTime(day, 15, 30), // 15:30pm EST
                            new EventScheduledTime(day, 18, 30), // 18:30pm EST
                            new EventScheduledTime(day, 21, 30) // 21:30pm EST

                    };

                    Collections.shuffle(allevents);

                    for (int eventTimeIndex = 0; eventTimeIndex < times.length; eventTimeIndex++) {
                        if (allevents.size() >= 1) {
                            EventScheduledTime eventTime = times[eventTimeIndex];
                            String eventName = allevents.get(eventTimeIndex % allevents.size());

                            schedule.put(eventTime.toString(), eventName);
                        }
                    }
                }

                FileUtils.write(eventSchedule, Stark.getGson().toJson(new JsonParser().parse(schedule.toString())));
            }

            BasicDBObject dbo = (BasicDBObject) JSON.parse(FileUtils.readFileToString(eventSchedule));

            if (dbo != null) {
                for (Map.Entry<String, Object> entry : dbo.entrySet()) {
                    EventScheduledTime scheduledTime = EventScheduledTime.parse(entry.getKey());
                    this.EventSchedule.put(scheduledTime, String.valueOf(entry.getValue()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveEvents() {
        try {
            File eventsBase = new File(Hulu.getInstance().getDataFolder(), "events");

            if (!eventsBase.exists()) {
                eventsBase.mkdir();
            }

            for (EventType eventType : EventType.values()) {

                File subEventsBase = new File(eventsBase, eventType.name().toLowerCase());

                if (!subEventsBase.exists()) {
                    subEventsBase.mkdir();
                }

                for (File eventFile : subEventsBase.listFiles()) {
                    eventFile.delete();
                }
            }

            for (Event event : events) {
                File eventFile = new File(new File(eventsBase, event.getType().name().toLowerCase()), event.getName() + ".json");
                FileUtils.write(eventFile, Stark.getGson().toJson(event));
                Bukkit.getLogger().info("Writing " + event.getName() + " to " + eventFile.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Event getEvent(String name) {
        for (Event event : events) {
            if (event.getName().equalsIgnoreCase(name)) {
                return (event);
            }
        }

        return (null);
    }

    private void activateKOTHs() {
        // Don't start a KOTH during EOTW.
        if (Hulu.getInstance().getServerHandler().isPreEOTW()) {
            return;
        }

        // Don't start a KOTH if another one is active.
        for (Event koth : Hulu.getInstance().getEventHandler().getEvents()) {
            if (koth.isActive()) {
                return;
            }
        }

        EventScheduledTime scheduledTime = EventScheduledTime.parse(new Date());

        if (Hulu.getInstance().getEventHandler().getEventSchedule().containsKey(scheduledTime)) {
            String resolvedName = Hulu.getInstance().getEventHandler().getEventSchedule().get(scheduledTime);
            Event resolved = Hulu.getInstance().getEventHandler().getEvent(resolvedName);

            if (scheduledTime.getHour() == 15 && scheduledTime.getMinutes() == 30 && resolvedName.equals("Conquest")) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "conquestadmin start");
                return;
            }

            if (resolved == null) {
                Hulu.getInstance().getLogger().warning("The event scheduler has a schedule for an event named " + resolvedName + ", but the event does not exist.");
                return;
            }

            resolved.activate();
        }
    }

    private void terminateKOTHs() {
        EventScheduledTime nextScheduledTime = EventScheduledTime.parse(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(30)));

        if (Hulu.getInstance().getEventHandler().getEventSchedule().containsKey(nextScheduledTime)) {
            // We have a KOTH about to start. Prepare for it.
            for (Event activeEvent : Hulu.getInstance().getEventHandler().getEvents()) {
                if (activeEvent.getType() != EventType.KOTH) continue;
                KOTH activeKoth = (KOTH) activeEvent;
                if (!activeKoth.isHidden() && activeKoth.isActive() && !activeKoth.getName().equals("Citadel") && !activeKoth.getName().equals("EOTW")) {
                    if (activeKoth.getCurrentCapper() != null && !activeKoth.isTerminate()) {
                        activeKoth.setTerminate(true);
                        Hulu.getInstance().getServer().broadcastMessage(ChatColor.GOLD + "[KingOfTheHill] " + ChatColor.BLUE + activeKoth.getName() + ChatColor.YELLOW + " will be terminated if knocked.");
                    } else {
                        activeKoth.deactivate();
                        Hulu.getInstance().getServer().broadcastMessage(ChatColor.GOLD + "[KingOfTheHill] " + ChatColor.BLUE + activeKoth.getName() + ChatColor.YELLOW + " has been terminated.");
                    }
                }
            }
        }
    }

}
