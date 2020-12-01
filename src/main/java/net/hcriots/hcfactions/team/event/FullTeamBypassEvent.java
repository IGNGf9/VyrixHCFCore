/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.team.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.hcriots.hcfactions.team.Team;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@RequiredArgsConstructor
@Getter
public class FullTeamBypassEvent extends Event {

    @Getter
    private static final HandlerList handlerList = new HandlerList();

    private final Player player;
    private final Team team;
    @Setter
    private boolean allowBypass = false;
    @Setter
    private int extraSlots = 0;

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

}
