/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.pvpclasses.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.hcriots.hcfactions.pvpclasses.PvPClass;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@AllArgsConstructor
public class BardRestoreEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    @Getter
    private final Player player;
    @Getter
    private final PvPClass.SavedPotion potions;

    public static HandlerList getHandlerList() {
        return (handlers);
    }

    public HandlerList getHandlers() {
        return (handlers);
    }

}