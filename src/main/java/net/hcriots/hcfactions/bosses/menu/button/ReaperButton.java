/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.bosses.menu.button;

import cc.fyre.stark.engine.menu.Button;
import com.google.common.collect.Lists;
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
 * Date: 7/21/2020
 * Time: 2:34 PM
 */

public class ReaperButton extends Button {

    private static final int PRICE = 75;

    @Override
    public String getName(Player player) {
        return ChatColor.RED + "Reaper Egg";
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> lore = Lists.newArrayList();
        lore.add(0, ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat("-", 30));
        boolean afford = CreditHandler.getPlayerCredits(player) >= PRICE;
        lore.add(ChatColor.GOLD + "Price: " + (afford ? ChatColor.GREEN + "" + PRICE + " Charms" : ChatColor.RED + "" + PRICE + " Charms"));
        lore.add("");
        lore.add(ChatColor.GRAY + "Reward: " + ChatColor.WHITE + "15 Faction Points");
        lore.add(ChatColor.GRAY + "Health: " + ChatColor.WHITE + "450");
        lore.add(ChatColor.GRAY + "Click here to purchase this item!");
        lore.add(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat("-", 30));
        return lore;
    }

    @Override
    public Material getMaterial(Player player) {
        return Material.MONSTER_EGG;
    }

    @Override
    public void clicked(Player player, int i, ClickType clickType) {
        if (clickType.isRightClick() || clickType.isLeftClick()) {
            if (CreditHandler.getPlayerCredits(player) < PRICE) {
                player.sendMessage(ChatColor.RED + "You do not have enough charms to purchase this item");
                return;
            }

            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "bossegg Reaper 1 " + player.getName());
            player.sendMessage(ChatColor.GOLD + "You have purchased 1 Reaper Egg");
            player.sendMessage(ChatColor.GRAY.toString() + ChatColor.ITALIC + "You have have lost " + ChatColor.WHITE + PRICE + ChatColor.GRAY.toString() + ChatColor.ITALIC + " charm");
            Hulu.getInstance().getCreditsMap().setCredits(player.getUniqueId(), Hulu.getInstance().getCreditsMap().getCredits(player.getUniqueId()) - PRICE);
            player.closeInventory();
        }
    }
}
