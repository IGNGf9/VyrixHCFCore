/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 9:09 PM
 */

package net.hcriots.hcfactions.challenges.maps;

import net.hcriots.hcfactions.persist.PersistMap;

import java.util.UUID;

public class ChallengeCooldownMap extends PersistMap<Long> {

    public ChallengeCooldownMap(String challengeName) {
        super("ChallengesCooldown", "ChallengesCooldown." + challengeName);
    }

    @Override
    public String getRedisValue(Long aLong) {
        return String.valueOf(aLong);
    }

    @Override
    public Object getMongoValue(Long aLong) {
        return (aLong);
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

    public void setCooldown(UUID update, long amount) {
        updateValueAsync(update, (System.currentTimeMillis() + amount));
    }

    public boolean isOnCooldown(UUID uuid) {
        long now = System.currentTimeMillis();
        long til = getCooldown(uuid);
        return (now < til);
    }
}
