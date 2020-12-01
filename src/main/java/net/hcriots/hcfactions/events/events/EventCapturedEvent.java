/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.events.events;

import lombok.Getter;
import lombok.Setter;
import net.hcriots.hcfactions.events.Event;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class EventCapturedEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    @Getter
    private final Event event;
    @Getter
    @Setter
    private boolean cancelled;

    public EventCapturedEvent(Event event, Player capper) {
        super(capper);

        this.event = event;
    }

    public static HandlerList getHandlerList() {
        return (handlers);
    }

    public HandlerList getHandlers() {
        return (handlers);
    }

}