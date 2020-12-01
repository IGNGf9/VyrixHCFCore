/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.elo;

import net.hcriots.hcfactions.Hulu;
import org.bukkit.entity.Player;

/**
 * Created by InspectMC
 * Date: 6/23/2020
 * Time: 2:55 AM
 */
public class EloHandler {

    public static int getPlayerELO(Player player) {
        return Hulu.getInstance().getEloMap().getELO(player.getUniqueId());
    }

    public static int getLostELO() {
        return 10;
    }

    public static int getWinELO() {
        return 20;
    }
}