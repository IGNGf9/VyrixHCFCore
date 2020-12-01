/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.persist.maps;

import net.hcriots.hcfactions.persist.PersistMap;

import java.util.UUID;

public class FishingKitMap extends PersistMap<Integer> {

    public FishingKitMap() {
        super("FishingKitUses", "FishingKitUses");
    }

    @Override
    public String getRedisValue(Integer uses) {
        return (String.valueOf(uses));
    }

    @Override
    public Integer getJavaObject(String str) {
        return (Integer.parseInt(str));
    }

    @Override
    public void setCredits(int i) {

    }

    @Override
    public Object getMongoValue(Integer uses) {
        return (uses);
    }

    public int getUses(UUID check) {
        return (contains(check) ? getValue(check) : 0);
    }

    public void setUses(UUID update, int uses) {
        updateValueAsync(update, uses);
    }

}