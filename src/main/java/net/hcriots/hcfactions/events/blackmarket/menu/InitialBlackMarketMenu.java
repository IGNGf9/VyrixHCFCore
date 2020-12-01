/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.events.blackmarket.menu;

import cc.fyre.stark.engine.menu.Button;
import cc.fyre.stark.engine.menu.Menu;
import net.hcriots.hcfactions.events.blackmarket.BlackMarketItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Missionary (missionarymc@gmail.com)
 * @since 8/2/2020
 */
public class InitialBlackMarketMenu extends Menu {

    private static final Button FILL = Button.placeholder(Material.STAINED_GLASS_PANE, (byte) 15, ChatColor.RESET + "");

    @NotNull
    @Override
    public String getTitle(@NotNull Player player) {
        return ChatColor.RED + "Select an option...";
    }

    @NotNull
    @Override
    public Map<Integer, Button> getButtons(@NotNull Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        for (int i = 0; i < 27; i++) {
            buttons.put(i, FILL);
        }
        buttons.put(11, new Button() {
            @Nullable
            @Override
            public String getName(@NotNull Player player) {
                return ChatColor.RED + ChatColor.BOLD.toString() + "SELL";
            }

            @Nullable
            @Override
            public List<String> getDescription(@NotNull Player player) {
                return Arrays.asList(
                        "",
                        ChatColor.YELLOW + "Take a risk selling valuables for increased profit.",
                        ""
                );
            }

            @Nullable
            @Override
            public Material getMaterial(@NotNull Player player) {
                return Material.INK_SACK;
            }

            @Override
            public byte getDamageValue(@NotNull Player player) {
                return (byte) 1;
            }

            @Override
            public void clicked(@NotNull Player player, int slot, @NotNull ClickType clickType) {
                new SellTransactionMenu(BlackMarketItem.TransactionType.SELL).openMenu(player);
            }
        });
        buttons.put(15, new Button() {
            @Nullable
            @Override
            public String getName(@NotNull Player player) {
                return ChatColor.GREEN + ChatColor.BOLD.toString() + "BUY";
            }

            @Nullable
            @Override
            public List<String> getDescription(@NotNull Player player) {
                return Arrays.asList(
                        "",
                        ChatColor.YELLOW + "Take a risk purchasing valuables for a cheap price.",
                        ""
                );
            }

            @Nullable
            @Override
            public Material getMaterial(@NotNull Player player) {
                return Material.INK_SACK;
            }

            @Override
            public byte getDamageValue(@NotNull Player player) {
                return (byte) 10;
            }

            @Override
            public void clicked(@NotNull Player player, int slot, @NotNull ClickType clickType) {
                new BuyTransactionMenu().openMenu(player);
            }
        });

        return buttons;
    }
}
