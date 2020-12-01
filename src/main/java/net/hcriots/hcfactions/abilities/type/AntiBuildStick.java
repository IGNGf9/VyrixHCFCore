/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.abilities.type;

import net.hcriots.hcfactions.abilities.Ability;
import net.hcriots.hcfactions.util.Color;
import net.hcriots.hcfactions.util.Cooldowns;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cluch
 * Date: 06/10/2020
 * Time: 4:36 PM
 */

public class AntiBuildStick extends Ability {

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
        return ChatColor.GREEN.toString() + ChatColor.BOLD + "Anti Build Stick";
    }

    @Override
    public List<String> getLore() {
        List<String> toReturn = new ArrayList();
        toReturn.add("&7Hit someone 3 times to");
        toReturn.add("&7restrict their building!");
        return toReturn;
    }

    @Override
    public long getCooldown() {
        return (long) (120.0 * 1000L);
    }


    @EventHandler
    public void on(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player)) return;
        if (!(e.getEntity() instanceof Player)) return;

        Player damaged = (Player) e.getEntity();
        Player damager = (Player) e.getDamager();

        if (!(damager.getItemInHand().getType() == getMaterial())) return;
        if (!(damager.getItemInHand().getItemMeta().getLore().contains(Color.translate("&7Hit someone 3 times to restrict their building!"))))
            return;

        if (this.hasCooldown((Player) e.getDamager())) {
            this.sendCooldownMessage((Player) e.getDamager());
            e.setCancelled(true);
            return;
        }

        if (e.isCancelled()) return;

        if (Cooldowns.isOnCooldown("bs3", damaged)) {
            Cooldowns.removeCooldown("bs1", damaged);
            Cooldowns.removeCooldown("bs2", damaged);
            Cooldowns.removeCooldown("bs3", damaged);
            Cooldowns.addCooldown("bs1", damaged, 7);
        }
        if (!Cooldowns.isOnCooldown("bs1", damaged)) {
            Cooldowns.addCooldown("bs1", damaged, 7);
            return;
        }
        if (!Cooldowns.isOnCooldown("bs2", damaged)) {
            if (Cooldowns.isOnCooldown("bs1", damaged)) {
                Cooldowns.addCooldown("bs2", damaged, 7);
                return;
            } else {
                return;
            }
        }

        if (Cooldowns.isOnCooldown("bs1", damaged)) {
            if (Cooldowns.isOnCooldown("bs2", damaged)) {
                this.applyCooldown(damager);
                Cooldowns.addCooldown("restricted", damaged, 15);
                damaged.sendMessage(Color.translate("&fYou have been hit with an &a&lAnti Build Stick&f!"));
                damager.sendMessage(Color.translate("&fYou have hit &e" + damaged.getName() + " &fwith an &a&lAnti Build Stick&f!"));
                if (damager.getItemInHand().getAmount() == 1) damager.setItemInHand(null);
                damager.getItemInHand().setAmount(damager.getItemInHand().getAmount() - 1);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (Cooldowns.isOnCooldown("restricted", player)) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "You can build again in " + ChatColor.BOLD + Cooldowns.getCooldownForPlayerInt("restricted", player) + ChatColor.RED + "s!");
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (Cooldowns.isOnCooldown("restricted", player)) {
            player.sendMessage(ChatColor.RED + "You can build again in " + ChatColor.BOLD + Cooldowns.getCooldownForPlayerInt("restricted", player) + ChatColor.RED + "s!");
            player.updateInventory();
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void fencegatechest(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getClickedBlock() != null) {
                if (event.getClickedBlock().getType() == Material.CHEST || event.getClickedBlock().getType() == Material.FENCE_GATE || event.getClickedBlock().getType() == Material.TRAPPED_CHEST || event.getClickedBlock().getType() == Material.TRAP_DOOR || event.getClickedBlock().getType() == Material.WORKBENCH || event.getClickedBlock().getType() == Material.BREWING_STAND) {
                    if (Cooldowns.isOnCooldown("restricted", event.getPlayer())) {
                        event.getPlayer().sendMessage(ChatColor.RED + "You can build again in " + ChatColor.BOLD + Cooldowns.getCooldownForPlayerInt("restricted", event.getPlayer()) + ChatColor.RED + "s!");
                        event.getPlayer().updateInventory();
                        event.setCancelled(true);
                        return;
                    }
                }
            }
        }
    }
}