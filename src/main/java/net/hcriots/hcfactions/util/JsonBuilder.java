/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.util;


import net.minecraft.util.com.google.gson.JsonElement;
import net.minecraft.util.com.google.gson.JsonObject;

public class JsonBuilder {

    private final JsonObject json = new JsonObject();

    public JsonBuilder addProperty(String property, String value) {
        json.addProperty(property, value);
        return this;
    }

    public JsonBuilder addProperty(String property, Number value) {
        json.addProperty(property, value);
        return this;
    }

    public JsonBuilder addProperty(String property, Boolean value) {
        json.addProperty(property, value);
        return this;
    }

    public JsonBuilder addProperty(String property, Character value) {
        json.addProperty(property, value);
        return this;
    }

    public JsonBuilder add(String property, JsonElement element) {
        json.add(property, element);
        return this;
    }

    public JsonObject get() {
        return json;
    }

}
