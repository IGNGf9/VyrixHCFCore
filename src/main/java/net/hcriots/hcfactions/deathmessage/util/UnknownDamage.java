/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.deathmessage.util;

import net.hcriots.hcfactions.deathmessage.objects.Damage;

public class UnknownDamage extends Damage {

    public UnknownDamage(String damaged, double damage) {
        super(damaged, damage);
    }

    public String getDeathMessage() {
        return (wrapName(getDamaged()) + " died.");
    }

}