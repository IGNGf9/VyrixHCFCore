/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.listener;

import cc.fyre.stark.engine.command.Command;
import cc.fyre.stark.engine.command.data.parameter.Param;
import com.google.common.collect.Maps;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.map.kits.Kit;
import net.hcriots.hcfactions.server.SpawnTagHandler;
import net.hcriots.hcfactions.team.Team;
import net.hcriots.hcfactions.team.claims.LandBoard;
import net.hcriots.hcfactions.team.dtr.DTRBitmask;
import net.hcriots.hcfactions.util.Players;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World.Environment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

public class KitMapListener implements Listener {

    private static final Map<UUID, String> kitEditing = Maps.newHashMap();

//    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
//    public void onCreatureSpawn(CreatureSpawnEvent event) {
//        event.setCancelled(true);
//    }

    @Command(names = {"kiteditor"}, permission = "")
    public static void editKit(Player player, @Param(name = "kit") Kit kit) {
        if (kit == null) {
            player.sendMessage(ChatColor.RED + "Unable to locate kit...");
            return;
        }

        if (!kit.getName().equalsIgnoreCase("pvp") && !kit.getName().equalsIgnoreCase("archer") && !kit.getName().equalsIgnoreCase("bard") && !kit.getName().equalsIgnoreCase("rogue") && !kit.getName().equalsIgnoreCase("builder") && !kit.getName().equalsIgnoreCase("miner")) {
            player.sendMessage(ChatColor.RED + "Unable to edit this kit...");
            return;
        }

        if (kitEditing.containsKey(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "You already seem to be editing a kit...");
            return;
        }

        Team team = LandBoard.getInstance().getTeam(player.getLocation());
        if (team == null || !team.hasDTRBitmask(DTRBitmask.SAFE_ZONE)) {
            player.sendMessage(ChatColor.RED + "You can only edit a kit from inside spawn!");
            return;
        }

        kitEditing.put(player.getUniqueId(), kit.getName());

        Inventory inventory = Bukkit.createInventory(player, kit.getInventoryContents().length, ChatColor.GOLD + "Kit Editor");
        inventory.setContents(Arrays.copyOf(kit.getInventoryContents(), kit.getInventoryContents().length));
        player.openInventory(inventory);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent e) {
        if (Hulu.getInstance().getConfig().getString("scoreboard.title").contains("cane")) {
            return;
        }
        Player victim = e.getEntity();

        // 1. killer should not be null
        // 2. victim should not be equal to killer
        // 3. victim should not be naked
        if (victim.getKiller() != null && !victim.getUniqueId().equals(victim.getKiller().getUniqueId()) && !Players.isNaked(victim)) {
            String killerName = victim.getKiller().getName();
            Hulu.getInstance().getEconomyHandler().deposit(victim.getKiller().getUniqueId(), 100 + getAdditional(victim.getKiller()));
            victim.getKiller().sendMessage(ChatColor.RED + "You received a reward for killing " + ChatColor.GREEN + victim.getName() + ChatColor.RED + ".");
        }
    }

    private int getAdditional(Player killer) {
        if (killer.hasPermission("hulu.money.basic")) {
            return 110;
        } else if (killer.hasPermission("hulu.money.iron")) {
            return 120;
        } else if (killer.hasPermission("hulu.money.gold")) {
            return 130;
        } else if (killer.hasPermission("hulu.money.diamond")) {
            return 140;
        } else if (killer.hasPermission("hulu.money.emerald")) {
            return 150;
        } else if (killer.hasPermission("hulu.money.riot")) {
            return 160;
        } else if (killer.hasPermission("hulu.money.riot-plus")) {
            return 170;
        } else {
            return 0;
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onProjectileHit(ProjectileHitEvent event) {
        Bukkit.getScheduler().runTaskLater(Hulu.getInstance(), event.getEntity()::remove, 1L);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        Team team = LandBoard.getInstance().getTeam(event.getEntity().getLocation());
        if (team != null && event.getEntity() instanceof Arrow && team.hasDTRBitmask(DTRBitmask.SAFE_ZONE)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        String command = event.getMessage().toLowerCase();
        if (command.startsWith("/pv") || command.startsWith("/playervault") || command.startsWith("pv") || command.startsWith("playervaults") || command.startsWith("/vault") || command.startsWith("vault") || command.startsWith("vc") || command.startsWith("/vc")) {
            if (SpawnTagHandler.isTagged(event.getPlayer())) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(ChatColor.RED + "You can't personal vaults in combat.");
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onDrop(PlayerDropItemEvent event) {
        if (kitEditing.containsKey(event.getPlayer().getUniqueId())) {
            event.getPlayer().sendMessage(ChatColor.RED + "You're unable to drop items whilst in the kit editor...");
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPickup(PlayerPickupItemEvent event) {
        if (kitEditing.containsKey(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        kitEditing.remove(event.getPlayer().getUniqueId());

        Hulu.getInstance().getMapHandler().getKitManager().logout(event.getPlayer().getUniqueId());
    }

    @EventHandler(ignoreCancelled = true)
    public void onClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        Inventory inventory = event.getInventory();

        if (inventory.getName().equals(ChatColor.GOLD + "Kit Editor")) {
            String editingKit = kitEditing.remove(player.getUniqueId());

            if (editingKit == null) {
                Bukkit.getLogger().info("wtf");
                return;
            }

            Hulu.getInstance().getMapHandler().getKitManager().saveKit(event.getPlayer().getUniqueId(), editingKit, inventory.getContents());
            player.sendMessage(ChatColor.GREEN + "Successfully saved kit!");
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Hulu.getInstance().getMapHandler().getKitManager().loadKits(event.getPlayer().getUniqueId());
    }

    @EventHandler(ignoreCancelled = true)
    public void onPortal(PlayerPortalEvent event) {
        if (event.getCause() != TeleportCause.NETHER_PORTAL) {
            return;
        }

        if (event.getTo().getWorld().getEnvironment() != Environment.NETHER) {
            return;
        }

        event.setTo(event.getTo().getWorld().getSpawnLocation().clone());
    }

    @EventHandler(ignoreCancelled = true)
    public void onClick(InventoryClickEvent event) {
        if (event.getInventory() == null || event.getInventory().getName() == null || event.getClickedInventory() == null || event.getClickedInventory().getName() == null) {
            return;
        }

        if (!event.getInventory().getName().equals(ChatColor.GOLD + "Kit Editor") && !event.getClickedInventory().getName().equals(ChatColor.GOLD + "Kit Editor")) {
            return;
        }

        // now we know that they're in the kit editor
        if (!event.getClickedInventory().getName().equals(ChatColor.GOLD + "Kit Editor")) {
            event.setCancelled(true); // they can only click shit in the kit editor
            return;
        }

        if (event.getClickedInventory() != event.getInventory()) {
            event.setCancelled(true);
            return;
        }

        if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
            event.setCancelled(true);
            return;
        }

        if (!event.getClickedInventory().getName().equals(ChatColor.GOLD + "Kit Editor")) {
            event.setCancelled(true);
            return;
        }

        if (event.getClickedInventory().getName().equals(ChatColor.GOLD + "Kit Editor") && event.getAction().name().startsWith("DROP")) {
            event.setCancelled(true);
            return;
        }

        if (event.getAction() == InventoryAction.HOTBAR_MOVE_AND_READD) {
            event.setCancelled(true);
            return;
        }

        if (event.getAction() == InventoryAction.HOTBAR_SWAP) {
            event.setCancelled(true);
            return;
        }

        Bukkit.getLogger().info(event.getViewers().get(0).getName() + ": " + event.getAction().toString());
    }
}