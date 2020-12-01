/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.persist.maps;

import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.persist.PersistMap;

import java.util.UUID;

public class KDRMap extends PersistMap<Double> {

    public KDRMap() {
        super("KDR", "KDR");
    }

    @Override
    public String getRedisValue(Double kdr) {
        return (String.valueOf(kdr));
    }

    @Override
    public Double getJavaObject(String str) {
        return (Double.parseDouble(str));
    }

    @Override
    public void setCredits(int i) {

    }

    @Override
    public Object getMongoValue(Double kdr) {
        return (kdr);
    }

    public void setKDR(UUID update, double kdr) {
        updateValueAsync(update, kdr);
    }

    public void updateKDR(UUID update) {
        setKDR(update, ((double) Hulu.getInstance().getKillsMap().getKills(update)) / Math.max(Hulu.getInstance().getDeathsMap().getDeaths(update), 1));
    }
}
