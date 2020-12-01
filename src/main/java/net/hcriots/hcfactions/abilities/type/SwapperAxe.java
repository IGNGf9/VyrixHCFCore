/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.abilities.type;

import net.hcriots.hcfactions.abilities.Ability;
import net.hcriots.hcfactions.team.dtr.DTRBitmask;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by InspectMC
 * Date: 8/14/2020
 * Time: 5:33 PM
 */
public class SwapperAxe extends Ability {

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public Material getMaterial() {
        return Material.GOLD_AXE;
    }

    @Override
    public String getDisplayName() {
        return ChatColor.BLUE.toString() + ChatColor.BOLD + "Swapper Axe";
    }

    @Override
    public List<String> getLore() {
        List<String> toReturn = new ArrayList<>();
        toReturn.add(ChatColor.translateAlternateColorCodes('&', "&7&oHit a player to remove their helmet"));
        return toReturn;
    }

    @Override
    public long getCooldown() {
        return (long) (60.0 * 1000L);
    }

    @EventHandler
    public void on(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player)) return;
        if (!(e.getEntity() instanceof Player)) return;
        Player player = (Player) e.getDamager();
        Player hit = (Player) e.getEntity();
        ItemStack air = new ItemStack(Material.AIR);
        ItemStack helmet = hit.getInventory().getHelmet();
        if (!this.isSimilar(player.getItemInHand())) {
            return;
        }

        if (DTRBitmask.SAFE_ZONE.appliesAt(player.getLocation())) {
            e.setCancelled(true);
            player.sendMessage(ChatColor.RED + "You can't use this in spawn!");
            return;
        }

        if (hit.getInventory().getHelmet() == null) {
            return;
        }

        if (hit.getInventory().firstEmpty() < 0) {
            player.sendMessage(ChatColor.RED + "This player inventory is currently full.");
            return;
        }


        if (this.hasCooldown(player)) {
            this.sendCooldownMessage(player);
            e.setCancelled(true);
            return;
        }

        hit.getInventory().addItem(helmet);
        hit.getInventory().setHelmet(air);
        hit.sendMessage(ChatColor.GOLD + "You have lost your helmet by Swapper Axe!");
        hit.updateInventory();

        player.sendMessage(ChatColor.GOLD + "You have took " + hit.getName() + ChatColor.GOLD + " helmet off!");
        this.applyCooldown(player);
    }
}