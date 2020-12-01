/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.team.commands.brew.buttons;

import cc.fyre.stark.engine.menu.Button;
import net.hcriots.hcfactions.team.commands.brew.enums.PotionMetaID;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class InvisButton extends Button {

    @Override
    public String getName(@NotNull Player player) {
        return getMaterial(player).name();
    }

    @Override
    public List<String> getDescription(@NotNull Player player) {
        return Arrays.asList(
                "&7&m-------------------------------------------",
                "&7Left Click to receive this potion in your &einventory.",
                "&7&m-------------------------------------------");
    }

    @Override
    public ItemStack getButtonItem(@NotNull Player player) {
        return PotionMetaID.getPotion("&7Invisibility I",
                PotionMetaID.INVIS,
                getDescription(player));
    }

    @Override
    public void clicked(Player player, int i, ClickType clickType) {
        if (clickType.isLeftClick()) {
            player.sendMessage("TODO");
        }
    }

    @Override
    public Material getMaterial(@NotNull Player player) {
        return null;
    }
}
