/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.persist.maps;

import net.hcriots.hcfactions.persist.PersistMap;

import java.util.UUID;

public class StartingPvPTimerMap extends PersistMap<Boolean> {

    public StartingPvPTimerMap() {
        super("StartingTimer", "StartingTimer");
    }

    @Override
    public String getRedisValue(Boolean value) {
        return value.toString();
    }

    @Override
    public Object getMongoValue(Boolean value) {
        return value.toString();
    }

    @Override
    public Boolean getJavaObject(String string) {
        return Boolean.valueOf(string);
    }

    @Override
    public void setCredits(int i) {

    }

    public void set(UUID uuid, boolean value) {
        updateValueAsync(uuid, value);
    }

    public boolean get(UUID uuid) {
        return contains(uuid) && getValue(uuid);
    }

}
