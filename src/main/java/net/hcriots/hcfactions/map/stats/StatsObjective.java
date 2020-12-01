/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.map.stats;

import lombok.Getter;

/**
 * Created by InspectMC
 * Date: 6/24/2020
 * Time: 6:57 PM
 */
@Getter
public enum StatsObjective {

    KILLS("Kills", "k"),
    DEATHS("Deaths", "d"),
    KD("KD", "kdr"),
    HIGHEST_KILLSTREAK("killstreak", "ks");

    private final String name;
    private final String[] aliases;

    StatsObjective(String name, String... aliases) {
        this.name = name;
        this.aliases = aliases;
    }
}