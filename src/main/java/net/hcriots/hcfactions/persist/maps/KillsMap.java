/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.persist.maps;

import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.persist.PersistMap;

import java.util.UUID;

public class KillsMap extends PersistMap<Integer> {

    public KillsMap() {
        super("Kills", "Kills");
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
    public Object getMongoValue(Integer kills) {
        return (kills);
    }

    public int getKills(UUID check) {
        return (contains(check) ? getValue(check) : 0);
    }

    public void setKills(UUID update, int kills) {
        updateValueAsync(update, kills);
        Hulu.getInstance().getKdrMap().updateKDR(update);
    }
}