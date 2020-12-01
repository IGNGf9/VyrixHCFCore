/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.events.citadel.events;

import lombok.Getter;
import org.bson.types.ObjectId;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CitadelCapturedEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    @Getter
    private final ObjectId capper;

    @Getter
    private final Player player;

    public CitadelCapturedEvent(ObjectId capper, Player player) {
        this.capper = capper;
        this.player = player;
    }

    public CitadelCapturedEvent(boolean isAsync, ObjectId capper, Player player) {
        super(isAsync);
        this.capper = capper;
        this.player = player;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

}