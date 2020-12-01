/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.deathmessage.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.hcriots.hcfactions.deathmessage.objects.Damage;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;

@AllArgsConstructor
public class CustomPlayerDamageEvent extends Event {

    private static final HandlerList handlerList = new HandlerList();

    @Getter
    private final EntityDamageEvent cause;
    @Getter
    @Setter
    private Damage trackerDamage;

    public static HandlerList getHandlerList() {
        return (handlerList);
    }

    public Player getPlayer() {
        return ((Player) cause.getEntity());
    }

    public double getDamage() {
        return (cause.getDamage());
    }

    public HandlerList getHandlers() {
        return (handlerList);
    }

}