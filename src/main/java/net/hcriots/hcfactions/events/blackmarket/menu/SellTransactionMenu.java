/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.events.blackmarket.menu;

import cc.fyre.stark.engine.menu.Button;
import cc.fyre.stark.engine.menu.pagination.PaginatedMenu;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.events.blackmarket.BlackMarketItem;
import net.hcriots.hcfactions.events.blackmarket.npc.BlackMarketNPCHandler;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.NumberFormat;
import java.util.*;

/**
 * @author Missionary (missionarymc@gmail.com)
 * @since 8/7/2020
 */
public class SellTransactionMenu extends PaginatedMenu {

    private final BlackMarketItem.TransactionType transactionType;

    public SellTransactionMenu(BlackMarketItem.TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    // left click to sell all in inventory
    // right click to sell one

    @NotNull
    @Override
    public String getPrePaginatedTitle(@NotNull Player player) {
        return ChatColor.YELLOW + "Select an item...";
    }

    @NotNull
    @Override
    public Map<Integer, Button> getAllPagesButtons(@NotNull Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        for (BlackMarketItem value : BlackMarketItem.values()) {
            if (value.getTransactionType() == transactionType) {
                buttons.put(buttons.size(), new SellTransactionButton(value));
            }
        }
        return buttons;
    }

    private static class SellTransactionButton extends Button {

        private final BlackMarketItem item;

        public SellTransactionButton(BlackMarketItem item) {
            this.item = item;
        }

        @Nullable
        @Override
        public List<String> getDescription(@NotNull Player player) {
            int result = 0;
            for (ItemStack itemStack : player.getInventory().getContents()) {
                if (itemStack == null) {
                    continue;
                }

                if (itemStack.hasItemMeta()) {
                    continue;
                }

                if (itemStack.getType() == null) {
                    continue;
                }
                if (itemStack.getType() == item.getDisplayMaterial()) {
                    result += itemStack.getAmount();
                }
            }
            return Arrays.asList(
                    ChatColor.YELLOW + "Left-Click to sell the contents in your inventory " + ChatColor.GRAY + "(x" + ChatColor.LIGHT_PURPLE + result + ChatColor.GRAY + ")",
                    ChatColor.YELLOW + "Right-Click to sell one item"
            );
        }

        @Nullable
        @Override
        public Material getMaterial(@NotNull Player player) {
            return item.getDisplayMaterial();
        }

        @Nullable
        @Override
        public String getName(@NotNull Player player) {
            return ChatColor.LIGHT_PURPLE + WordUtils.capitalize(item.name().toLowerCase().replace("_", " "));
        }

        @Override
        public void clicked(@NotNull Player player, int slot, @NotNull ClickType clickType) {
            switch (clickType) {
                case LEFT:
                    int result = 0;
                    Set<ItemStack> toRemove = new HashSet<>();
                    for (ItemStack itemStack : player.getInventory().getContents()) {
                        if (itemStack == null) {
                            continue;
                        }

                        if (itemStack.hasItemMeta()) {
                            continue;
                        }

                        if (itemStack.getType() == null) {
                            continue;
                        }

                        if (itemStack.getType() == item.getDisplayMaterial()) {
                            // typically if an item has some lore it is not a "default" item
                            result += itemStack.getAmount();
                            toRemove.add(itemStack);
                        }
                    }
                    if (result == 0) {
                        player.sendMessage(ChatColor.RED + "That item is not in your inventory.");
                        return;
                    }
                    if (BlackMarketNPCHandler.gettingRobbed()) {
                        toRemove.forEach(itemStack -> player.getInventory().remove(itemStack));
                        player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "YOU HAVE BEEN ROBBED!");
                        player.getWorld().strikeLightningEffect(player.getLocation());
                        return;
                    }
                    double payment = Math.round(item.getUnitPrice() * result);
                    Hulu.getInstance().getEconomyHandler().deposit(player.getUniqueId(), payment);
                    player.sendMessage(ChatColor.GREEN + "Sold " + result + " " + WordUtils.capitalize(item.name().toLowerCase().replace("_", " ")) + "'s for " + NumberFormat.getCurrencyInstance(Locale.US).format(payment) + '.');
                    toRemove.forEach(stack -> player.getInventory().remove(stack));
                    break;
                case RIGHT:
                    int oneResult = 0;
                    Optional<ItemStack> toRemoveRight = Optional.empty();
                    for (ItemStack itemStack : player.getInventory().getContents()) {
                        if (itemStack == null) {
                            continue;
                        }

                        if (itemStack.hasItemMeta()) {
                            continue;
                        }

                        if (itemStack.getType() == null) {
                            continue;
                        }

                        if (itemStack.getType() == item.getDisplayMaterial()) {
                            // typically if an item has some lore it is not a "default" item
                            oneResult += itemStack.getAmount();
                            toRemoveRight = Optional.of(itemStack);
                            break;
                        }
                    }
                    if (oneResult == 0) {
                        player.sendMessage(ChatColor.RED + "That item is not in your inventory.");
                        return;
                    }
                    toRemoveRight.ifPresent(itemStack -> {
                        if (BlackMarketNPCHandler.gettingRobbed()) {
                            if (itemStack.getAmount() > 1) {
                                itemStack.setAmount(itemStack.getAmount() - 1);
                                player.updateInventory();
                            } else if (itemStack.getAmount() == 0) {
                                player.getInventory().removeItem(itemStack);
                            }
                            player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "YOU HAVE BEEN ROBBED!");
                            player.getWorld().strikeLightningEffect(player.getLocation());
                            return;
                        }
                        Hulu.getInstance().getEconomyHandler().deposit(player.getUniqueId(), item.getUnitPrice());
                        player.sendMessage(ChatColor.GREEN + "Sold 1 " + WordUtils.capitalize(item.name().toLowerCase().replace("_", " ")) + " for " + NumberFormat.getCurrencyInstance(Locale.US).format(item.getUnitPrice()) + '.');
                        if (itemStack.getAmount() > 1) {
                            itemStack.setAmount(itemStack.getAmount() - 1);
                            player.updateInventory();
                        } else if (itemStack.getAmount() == 0) {
                            player.getInventory().removeItem(itemStack);
                        }
                    });
                    break;
            }
        }
    }
}
