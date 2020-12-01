/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.events.conquest;

import lombok.Getter;
import lombok.Setter;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.events.conquest.game.ConquestGame;
import org.bukkit.ChatColor;

public class ConquestHandler {

    public static final String PREFIX = ChatColor.YELLOW + "[Conquest]";

    public static final int POINTS_DEATH_PENALTY = 20;
    public static final String KOTH_NAME_PREFIX = "Conquest-";
    public static final int TIME_TO_CAP = 30;

    @Getter
    @Setter
    private ConquestGame game;

    public static int getPointsToWin() {
        return Hulu.getInstance().getConfig().getInt("conquestWinPoints", 250);
    }
}