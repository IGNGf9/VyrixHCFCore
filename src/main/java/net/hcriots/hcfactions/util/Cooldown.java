/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.util;

import com.google.gson.JsonObject;
import lombok.Data;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bson.Document;

@Data
public class Cooldown {

    private long start = System.currentTimeMillis();
    private long expire;
    private boolean notified = false;

    public Cooldown(long duration) {
        this.expire = this.start + duration;
    }

    public Cooldown(Document document) {
        this.start = document.getLong("start");
        this.expire = document.getLong("expire");
        this.notified = document.getBoolean("notified");
    }

    public Cooldown(JsonObject jsonObject) {
        this.start = jsonObject.get("start").getAsLong();
        this.expire = jsonObject.get("expire").getAsLong();
        this.notified = jsonObject.get("notified").getAsBoolean();
    }

    public Document toDocument() {
        return new Document("start", this.start)
                .append("expire", this.expire)
                .append("notified", this.notified);
    }

    public JsonObject toJson() {
        JsonObject object = new JsonObject();
        object.addProperty("start", this.start);
        object.addProperty("expire", this.expire);
        object.addProperty("notified", this.notified);
        return object;
    }

    public long getPassed() {
        return System.currentTimeMillis() - this.start;
    }

    public long getRemaining() {
        return this.expire - System.currentTimeMillis();
    }

    public boolean hasExpired() {
        return System.currentTimeMillis() - this.expire >= 0;
    }

    public String getTimeLeft() {
        return DurationFormatUtils.formatDurationWords(this.getRemaining(), true, true);
    }

}
