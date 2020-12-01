/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.server.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@AllArgsConstructor
public class EnderpearlCooldownAppliedEvent extends Event {

    @Getter
    private static final HandlerList handlerList = new HandlerList();

    @Getter
    private final Player player;

    @Getter
    @Setter
    private long timeToApply;

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

}
