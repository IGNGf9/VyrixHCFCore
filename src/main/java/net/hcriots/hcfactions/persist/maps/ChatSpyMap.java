/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.persist.maps;

import net.hcriots.hcfactions.persist.PersistMap;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChatSpyMap extends PersistMap<List<ObjectId>> {

    public ChatSpyMap() {
        super("ChatSpy", "ChatSpy");
    }

    @Override
    public String getRedisValue(List<ObjectId> teams) {
        StringBuilder stringBuilder = new StringBuilder();

        for (ObjectId team : teams) {
            stringBuilder.append(team).append(",");
        }

        if (stringBuilder.length() > 1) {
            stringBuilder.setLength(stringBuilder.length() - 1);
        }

        return (stringBuilder.toString());
    }

    @Override
    public List<ObjectId> getJavaObject(String str) {
        List<ObjectId> results = new ArrayList<>();

        for (String split : str.split(",")) {
            if (split.equals("")) {
                continue;
            }

            results.add(new ObjectId(split));
        }

        return (results);
    }

    @Override
    public void setCredits(int i) {

    }

    @Override
    public Object getMongoValue(List<ObjectId> teams) {
        return (teams);
    }

    public List<ObjectId> getChatSpy(UUID check) {
        return (contains(check) ? getValue(check) : new ArrayList<ObjectId>());
    }

    public void setChatSpy(UUID update, List<ObjectId> teams) {
        updateValueAsync(update, teams);
    }

}