/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.scoreboard;

import cc.fyre.stark.engine.scoreboard.ScoreboardConfiguration;
import cc.fyre.stark.engine.scoreboard.TitleGetter;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.util.CC;

public class HuluScoreboardConfiguration {

    public static ScoreboardConfiguration create() {
        return new ScoreboardConfiguration(
                new TitleGetter(Hulu.getInstance().getConfig().getString("scoreboard.title").replace("%bar%", CC.VERTICAL_SEPARATOR)),
                new HuluScorboard()
        );
    }

}