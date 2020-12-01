/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.persist.maps;

import net.hcriots.hcfactions.persist.PersistMap;

import java.util.Date;
import java.util.UUID;

public class OppleMap extends PersistMap<Long> {

    public OppleMap() {
        super("OppleCooldowns", "OppleCooldown");
    }

    @Override
    public String getRedisValue(Long time) {
        return (String.valueOf(time));
    }

    @Override
    public Long getJavaObject(String str) {
        return (Long.parseLong(str));
    }

    @Override
    public void setCredits(int i) {

    }

    @Override
    public Object getMongoValue(Long time) {
        return (new Date(time));
    }

    public boolean isOnCooldown(UUID check) {
        return (getValue(check) != null && getValue(check) > System.currentTimeMillis());
    }

    public void useGoldenApple(UUID update, long seconds) {
        updateValueAsync(update, System.currentTimeMillis() + (seconds * 1000));
    }

    public void resetCooldown(UUID update) {
        updateValueAsync(update, 0L);
    }

    public long getCooldown(UUID check) {
        return (contains(check) ? getValue(check) : -1L);
    }

}