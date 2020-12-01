/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.potion.PotionEffectType;

@AllArgsConstructor
@Data
public class EffectSnapshot {

    private PotionEffectType effectType;
    private int amplifier;
    private int duration;

}
