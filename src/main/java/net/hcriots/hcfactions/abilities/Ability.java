/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.abilities;

import cc.fyre.stark.util.ItemBuilder;
import net.hcriots.hcfactions.Hulu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public abstract class Ability implements Listener {

    public Ability() {
        Bukkit.getServer().getPluginManager().registerEvents(this, Hulu.getInstance());
    }

    public abstract String getName();

    public abstract Material getMaterial();

    public abstract String getDisplayName();

    public abstract List<String> getLore();

    public abstract long getCooldown();

    public ItemStack getItemStack() {
        return ItemBuilder.of(this.getMaterial())
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

        return itemStack.getType() == this.getMaterial() && itemStack.getItemMeta().getDisplayName().equals(this.getDisplayName()) && itemStack.getItemMeta().getLore().equals(this.getLore());
    }

    public void applyCooldown(Player player) {
        Hulu.getInstance().getAbilityHandler().applyCooldown(this, player);
    }

    public boolean hasCooldown(Player player) {
        return Hulu.getInstance().getAbilityHandler().hasCooldown(this, player);
    }

    public long getRemaining(Player player) {
        return Hulu.getInstance().getAbilityHandler().getRemaining(this, player);
    }

    public void sendCooldownMessage(Player player) {

        double value = (this.getRemaining(player) / 1000D);
        double sec = value > 0.1 ? Math.round(10.0 * value) / 10.0 : 0.1; // don't tell user 0.0

        player.sendMessage(ChatColor.RED + "You cannot use this for another " + ChatColor.BOLD + sec + ChatColor.RED + " seconds!");
    }
}