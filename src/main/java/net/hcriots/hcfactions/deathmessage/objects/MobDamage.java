/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.deathmessage.objects;

import lombok.Getter;
import org.bukkit.entity.EntityType;

public abstract class MobDamage extends Damage {

    @Getter
    private final EntityType mobType;

    public MobDamage(String damaged, double damage, EntityType mobType) {
        super(damaged, damage);
        this.mobType = mobType;
    }

}