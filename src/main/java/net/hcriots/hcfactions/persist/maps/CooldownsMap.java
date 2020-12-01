/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.persist.maps;

import net.hcriots.hcfactions.persist.PersistMap;

import java.util.UUID;

/**
 * Created by InspectMC
 * Date: 7/29/2020
 * Time: 4:01 PM
 */

public class CooldownsMap extends PersistMap<Boolean> {

    public CooldownsMap() {
        super("CooldownsShown", "Cooldowns");
    }

    @Override
    public String getRedisValue(Boolean toggled) {
        return (String.valueOf(toggled));
    }

    @Override
    public Boolean getJavaObject(String str) {
        return (Boolean.valueOf(str));
    }

    @Override
    public void setCredits(int i) {

    }

    @Override
    public Object getMongoValue(Boolean toggled) {
        return (toggled);
    }

    public void setCooldowns(UUID update, boolean toggled) {
        updateValueAsync(update, toggled);
    }

    public boolean isCooldownToggled(UUID check) {
        return (contains(check) ? getValue(check) : true);
    }

}