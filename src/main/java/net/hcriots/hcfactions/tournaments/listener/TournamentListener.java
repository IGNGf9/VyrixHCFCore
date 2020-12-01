/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.tournaments.listener;

import cc.fyre.stark.util.ItemBuilder;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.tournaments.Tournament;
import net.hcriots.hcfactions.tournaments.TournamentState;
import net.hcriots.hcfactions.tournaments.TournamentType;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by InspectMC
 * Date: 8/3/2020
 * Time: 7:57 PM
 */

public class TournamentListener implements Listener {

    private final Hulu plugin = Hulu.getInstance();


    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (player.getGameMode() == GameMode.CREATIVE && player.hasPermission("stark.staff")) {
            return;
        }
        if (this.plugin.getTournamentHandler().getTournament() != null && this.plugin.getTournamentHandler().isInTournament(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (player.getGameMode() == GameMode.CREATIVE && player.hasPermission("stark.staff")) {
            return;
        }
        if (this.plugin.getTournamentHandler().getTournament() != null && this.plugin.getTournamentHandler().isInTournament(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        final Player player = event.getPlayer();
        if (this.plugin.getTournamentHandler().isInTournament(player) && (event.getMessage().equalsIgnoreCase("/staff") || event.getMessage().equalsIgnoreCase("/mod") || event.getMessage().equalsIgnoreCase("/v") || event.getMessage().equalsIgnoreCase("/vanish") || event.getMessage().equalsIgnoreCase("/hcf:staff") || event.getMessage().equalsIgnoreCase("/hcf:vanish") || event.getMessage().equalsIgnoreCase("/hcf:mod") || event.getMessage().equalsIgnoreCase("/hcf:v") || event.getMessage().equalsIgnoreCase("/f") || event.getMessage().equalsIgnoreCase("hcf:/spawn") || event.getMessage().equalsIgnoreCase("/spawn") || event.getMessage().equalsIgnoreCase("/enderchest") || event.getMessage().equalsIgnoreCase("/ec") || event.getMessage().equalsIgnoreCase("/echest") || event.getMessage().equalsIgnoreCase("/chest") || event.getMessage().equalsIgnoreCase("/pv") || event.getMessage().equalsIgnoreCase("/playervault") || event.getMessage().equalsIgnoreCase("/faction") || event.getMessage().equalsIgnoreCase("/f") || event.getMessage().equalsIgnoreCase("/fac") || event.getMessage().equalsIgnoreCase("/f home") || event.getMessage().equalsIgnoreCase("/fac home") || event.getMessage().equalsIgnoreCase("/faction home") || event.getMessage().equalsIgnoreCase("/kit") || event.getMessage().equalsIgnoreCase("/gkit") || event.getMessage().equalsIgnoreCase("/kits") || event.getMessage().equalsIgnoreCase("/reclaim") || event.getMessage().equalsIgnoreCase("/more") || event.getMessage().equalsIgnoreCase("/feed") || event.getMessage().equalsIgnoreCase("/heal") || event.getMessage().equalsIgnoreCase("/rename") || event.getMessage().equalsIgnoreCase("/reclaim") || event.getMessage().equalsIgnoreCase("/fix") || event.getMessage().equalsIgnoreCase("/repair") || event.getMessage().contains("/ec") || event.getMessage().contains("/enderchest") || event.getMessage().equalsIgnoreCase("/fixall"))) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "You cannot use this command in the event!");
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!this.plugin.getTournamentHandler().isInTournament(player.getUniqueId())) {
            return;
        }
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
            if (player.getItemInHand().getType().equals(Material.NETHER_STAR) && player.getItemInHand().getItemMeta().hasDisplayName() && player.getItemInHand().getItemMeta().hasLore() && this.plugin.getTournamentHandler().getTournament() != null && this.plugin.getTournamentHandler().getTournament().getTournamentState() != TournamentState.FIGHTING && this.plugin.getTournamentHandler().isInTournament(player.getUniqueId())) {
                player.performCommand("tournament leave");
            }
            Tournament tournament = this.plugin.getTournamentHandler().getTournament();
            if (tournament.getType() == TournamentType.DIAMOND && player.getItemInHand().getType().equals(Material.ENCHANTED_BOOK) && player.getItemInHand().getItemMeta().hasDisplayName() && player.getItemInHand().getItemMeta().hasLore() && this.plugin.getTournamentHandler().getTournament() != null && this.plugin.getTournamentHandler().isInTournament(player.getUniqueId())) {
                final PlayerInventory inventory = player.getInventory();
                inventory.clear();
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999, 1));
                player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999, 0));
                inventory.setHelmet(new ItemBuilder(Material.DIAMOND_HELMET).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).enchant(Enchantment.DURABILITY, 3).build());
                inventory.setChestplate(new ItemBuilder(Material.DIAMOND_CHESTPLATE).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).enchant(Enchantment.DURABILITY, 3).build());
                inventory.setLeggings(new ItemBuilder(Material.DIAMOND_LEGGINGS).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).enchant(Enchantment.DURABILITY, 3).build());
                inventory.setBoots(new ItemBuilder(Material.DIAMOND_BOOTS).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).enchant(Enchantment.DURABILITY, 3).enchant(Enchantment.PROTECTION_FALL, 4).build());
                inventory.setItem(0, new ItemBuilder(Material.DIAMOND_SWORD).enchant(Enchantment.DAMAGE_ALL, 1).enchant(Enchantment.DURABILITY, 3).build());
                inventory.setItem(1, new ItemBuilder(Material.ENDER_PEARL, 16).build());
                inventory.setItem(8, new ItemBuilder(Material.BAKED_POTATO, 64).build());
                for (int i = 0; i < 17; ++i) {
                    inventory.addItem(new ItemStack(Material.POTION, 1, (short) 16421));
                }
                player.updateInventory();
            }
            if (tournament.getType() == TournamentType.AXE && player.getItemInHand().getType().equals(Material.ENCHANTED_BOOK) && player.getItemInHand().getItemMeta().hasDisplayName() && player.getItemInHand().getItemMeta().hasLore() && this.plugin.getTournamentHandler().getTournament() != null && this.plugin.getTournamentHandler().isInTournament(player.getUniqueId())) {
                final PlayerInventory inventory = player.getInventory();
                inventory.clear();
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999, 1));
                player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999, 0));
                inventory.setHelmet(new ItemBuilder(Material.IRON_HELMET).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).enchant(Enchantment.DURABILITY, 3).build());
                inventory.setChestplate(new ItemBuilder(Material.IRON_CHESTPLATE).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).enchant(Enchantment.DURABILITY, 3).build());
                inventory.setLeggings(new ItemBuilder(Material.IRON_LEGGINGS).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).enchant(Enchantment.DURABILITY, 3).build());
                inventory.setBoots(new ItemBuilder(Material.IRON_BOOTS).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).enchant(Enchantment.DURABILITY, 3).enchant(Enchantment.PROTECTION_FALL, 4).build());
                inventory.setItem(0, new ItemBuilder(Material.IRON_AXE).enchant(Enchantment.DAMAGE_ALL, 1).enchant(Enchantment.DURABILITY, 3).build());
                inventory.setItem(8, new ItemBuilder(Material.BAKED_POTATO, 64).build());
                for (int i = 0; i < 8; ++i) {
                    inventory.addItem(new ItemStack(Material.POTION, 1, (short) 16421));
                }
                player.updateInventory();
            }
            if (tournament.getType() == TournamentType.ARCHER && player.getItemInHand().getType().equals(Material.ENCHANTED_BOOK) && player.getItemInHand().getItemMeta().hasDisplayName() && player.getItemInHand().getItemMeta().hasLore() && this.plugin.getTournamentHandler().getTournament() != null && this.plugin.getTournamentHandler().isInTournament(player.getUniqueId())) {
                final PlayerInventory inventory = player.getInventory();
                inventory.clear();
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999, 1));
                player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999, 0));
                inventory.setHelmet(new ItemBuilder(Material.LEATHER_HELMET).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).enchant(Enchantment.DURABILITY, 3).build());
                inventory.setChestplate(new ItemBuilder(Material.LEATHER_CHESTPLATE).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).enchant(Enchantment.DURABILITY, 3).build());
                inventory.setLeggings(new ItemBuilder(Material.LEATHER_LEGGINGS).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).enchant(Enchantment.DURABILITY, 3).build());
                inventory.setBoots(new ItemBuilder(Material.LEATHER_BOOTS).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).enchant(Enchantment.DURABILITY, 3).enchant(Enchantment.PROTECTION_FALL, 4).build());
                inventory.setItem(0, new ItemBuilder(Material.BOW).enchant(Enchantment.ARROW_DAMAGE, 4).enchant(Enchantment.ARROW_INFINITE, 1).enchant(Enchantment.DURABILITY, 3).build());
                inventory.setItem(1, new ItemBuilder(Material.ARROW, 1).build());
                inventory.setItem(7, new ItemBuilder(Material.GOLDEN_APPLE, 4).build());
                inventory.setItem(8, new ItemBuilder(Material.BAKED_POTATO, 64).build());
                player.updateInventory();
            }
            if (tournament.getType() == TournamentType.ROGUE && player.getItemInHand().getType().equals(Material.ENCHANTED_BOOK) && player.getItemInHand().getItemMeta().hasDisplayName() && player.getItemInHand().getItemMeta().hasLore() && this.plugin.getTournamentHandler().getTournament() != null && this.plugin.getTournamentHandler().isInTournament(player.getUniqueId())) {
                final PlayerInventory inventory = player.getInventory();
                inventory.clear();
                player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999, 0));
                inventory.setHelmet(new ItemBuilder(Material.CHAINMAIL_HELMET).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).enchant(Enchantment.DURABILITY, 3).build());
                inventory.setChestplate(new ItemBuilder(Material.CHAINMAIL_CHESTPLATE).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).enchant(Enchantment.DURABILITY, 3).build());
                inventory.setLeggings(new ItemBuilder(Material.CHAINMAIL_LEGGINGS).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).enchant(Enchantment.DURABILITY, 3).build());
                inventory.setBoots(new ItemBuilder(Material.CHAINMAIL_BOOTS).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).enchant(Enchantment.DURABILITY, 3).enchant(Enchantment.PROTECTION_FALL, 4).build());
                inventory.setItem(0, new ItemBuilder(Material.DIAMOND_SWORD).enchant(Enchantment.DAMAGE_ALL, 1).enchant(Enchantment.DURABILITY, 3).build());
                inventory.setItem(1, new ItemBuilder(Material.ENDER_PEARL, 16).build());
                inventory.setItem(8, new ItemBuilder(Material.BAKED_POTATO, 64).build());
                for (int i = 0; i < 17; ++i) {
                    inventory.addItem(new ItemStack(Material.POTION, 1, (short) 16421));
                }
                player.updateInventory();
            }
            if (player.getGameMode() == GameMode.CREATIVE) {
                return;
            }
            if (player.getItemInHand().getType().equals(Material.ENDER_PEARL) && this.plugin.getTournamentHandler().getTournament().getTournamentState() == TournamentState.FIGHTING && this.plugin.getTournamentHandler().getTournament().isActiveProtection()) {
                event.setUseItemInHand(Event.Result.DENY);
                player.sendMessage(ChatColor.RED + "You cannot pearl while in an event.");
            }
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand().getType().equals(Material.ENCHANTED_BOOK) && player.getItemInHand().getItemMeta().hasDisplayName() && player.getItemInHand().getItemMeta().hasLore() && this.plugin.getTournamentHandler().getTournament() != null && this.plugin.getTournamentHandler().isInTournament(player.getUniqueId())) {
            event.setCancelled(true);
            return;
        }
        if (this.plugin.getTournamentHandler().getTournament() != null && this.plugin.getTournamentHandler().isInTournament(player.getUniqueId())) {
            if (this.plugin.getTournamentHandler().getTournament().getTournamentState() != TournamentState.FIGHTING) {
                event.setCancelled(true);
            } else {
                event.setCancelled(true);
                if (player.getItemInHand().getAmount() > 1) {
                    player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
                } else {
                    player.getItemInHand().setType(Material.AIR);
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player player = (Player) event.getEntity();
            Player damager = (Player) event.getDamager();
            if (this.plugin.getTournamentHandler().getTournament() != null && this.plugin.getTournamentHandler().getTournament().getTournamentState() == TournamentState.FIGHTING && this.plugin.getTournamentHandler().getTournament().getType() == TournamentType.SUMO && this.plugin.getTournamentHandler().isInTournament(player.getUniqueId()) && this.plugin.getTournamentHandler().isInTournament(damager.getUniqueId())) {
                event.setDamage(0.0);
            }
        }
    }

//    @EventHandler
//    public void onPlayerMove(PlayerMoveEvent event) {
//        Location from = event.getFrom();
//        Location to = event.getTo();
//        if ((from.getBlockX() == to.getBlockX()) && (from.getBlockY() == to.getBlockY()) && (from.getBlockZ() == to.getBlockZ())) return;
//        Player player = event.getPlayer();
//        if ((this.plugin.getTournamentHandler().getTournament() != null) && (this.plugin.getTournamentHandler().getTournament().getTournamentState() == TournamentState.FIGHTING) &&
//                (this.plugin.getTournamentHandler().isInTournament(player.getUniqueId()))) {
//            if (player.getLocation().getBlockY() <= 50) {
//                if (!player.isDead()) {
//                    player.setHealth(0);
//                }
//            } else if ((this.plugin.getTournamentHandler().getTournament().getType() == TournamentType.SUMO) && (player.getLocation().getBlockY() <= LocationUtils.getLocation(TournamentFile.getConfig().getString("Locations.Sumo.First")).getBlockY() - 2) &&
//                    (!player.isDead())) {
//                player.setHealth(0);
//            }
//        }
//    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (this.plugin.getTournamentHandler().getTournament() != null && this.plugin.getTournamentHandler().getTournament().getTournamentState() == TournamentState.FIGHTING && this.plugin.getTournamentHandler().isInTournament(player.getUniqueId())) {
            new BukkitRunnable() {
                public void run() {
                    TournamentListener.this.plugin.getTournamentHandler().playerLeft(TournamentListener.this.plugin.getTournamentHandler().getTournament(), player, false);
                }
            }.runTaskLater(this.plugin, 20L);
        }
        final Tournament tournament = this.plugin.getTournamentHandler().getTournament();
        if (this.plugin.getTournamentHandler().isInTournament(player) && (tournament.getType() == TournamentType.DIAMOND ||
                tournament.getType() == TournamentType.AXE ||
                tournament.getType() == TournamentType.ARCHER ||
                tournament.getType() == TournamentType.ROGUE)) {
            event.getDrops().clear();
        }
    }


    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (this.plugin.getTournamentHandler().isInTournament(player.getUniqueId())) {
            this.plugin.getTournamentHandler().playerLeft(this.plugin.getTournamentHandler().getTournament(), player, false);
        }
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        Player player = event.getPlayer();
        if (this.plugin.getTournamentHandler().isInTournament(player.getUniqueId())) {
            this.plugin.getTournamentHandler().playerLeft(this.plugin.getTournamentHandler().getTournament(), player, false);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            Tournament tournament = this.plugin.getTournamentHandler().getTournament();
            if (tournament != null && this.plugin.getTournamentHandler().isInTournament(player)) {
                if (tournament.getTournamentState() == TournamentState.WAITING) {
                    event.setCancelled(true);
                } else if (tournament.getTournamentState() == TournamentState.STARTING) {
                    event.setCancelled(true);
                } else if (tournament.getTournamentState() == TournamentState.FIGHTING && tournament.isActiveProtection()) {
                    event.setCancelled(true);
                } else if (tournament.getTournamentState() == TournamentState.FIGHTING && tournament.getType() == TournamentType.SUMO) {
                    if (tournament.getFirstPlayer() == player || tournament.getSecondPlayer() == player) {
                        return;
                    }
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (!this.plugin.getTournamentHandler().isInTournament(player)) {
            return;
        }
        if (event.getCurrentItem() == null || event.getSlotType() == null || event.getCurrentItem().getType() == Material.AIR) {
            return;
        }
        if (event.getCurrentItem().getType().equals(Material.DIAMOND_BOOTS) || event.getCurrentItem().getType().equals(Material.DIAMOND_LEGGINGS) || event.getCurrentItem().getType().equals(Material.DIAMOND_CHESTPLATE) || event.getCurrentItem().getType().equals(Material.DIAMOND_HELMET)) {
            player.sendMessage(ChatColor.RED + "You cannot move these items in your inventory.");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent event) {
        Player player = (Player) event.getEntity();
        if (this.plugin.getTournamentHandler().isInTournament(player)) {
            if (this.plugin.getTournamentHandler().getTournament().getType() == TournamentType.SUMO) {
                event.setCancelled(true);
            } else if (this.plugin.getTournamentHandler().getTournament().getTournamentState() != TournamentState.FIGHTING) {
                event.setCancelled(true);
            }
            if (event.isCancelled()) {
                player.setFoodLevel(20);
            }
        }
    }

    @EventHandler
    public void onPlayerHitsWater(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        Tournament tournament = this.plugin.getTournamentHandler().getTournament();
        Block legs = player.getLocation().getBlock();
        Block head = legs.getRelative(BlockFace.UP);
        if (this.plugin.getTournamentHandler().isInTournament(player.getUniqueId())) {
            if (tournament.getType() == TournamentType.SUMO && tournament.getTournamentState() == TournamentState.FIGHTING) {
                if (legs.getType() == Material.WATER || legs.getType() == Material.STATIONARY_WATER || head.getType() == Material.WATER || head.getType() == Material.STATIONARY_WATER) {
                    this.plugin.getTournamentHandler().playerLeft(this.plugin.getTournamentHandler().getTournament(), player, false);

                }
            }
        }
    }
}