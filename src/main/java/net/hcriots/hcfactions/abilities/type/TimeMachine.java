/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.abilities.type;

import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.abilities.Ability;
import net.hcriots.hcfactions.bosses.particles.ParticleEffect;
import net.hcriots.hcfactions.util.Color;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimeMachine extends Ability {

    public static Map<Location, Player> idontcare = new HashMap<>();
    public Location loc;

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public Material getMaterial() {
        return Material.WATCH;
    }

    @Override
    public String getDisplayName() {
        return ChatColor.GOLD.toString() + ChatColor.BOLD + "Time Machine";
    }

    @Override
    public List<String> getLore() {
        List<String> toReturn = new ArrayList();
        toReturn.add("&7Right-Click to move freely for 10");
        toReturn.add("&7seconds and then get teleported back!");
        return toReturn;
    }

    @Override
    public long getCooldown() {
        return (long) (120.0 * 1000L);
    }

    @EventHandler
    public void rightclicklol(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack inhand = player.getItemInHand();
        if (!event.getAction().name().contains("RIGHT")) {
            return;
        }
        if (!this.isSimilar(inhand)) {
            return;
        }

        if (this.hasCooldown(player)) {
            this.sendCooldownMessage(player);
            event.setCancelled(true);
            return;
        }

        Location prelocation = player.getLocation();

        applyCooldown(player);
        inhand.setAmount(inhand.getAmount() - 1);
        player.setItemInHand(inhand);

        player.sendMessage(Color.translate("&fYou have activated your &6&lTime Machine&f! &fYou have &e20 seconds &fbefore you will be teleported back!")); // you will get teleported in 20 seconds

        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, (20 * 10), 2));

        try {
            ParticleEffect.EXPLOSION_LARGE.sendToPlayers(Bukkit.getOnlinePlayers(), player.getLocation(), 1, 0, 1, 0, 50);
        } catch (Exception e) {
            e.printStackTrace();
        }

        new BukkitRunnable() {

            @Override
            public void run() {
                player.teleport(prelocation);
                player.sendMessage(Color.translate("&fYour time has &cexpired&f! You have been teleported back!")); // you just got teleported back
            }
        }.runTaskLaterAsynchronously(Hulu.getInstance(), (20 * 10));
    }
}
