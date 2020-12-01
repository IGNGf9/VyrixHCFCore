/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.events.blackmarket;

import cc.fyre.stark.util.ItemBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;

/**
 * @author Missionary (missionarymc@gmail.com)
 * @since 8/4/2020
 */
@Getter
@AllArgsConstructor
public enum BlackMarketItem {
    // https://docs.google.com/spreadsheets/d/1BWzu-lyvAltGshRCkUxLTtK8Qxyb5R2NsZTjwsXpUUc/edit#gid=0
    EMERALD_BLOCK(156.25, TransactionType.SELL, Material.EMERALD_BLOCK, null, null),
    DIAMOND_BLOCK(125.00, TransactionType.SELL, Material.DIAMOND_BLOCK, null, null),
    GOLD_BLOCK(93.75, TransactionType.SELL, Material.GOLD_BLOCK, null, null),
    IRON_BLOCK(78.13, TransactionType.SELL, Material.IRON_BLOCK, null, null),
    LAPIS_BLOCK(62.50, TransactionType.SELL, Material.LAPIS_BLOCK, null, null),
    REDSTONE_BLOCK(62.50, TransactionType.SELL, Material.REDSTONE_BLOCK, null, null),
    COAL_BLOCK(62.50, TransactionType.SELL, Material.COAL_BLOCK, null, null),
    HALLOWEEN_KEY(2500.00, TransactionType.BUY, Material.NAME_TAG, null, "cr givekey %s Halloween %d"),
    LOOTBOX(13000.00, TransactionType.BUY, Material.CHEST, null, "monthlycrate give %s Pandora %d"),
    PARTNER_PACKAGE(10000.00, TransactionType.BUY, Material.ENDER_CHEST, null, "partnerpackage %s %d"),
    AIRDROP(5000.00, TransactionType.BUY, Material.DISPENSER, null, "airdrops give %s %d"),
    SKELETON_SPAWNER(10000.00, TransactionType.BUY, Material.MOB_SPAWNER, ItemBuilder.of(Material.MOB_SPAWNER).name(ChatColor.GOLD + ChatColor.BOLD.toString() + "Skeleton Spawner").setLore(Collections.singleton(ChatColor.WHITE + "Type: SKELETON")).build(), null),
    SPIDER_SPAWNER(10000.00, TransactionType.BUY, Material.MOB_SPAWNER, ItemBuilder.of(Material.MOB_SPAWNER).name(ChatColor.GOLD + ChatColor.BOLD.toString() + "Spider Spawner").setLore(Collections.singleton(ChatColor.WHITE + "Type: SPIDER")).build(), null),
    ZOMBIE_SPAWNER(10000.00, TransactionType.BUY, Material.MOB_SPAWNER, ItemBuilder.of(Material.MOB_SPAWNER).name(ChatColor.GOLD + ChatColor.BOLD.toString() + "Zombie Spawner").setLore(Collections.singleton(ChatColor.WHITE + "Type: ZOMBIE")).build(), null),
    MELON(15.63, TransactionType.BUY, Material.MELON, null, null),
    CARROT(15.63, TransactionType.BUY, Material.CARROT_ITEM, null, null),
    POTATO(15.63, TransactionType.BUY, Material.POTATO_ITEM, null, null),
    SUGAR_CANE(15.63, TransactionType.BUY, Material.SUGAR_CANE, null, null),
    NETHER_WART(15.63, TransactionType.BUY, Material.NETHER_STALK, null, null),
    GLOWSTONE_DUST(15.63, TransactionType.BUY, Material.GLOWSTONE_DUST, null, null),
    SAND(15.63, TransactionType.BUY, Material.SAND, null, null),
    SOUL_SAND(15.63, TransactionType.BUY, Material.SOUL_SAND, null, null),
    GHAST_TEAR(93.75, TransactionType.BUY, Material.GHAST_TEAR, null, null),
    BLAZE_ROD(93.75, TransactionType.BUY, Material.BLAZE_ROD, null, null),
    SLIME_BALL(93.75, TransactionType.BUY, Material.SLIME_BALL, null, null),
    END_PORTAL_FRAME(166.67, TransactionType.BUY, Material.ENDER_PORTAL_FRAME, null, null),
    GUNPOWDER(187.50, TransactionType.BUY, Material.SULPHUR, null, null);

    private final double unitPrice;
    private final TransactionType transactionType;
    private final Material displayMaterial;
    @Nullable
    private final ItemStack item;
    @Nullable
    private final String command;

    public enum TransactionType {
        BUY,
        SELL
    }
}
