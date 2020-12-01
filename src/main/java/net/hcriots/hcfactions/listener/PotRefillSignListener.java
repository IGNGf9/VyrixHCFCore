/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PotRefillSignListener implements Listener {

    public static Inventory menu(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 54, ChatColor.DARK_GRAY + "Potions");
        ItemStack glass = new ItemStack(Material.POTION, 1, (short) 16421);
        ItemMeta glassMeta = glass.getItemMeta();
        glass.setItemMeta(glassMeta);
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack itemStack = inventory.getItem(i);
            if (itemStack == null) {
                inventory.setItem(i, glass);
            }
        }
        return inventory;
    }

    @EventHandler
    public void onSignChange(SignChangeEvent e) {
        Player player = e.getPlayer();
        if (!player.hasPermission("hcf.sign.create")) {
            if (e.getLine(0).equalsIgnoreCase("[Refill]")) {
                e.setCancelled(true);
                return;
            }
        }
        if (e.getLine(0).equalsIgnoreCase("[Refill]")) {
            e.setLine(0, ChatColor.GOLD + "- Free -");
            e.setLine(1, ChatColor.BLACK + "Potions");
            e.setLine(2, "");
            e.setLine(3, "");
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (!(e.getAction() == Action.RIGHT_CLICK_BLOCK)) return;
        if (e.getClickedBlock().getState() instanceof Sign) {
            Sign s = (Sign) e.getClickedBlock().getState();
            if (s.getLine(1).equalsIgnoreCase(ChatColor.BLACK + "Potions")) {
                player.openInventory(menu(player));
            }
        }
    }
}
