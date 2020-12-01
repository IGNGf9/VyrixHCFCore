/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.persist.maps;

import net.hcriots.hcfactions.persist.PersistMap;

import java.util.UUID;

public class CoalMinedMap extends PersistMap<Integer> {

    public CoalMinedMap() {
        super("CoalMined", "MiningStats.Coal");
    }

    @Override
    public String getRedisValue(Integer kills) {
        return (String.valueOf(kills));
    }

    @Override
    public Integer getJavaObject(String str) {
        return (Integer.parseInt(str));
    }

    @Override
    public void setCredits(int i) {

    }

    @Override
    public Object getMongoValue(Integer mined) {
        return (mined);
    }

    public int getMined(UUID check) {
        return (contains(check) ? getValue(check) : 0);
    }

    public void setMined(UUID update, int mined) {
        updateValueAsync(update, mined);
    }

}