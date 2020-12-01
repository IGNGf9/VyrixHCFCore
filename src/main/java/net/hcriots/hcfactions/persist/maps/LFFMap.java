/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.persist.maps;

import net.hcriots.hcfactions.persist.PersistMap;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class LFFMap extends PersistMap<Long> {

    public LFFMap() {
        super("LFFMap", "LFFMap");
    }

    @Override
    public String getRedisValue(Long aLong) {
        return String.valueOf(aLong);
    }

    @Override
    public Object getMongoValue(Long aLong) {
        return new Date(aLong);
    }

    @Override
    public Long getJavaObject(String str) {
        return Long.parseLong(str);
    }

    @Override
    public void setCredits(int i) {

    }

    public long getCooldown(UUID check) {
        return (contains(check) ? getValue(check) : 0L);
    }

    public boolean hasCooldown(UUID uuid) {
        return contains(uuid);
    }

    public void setCurrentTime(UUID uuid) {
        updateValueAsync(uuid, System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5));
    }
}
