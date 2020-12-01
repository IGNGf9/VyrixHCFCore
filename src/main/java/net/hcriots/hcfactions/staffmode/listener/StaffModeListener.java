/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.staffmode.listener;

import cc.fyre.stark.Stark;
import cc.fyre.stark.core.profile.Profile;
import cc.fyre.stark.core.rank.Rank;
import cc.fyre.stark.util.ItemBuilder;
import net.hcriots.hcfactions.staffmode.StaffModeHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.ContainerBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class StaffModeListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("stark.staff")) {
            new StaffModeHandler(player);
            StaffModeHandler.getStaffModeMap().values()
                    .stream()
                    .filter(StaffModeHandler::isHidden)
                    .map(StaffModeHandler::getPlayer)
                    .forEach(player::hidePlayer);
        }
    }

    @EventHandler
    public void onPlayerInteractChestEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (StaffModeHandler.hasStaffMode(player)) {
            event.setCancelled(true);

            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                Block block = event.getClickedBlock();

                if (block.getState() instanceof ContainerBlock) {
                    ContainerBlock container = (ContainerBlock) block.getState();

                    Inventory copy = Bukkit.createInventory(null, container.getInventory().getSize(), container.getInventory().getTitle());
                    for (int i = 0; i < container.getInventory().getSize(); i++) {
                        copy.setItem(i, container.getInventory().getItem(i));
                    }
                    player.openInventory(copy);
                    player.sendMessage(ChatColor.YELLOW + "Opening chest silently...");
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        if (StaffModeHandler.hasStaffMode(player)) {
            if (event.getPlayer().getItemInHand() != null && event.getRightClicked() instanceof Player && event.getPlayer().getItemInHand().getType() == Material.BOOK) {
                player.performCommand("invsee " + ((Player) event.getRightClicked()).getName());
            }

            if (event.getPlayer().getItemInHand() != null && event.getRightClicked() instanceof Player && event.getPlayer().getItemInHand().getType() == Material.PACKED_ICE) {
                player.performCommand("freeze " + ((Player) event.getRightClicked()).getName());
            }
        }
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (StaffModeHandler.hasStaffMode(player) && event.getClickedInventory() != null && event.getClickedInventory().getName().equalsIgnoreCase("Online Staff") && (event.getCurrentItem() != null && event.getCurrentItem().hasItemMeta() && event.getCurrentItem().hasItemMeta())) {
            if (event.getClickedInventory().getName().equalsIgnoreCase("Online Staff")) {
                String toTp = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
                player.performCommand("teleport " + toTp);
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (StaffModeHandler.hasStaffMode(player)) {
            StaffModeHandler.getStaffModeMap().get(player).remove();
        }
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();


        if (event.hasItem() && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) && StaffModeHandler.hasStaffMode(player)) {

            event.setCancelled(!player.hasPermission("hcf.staffmode.bypass"));

            StaffModeHandler mode = StaffModeHandler.getStaffModeMap().get(player);
            if (item.getType() == Material.WATCH) {
                List<Player> players = Bukkit.getOnlinePlayers()
                        .stream()
                        .filter(user -> !user.hasPermission("stark.staff"))
                        .collect(Collectors.toList());
                Random random = new Random();
                try {
                    int rand = random.nextInt(players.size());
                    Player playerTP = players.get(rand);
                    player.teleport(playerTP);
                    player.sendMessage(ChatColor.YELLOW + "You have been teleported to " + ChatColor.WHITE + playerTP.getName());
                } catch (IllegalArgumentException exception) {
                    player.sendMessage(ChatColor.RED + "It seems there are no players online!");
                }

            } else if (item.getType() == Material.INK_SACK) {
                mode.setHidden(!mode.isHidden());
                player.setItemInHand(new ItemBuilder(Material.INK_SACK).data((byte) (mode.isHidden() ? 8 : 10)).name(mode.isHidden() ? ChatColor.GOLD + "Become Visible" : ChatColor.GOLD + "Become Invisible").build());

            } else if (item.getType() == Material.SKULL_ITEM) {
                event.setCancelled(true);
                Set<Player> members = StaffModeHandler.getStaffModeMap().keySet();
                int rows = (int) Math.ceil(members.size() / 9.0);
                Inventory inventory = Bukkit.createInventory(null, (rows == 0) ? 9 : (9 * rows), "Online Staff");
                for (Player member : members) {
                    Profile profile = Stark.instance.core.getProfileHandler().getByUUID(member.getUniqueId());
                    Rank rank = profile.getRank();
                    inventory.addItem(new ItemBuilder(Material.SKULL_ITEM).data((byte) 3).name(member.getDisplayName())
                            .addToLore(ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "------------------------")
                            .addToLore(ChatColor.GOLD + "Rank: " + rank.getColoredName())
                            .addToLore(ChatColor.GOLD + "Mod Mode: " + ChatColor.GREEN + StaffModeHandler.getStaffModeMap().get(player).isHidden())
                            .addToLore(" ")
                            .addToLore(ChatColor.GRAY + "Right Click to teleport.")
                            .addToLore(ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "------------------------")
                            .build());
                }
                player.openInventory(inventory);
            }
        }
    }

    @EventHandler
    public void onPlayerPlaceBlock(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (StaffModeHandler.hasStaffMode(player)) {
            if (player.hasPermission("stark.admin") || player.isOp()) {
                return;
            }
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerBreakBlock(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (StaffModeHandler.hasStaffMode(player)) {
            if (player.hasPermission("stark.admin") || player.isOp()) {
                return;
            }
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerPickupItemEvent(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        if (StaffModeHandler.hasStaffMode(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            if (StaffModeHandler.hasStaffMode(player) && StaffModeHandler.getStaffModeMap().get(player).isHidden()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (StaffModeHandler.hasStaffMode(player)) {
            event.getDrops().clear();
        }
    }

    @EventHandler
    public void onPlayerDropItemEvent(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (StaffModeHandler.hasStaffMode(player)) {
            event.getItemDrop().remove();
            if (player.getItemInHand().getAmount() == 0) {
                player.setItemInHand(event.getItemDrop().getItemStack());
            } else {
                player.getItemInHand().setAmount(player.getItemInHand().getAmount() + 1);
            }
            player.updateInventory();
        }
    }
}