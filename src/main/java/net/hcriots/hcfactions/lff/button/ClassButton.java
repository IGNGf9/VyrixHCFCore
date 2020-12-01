/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.lff.button;

import cc.fyre.stark.engine.menu.Button;
import cc.fyre.stark.util.ItemBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.hcriots.hcfactions.lff.LFFMenu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@Getter
public class ClassButton extends Button {

    private final ChatColor color;
    private final String name;
    private final Material material;
    private final LFFMenu lffMenu;

    @Override
    public String getName(Player player) {
        return color + name;
    }

    @Override
    public List<String> getDescription(Player player) {
        return Collections.singletonList(ChatColor.translateAlternateColorCodes('&', " &7&oLeft-Click to select " + color + name + " Class"));
    }

    @Override
    public Material getMaterial(Player player) {
        return material;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        ItemBuilder itemBuilder = ItemBuilder.of(material).name(getName(player));

        if (lffMenu.checkIfClassIsSelected(name)) {
            itemBuilder.enchant(Enchantment.DURABILITY, 10);
        }

        return itemBuilder.setLore(getDescription(player)).build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
        if (lffMenu.checkIfClassIsSelected(name)) {
            lffMenu.getClasses().remove(lffMenu.getClassButton(name));
        } else {
            lffMenu.getClasses().add(this);
        }
    }
}
