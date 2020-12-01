/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.team.commands.brew.enums;

import cc.fyre.stark.util.CC;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

/**
 * Creator : YoloSanta
 * this was a pain in the ass to make
 */
@Getter
public enum PotionMetaID {
    REGEN(PotionEffectType.REGENERATION, 8193, 16385),
    SPEED(PotionEffectType.SPEED, 8194, 16386),
    FIRE_RES(PotionEffectType.FIRE_RESISTANCE, 8195, 16387),
    POISON(PotionEffectType.POISON, 8196, 16388),
    INSTANT_HEALTH(PotionEffectType.HEAL, 8197, 16389),
    NIGHT_VISION(PotionEffectType.NIGHT_VISION, 8198, 16390),
    WEAKNESS(PotionEffectType.WEAKNESS, 8200, 16392),
    STRENGTH(PotionEffectType.INCREASE_DAMAGE, 8201, 16393),
    SLOW(PotionEffectType.SLOW, 8202, 16394),
    LEAP(PotionEffectType.JUMP, 8203, 16395),
    INSTANT_DAMAGE(PotionEffectType.HARM, 8204, 16396),
    WATER_BREATHING(PotionEffectType.WATER_BREATHING, 8205, 16397),
    INVIS(PotionEffectType.INVISIBILITY, 8206, 16398),
    ;

    private final PotionEffectType potionEffectType;
    private final int metaID;
    private final int metaIDasSplash;

    PotionMetaID(PotionEffectType potionEffectType, int metaID, int metaIDasSplash) {
        this.potionEffectType = potionEffectType;
        this.metaID = metaID;
        this.metaIDasSplash = metaIDasSplash;
    }

    public static ItemStack getPotion(String name, PotionMetaID potionMetaID, List<String> lore) {
        ItemStack item = new ItemStack(Material.POTION, 1, (short) potionMetaID.getMetaID());
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(CC.INSTANCE.translate(name));
        for (int i = 0; i < lore.size(); i++) {
            lore.set(i, CC.INSTANCE.translate(lore.get(i)));
        }
        meta.setLore(lore);

        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getSplash(String name, PotionMetaID potionMetaID, List<String> lore) {
        ItemStack item = new ItemStack(Material.POTION, 1, (short) potionMetaID.getMetaIDasSplash());
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(CC.INSTANCE.translate(name));
        for (int i = 0; i < lore.size(); i++) {
            lore.set(i, CC.INSTANCE.translate(lore.get(i)));
        }
        meta.setLore(lore);

        item.setItemMeta(meta);
        return item;
    }
}
