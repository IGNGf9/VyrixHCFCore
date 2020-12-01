/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.credit.menu.button;

import cc.fyre.stark.engine.menu.Button;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.credit.CreditHandler;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.List;

/**
 * Created by InspectMC
 * Date: 7/23/2020
 * Time: 4:37 PM
 */

@AllArgsConstructor
public class FireButton extends Button {

    private static final int PRICE = 55;

    @Override
    public String getName(Player player) {
        return ChatColor.GOLD + "Fire Sword";
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> lore = Lists.newArrayList();
        lore.add(0, ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat("-", 30));
        boolean afford = CreditHandler.getPlayerCredits(player) >= PRICE;
        lore.add(ChatColor.GOLD + "Price: " + (afford ? ChatColor.GREEN + "" + PRICE + " Charms" : ChatColor.RED + "" + PRICE + " Charms"));
        lore.add("");
        lore.add(ChatColor.GOLD.toString() + ChatColor.BOLD + "Enchantments:");
        lore.add(ChatColor.YELLOW + "Sharpness: " + ChatColor.WHITE + "2");
        lore.add(ChatColor.YELLOW + "Fire Aspect: " + ChatColor.WHITE + "1");
        lore.add(ChatColor.YELLOW + "Unbreaking: " + ChatColor.WHITE + "3");
        lore.add("");
        lore.add(ChatColor.GRAY + "Click here to purchase this item!");
        lore.add(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat("-", 30));
        return lore;
    }

    @Override
    public Material getMaterial(Player player) {
        return Material.DIAMOND_SWORD;
    }

    @Override
    public void clicked(Player player, int i, ClickType clickType) {
        if (clickType.isRightClick() || clickType.isLeftClick()) {
            if (CreditHandler.getPlayerCredits(player) < PRICE) {
                player.sendMessage(ChatColor.RED + "You do not have enough charms to purchase this item");
                return;
            }

            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "give " + player.getName() + " DIAMOND_SWORD 1 0 {ench:[{id:16,lvl:2},{id:20,lvl:1},{id:34,lvl:3}]}");
            player.sendMessage(ChatColor.GOLD + "You have have lost " + ChatColor.WHITE + PRICE + ChatColor.GOLD + " charms");
            Hulu.getInstance().getCreditsMap().setCredits(player.getUniqueId(), Hulu.getInstance().getCreditsMap().getCredits(player.getUniqueId()) - PRICE);
            player.closeInventory();
        }
    }
}