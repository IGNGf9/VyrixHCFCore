/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.persist.maps;

import net.hcriots.hcfactions.persist.PersistMap;
import net.hcriots.hcfactions.tab.TabListMode;

import java.util.UUID;

/**
 * Created by InspectMC
 * Date: 7/31/2020
 * Time: 7:19 PM
 */

public class TabListModeMap extends PersistMap<TabListMode> {

    public TabListModeMap() {
        super("TabListInfo", "TabListInfo", false);
    }

    @Override
    public String getRedisValue(TabListMode toggled) {
        return (toggled.name());
    }

    @Override
    public TabListMode getJavaObject(String str) {
        if (str.equals("VANILLA")) return TabListMode.DETAILED;
        return (TabListMode.valueOf(str));
    }

    @Override
    public void setCredits(int i) {

    }

    @Override
    public Object getMongoValue(TabListMode toggled) {
        return (toggled);
    }

    public void setTabListMode(UUID update, TabListMode mode) {
        updateValueAsync(update, mode);
    }

    public TabListMode getTabListMode(UUID check) {
        return (contains(check) ? getValue(check) : TabListMode.DETAILED);
    }

}