/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.persist.maps;

import net.hcriots.hcfactions.persist.PersistMap;

import java.util.UUID;

public class FriendLivesMap extends PersistMap<Integer> {

    public FriendLivesMap() {
        super("FriendLives", "Lives.Friend");
    }

    @Override
    public String getRedisValue(Integer lives) {
        return (String.valueOf(lives));
    }

    @Override
    public Integer getJavaObject(String str) {
        return (Integer.parseInt(str));
    }

    @Override
    public void setCredits(int i) {

    }

    @Override
    public Object getMongoValue(Integer lives) {
        return (lives);
    }

    public int getLives(UUID check) {
        return (contains(check) ? getValue(check) : 0);
    }

    public void setLives(UUID update, int lives) {
        updateValueSync(update, lives);
    }

}