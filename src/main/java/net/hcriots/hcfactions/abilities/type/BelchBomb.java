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
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class BelchBomb extends Ability {

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public Material getMaterial() {
        return Material.SLIME_BALL;
    }

    @Override
    public String getDisplayName() {
        return ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "Belch Bomb";
    }

    @Override
    public List<String> getLore() {
        List<String> toReturn = new ArrayList();
        toReturn.add("&7Right-Click to give nearby");
        toReturn.add("&7enemies slowness and blindness!");
        return toReturn;
    }


    @Override
    public long getCooldown() {
        return (long) (45.0 * 1000L);
    }

    @EventHandler
    public void on(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack itemInHand = player.getItemInHand();
        int radius = 10;

        if (!e.getAction().name().contains("RIGHT")) {
            return;
        }

        if (!this.isSimilar(itemInHand)) {
            return;
        }

        if (DTRBitmask.SAFE_ZONE.appliesAt(player.getLocation())) {
            e.setCancelled(true);
            player.sendMessage(ChatColor.RED + "You can't use this in spawn!");
            return;
        }


        if (this.hasCooldown(e.getPlayer())) {
            this.sendCooldownMessage(e.getPlayer());
            e.setCancelled(true);
            return;
        }

        if (itemInHand.getAmount() == 1) {
            player.setItemInHand(new ItemStack(Material.AIR, 1));
        } else {
            itemInHand.setAmount(itemInHand.getAmount() - 1);
        }

        List<Entity> nearby = player.getNearbyEntities(radius, radius, radius);
        for (Entity entity : nearby) {
            if (entity instanceof Player) {
                Player players = ((Player) entity).getPlayer();

                players.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 2));
                players.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 2));
            }
        }

        player.updateInventory();
        this.applyCooldown(player);
    }
}