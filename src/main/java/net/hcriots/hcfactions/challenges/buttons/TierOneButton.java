/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.challenges.buttons;

import cc.fyre.stark.engine.menu.Button;
import cc.fyre.stark.util.CC;
import net.hcriots.hcfactions.challenges.menu.TierOneMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class TierOneButton extends Button {

    @Override
    public String getName(@NotNull Player player) {
        return CC.INSTANCE.translate("&a&lTier 1 Challenges");
    }

    @Nullable
    @Override
    public List<String> getDescription(@NotNull Player player) {
        return Arrays.asList(
                CC.INSTANCE.translate("&7&oEasiest challenges to complete"),
                "",
                CC.INSTANCE.translate(" &e* &fNo challenge cooldown"),
                CC.INSTANCE.translate(" &e* &fEasy challenges to complete"),
                CC.INSTANCE.translate(" &e* &fGrind hard for charms!"),
                "",
                CC.INSTANCE.translate("&7Right-click to open menu"));
    }

    @Nullable
    @Override
    public Material getMaterial(@NotNull Player player) {
        return Material.WOOD_SWORD;
    }

    @Override
    public void clicked(@NotNull Player player, int slot, @NotNull ClickType clickType) {
        new TierOneMenu().openMenu(player);
    }
}
