/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.challenges.buttons;

import cc.fyre.stark.engine.menu.Button;
import cc.fyre.stark.util.CC;
import net.hcriots.hcfactions.challenges.menu.TierTwoMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class TierTwoButton extends Button {

    @Override
    public String getName(@NotNull Player player) {
        return CC.INSTANCE.translate("&e&lTier 2 Challenges");
    }

    @Nullable
    @Override
    public List<String> getDescription(@NotNull Player player) {
        return Arrays.asList(
                CC.INSTANCE.translate("&7&oSemi-Hard challenges to complete"),
                "",
                CC.INSTANCE.translate(" &e* &f12 hour cooldown"),
                CC.INSTANCE.translate(" &e* &fSemi-Hard challenges to complete"),
                CC.INSTANCE.translate(" &e* &fGrind hard for charms!"),
                "",
                CC.INSTANCE.translate("&7Right-click to open menu"));
    }

    @Nullable
    @Override
    public Material getMaterial(@NotNull Player player) {
        return Material.STONE_SWORD;
    }

    @Override
    public void clicked(@NotNull Player player, int slot, @NotNull ClickType clickType) {
        new TierTwoMenu().openMenu(player);
    }
}
