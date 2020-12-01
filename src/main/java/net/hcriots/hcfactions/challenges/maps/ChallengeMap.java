/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.challenges.maps;

import net.hcriots.hcfactions.persist.PersistMap;

import java.util.UUID;

public class ChallengeMap extends PersistMap<Integer> {

    public ChallengeMap(String challengeName) {
        super("Challenges", "Challenges." + challengeName);
    }

    @Override
    public String getRedisValue(Integer amount) {
        return (String.valueOf(amount));
    }

    @Override
    public Integer getJavaObject(String str) {
        return (Integer.parseInt(str));
    }

    @Override
    public void setCredits(int i) {

    }

    @Override
    public Object getMongoValue(Integer amount) {
        return (amount);
    }

    public int getAmount(UUID check) {
        return (contains(check) ? getValue(check) : 0);
    }

    public void setAmount(UUID update, int amount) {
        updateValueAsync(update, amount);
    }
}
