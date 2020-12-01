package net.hcriots.hcfactions.pvpclasses.event;

import net.hcriots.hcfactions.pvpclasses.PvPClass;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public final class ArmorEquipEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final PvPClass type;
    private boolean cancel = false;


    public ArmorEquipEvent(final Player player, final PvPClass type) {
        super(player);
        this.type = type;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * Gets a list of handlers handling this event.
     *
     * @return A list of handlers handling this event.
     */
    @Override
    public final HandlerList getHandlers() {
        return handlers;
    }

    /**
     * Gets if this event is cancelled.
     *
     * @return If this event is cancelled
     */
    public final boolean isCancelled() {
        return cancel;
    }

    /**
     * Sets if this event should be cancelled.
     *
     * @param cancel If this event should be cancelled.
     */
    public final void setCancelled(final boolean cancel) {
        this.cancel = cancel;
    }

    public final PvPClass getType() {
        return type;
    }
}
