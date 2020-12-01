/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.map.killstreaks;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class PersistentKillstreak {

    @Getter
    private final String name;
    @Getter
    private final int killsRequired;

    public boolean matchesExactly(int kills) {
        return kills == killsRequired;
    }

    public boolean check(int count) {
        return killsRequired <= count;
    }

    public void apply(Player player) {
    }

}
