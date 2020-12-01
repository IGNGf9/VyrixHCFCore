/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.abilities.type;

import net.hcriots.hcfactions.abilities.Ability;
import net.hcriots.hcfactions.util.Color;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;
import java.util.List;

public class SwitchStick extends Ability {

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public Material getMaterial() {
        return Material.STICK;
    }

    @Override
    public String getDisplayName() {
        return ChatColor.GOLD.toString() + ChatColor.BOLD + "Switch Stick";
    }

    @Override
    public List<String> getLore() {
        List<String> toReturn = new ArrayList();
        toReturn.add("&7Hit someone to spin them around");
        toReturn.add("&7180 degrees!");
        return toReturn;
    }

    @Override
    public long getCooldown() {
        return (long) (90.0 * 1000L);
    }

    @EventHandler
    public void blackmanattack(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        if (!(event.getEntity() instanceof Player)) return;

        Player damaged = (Player) event.getEntity();
        Player damager = (Player) event.getDamager();

        if (!(damager.getItemInHand().getType() == getMaterial())) return;
        if (!(damager.getItemInHand().getItemMeta().getLore().contains(Color.translate("&7Hit someone to spin them around 180 degrees!"))))
            return;

        if (this.hasCooldown(damager)) {
            this.sendCooldownMessage(damager);
            event.setCancelled(true);
            return;
        }

        this.applyCooldown(damager);
        Location beanerlocation = damaged.getLocation();
        beanerlocation.setYaw(beanerlocation.getYaw() + 180);
        damaged.teleport(beanerlocation);
        damaged.sendMessage(Color.translate("&fYou have been turned &e180 degrees &fby &e" + damager.getName()));
    }

}
