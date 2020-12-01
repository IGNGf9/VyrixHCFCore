/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.team.upgrades.menu.button;

import cc.fyre.stark.engine.menu.Button;
import lombok.AllArgsConstructor;
import net.hcriots.hcfactions.team.upgrades.TeamUpgrade;
import net.hcriots.hcfactions.team.upgrades.menu.CategoryUpgradesMenu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by InspectMC
 * Date: 7/16/2020
 * Time: 9:37 PM
 */

@AllArgsConstructor
public class CategoryButton extends Button {

    private final TeamUpgrade upgrade;

    @Override
    public String getName(Player player) {
        return ChatColor.YELLOW + upgrade.getUpgradeName();
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatColor.GRAY + ChatColor.ITALIC.toString() + upgrade.getDescription());
        lore.add("");
        lore.add(ChatColor.YELLOW + "Click to view all purchasable upgrades");

        return lore;
    }

    @Override
    public Material getMaterial(Player player) {
        return upgrade.getIcon().getType();
    }

    @Override
    public byte getDamageValue(Player player) {
        return (byte) upgrade.getIcon().getDurability();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
        new CategoryUpgradesMenu(ChatColor.GOLD + upgrade.getUpgradeName(), upgrade.getCategoryElements()).openMenu(player);
    }

}
