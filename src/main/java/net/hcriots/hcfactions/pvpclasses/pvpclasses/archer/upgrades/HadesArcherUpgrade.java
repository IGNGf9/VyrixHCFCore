/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.pvpclasses.pvpclasses.archer.upgrades;

import net.hcriots.hcfactions.pvpclasses.pvpclasses.archer.ArcherUpgrade;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;

/**
 * Created by InspectMC
 * Date: 7/6/2020
 * Time: 3:45 PM
 */

public class HadesArcherUpgrade implements ArcherUpgrade {

    @Override
    public String getUpgradeName() {
        return "Hades";
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList(
                "Players shot by this upgrade will receive",
                "Weakness 4 and Hunger 5 for 10 seconds."
        );
    }

    @Override
    public int getKillsNeeded() {
        return 75;
    }

    @Override
    public short getMaterialData() {
        return 1;
    }

    @Override
    public void onHit(Player shooter, Player victim) {
        victim.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20 * 10, 3));
        victim.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 20 * 10, 4));
    }

    @Override
    public boolean applies(Player shooter) {
        for (ItemStack itemStack : shooter.getInventory().getArmorContents()) {
            if (itemStack == null || !itemStack.getType().name().contains("LEATHER_")) {
                return false;
            }

            LeatherArmorMeta meta = (LeatherArmorMeta) itemStack.getItemMeta();

            if (meta.getColor().getRed() != 127 || meta.getColor().getGreen() != 204 || meta.getColor().getBlue() != 25) {
                return false;
            }
        }

        return true;
    }
}