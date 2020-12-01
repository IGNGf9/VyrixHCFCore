/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.abilities.type;

import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.abilities.Ability;
import net.hcriots.hcfactions.bosses.particles.ParticleEffect;
import net.hcriots.hcfactions.team.dtr.DTRBitmask;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DamageSnowball extends Ability {

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public Material getMaterial() {
        return Material.SNOW_BALL;
    }

    @Override
    public String getDisplayName() {
        return ChatColor.RED.toString() + ChatColor.BOLD + "Damage Snowball";
    }

    @Override
    public List<String> getLore() {
        List<String> toReturn = new ArrayList();
        toReturn.add("&7Throw and hit someone to");
        toReturn.add("&7deal 2 hearts of damage!");
        return toReturn;
    }


    @Override
    public long getCooldown() {
        return (long) (45.0 * 1000L);
    }


    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!event.hasItem()) {
            return;
        }

        if (!event.getAction().name().contains("RIGHT")) {
            return;
        }

        if (!this.isSimilar(player.getItemInHand())) {
            return;
        }

        if (DTRBitmask.SAFE_ZONE.appliesAt(player.getLocation())) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "You can't use this in spawn!");
            return;
        }

        if (Hulu.getInstance().getPvPTimerMap().hasTimer(player.getUniqueId()) && player.getGameMode() != GameMode.CREATIVE) {
            player.sendMessage(ChatColor.RED + "You cannot do this while your PVPTimer is active!");
            player.sendMessage(ChatColor.GRAY.toString() + ChatColor.ITALIC + "To remove your PvPTimer type '" + ChatColor.WHITE + "/pvp enable" + ChatColor.GRAY.toString() + ChatColor.ITALIC + "'.");
            return;
        }

        if (this.hasCooldown(event.getPlayer())) {
            this.sendCooldownMessage(event.getPlayer());
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if (!(event.getEntity() instanceof Snowball) && !(event.getEntity().getShooter() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity().getShooter();

        if (!this.isSimilar(player.getItemInHand())) {
            return;
        }

        event.getEntity().setMetadata("DAMAGESNOWBALL", new FixedMetadataValue(Hulu.getInstance(), player.getUniqueId().toString()));
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityDamage(EntityDamageByEntityEvent event) throws Exception {

        if (event.isCancelled()) {
            return;
        }

        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        if (!(event.getDamager() instanceof Snowball)) {
            return;
        }

        Snowball snowball = (Snowball) event.getDamager();

        if (!snowball.hasMetadata("DAMAGESNOWBALL")) {
            return;
        }

        UUID uuid = UUID.fromString(snowball.getMetadata("DAMAGESNOWBALL").get(0).asString());

        Player player = Hulu.getInstance().getServer().getPlayer(uuid);
        Player damaged = (Player) event.getEntity();

        if (player == null) {
            return;
        }

        damaged.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 170, 1));
        damaged.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 170, 1));
        damaged.setHealth(damaged.getHealth() - 2.5);
        ParticleEffect.FIREWORKS_SPARK.sendToPlayers(Bukkit.getOnlinePlayers(), damaged.getLocation(), 1, 0, 1, 0, 50);
        this.applyCooldown(player);
    }
}