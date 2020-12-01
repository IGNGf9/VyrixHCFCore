/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.abilities.type;

import net.hcriots.hcfactions.abilities.Ability;
import net.hcriots.hcfactions.team.dtr.DTRBitmask;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class GrappleHook extends Ability {

    private int uses = 50;

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public Material getMaterial() {
        return Material.FISHING_ROD;
    }

    @Override
    public String getDisplayName() {
        return ChatColor.YELLOW.toString() + ChatColor.BOLD + "Grapple Hook";
    }

    @Override
    public List<String> getLore() {
        List<String> toReturn = new ArrayList<>();
        toReturn.add(ChatColor.translateAlternateColorCodes('&', "&7&oShooting the rod will fly you in that direction."));
        return toReturn;
    }

    @Override
    public long getCooldown() {
        return (long) (5.0 * 1000L);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerFish(PlayerFishEvent event) {
        Player player = event.getPlayer();
        if (!this.isSimilar(event.getPlayer().getItemInHand())) {
            return;
        }

        if (this.uses == 0) {
            player.sendMessage(ChatColor.RED + "This rod is out of uses!");
            return;
        }

        if (DTRBitmask.SAFE_ZONE.appliesAt(player.getLocation())) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "You can't use this in spawn!");
            if (player.getName().equals("YoloSanta")) {
                player.sendMessage("Caused by " + getName());
            }
            return;
        }

        if (DTRBitmask.CITADEL.appliesAt(player.getLocation())) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "You can't use this in Citadel!");
            return;
        }

        if (event.getState() == PlayerFishEvent.State.FISHING) {
            return;
        }

        Entity bobber = event.getHook();
        if (bobber == null) {
            return;
        }

        if (event.getCaught() != null) {
            event.getHook().remove();
            return;
        }

        Location bobberLocation = bobber.getLocation();
        if (this.hasCooldown(event.getPlayer())) {
            this.sendCooldownMessage(event.getPlayer());
            event.setCancelled(true);
            return;
        }

        if (player.getFireTicks() <= 0 && (isValidForGrapple(bobberLocation.getBlock()) || this.isValidForGrapple(bobberLocation.getBlock().getRelative(BlockFace.UP)) || this.isValidForGrapple(bobberLocation.getBlock().getRelative(BlockFace.DOWN)))) {
            Location playerLoc = player.getLocation();
            player.setVelocity(new Vector(bobberLocation.getX() - playerLoc.getX(), bobberLocation.getY() - playerLoc.getY(), bobberLocation.getZ() - playerLoc.getZ()).multiply(0.2));
            event.setExpToDrop(0);
            event.getHook().remove();
            this.applyCooldown(player);
            --this.uses;
            if (event.getCaught() != null && !(event.getCaught() instanceof LivingEntity)) {
                event.getCaught().remove();
            }
        }
    }

    private boolean isValidForGrapple(Block block) {
        return block.getTypeId() != 0 && block.getType() != Material.STATIONARY_WATER && block.getType() != Material.WATER;
    }
}