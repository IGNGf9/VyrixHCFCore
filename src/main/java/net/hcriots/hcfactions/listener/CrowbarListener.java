/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.listener;

import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.server.event.CrowbarSpawnerBreakEvent;
import net.hcriots.hcfactions.team.Team;
import net.hcriots.hcfactions.team.claims.LandBoard;
import net.hcriots.hcfactions.team.dtr.DTRBitmask;
import net.hcriots.hcfactions.util.InventoryUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CrowbarListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        boolean value = Hulu.getInstance().getLanguageMap().isNewLanguageToggle(event.getPlayer().getUniqueId());

        if (event.getItem() == null || !InventoryUtils.isSimilar(event.getItem(), InventoryUtils.CROWBAR_NAME) || !(event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            return;
        }

        if (!Hulu.getInstance().getServerHandler().isUnclaimedOrRaidable(event.getClickedBlock().getLocation()) && !Hulu.getInstance().getServerHandler().isAdminOverride(event.getPlayer())) {
            Team team = LandBoard.getInstance().getTeam(event.getClickedBlock().getLocation());

            if (team != null && !team.isMember(event.getPlayer().getUniqueId())) {
                event.getPlayer().sendMessage((value ? ChatColor.YELLOW + "You cannot crowbar in " + ChatColor.RED + team.getName(event.getPlayer()) + ChatColor.YELLOW + "'s territory!" : ChatColor.YELLOW + "No puedes usar la crowbar en " + ChatColor.RED + team.getName(event.getPlayer()) + ChatColor.YELLOW + "'s territorios"));
                return;
            }
        }

        if (DTRBitmask.SAFE_ZONE.appliesAt(event.getClickedBlock().getLocation())) {
            event.getPlayer().sendMessage((value ? ChatColor.RED + "You cannot crowbar spawn!" : ChatColor.RED + "No puedes usar una crowbar en el spawn!"));
            return;
        }

        if (event.getClickedBlock().getType() == Material.ENDER_PORTAL_FRAME) {
            int portals = InventoryUtils.getCrowbarUsesPortal(event.getItem());

            if (portals == 0) {
                event.getPlayer().sendMessage((value ? ChatColor.RED + "This crowbar has no more uses on end portals!" : ChatColor.RED + "La crowbar no tiene más usos para romper fragmentos del portal al end."));

                return;
            }

            event.getClickedBlock().getWorld().playEffect(event.getClickedBlock().getLocation(), Effect.STEP_SOUND, event.getClickedBlock().getTypeId());
            event.getClickedBlock().setType(Material.AIR);
            event.getClickedBlock().getState().update();

            event.getClickedBlock().getWorld().dropItemNaturally(event.getClickedBlock().getLocation(), new ItemStack(Material.ENDER_PORTAL_FRAME));
            event.getClickedBlock().getWorld().playSound(event.getClickedBlock().getLocation(), Sound.ANVIL_USE, 1.0F, 1.0F);

            for (int x = -3; x < 3; x++) {
                for (int z = -3; z < 3; z++) {
                    Block block = event.getClickedBlock().getLocation().add(x, 0, z).getBlock();

                    if (block.getType() == Material.ENDER_PORTAL) {
                        block.setType(Material.AIR);
                        block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, Material.ENDER_PORTAL.getId());
                    }
                }
            }

            portals -= 1;

            if (portals == 0) {
                event.getPlayer().setItemInHand(null);
                event.getClickedBlock().getLocation().getWorld().playSound(event.getClickedBlock().getLocation(), Sound.ITEM_BREAK, 1.0F, 1.0F);
                return;
            }

            ItemMeta meta = event.getItem().getItemMeta();

            meta.setLore(InventoryUtils.getCrowbarLore(portals, 0));

            event.getItem().setItemMeta(meta);

            double max = Material.DIAMOND_HOE.getMaxDurability();
            double dura = (max / (double) InventoryUtils.CROWBAR_PORTALS) * portals;

            event.getItem().setDurability((short) (max - dura));
            event.getPlayer().setItemInHand(event.getItem());
        } else if (event.getClickedBlock().getType() == Material.MOB_SPAWNER) {
            CreatureSpawner spawner = (CreatureSpawner) event.getClickedBlock().getState();
            int spawners = InventoryUtils.getCrowbarUsesSpawner(event.getItem());

            if (spawners == 0) {
                event.getPlayer().sendMessage((value ? ChatColor.RED + "This crowbar has no more uses on mob spawners!" : ChatColor.RED + "La crowbar no tiene más usos para romper ls spawners!"));
                return;
            }

            if (event.getClickedBlock().getWorld().getEnvironment() == World.Environment.NETHER) {
                event.getPlayer().sendMessage((value ? ChatColor.RED + "You cannot break spawners in the nether!" : ChatColor.RED + "No puedes romper spawners en el nether!"));
                event.setCancelled(true);
                return;
            }

            if (event.getClickedBlock().getWorld().getEnvironment() == World.Environment.THE_END) {
                event.getPlayer().sendMessage((value ? ChatColor.RED + "You cannot break spawners in the end!" : ChatColor.RED + "No puedes romper spawners en el end!"));

                event.setCancelled(true);
                return;
            }

            CrowbarSpawnerBreakEvent crowbarSpawnerBreakEvent = new CrowbarSpawnerBreakEvent(event.getPlayer(), event.getClickedBlock());
            Hulu.getInstance().getServer().getPluginManager().callEvent(crowbarSpawnerBreakEvent);

            if (crowbarSpawnerBreakEvent.isCancelled()) {
                return;
            }

            event.getClickedBlock().getLocation().getWorld().playEffect(event.getClickedBlock().getLocation(), Effect.STEP_SOUND, event.getClickedBlock().getTypeId());
            event.getClickedBlock().setType(Material.AIR);
            event.getClickedBlock().getState().update();

            ItemStack drop = new ItemStack(Material.MOB_SPAWNER);
            ItemMeta meta = drop.getItemMeta();

            meta.setDisplayName(ChatColor.RESET + StringUtils.capitaliseAllWords(spawner.getSpawnedType().toString().toLowerCase().replaceAll("_", " ")) + " Spawner");
            drop.setItemMeta(meta);

            event.getClickedBlock().getLocation().getWorld().dropItemNaturally(event.getClickedBlock().getLocation(), drop);
            event.getClickedBlock().getLocation().getWorld().playSound(event.getClickedBlock().getLocation(), Sound.ANVIL_USE, 1.0F, 1.0F);

            spawners -= 1;

            if (spawners == 0) {
                event.getPlayer().setItemInHand(null);
                event.getClickedBlock().getLocation().getWorld().playSound(event.getClickedBlock().getLocation(), Sound.ITEM_BREAK, 1.0F, 1.0F);
                return;
            }

            meta = event.getItem().getItemMeta();

            meta.setLore(InventoryUtils.getCrowbarLore(0, spawners));

            event.getItem().setItemMeta(meta);

            double max = Material.DIAMOND_HOE.getMaxDurability();
            double dura = (max / (double) InventoryUtils.CROWBAR_SPAWNERS) * spawners;

            event.getItem().setDurability((short) (max - dura));
            event.getPlayer().setItemInHand(event.getItem());
        } else {
            event.getPlayer().sendMessage((value ? ChatColor.RED + "Crowbars can only break end portals and mob spawners!" : ChatColor.RED + "Crowbars solo pueden romper portales al end y spawners!"));

            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        boolean value = Hulu.getInstance().getLanguageMap().isNewLanguageToggle(event.getPlayer().getUniqueId());
        if (event.getPlayer().getWorld().getEnvironment() == World.Environment.NETHER && event.getBlock().getType() == Material.MOB_SPAWNER) {
            event.getPlayer().sendMessage((value ? ChatColor.RED + "You cannot break spawners in the nether!" : ChatColor.RED + "No puedes romper spawners en el nether!"));
            event.setCancelled(true);
            return;
        } else if (event.getBlock().getType() == Material.MOB_SPAWNER) {
            event.getPlayer().sendMessage((value ? ChatColor.RED + "You must use a crowbar to break this." : ChatColor.RED + "Necesitas usar una crowbar para romper esto."));
            event.setCancelled(true);
        }
    }

}
