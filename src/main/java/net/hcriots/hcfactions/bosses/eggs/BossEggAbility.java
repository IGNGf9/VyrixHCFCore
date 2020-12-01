/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.bosses.eggs;

import cc.fyre.stark.util.ItemBuilder;
import net.hcriots.hcfactions.Hulu;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by InspectMC
 * Date: 7/20/2020
 * Time: 8:52 PM
 */
public abstract class BossEggAbility implements Listener {

    public BossEggAbility() {
        Bukkit.getServer().getPluginManager().registerEvents(this, Hulu.getInstance());
    }

    public abstract String getName();

    public abstract ItemStack getMaterial();

    public abstract String getDisplayName();

    public abstract List<String> getLore();

    public ItemStack getItemStack() {
        return ItemBuilder.of(this.getMaterial().getType())
                .name(this.getDisplayName() == null ? this.getName() : this.getDisplayName())
                .setLore(this.getLore() == null ? new ArrayList<>() : this.getLore())
                .build();
    }

    public boolean isSimilar(ItemStack itemStack) {

        if (itemStack == null || itemStack.getItemMeta() == null) {
            return false;
        }

        if (itemStack.getItemMeta().getDisplayName() == null) {
            return false;
        }

        if (itemStack.getItemMeta().getLore() == null || itemStack.getItemMeta().getLore().isEmpty()) {
            return false;
        }

        return itemStack.getType() == this.getMaterial().getType() && itemStack.getItemMeta().getDisplayName().equals(this.getDisplayName()) && itemStack.getItemMeta().getLore().equals(this.getLore());
    }
}