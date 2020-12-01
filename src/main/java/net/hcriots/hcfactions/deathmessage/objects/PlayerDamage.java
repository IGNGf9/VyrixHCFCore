/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.deathmessage.objects;

import lombok.Getter;

public abstract class PlayerDamage extends Damage {

    @Getter
    private final String damager;

    public PlayerDamage(String damaged, double damage, String damager) {
        super(damaged, damage);
        this.damager = damager;
    }

}