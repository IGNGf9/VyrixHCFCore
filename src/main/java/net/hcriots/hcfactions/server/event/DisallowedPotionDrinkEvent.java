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
import org.bukkit.potion.Potion;

public class DisallowedPotionDrinkEvent extends PlayerEvent {

    @Getter
    private static final HandlerList handlerList = new HandlerList();

    @Getter
    private final Potion potion;
    @Getter
    @Setter
    private boolean allowed = false;

    public DisallowedPotionDrinkEvent(Player who, Potion potion) {
        super(who);
        this.potion = potion;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

}
