/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.pvpclasses.pvpclasses.archer.upgrades;

import net.hcriots.hcfactions.pvpclasses.pvpclasses.archer.ArcherUpgrade;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.Arrays;
import java.util.List;

/**
 * Created by InspectMC
 * Date: 7/8/2020
 * Time: 7:46 PM
 */

public class ZeusArcherUpgrade implements ArcherUpgrade {

    @Override
    public String getUpgradeName() {
        return "Zeus";
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList(
                "Players shot by this upgrade will",
                "be struct by lighting"
        );
    }

    @Override
    public int getKillsNeeded() {
        return 150;
    }

    @Override
    public short getMaterialData() {
        return 12;
    }

    @Override
    public void onHit(Player shooter, Player victim) {
        victim.getWorld().spawn(victim.getLocation(), LightningStrike.class);
    }

    @Override
    public boolean applies(Player shooter) {
        for (ItemStack itemStack : shooter.getInventory().getArmorContents()) {
            if (itemStack == null || !itemStack.getType().name().contains("LEATHER_")) {
                return false;
            }

            LeatherArmorMeta meta = (LeatherArmorMeta) itemStack.getItemMeta();

            if (meta.getColor().getRed() != 102 || meta.getColor().getGreen() != 153 || meta.getColor().getBlue() != 216) {
                return false;
            }
        }

        return true;
    }
}