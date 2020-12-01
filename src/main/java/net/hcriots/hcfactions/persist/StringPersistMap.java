/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.persist;

import cc.fyre.stark.Stark;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import net.hcriots.hcfactions.Hulu;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class StringPersistMap<T> {

    private final String keyPrefix;
    private final String mongoName;
    protected Map<String, T> wrappedMap = new ConcurrentHashMap<>();

    public StringPersistMap(String keyPrefix, String mongoName) {
        this.keyPrefix = keyPrefix;
        this.mongoName = mongoName;

        loadFromRedis();
    }

    public void loadFromRedis() {
        Stark.instance.getCore().getRedis().runRedisCommand(redis -> {
            Map<String, String> results = redis.hgetAll(keyPrefix);

            for (Map.Entry<String, String> resultEntry : results.entrySet()) {
                T object = getJavaObjectSafe(resultEntry.getKey(), resultEntry.getValue());

                if (object != null) {
                    wrappedMap.put(resultEntry.getKey(), object);
                }
            }

            return (null);
        });
    }

    protected void wipeValues() {
        wrappedMap.clear();

        Stark.instance.getCore().getRedis().runRedisCommand(redis -> {
            redis.del(keyPrefix);
            return (null);
        });
    }

    protected void updateValueSync(final String key, final T value) {
        wrappedMap.put(key, value);

        Stark.instance.getCore().getRedis().runRedisCommand(redis -> {
            redis.hset(keyPrefix, key, getRedisValue(getValue(key)));

            DBCollection playersCollection = Hulu.getInstance().getMongoPool().getDB(Hulu.MONGO_DB_NAME).getCollection("Players");
            BasicDBObject player = new BasicDBObject("_id", key.replace("-", ""));

            playersCollection.update(player, new BasicDBObject("$set", new BasicDBObject(mongoName, getMongoValue(getValue(key)))), true, false);
            return (null);
        });
    }

    protected void updateValueAsync(final String key, T value) {
        wrappedMap.put(key, value);

        new BukkitRunnable() {
            public void run() {
                Stark.instance.getCore().getRedis().runRedisCommand(redis -> {
                    redis.hset(keyPrefix, key, getRedisValue(getValue(key)));

                    DBCollection playersCollection = Hulu.getInstance().getMongoPool().getDB(Hulu.MONGO_DB_NAME).getCollection("Players");
                    BasicDBObject player = new BasicDBObject("_id", key.replace("-", ""));

                    playersCollection.update(player, new BasicDBObject("$set", new BasicDBObject(mongoName, getMongoValue(getValue(key)))), true, false);
                    return (null);
                });
            }
        }.runTaskAsynchronously(Hulu.getInstance());
    }

    protected T getValue(String key) {
        return (wrappedMap.get(key));
    }

    protected boolean contains(String key) {
        return (wrappedMap.containsKey(key));
    }

    public abstract String getRedisValue(T t);

    public abstract Object getMongoValue(T t);

    public T getJavaObjectSafe(String key, String redisValue) {
        try {
            return (getJavaObject(redisValue));
        } catch (Exception e) {
            System.out.println("Error parsing Redis result.");
            System.out.println(" - Prefix: " + keyPrefix);
            System.out.println(" - Key: " + key);
            System.out.println(" - Value: " + redisValue);
            return (null);
        }
    }

    public abstract T getJavaObject(String str);

}