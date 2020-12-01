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
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public abstract class PersistMap<T> {

    private final String keyPrefix;
    private final String mongoName;
    private final boolean useMongo;
    protected Map<UUID, T> wrappedMap = new ConcurrentHashMap<>();

    public PersistMap(String keyPrefix, String mongoName) {
        this(keyPrefix, mongoName, true); // use mongo by default
    }

    public PersistMap(String keyPrefix, String mongoName, boolean useMongo) {
        this.keyPrefix = keyPrefix;
        this.mongoName = mongoName;
        this.useMongo = useMongo;

        loadFromRedis();
    }

    public void loadFromRedis() {
        Stark.instance.getCore().getRedis().runRedisCommand(redis -> {
            Map<String, String> results = redis.hgetAll(keyPrefix);

            for (Map.Entry<String, String> resultEntry : results.entrySet()) {
                T object = getJavaObjectSafe(resultEntry.getKey(), resultEntry.getValue());

                if (object != null) {
                    wrappedMap.put(UUID.fromString(resultEntry.getKey()), object);
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

    protected void updateValueSync(final UUID key, final T value) {
        wrappedMap.put(key, value);

        Stark.instance.getCore().getRedis().<T>runRedisCommand(redis -> {
            redis.hset(keyPrefix, key.toString(), getRedisValue(getValue(key)));

            boolean succeeded = false;
            int tries = 0;
            while (useMongo && !succeeded && tries++ < 5) {
                try {
                    DBCollection playersCollection = Hulu.getInstance().getMongoPool().getDB(Hulu.MONGO_DB_NAME).getCollection("Players");
                    BasicDBObject player = new BasicDBObject("_id", key.toString().replace("-", ""));

                    BasicDBObject toSet = new BasicDBObject(mongoName, getMongoValue(getValue(key)));
                    toSet.put("lastUsername", Stark.instance.getCore().getUuidCache().name(key));

                    playersCollection.update(player, new BasicDBObject("$set", toSet), true, false);
                    succeeded = true;
                } catch (Exception e) {
                    succeeded = false;
                }
            }

            if (useMongo && !succeeded) {
                throw new RuntimeException("Mongo insert failed after five retries! User: " + key + ".");
            }
            return (null);
        });
    }

    protected void updateValueAsync(final UUID key, T value) {
        wrappedMap.put(key, value);

        new BukkitRunnable() {

            public void run() {
                Stark.instance.getCore().getRedis().runRedisCommand(redis -> {
                    redis.hset(keyPrefix, key.toString(), getRedisValue(getValue(key)));

                    boolean succeeded = false;
                    int tries = 0;
                    while (useMongo && !succeeded && tries++ < 5) {
                        try {
                            DBCollection playersCollection = Hulu.getInstance().getMongoPool().getDB(Hulu.MONGO_DB_NAME).getCollection("Players");
                            BasicDBObject player = new BasicDBObject("_id", key.toString().replace("-", ""));

                            BasicDBObject toSet = new BasicDBObject(mongoName, getMongoValue(getValue(key)));
                            toSet.put("lastUsername", Stark.instance.getCore().getUuidCache().name(key));

                            playersCollection.update(player, new BasicDBObject("$set", toSet), true, false);
                            succeeded = true;
                        } catch (Exception e) {
                            succeeded = false;
                        }
                    }

                    if (useMongo && !succeeded) {
                        throw new RuntimeException("Mongo update failed after five retries! User: " + key + ".");
                    }
                    return (null);
                });
            }

        }.runTaskAsynchronously(Hulu.getInstance());
    }

    protected T getValue(UUID key) {
        return (wrappedMap.get(key));
    }

    protected boolean contains(UUID key) {
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

    public abstract void setCredits(int i);
}