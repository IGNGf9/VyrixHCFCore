/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.persist.maps;

import net.hcriots.hcfactions.persist.PersistMap;

import java.util.UUID;

public class KitmapTokensMap extends PersistMap<Integer> {

    public KitmapTokensMap() {
        super("Tokens", "KitMap.Tokens");
    }

    @Override
    public String getRedisValue(Integer tokens) {
        return (String.valueOf(tokens));
    }

    @Override
    public Integer getJavaObject(String str) {
        return (Integer.parseInt(str));
    }

    @Override
    public void setCredits(int i) {

    }

    @Override
    public Object getMongoValue(Integer tokens) {
        return (tokens);
    }

    public int getTokens(UUID check) {
        return (contains(check) ? getValue(check) : 0);
    }

    public void setTokens(UUID update, int tokens) {
        updateValueAsync(update, tokens);
    }

}