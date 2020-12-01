/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.tab;

import lombok.AllArgsConstructor;

/**
 * Created by InspectMC
 * Date: 7/31/2020
 * Time: 7:18 PM
 */

@AllArgsConstructor
public enum TabListMode {

    DETAILED("Normal"),
    DETAILED_WITH_FACTION_INFO("Normal w/ Team List");

    private final String name;

    public String getName() {
        return name;
    }

}
