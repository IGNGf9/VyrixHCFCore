/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.persist.maps;

import net.hcriots.hcfactions.persist.PersistMap;

import java.util.Map;
import java.util.UUID;

/**
 * ---------- hcteams ----------
 * Created by Fraser.Cumming on 29/03/2016.
 * © 2016 Fraser Cumming All Rights Reserved
 */
public class WhitelistedIPMap extends PersistMap<UUID> {

    public WhitelistedIPMap() {
        super("whitelistedipaddresses", "whitelistedipaddresses");
    }

    @Override
    public String getRedisValue(UUID uuid) {
        return uuid.toString();
    }

    @Override
    public Object getMongoValue(UUID uuid) {
        return uuid.toString();
    }

    @Override
    public UUID getJavaObject(String str) {
        return UUID.fromString(str);
    }

    @Override
    public void setCredits(int i) {

    }

    public boolean contains(UUID id) {
        return super.contains(id);
    }

    public void add(UUID id, UUID ass) {
        this.updateValueAsync(id, ass);
    }

    public UUID get(UUID id) {
        return super.getValue(id);
    }

    public boolean containsValue(UUID id) {
        return wrappedMap.containsValue(id);
    }

    public Map<UUID, UUID> getMap() {
        return wrappedMap;
    }
}
