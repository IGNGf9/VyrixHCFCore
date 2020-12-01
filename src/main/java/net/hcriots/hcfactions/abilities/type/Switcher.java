/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.abilities.type;

import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.abilities.Ability;
import net.hcriots.hcfactions.team.dtr.DTRBitmask;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Switcher extends Ability {

    public static int SWAP_RADIUS = 10;

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
        return ChatColor.GREEN.toString() + ChatColor.BOLD + "Swapper";
    }

    @Override
    public List<String> getLore() {
        List<String> toReturn = new ArrayList();
        toReturn.add("&7Hit and switch to switch");
        toReturn.add("&7locations with the person!");
        return toReturn;
    }

    @Override
    public long getCooldown() {
        return (long) (10.0 * 1000L);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!event.getAction().name().contains("RIGHT")) {
            return;
        }
        if (!event.hasItem()) {
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

        if (DTRBitmask.CITADEL.appliesAt(player.getLocation())) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "You can't use this in Citadel!");
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

        event.getEntity().setMetadata("SHOOTER", new FixedMetadataValue(Hulu.getInstance(), player.getUniqueId().toString()));
    }


    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityDamage(EntityDamageByEntityEvent event) {

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

        if (!snowball.hasMetadata("SHOOTER")) {
            return;
        }

        UUID uuid = UUID.fromString(snowball.getMetadata("SHOOTER").get(0).asString());

        Player player = Hulu.getInstance().getServer().getPlayer(uuid);
        Player swapped = (Player) event.getEntity();

        if (player == null) {
            return;
        }

        Location location = player.getLocation().clone();
        Location swappedLocation = event.getEntity().getLocation().clone();

        if (location.distance(swappedLocation) > 10) {
            player.sendMessage(swapped.getDisplayName() + ChatColor.RED + " is more then " + ChatColor.WHITE + SWAP_RADIUS + ChatColor.RED + " blocks away from you!");
            return;
        }

        player.teleport(swappedLocation);
        event.getEntity().teleport(location);
        player.sendMessage(ChatColor.GOLD + "You have swapped locations with " + swapped.getDisplayName() + ChatColor.GOLD + ".");

        this.applyCooldown(player);
    }
}
