/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.persist.maps;

import net.hcriots.hcfactions.persist.PersistMap;

import java.util.UUID;

public class WrappedBalanceMap extends PersistMap<Double> {

    public WrappedBalanceMap() {
        super("WrappedBalances", "Balance");
    }

    @Override
    public String getRedisValue(Double balance) {
        return (String.valueOf(balance));
    }

    @Override
    public Double getJavaObject(String str) {
        return (Double.parseDouble(str));
    }

    @Override
    public void setCredits(int i) {

    }

    @Override
    public Object getMongoValue(Double balance) {
        return (balance);
    }

    public double getBalance(UUID check) {
        return (contains(check) ? getValue(check) : 0);
    }

    public void setBalance(UUID update, double balance) {
        updateValueAsync(update, balance);
    }

}