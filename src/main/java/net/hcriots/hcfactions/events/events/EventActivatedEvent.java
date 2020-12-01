/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.events.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@AllArgsConstructor
public class EventActivatedEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    @Getter
    private final net.hcriots.hcfactions.events.Event event;

    public static HandlerList getHandlerList() {
        return (handlers);
    }

    public HandlerList getHandlers() {
        return (handlers);
    }

}