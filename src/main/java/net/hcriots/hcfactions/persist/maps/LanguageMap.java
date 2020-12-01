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
 * Date: 7/27/2020
 * Time: 10:58 PM
 */
public class LanguageMap extends PersistMap<Boolean> {

    public LanguageMap() {
        super("Language", "LanguageSwitch");
    }

    @Override
    public String getRedisValue(Boolean toggled) {
        return (String.valueOf(toggled));
    }

    @Override
    public Boolean getJavaObject(String str) {
        return (Boolean.valueOf(str));
    }

    @Override
    public void setCredits(int i) {

    }

    @Override
    public Object getMongoValue(Boolean toggled) {
        return (toggled);
    }

    public void setNewLanguage(UUID update, boolean toggled) {
        updateValueAsync(update, toggled);
    }

    public boolean isNewLanguageToggle(UUID check) {
        return (contains(check) ? getValue(check) : true);
    }

}