/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.listener;

import cc.fyre.stark.util.PlayerUtils;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.commands.CustomTimerCreateCommand;
import net.hcriots.hcfactions.map.stats.command.ChestCommand;
import net.hcriots.hcfactions.team.claims.LandBoard;
import net.hcriots.hcfactions.team.dtr.DTRBitmask;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;

import java.util.concurrent.ThreadLocalRandom;

public class BasicPreventionListener implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerKickEvent event) {
        event.setLeaveMessage(null);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        if (event.getEntity() instanceof Wither) {
            event.setCancelled(true);
        }

        if (DTRBitmask.SAFE_ZONE.appliesAt(event.getBlock().getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && player.getGameMode() != GameMode.CREATIVE) {
            if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.ENDER_CHEST) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (event.getInventory().getType() == InventoryType.ENDER_CHEST && !ChestCommand.getBYPASS().contains(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (event.getMessage().toLowerCase().startsWith("/kill") || event.getMessage().toLowerCase().startsWith("/slay") || event.getMessage().toLowerCase().startsWith("/bukkit:kill") || event.getMessage().toLowerCase().startsWith("/bukkit:slay") || event.getMessage().toLowerCase().startsWith("/suicide")) {
            if (!event.getPlayer().isOp()) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(ChatColor.RED + "No permission.");
            }
        }
    }

    @EventHandler
    public void onVehicleEnter(VehicleEnterEvent event) {
        if (event.getVehicle() instanceof Horse && event.getEntered() instanceof Player) {
            Horse horse = (Horse) event.getVehicle();
            Player player = (Player) event.getEntered();

            if (horse.getOwner() != null && !horse.getOwner().getName().equals(player.getName())) {
                event.setCancelled(true);
                boolean value = Hulu.getInstance().getLanguageMap().isNewLanguageToggle(player.getUniqueId());
                player.sendMessage((value ? ChatColor.RED + "This is not your horse!" : ChatColor.RED + "Este no es tu caballo!"));

            }
        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (DTRBitmask.SAFE_ZONE.appliesAt(event.getEntity().getLocation()) && event.getFoodLevel() < ((Player) event.getEntity()).getFoodLevel()) {
            event.setCancelled(true);
            return;
        }

        if (event.getFoodLevel() < ((Player) event.getEntity()).getFoodLevel()) {
            // Make food drop 1/2 as fast if you have PvP protection
            if (ThreadLocalRandom.current().nextInt(100) > (Hulu.getInstance().getPvPTimerMap().hasTimer(event.getEntity().getUniqueId()) ? 10 : 30)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        event.setRespawnLocation(Hulu.getInstance().getServerHandler().getSpawnLocation());
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent event) {
        if (Hulu.getInstance().getServerHandler().isWarzone(event.getBlock().getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        boolean value = Hulu.getInstance().getLanguageMap().isNewLanguageToggle(event.getPlayer().getUniqueId());
        if (Hulu.getInstance().getServerHandler().isSkybridgePrevention() && 150 < event.getBlock().getLocation().getY() && event.getPlayer().getGameMode() != GameMode.CREATIVE) {
            event.getPlayer().sendMessage((value ? ChatColor.RED + "You can't build higher than 150 blocks." : ChatColor.RED + "No puedes construir más de 150 bloques."));

            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onFish(PlayerInteractEvent event) {
        if (!Hulu.getInstance().getServerHandler().isRodPrevention() || (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)) {
            return;
        }

        if (event.getPlayer().getItemInHand() != null && event.getPlayer().getItemInHand().getType() == Material.FISHING_ROD) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL && event.getEntity().getType() == EntityType.SKELETON && ((Skeleton) event.getEntity()).getSkeletonType() == Skeleton.SkeletonType.WITHER) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockPlace(BlockPlaceEvent event) {
        boolean value = Hulu.getInstance().getLanguageMap().isNewLanguageToggle(event.getPlayer().getUniqueId());
        if (event.getPlayer().getWorld().getEnvironment() == World.Environment.NETHER && (event.getBlock().getType() == Material.BED || event.getBlock().getType() == Material.BED_BLOCK)) {
            event.setCancelled(true);
            event.getPlayer().sendMessage((value ? ChatColor.RED + "You cannot place beds in the Nether." : ChatColor.RED + "No puedes colocar camas en el Nether."));

        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockIgnite(BlockIgniteEvent event) {
        if (event.getCause() == BlockIgniteEvent.IgniteCause.SPREAD) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onFireBurn(BlockBurnEvent event) {
        if (Hulu.getInstance().getServerHandler().isWarzone(event.getBlock().getLocation())) {
            event.setCancelled(true);
            return;
        }

        if (Hulu.getInstance().getServerHandler().isUnclaimedOrRaidable(event.getBlock().getLocation())) {
            return;
        }

        if (LandBoard.getInstance().getTeam(event.getBlock().getLocation()) != null) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        event.blockList().clear();
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        EntityType type = event.getEntityType();

        if (type == EntityType.MINECART_TNT) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        if (!CustomTimerCreateCommand.isSOTWTimer()) {
            return;
        }

        Player damager = PlayerUtils.getDamageSource(event.getDamager());
        Entity damaged = event.getEntity();

        if (!(damaged instanceof Player)) {
            return;
        }

        if (!CustomTimerCreateCommand.hasSOTWEnabled(damager.getUniqueId())) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player && CustomTimerCreateCommand.isSOTWTimer() && !CustomTimerCreateCommand.hasSOTWEnabled(event.getEntity().getUniqueId())) {
            event.setCancelled(true);
        }
    }
}
