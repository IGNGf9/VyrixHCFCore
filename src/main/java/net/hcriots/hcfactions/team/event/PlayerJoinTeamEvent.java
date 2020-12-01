/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.team.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.hcriots.hcfactions.team.Team;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@AllArgsConstructor
@Getter
public class PlayerJoinTeamEvent extends Event {

    @Getter
    private static final HandlerList handlerList = new HandlerList();

    private final Player player;
    private final Team team;

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

}
