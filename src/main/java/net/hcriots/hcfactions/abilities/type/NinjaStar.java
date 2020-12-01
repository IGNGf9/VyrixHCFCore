/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.abilities.type;

import net.hcriots.hcfactions.abilities.Ability;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TODO: Fix when it expires teleportation
public class NinjaStar extends Ability {

    public static Map<String, Long> teleport = new HashMap<>();
    public Location loc;
    public Player target;

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public Material getMaterial() {
        return Material.NETHER_STAR;
    }

    @Override
    public String getDisplayName() {
        return ChatColor.GRAY.toString() + ChatColor.BOLD + "Ninja Star";
    }

    @Override
    public List<String> getLore() {
        List<String> toReturn = new ArrayList<>();
        toReturn.add(ChatColor.translateAlternateColorCodes('&', "&7&oWhen you click, you touch the last player who has hurt you."));
        return toReturn;
    }

    @Override
    public long getCooldown() {
        return (long) (60.0 * 1000L);
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player damager = (Player) event.getDamager();
            Player victim = (Player) event.getEntity();
            teleport.put(victim.getName(), System.currentTimeMillis() + 1000L);
            this.loc = damager.getLocation();
            this.target = damager;
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack inHand = player.getItemInHand();
        if (!event.getAction().name().contains("RIGHT")) {
            return;
        }
        if (!this.isSimilar(player.getItemInHand())) {
            return;
        }

        if (player != target) {
            player.sendMessage(ChatColor.RED + "Nobody has hit you so you may not use this item...");
            return;
        }

        if (this.hasCooldown(event.getPlayer())) {
            this.sendCooldownMessage(event.getPlayer());
            event.setCancelled(true);
            return;
        }


        player.sendMessage(ChatColor.YELLOW + "You've used the ninja star against " + this.target.getName());
        this.target.sendMessage(ChatColor.DARK_RED.toString() + ChatColor.BOLD + player.getName() + ChatColor.YELLOW + " has used a ninja star against you.");
        player.teleport(this.loc);
        player.playEffect(player.getLocation().add(0.5D, 2.0D, 0.5D), Effect.ENDER_SIGNAL, 5);
        player.playEffect(player.getLocation().add(0.5D, 1.5D, 0.5D), Effect.ENDER_SIGNAL, 5);
        player.playEffect(player.getLocation().add(0.5D, 1.0D, 0.5D), Effect.ENDER_SIGNAL, 5);
        player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0F, 1.0F);
        if (inHand.getAmount() == 1) {
            player.setItemInHand(new ItemStack(Material.AIR, 1));
        } else {
            inHand.setAmount(inHand.getAmount() - 1);
        }
        this.applyCooldown(player);
        player.updateInventory();
    }
}