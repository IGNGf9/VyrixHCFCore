/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.server.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@RequiredArgsConstructor
public class CrowbarSpawnerBreakEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    @Getter
    private final Player player;
    @Getter
    private final Block block;
    @Getter
    @Setter
    private boolean cancelled;

    public static HandlerList getHandlerList() {
        return (handlers);
    }

    public HandlerList getHandlers() {
        return (handlers);
    }

}
