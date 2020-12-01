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
 * Date: 6/28/2020
 * Time: 4:48 PM
 */
public class CreditsMap extends PersistMap<Integer> {

    public CreditsMap() {
        super("Credits", "Credits");
    }

    @Override
    public String getRedisValue(Integer elo) {
        return (String.valueOf(elo));
    }

    @Override
    public Integer getJavaObject(String str) {
        return (Integer.parseInt(str));
    }

    @Override
    public void setCredits(int i) {

    }

    @Override
    public Object getMongoValue(Integer elo) {
        return (elo);
    }

    public int getCredits(UUID check) {
        return (contains(check) ? getValue(check) : 0);
    }

    public void setCredits(UUID update, int credits) {
        updateValueAsync(update, credits);
    }
}