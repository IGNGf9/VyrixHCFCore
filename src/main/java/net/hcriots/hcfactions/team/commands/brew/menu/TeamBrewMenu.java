/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.team.commands.brew.menu;

import cc.fyre.stark.engine.menu.Button;
import cc.fyre.stark.engine.menu.Menu;
import lombok.AllArgsConstructor;
import net.hcriots.hcfactions.team.Team;
import net.hcriots.hcfactions.team.commands.brew.buttons.*;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class TeamBrewMenu extends Menu {

    private final Team team;

    @Override
    public Map<Integer, Button> getButtons(@NotNull Player player) {
        List<Integer> walls = Arrays.asList(
                // left wall
                0, 9, 18, 27, 36, 45,

                // right wall
                8, 17, 26, 35, 44, 53);
        HashMap<Integer, Button> buttons = new HashMap<>();

        walls.forEach(slot -> buttons.put(slot, Button.placeholder(Material.STAINED_GLASS_PANE, (byte) 13)));

        buttons.put(11, new RegenButton());
        buttons.put(12, new SpeedButton());
        buttons.put(13, new PotionStorageButton());
        buttons.put(14, new InvisButton());
        buttons.put(15, new FireResButton());

        Inventory inv = team.getBrewingVault();
        buttons.put(29, Button.placeholder(Material.NETHER_STALK,
                "Remaining: " + getCount(inv, Material.NETHER_STALK)));
        buttons.put(30, Button.placeholder(Material.SPECKLED_MELON,
                "Remaining: " + getCount(inv, Material.SPECKLED_MELON)));
        buttons.put(31, Button.placeholder(Material.SULPHUR,
                "Remaining: " + getCount(inv, Material.SULPHUR)));
        buttons.put(32, Button.placeholder(Material.GLOWSTONE_DUST,
                "Remaining: " + getCount(inv, Material.GLOWSTONE_DUST)));
        buttons.put(33, Button.placeholder(Material.SUGAR,
                "Remaining: " + getCount(inv, Material.SUGAR)));

        buttons.put(38, Button.placeholder(Material.MAGMA_CREAM,
                "Remaining: " + getCount(inv, Material.MAGMA_CREAM)));
        buttons.put(39, Button.placeholder(Material.GOLDEN_CARROT,
                "Remaining: " + getCount(inv, Material.GOLDEN_CARROT)));
        buttons.put(40, Button.placeholder(Material.GLASS_BOTTLE,
                "Remaining: " + getCount(inv, Material.GLASS_BOTTLE)));
        buttons.put(41, Button.placeholder(Material.FERMENTED_SPIDER_EYE,
                "Remaining: " + getCount(inv, Material.FERMENTED_SPIDER_EYE)));

        buttons.put(49, Button.placeholder(Material.BLAZE_ROD,
                "Remaining: " + getCount(inv, Material.BLAZE_ROD)));
        return buttons;
    }

    public int getCount(Inventory inv, Material material) {
        int amount;
        try {
            amount = Math.toIntExact(Arrays.stream(inv.getContents()).filter(itemStack -> itemStack.getType() == material).count());
        } catch (Exception ignored) {
            amount = 0;
        }

        return (amount);
    }
}
