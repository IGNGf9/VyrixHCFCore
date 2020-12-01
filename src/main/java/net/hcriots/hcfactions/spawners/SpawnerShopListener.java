/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.spawners;

import cc.fyre.stark.util.ItemBuilder;
import com.google.common.collect.ImmutableMap;
import net.hcriots.hcfactions.Hulu;
import net.minecraft.util.org.apache.commons.lang3.text.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;

/**
 * Created by InspectMC
 * Date: 7/2/2020
 * Time: 4:57 PM
 */

public class SpawnerShopListener implements Listener {

    private final Hulu plugin;
    private final String[] error = {ChatColor.GREEN + "- Spawners -", ChatColor.RED + "Error", "", ""};
    private final ImmutableMap<EntityType, Integer> SPAWNERITEMS = new ImmutableMap.Builder<EntityType, Integer>().put(EntityType.SKELETON, 10).put(EntityType.SPIDER, 12).put(EntityType.ZOMBIE, 14).put(EntityType.CAVE_SPIDER, 16).build();
    private final int SPAWNER_PRICE = 20000;
    private final ImmutableMap<EntityType, Integer> spawners = new ImmutableMap.Builder<EntityType, Integer>().put(EntityType.SKELETON, SPAWNER_PRICE).put(EntityType.ZOMBIE, SPAWNER_PRICE).put(EntityType.SPIDER, SPAWNER_PRICE).put(EntityType.CAVE_SPIDER, SPAWNER_PRICE).build();
    private final String OUTLINE = ChatColor.GREEN + "- Spawners -";
    private final String[] lines = {"", OUTLINE, ChatColor.BLACK + "Click to buy", ""};
    private final Inventory inventory;

    public SpawnerShopListener(Hulu plugin) {
        this.plugin = plugin;
        inventory = Bukkit.createInventory(null, 3 * 9, ChatColor.DARK_GRAY + "Purchasable Spawners");
        for (Map.Entry<EntityType, Integer> entry : spawners.entrySet()) {
            EntityType entityType = entry.getKey();
            int value = entry.getValue();
            String name = ChatColor.GOLD + capitalizeString(entityType.name() + " Spawner");
            String costline = ChatColor.GRAY + "Price: " + ChatColor.GREEN + "$" + value;
            String information = ChatColor.GRAY.toString() + ChatColor.ITALIC + "Click to purchase..";
            ItemStack itemStack = new ItemBuilder(Material.MOB_SPAWNER).data(entityType.getTypeId()).name(name).addToLore("", costline, "", information).build();
            Integer slot = SPAWNERITEMS.get(entityType);
            if (slot != null) {
                inventory.setItem(slot, itemStack);
            }
        }
    }

    public static String capitalizeString(String string) {
        string = string.replace("_", " ");
        char[] chars = string.toLowerCase().toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i]) || chars[i] == '.' || chars[i] == '\'') {
                found = false;
            }
        }
        return String.valueOf(chars);
    }

    @EventHandler
    public void onSignPlace(SignChangeEvent e) {
        if (e.getLine(0).equals("-Spawners-")) {
            Player player = e.getPlayer();
            if (player.hasPermission("spawnershop.create")) {
                for (int i = 0; i < lines.length; i++) {
                    e.setLine(i, lines[i]);
                }
            } else {
                for (int i = 0; i < error.length; i++) {
                    e.setLine(i, error[i]);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Block block = e.getClickedBlock();
        if (e.useInteractedBlock() == Event.Result.ALLOW && block.getState() instanceof Sign) {
            Sign sign = (Sign) block.getState();
            for (int i = 0; i < lines.length; i++) {
                if (!sign.getLine(i).equals(lines[i])) {
                    return;
                }
            }
            player.openInventory(inventory);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player && e.getView() != null && e.getView().getTopInventory() != null && e.getView().getTopInventory().equals(inventory)) {
            e.setCancelled(true);

            if (e.getClickedInventory() != null && e.getClickedInventory().equals(inventory)) {
                Player player = (Player) e.getWhoClicked();
                int slot = e.getSlot();
                EntityType entityType = null;
                for (Map.Entry<EntityType, Integer> entry : SPAWNERITEMS.entrySet()) {
                    if (entry.getValue() == slot) {
                        entityType = entry.getKey();
                        break;
                    }
                }
                if (entityType != null) {
                    int cost = spawners.get(entityType);
                    double balance = Hulu.getInstance().getEconomyHandler().getBalance(player.getUniqueId());
                    if (cost > balance) {
                        player.sendMessage(ChatColor.RED + "Your personal balance must be $" + SPAWNER_PRICE + " to purchase a spawner.");
                        player.playSound(player.getLocation(), Sound.BLAZE_HIT, 1f, 1f);
                        player.closeInventory();
                    } else {
                        Hulu.getInstance().getEconomyHandler().setBalance(player.getUniqueId(), Hulu.getInstance().getEconomyHandler().getBalance(player.getUniqueId()) - cost);
                        player.playSound(player.getLocation(), Sound.LEVEL_UP, 1f, 1f);
                        player.sendMessage(ChatColor.GREEN + "You have bought 1x " + capitalizeString(entityType.name() + " Spawner."));
                        player.closeInventory();
                        ItemStack stack = new ItemBuilder(Material.MOB_SPAWNER)
                                .name(ChatColor.GREEN + "Spawner")
                                .data(entityType.getTypeId())
                                .addToLore(ChatColor.WHITE + WordUtils.capitalizeFully(entityType.name())).build();
                        for (ItemStack itemStack : player.getInventory().addItem(stack).values()) {
                            player.getWorld().dropItem(player.getLocation(), itemStack);
                        }
                    }
                }
            }
        }
    }

    public void closeInventory(Player player) {
        new BukkitRunnable() {
            public void run() {
                player.closeInventory();
            }
        }.runTask(plugin);
    }
}