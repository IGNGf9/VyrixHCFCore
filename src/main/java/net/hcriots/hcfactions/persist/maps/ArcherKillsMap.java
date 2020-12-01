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
 * Date: 7/6/2020
 * Time: 3:48 PM
 */

public class ArcherKillsMap extends PersistMap<Integer> {

    public ArcherKillsMap() {
        super("ArcherKills", "KitMap.ArcherKills");
    }

    @Override
    public String getRedisValue(Integer integer) {
        return integer.toString();
    }

    @Override
    public Integer getJavaObject(String str) {
        return Integer.valueOf(str);
    }

    @Override
    public void setCredits(int i) {

    }

    @Override
    public Object getMongoValue(Integer integer) {
        return integer;
    }

    public int getArcherKills(UUID check) {
        return (contains(check) ? getValue(check) : 0);
    }

    public void setArcherKills(UUID update, int archerKills) {
        updateValueAsync(update, archerKills);
    }

    public void increment(UUID update) {
        setArcherKills(update, getArcherKills(update) + 1);
    }

}