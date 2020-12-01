/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.pvpclasses.pvpclasses.archer.upgrades;

import net.hcriots.hcfactions.pvpclasses.pvpclasses.archer.ArcherUpgrade;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.Arrays;
import java.util.List;

/**
 * Created by InspectMC
 * Date: 7/9/2020
 * Time: 4:06 AM
 */

public class StealerArcherUpgrade implements ArcherUpgrade {

    @Override
    public String getUpgradeName() {
        return "Stealer";
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList(
                "Players shot by this upgrade will",
                "lose their helmet"
        );
    }

    @Override
    public int getKillsNeeded() {
        return 250;
    }

    @Override
    public short getMaterialData() {
        return 0;
    }

    @Override
    public void onHit(Player shooter, Player victim) {
        ItemStack helmet = victim.getInventory().getHelmet();
        ItemStack air = new ItemStack(Material.AIR);

        if (victim.getInventory().getHelmet() == null) {
            return;
        }

        if (victim.getInventory().firstEmpty() < 0) {
            shooter.sendMessage(ChatColor.RED + "This player inventory is currently full.");
            return;
        }

        victim.getInventory().addItem(helmet);
        victim.getInventory().setHelmet(air);
        victim.sendMessage(ChatColor.GOLD + "You have lost your helmet by the Stealer Archer!");
        victim.updateInventory();

        shooter.sendMessage(ChatColor.GOLD + "You have took " + victim.getName() + ChatColor.GOLD + " helmet off!");
    }

    @Override
    public boolean applies(Player shooter) {
        for (ItemStack itemStack : shooter.getInventory().getArmorContents()) {
            if (itemStack == null || !itemStack.getType().name().contains("LEATHER_")) {
                return false;
            }

            LeatherArmorMeta meta = (LeatherArmorMeta) itemStack.getItemMeta();

            if (meta.getColor().getRed() != 25 || meta.getColor().getGreen() != 25 || meta.getColor().getBlue() != 25) {
                return false;
            }
        }

        return true;
    }
}