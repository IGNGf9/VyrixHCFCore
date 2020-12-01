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
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class FreezeGun extends Ability {

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public Material getMaterial() {
        return Material.DIAMOND_HOE;
    }

    @Override
    public String getDisplayName() {
        return ChatColor.DARK_AQUA.toString() + ChatColor.BOLD + "Freeze Gun";
    }

    @Override
    public List<String> getLore() {
        List<String> toReturn = new ArrayList();
        toReturn.add("&7Shoot and hit someone");
        toReturn.add("&7to give them slowness!");
        return toReturn;
    }


    @Override
    public long getCooldown() {
        return (long) (180.0 * 1000L);
    }

    @EventHandler
    public void on(PlayerInteractEvent e) {
        Action action = e.getAction();
        Player player = e.getPlayer();
        ItemStack itemInHand = player.getItemInHand();
        if (action == Action.RIGHT_CLICK_AIR) {
            if (!this.isSimilar(itemInHand)) return;
            if (this.hasCooldown(e.getPlayer())) {
                this.sendCooldownMessage(e.getPlayer());
                e.setCancelled(true);
                return;
            }

            if (DTRBitmask.SAFE_ZONE.appliesAt(player.getLocation())) {
                e.setCancelled(true);
                player.sendMessage(ChatColor.RED + "You can't use this in spawn!");
                return;
            }

            if (DTRBitmask.CITADEL.appliesAt(player.getLocation())) {
                e.setCancelled(true);
                player.sendMessage(ChatColor.RED + "You can't use this in Citadel!");
                return;
            }

            player.launchProjectile(Snowball.class);
            itemInHand.setDurability((short) (itemInHand.getDurability() + 312));
            player.updateInventory();
            this.applyCooldown(player);
        }
    }

    @EventHandler
    public void on(ProjectileLaunchEvent e) {
        if (!(e.getEntity().getShooter() instanceof Player)) {
            return;
        }
        Projectile entity = e.getEntity();
        Player player = (Player) entity.getShooter();
        if (!this.isSimilar(player.getItemInHand())) return;
        if (entity instanceof Snowball) {
            Snowball snowball = (Snowball) entity;
            snowball.setMetadata("freezegun", new FixedMetadataValue(Hulu.getInstance(), player.getUniqueId()));
        }
    }


    @EventHandler
    public void on(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Snowball && e.getEntity() instanceof Player) {
            Player damaged = (Player) e.getEntity();
            Snowball snowball = (Snowball) e.getDamager();
            if (!snowball.hasMetadata("freezegun")) return;
            if (snowball.getShooter() instanceof Player) {
                damaged.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 2));
                damaged.sendMessage("");
                damaged.sendMessage(ChatColor.RED + "You have been hit with a " + ChatColor.DARK_AQUA + "Freeze Gun" + ChatColor.RED + "!");
                damaged.sendMessage(ChatColor.RED + "You now have Slowness 3 for 5 seconds.");
                damaged.sendMessage("");
            }
        }
    }
}