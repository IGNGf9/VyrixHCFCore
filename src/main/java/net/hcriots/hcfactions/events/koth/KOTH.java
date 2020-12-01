/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.events.koth;

import lombok.Getter;
import lombok.Setter;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.events.Event;
import net.hcriots.hcfactions.events.EventType;
import net.hcriots.hcfactions.events.events.EventActivatedEvent;
import net.hcriots.hcfactions.events.events.EventCapturedEvent;
import net.hcriots.hcfactions.events.events.EventDeactivatedEvent;
import net.hcriots.hcfactions.events.koth.events.EventControlTickEvent;
import net.hcriots.hcfactions.events.koth.events.KOTHControlLostEvent;
import net.hcriots.hcfactions.team.Team;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockVector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KOTH implements Event {

    @Getter
    private final String name;
    @Getter
    private final EventType type = EventType.KOTH;
    @Getter
    public boolean koth = true;
    @Getter
    @Setter
    boolean active;
    @Getter
    private BlockVector capLocation;
    @Getter
    private String world;
    @Getter
    private int capDistance;
    @Getter
    private int capTime;
    @Getter
    private boolean hidden = false;
    @Getter
    private transient String currentCapper;
    @Getter
    private transient int remainingCapTime;
    @Getter
    @Setter
    private transient boolean terminate;

    public KOTH(String name, Location location) {
        this.name = name;
        this.capLocation = location.toVector().toBlockVector();
        this.world = location.getWorld().getName();
        this.capDistance = 3;
        this.capTime = 60 * 15;
        this.terminate = false;

        Hulu.getInstance().getEventHandler().getEvents().add(this);
        Hulu.getInstance().getEventHandler().saveEvents();
    }

    public void setLocation(Location location) {
        this.capLocation = location.toVector().toBlockVector();
        this.world = location.getWorld().getName();
        Hulu.getInstance().getEventHandler().saveEvents();
    }

    public void setCapDistance(int capDistance) {
        this.capDistance = capDistance;
        Hulu.getInstance().getEventHandler().saveEvents();
    }

    public void setCapTime(int capTime) {
        int oldCapTime = this.capTime;
        this.capTime = capTime;

        if (this.remainingCapTime > capTime) {
            this.remainingCapTime = capTime;
        } else if (remainingCapTime == oldCapTime) { // this will catch the time going up
            this.remainingCapTime = capTime;
        }

        Hulu.getInstance().getEventHandler().saveEvents();
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
        Hulu.getInstance().getEventHandler().saveEvents();
    }

    public boolean activate() {
        if (active) {
            return (false);
        }

        Hulu.getInstance().getServer().getPluginManager().callEvent(new EventActivatedEvent(this));

        this.active = true;
        this.currentCapper = null;
        this.remainingCapTime = this.capTime;
        this.terminate = false;

        return (true);
    }

    public boolean deactivate() {
        if (!active) {
            return (false);
        }

        Hulu.getInstance().getServer().getPluginManager().callEvent(new EventDeactivatedEvent(this));

        this.active = false;
        this.currentCapper = null;
        this.remainingCapTime = this.capTime;
        this.terminate = false;

        return (true);
    }

    public void startCapping(Player player) {
        if (currentCapper != null) {
            resetCapTime();
        }

        this.currentCapper = player.getName();
        this.remainingCapTime = capTime;
    }

    public boolean finishCapping() {
        Player capper = Hulu.getInstance().getServer().getPlayerExact(currentCapper);

        if (capper == null) {
            resetCapTime();
            return (false);
        }

        EventCapturedEvent event = new EventCapturedEvent(this, capper);
        Hulu.getInstance().getServer().getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            resetCapTime();
            return (false);
        }

        deactivate();
        return (true);
    }

    public void resetCapTime() {
        Hulu.getInstance().getServer().getPluginManager().callEvent(new KOTHControlLostEvent(this));

        this.currentCapper = null;
        this.remainingCapTime = capTime;

        if (terminate) {
            deactivate();
            Hulu.getInstance().getServer().broadcastMessage(ChatColor.BLUE + "[KOTH] " + ChatColor.WHITE + getName() + ChatColor.YELLOW + " has been terminated.");
        }
    }

    @Override
    public void tick() {
        if (currentCapper != null) {
            Player capper = Hulu.getInstance().getServer().getPlayerExact(currentCapper);

            if (capper == null || !onCap(capper.getLocation()) || capper.isDead() || capper.getGameMode() != GameMode.SURVIVAL || capper.hasMetadata("invisible")) {
                resetCapTime();
            } else {
                if (remainingCapTime % 60 == 0 && remainingCapTime > 1 && !isHidden()) {
                    Team team = Hulu.getInstance().getTeamHandler().getTeam(capper);

                    if (team != null) {
                        for (Player player : Hulu.getInstance().getServer().getOnlinePlayers()) {
                            if (team.isMember(player.getUniqueId()) && capper != player) {
                                player.sendMessage(ChatColor.BLUE + "[KOTH]" + ChatColor.YELLOW + " Your team is controlling " + ChatColor.WHITE + getName() + ChatColor.YELLOW + ".");
                            }
                        }
                    }
                }

                if (remainingCapTime % 10 == 0 && remainingCapTime > 1 && !isHidden()) {
                    capper.sendMessage(ChatColor.GOLD + "[KingOfTheHill]" + ChatColor.YELLOW + " Attempting to control " + ChatColor.BLUE + getName() + ChatColor.YELLOW + ".");
                }

                if (remainingCapTime <= 0) {
                    finishCapping();
                } else {
                    Hulu.getInstance().getServer().getPluginManager().callEvent(new EventControlTickEvent(this));
                }

                this.remainingCapTime--;
            }
        } else {
            List<Player> onCap = new ArrayList<>();

            for (Player player : Hulu.getInstance().getServer().getOnlinePlayers()) {
                if (onCap(player.getLocation()) && !player.isDead() && player.getGameMode() == GameMode.SURVIVAL && !player.hasMetadata("invisible") && !Hulu.getInstance().getPvPTimerMap().hasTimer(player.getUniqueId())) {
                    onCap.add(player);
                }
            }

            Collections.shuffle(onCap);

            if (onCap.size() != 0) {
                startCapping(onCap.get(0));
            }
        }
    }

    public boolean onCap(Location location) {
        if (!location.getWorld().getName().equalsIgnoreCase(world)) {
            return (false);
        }

        int xDistance = Math.abs(location.getBlockX() - capLocation.getBlockX());
        int yDistance = Math.abs(location.getBlockY() - capLocation.getBlockY());
        int zDistance = Math.abs(location.getBlockZ() - capLocation.getBlockZ());

        return xDistance <= capDistance && yDistance <= 5 && zDistance <= capDistance;
    }

}