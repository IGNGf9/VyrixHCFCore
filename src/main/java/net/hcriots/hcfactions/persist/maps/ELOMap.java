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
 * Date: 6/23/2020
 * Time: 2:54 AM
 */
public class ELOMap extends PersistMap<Integer> {

    public ELOMap() {
        super("ELO", "ELO");
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

    public int getELO(UUID check) {
        return (contains(check) ? getValue(check) : 0);
    }

    public void setELO(UUID update, int elo) {
        updateValueAsync(update, elo);
    }
}