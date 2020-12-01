/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.events;

public interface Event {
    String getName();

    boolean isActive();

    void setActive(boolean active);

    void tick();

    boolean isHidden();

    void setHidden(boolean hidden);

    boolean activate();

    boolean deactivate();

    EventType getType();
}
