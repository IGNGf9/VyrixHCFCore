/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.listener;

import cc.fyre.stark.util.PlayerUtils;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.commands.CustomTimerCreateCommand;
import net.hcriots.hcfactions.events.Event;
import net.hcriots.hcfactions.events.koth.KOTH;
import net.hcriots.hcfactions.team.Team;
import net.hcriots.hcfactions.team.claims.LandBoard;
import net.hcriots.hcfactions.team.dtr.DTRBitmask;
import net.hcriots.hcfactions.util.InventoryUtils;
import net.hcriots.hcfactions.util.RegenUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.TimeUnit;

public class SpawnListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockIgnite(BlockIgniteEvent event) {
        if (event.getPlayer() != null) {
            if (Hulu.getInstance().getServerHandler().isAdminOverride(event.getPlayer())) {
                return;
            }
        }

        if (DTRBitmask.SAFE_ZONE.appliesAt(event.getBlock().getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (Hulu.getInstance().getServerHandler().isAdminOverride(event.getPlayer())) {
            return;
        }

        if (DTRBitmask.SAFE_ZONE.appliesAt(event.getBlock().getLocation())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.YELLOW + "You cannot build in spawn!");
        } else if (Hulu.getInstance().getServerHandler().isSpawnBufferZone(event.getBlock().getLocation()) || Hulu.getInstance().getServerHandler().isNetherBufferZone(event.getBlock().getLocation())) {
            if (!DTRBitmask.SAFE_ZONE.appliesAt(event.getBlock().getLocation()) && event.getItemInHand() != null && event.getItemInHand().getType() == Material.WEB && (Hulu.getInstance().getMapHandler().isKitMap())) {
                for (Event playableEvent : Hulu.getInstance().getEventHandler().getEvents()) {
                    if (!playableEvent.isActive() || !(playableEvent instanceof KOTH)) {
                        continue;
                    }

                    KOTH koth = (KOTH) playableEvent;

                    if (koth.onCap(event.getBlockPlaced().getLocation())) {
                        event.setCancelled(true);
                        event.getPlayer().sendMessage(ChatColor.YELLOW + "You can't place web on cap!");
                        event.getPlayer().setItemInHand(null);

                        event.getPlayer().setMetadata("ImmuneFromGlitchCheck", new FixedMetadataValue(Hulu.getInstance(), new Object()));

                        Bukkit.getScheduler().runTask(Hulu.getInstance(), () -> {
                            event.getPlayer().removeMetadata("ImmuneFromGlitchCheck", Hulu.getInstance());
                        });

                        return;
                    }
                }

                new BukkitRunnable() {

                    @Override
                    public void run() {
                        if (event.getBlock().getType() == Material.WEB) {
                            event.getBlock().setType(Material.AIR);
                        }
                    }

                }.runTaskLater(Hulu.getInstance(), 10 * 20L);
            } else {
                event.setCancelled(true);
                event.getPlayer().sendMessage(ChatColor.YELLOW + "You cannot build this close to spawn!");
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        if (Hulu.getInstance().getServerHandler().isAdminOverride(event.getPlayer())) {
            return;
        }

        if (DTRBitmask.SAFE_ZONE.appliesAt(event.getBlock().getLocation())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.YELLOW + "You cannot build in spawn!");
        } else if (!DTRBitmask.DTC.appliesAt(event.getBlock().getLocation()) && (Hulu.getInstance().getServerHandler().isSpawnBufferZone(event.getBlock().getLocation()) || Hulu.getInstance().getServerHandler().isNetherBufferZone(event.getBlock().getLocation()))) {
            event.setCancelled(true);

            if (event.getBlock().getType() != Material.LONG_GRASS && event.getBlock().getType() != Material.GRASS) {
                event.getPlayer().sendMessage(ChatColor.YELLOW + "You cannot build this close to spawn!");
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onHangingPlace(HangingPlaceEvent event) {
        if (Hulu.getInstance().getServerHandler().isAdminOverride(event.getPlayer())) {
            return;
        }

        if (DTRBitmask.SAFE_ZONE.appliesAt(event.getEntity().getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onHangingBreakByEntity(HangingBreakByEntityEvent event) {
        if (!(event.getRemover() instanceof Player) || Hulu.getInstance().getServerHandler().isAdminOverride((Player) event.getRemover())) {
            return;
        }

        if (DTRBitmask.SAFE_ZONE.appliesAt(event.getEntity().getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event) {
        if (event.getRightClicked().getType() != EntityType.ITEM_FRAME || Hulu.getInstance().getServerHandler().isAdminOverride(event.getPlayer())) {
            return;
        }

        if (DTRBitmask.SAFE_ZONE.appliesAt(event.getRightClicked().getLocation())) {
            event.setCancelled(true);
        }
    }

    // Used for item frames
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player) || event.getEntity().getType() != EntityType.ITEM_FRAME || Hulu.getInstance().getServerHandler().isAdminOverride((Player) event.getDamager())) {
            return;
        }

        if (DTRBitmask.SAFE_ZONE.appliesAt(event.getEntity().getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {
        if (Hulu.getInstance().getServerHandler().isAdminOverride(event.getPlayer())) {
            return;
        }

        if (Hulu.getInstance().getServerHandler().isSpawnBufferZone(event.getBlockClicked().getLocation())) {
            if (!Hulu.getInstance().getServerHandler().isWaterPlacementInClaimsAllowed()) {
                event.setCancelled(true);
            } else {
                final Block waterBlock = event.getBlockClicked().getRelative(event.getBlockFace());

                if (waterBlock.getRelative(BlockFace.NORTH).isLiquid() || waterBlock.getRelative(BlockFace.SOUTH).isLiquid() || waterBlock.getRelative(BlockFace.EAST).isLiquid() || waterBlock.getRelative(BlockFace.WEST).isLiquid()) {
                    event.setCancelled(true);
                    return;
                }

                RegenUtils.schedule(waterBlock, 30, TimeUnit.SECONDS, (block) -> InventoryUtils.fillBucket(event.getPlayer()), (block) -> true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        if ((event.getEntity() instanceof Player || event.getEntity() instanceof Horse) && DTRBitmask.SAFE_ZONE.appliesAt(event.getEntity().getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onEntityDamageByEntity2(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player damager = PlayerUtils.getDamageSource(event.getDamager()); // find the player damager if one exists

        if (damager != null) {
            Player victim = (Player) event.getEntity();

            if (DTRBitmask.SAFE_ZONE.appliesAt(victim.getLocation()) || DTRBitmask.SAFE_ZONE.appliesAt(damager.getLocation())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDrop(PlayerDropItemEvent event) {
        Team team = LandBoard.getInstance().getTeam(event.getPlayer().getLocation());
        if (team != null && team.hasDTRBitmask(DTRBitmask.SAFE_ZONE) && (CustomTimerCreateCommand.isSOTWTimer())) {
            event.getItemDrop().remove();
        }
    }

}