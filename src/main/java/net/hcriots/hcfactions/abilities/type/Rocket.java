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
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class Rocket extends Ability {

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public Material getMaterial() {
        return Material.FIREWORK;
    }

    @Override
    public String getDisplayName() {
        return ChatColor.RED.toString() + ChatColor.BOLD + "Rocket";
    }

    @Override
    public List<String> getLore() {
        List<String> toReturn = new ArrayList();
        toReturn.add("&7Right-Click to launch yourself");
        toReturn.add("&710 blocks in the air!");
        return toReturn;
    }


    @Override
    public long getCooldown() {
        return (long) (60.0 * 1000L);
    }

    @EventHandler
    public void onItem(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (!this.isSimilar(player.getItemInHand())) {
                return;
            }

            if (DTRBitmask.SAFE_ZONE.appliesAt(player.getLocation())) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "You can't use this in spawn!");
                return;
            }

            if (this.hasCooldown(event.getPlayer())) {
                this.sendCooldownMessage(event.getPlayer());
                event.setCancelled(true);
                return;
            }

            if (this.isSimilar(player.getItemInHand())) {
                player.setVelocity(new Vector(0.1D, 2.0D, 0.0D));

                ItemStack itemStack = player.getItemInHand();
                itemStack.setAmount(itemStack.getAmount() - 1);

                player.setItemInHand(itemStack);
                this.applyCooldown(player);
                player.setMetadata("rocket", new FixedMetadataValue(Hulu.getInstance(), true));
            }
        }
    }
}