/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.server.event;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class BackstabKillEvent extends PlayerEvent {

    @Getter
    private static final HandlerList handlerList = new HandlerList();

    @Getter
    private final Player killed;
    @Getter
    @Setter
    private boolean allowed = false;

    public BackstabKillEvent(Player who, Player killed) {
        super(who);
        this.killed = killed;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

}
