/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.deathmessage.trackers;

import net.hcriots.hcfactions.deathmessage.DeathMessageHandler;
import net.hcriots.hcfactions.deathmessage.event.CustomPlayerDamageEvent;
import net.hcriots.hcfactions.deathmessage.objects.Damage;
import net.hcriots.hcfactions.deathmessage.objects.PlayerDamage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class VoidTracker implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void onCustomPlayerDamage(CustomPlayerDamageEvent event) {
        if (event.getCause().getCause() != EntityDamageEvent.DamageCause.VOID) {
            return;
        }

        List<Damage> record = DeathMessageHandler.getDamage(event.getPlayer());
        Damage knocker = null;
        long knockerTime = 0L;

        if (record != null) {
            for (Damage damage : record) {
                if (damage instanceof VoidDamage || damage instanceof VoidDamageByPlayer) {
                    continue;
                }

                if (damage instanceof PlayerDamage && (knocker == null || damage.getTime() > knockerTime)) {
                    knocker = damage;
                    knockerTime = damage.getTime();
                }
            }
        }

        if (knocker != null && knockerTime + TimeUnit.MINUTES.toMillis(1) > System.currentTimeMillis()) {
            event.setTrackerDamage(new VoidDamageByPlayer(event.getPlayer().getName(), event.getDamage(), ((PlayerDamage) knocker).getDamager()));
        } else {
            event.setTrackerDamage(new VoidDamage(event.getPlayer().getName(), event.getDamage()));
        }
    }

    public static class VoidDamage extends Damage {

        public VoidDamage(String damaged, double damage) {
            super(damaged, damage);
        }

        public String getDeathMessage() {
            return (wrapName(getDamaged()) + " fell into the void.");
        }

    }

    public static class VoidDamageByPlayer extends PlayerDamage {

        public VoidDamageByPlayer(String damaged, double damage, String damager) {
            super(damaged, damage, damager);
        }

        public String getDeathMessage() {
            return (wrapName(getDamaged()) + " fell into the void thanks to " + wrapName(getDamager()) + ".");
        }

    }

}