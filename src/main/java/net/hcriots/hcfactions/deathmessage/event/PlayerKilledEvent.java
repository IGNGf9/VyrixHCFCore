/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.deathmessage.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@AllArgsConstructor
public class PlayerKilledEvent extends Event {

    private static final HandlerList handlerList = new HandlerList();

    @Getter
    private final Player killer;
    @Getter
    private final Player victim;

    public static HandlerList getHandlerList() {
        return (handlerList);
    }

    public HandlerList getHandlers() {
        return (handlerList);
    }

}
