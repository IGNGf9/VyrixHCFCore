/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.persist.maps;

import net.hcriots.hcfactions.persist.PersistMap;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

public class DeathbanMap extends PersistMap<Long> {

    public DeathbanMap() {
        super("Deathbans", "Deathban");
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
        return Long.toString(time);
    }

    public boolean isDeathbanned(UUID check) {
        if (getValue(check) != null) {
            return (getValue(check) > System.currentTimeMillis());
        }

        return (false);
    }

    public void deathban(UUID update, long seconds) {
        updateValueAsync(update, System.currentTimeMillis() + (seconds * 1000));
    }

    public void revive(UUID update) {
        updateValueAsync(update, 0L);
    }

    public long getDeathban(UUID check) {
        return (contains(check) ? getValue(check) : 0L);
    }

    public void wipeDeathbans() {
        wipeValues();
    }

    public Collection<UUID> getDeathbannedPlayers() {
        Collection<UUID> deathbannedPlayers = new HashSet<>();

        for (Map.Entry<UUID, Long> entry : wrappedMap.entrySet()) {
            if (isDeathbanned(entry.getKey())) {
                deathbannedPlayers.add(entry.getKey());
            }
        }

        return (deathbannedPlayers);
    }

}