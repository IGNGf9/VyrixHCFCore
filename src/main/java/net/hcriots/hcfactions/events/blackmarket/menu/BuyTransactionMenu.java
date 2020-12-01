/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.events.blackmarket.menu;

import cc.fyre.stark.engine.menu.Button;
import cc.fyre.stark.engine.menu.Menu;
import cc.fyre.stark.engine.menu.pagination.PaginatedMenu;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.events.blackmarket.BlackMarketItem;
import net.hcriots.hcfactions.events.blackmarket.npc.BlackMarketNPCHandler;
import net.hcriots.hcfactions.util.Pair;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.NumberFormat;
import java.util.*;
import java.util.function.Consumer;

/**
 * @author Missionary (missionarymc@gmail.com)
 * @since 8/7/2020
 */
public class BuyTransactionMenu extends PaginatedMenu {

    @NotNull
    @Override
    public Map<Integer, Button> getAllPagesButtons(@NotNull Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        for (BlackMarketItem value : BlackMarketItem.values()) {
            if (value.getTransactionType() == BlackMarketItem.TransactionType.BUY) {
                buttons.put(buttons.size(), new BuyTransactionButton(value));
            }
        }
        return buttons;
    }

    @NotNull
    @Override
    public String getPrePaginatedTitle(@NotNull Player player) {
        return ChatColor.YELLOW + "Select an item...";
    }


    private static class BuyTransactionButton extends Button {

        private final BlackMarketItem item;

        public BuyTransactionButton(BlackMarketItem item) {
            this.item = item;
        }

        @Override
        public void clicked(@NotNull Player player, int slot, @NotNull ClickType clickType) {

            if (clickType.isRightClick()) {
                new BuyAmountMenu(item).openMenu(player);
            } else if (clickType.isLeftClick()) {

                if (Hulu.getInstance().getEconomyHandler().getBalance(player.getUniqueId()) < item.getUnitPrice()) {
                    player.sendMessage(ChatColor.RED + "You don't have enough money to purchase that item.");
                    return;
                }

                if (BlackMarketNPCHandler.gettingRobbed()) {
                    double robbed = item.getUnitPrice() * .25;
                    Hulu.getInstance().getEconomyHandler().withdraw(player.getUniqueId(), robbed);
                    player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "YOU HAVE BEEN ROBBED FOR " + NumberFormat.getCurrencyInstance(Locale.US).format(robbed) + '.');
                    player.getWorld().strikeLightningEffect(player.getLocation());
                } else {
                    Hulu.getInstance().getEconomyHandler().withdraw(player.getUniqueId(), item.getUnitPrice());
                    if (item.getCommand() != null) {
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), String.format(item.getCommand(), player.getName(), 1));
                    } else if (item.getItem() != null) {
                        ItemStack itemStack = item.getItem();
                        itemStack.setAmount(1);
                        player.getInventory().addItem(itemStack);
                    } else {
                        ItemStack stack = new ItemStack(item.getDisplayMaterial(), 1);
                        player.getInventory().addItem(stack);
                    }
                    player.sendMessage(ChatColor.YELLOW + "You have purchased " + WordUtils.capitalize(item.name().toLowerCase().replace("_", " ")) + " for " + NumberFormat.getCurrencyInstance(Locale.US).format(item.getUnitPrice()));
                }
            }
        }

        @Nullable
        @Override
        public List<String> getDescription(@NotNull Player player) {
            return Arrays.asList(
                    ChatColor.YELLOW + "Left-Click to purchase one " + ChatColor.LIGHT_PURPLE + WordUtils.capitalize(item.name().toLowerCase().replace("_", " ") + ChatColor.GRAY + " (" + ChatColor.LIGHT_PURPLE + item.getUnitPrice() + ChatColor.GRAY + ")"),
                    ChatColor.YELLOW + "Right Click to select the amount of " + ChatColor.LIGHT_PURPLE + WordUtils.capitalize(item.name().toLowerCase().replace("_", " ")) + ChatColor.YELLOW + " to purchase"
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
    }


    private static class BuyAmountMenu extends Menu {

        private final BlackMarketItem item;

        public BuyAmountMenu(BlackMarketItem item) {
            this.item = item;
        }

        @NotNull
        @Override
        public Map<Integer, Button> getButtons(@NotNull Player player) {
            Map<Integer, Button> buttons = new HashMap<>();
            buttons.put(1, new BuyAmountButton(new Pair<>(Hulu.getInstance().getEconomyHandler().getBalance(player.getUniqueId()), item.getUnitPrice() * 8), 8, integer -> {
                double amount = item.getUnitPrice() * integer;
                if (BlackMarketNPCHandler.gettingRobbed()) {
                    double robbed = amount * .25;
                    Hulu.getInstance().getEconomyHandler().withdraw(player.getUniqueId(), robbed);
                    player.getWorld().strikeLightningEffect(player.getLocation());
                    player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "YOU HAVE BEEN ROBBED FOR " + NumberFormat.getCurrencyInstance(Locale.US).format(amount) + '.');
                } else {
                    Hulu.getInstance().getEconomyHandler().withdraw(player.getUniqueId(), amount);
                    if (item.getCommand() != null) {
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), String.format(item.getCommand(), player.getName(), integer));
                    } else if (item.getItem() != null) {
                        ItemStack itemStack = item.getItem();
                        itemStack.setAmount(integer);
                        player.getInventory().addItem(itemStack);
                    } else {
                        ItemStack stack = new ItemStack(item.getDisplayMaterial(), integer);
                        player.getInventory().addItem(stack);
                    }
                }
                player.closeInventory();
            }));
            buttons.put(3, new BuyAmountButton(new Pair<>(Hulu.getInstance().getEconomyHandler().getBalance(player.getUniqueId()), item.getUnitPrice() * 16), 16, integer -> {
                double amount = item.getUnitPrice() * integer;
                if (BlackMarketNPCHandler.gettingRobbed()) {
                    double robbed = amount * .25;
                    Hulu.getInstance().getEconomyHandler().withdraw(player.getUniqueId(), robbed);
                    player.getWorld().strikeLightningEffect(player.getLocation());
                    player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "YOU HAVE BEEN ROBBED FOR " + NumberFormat.getCurrencyInstance(Locale.US).format(amount) + '.');
                } else {
                    Hulu.getInstance().getEconomyHandler().withdraw(player.getUniqueId(), amount);
                    if (item.getCommand() != null) {
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), String.format(item.getCommand(), player.getName(), integer));
                    } else if (item.getItem() != null) {
                        ItemStack itemStack = item.getItem();
                        itemStack.setAmount(integer);
                        player.getInventory().addItem(itemStack);
                    } else {
                        ItemStack stack = new ItemStack(item.getDisplayMaterial(), integer);
                        player.getInventory().addItem(stack);
                    }
                }
                player.closeInventory();
            }));
            buttons.put(5, new BuyAmountButton(new Pair<>(Hulu.getInstance().getEconomyHandler().getBalance(player.getUniqueId()), item.getUnitPrice() * 32), 32, integer -> {
                double amount = item.getUnitPrice() * integer;
                if (BlackMarketNPCHandler.gettingRobbed()) {
                    double robbed = amount * .25;
                    Hulu.getInstance().getEconomyHandler().withdraw(player.getUniqueId(), robbed);
                    player.getWorld().strikeLightningEffect(player.getLocation());
                    player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "YOU HAVE BEEN ROBBED FOR " + NumberFormat.getCurrencyInstance(Locale.US).format(amount) + '.');
                } else {
                    Hulu.getInstance().getEconomyHandler().withdraw(player.getUniqueId(), amount);
                    if (item.getCommand() != null) {
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), String.format(item.getCommand(), player.getName(), integer));
                    } else if (item.getItem() != null) {
                        ItemStack itemStack = item.getItem();
                        itemStack.setAmount(integer);
                        player.getInventory().addItem(itemStack);
                    } else {
                        ItemStack stack = new ItemStack(item.getDisplayMaterial(), integer);
                        player.getInventory().addItem(stack);
                    }
                }
                player.closeInventory();
            }));
            buttons.put(7, new BuyAmountButton(new Pair<>(Hulu.getInstance().getEconomyHandler().getBalance(player.getUniqueId()), item.getUnitPrice() * 64), 64, integer -> {
                double amount = item.getUnitPrice() * integer;
                if (BlackMarketNPCHandler.gettingRobbed()) {
                    double robbed = amount * .25;
                    player.getWorld().strikeLightningEffect(player.getLocation());
                    Hulu.getInstance().getEconomyHandler().withdraw(player.getUniqueId(), robbed);
                    player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "YOU HAVE BEEN ROBBED FOR " + NumberFormat.getCurrencyInstance(Locale.US).format(amount) + '.');
                } else {
                    Hulu.getInstance().getEconomyHandler().withdraw(player.getUniqueId(), amount);
                    if (item.getCommand() != null) {
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), String.format(item.getCommand(), player.getName(), integer));
                    } else if (item.getItem() != null) {
                        ItemStack itemStack = item.getItem();
                        itemStack.setAmount(integer);
                        player.getInventory().addItem(itemStack);
                    } else {
                        ItemStack stack = new ItemStack(item.getDisplayMaterial(), integer);
                        player.getInventory().addItem(stack);
                    }
                }
                player.closeInventory();
            }));
            return buttons;
        }

        @NotNull
        @Override
        public String getTitle(@NotNull Player player) {
            return ChatColor.YELLOW + "Select an amount";
        }

        private static class BuyAmountButton extends Button {

            private final Consumer<Integer> action;
            private final int amount;
            private final Pair<Double, Double> balancePricePair;

            private BuyAmountButton(Pair<Double, Double> balancePricePair, int amount, Consumer<Integer> action) {
                this.balancePricePair = balancePricePair;
                this.amount = amount;
                this.action = action;
            }

            @Override
            public int getAmount(@NotNull Player player) {
                return amount;
            }

            @Nullable
            @Override
            public List<String> getDescription(@NotNull Player player) {
                return Collections.singletonList(ChatColor.YELLOW + "Click to select " + ChatColor.LIGHT_PURPLE + amount + ChatColor.YELLOW + '.' + ChatColor.GRAY + " (" + ChatColor.LIGHT_PURPLE + NumberFormat.getCurrencyInstance(Locale.US).format(balancePricePair.second) + ChatColor.GRAY + ")");
            }

            @Nullable
            @Override
            public Material getMaterial(@NotNull Player player) {
                return Material.INK_SACK;
            }

            @Override
            public byte getDamageValue(@NotNull Player player) {
                return (byte) 12;
            }

            @Nullable
            @Override
            public String getName(@NotNull Player player) {
                return ChatColor.LIGHT_PURPLE.toString() + amount;
            }

            @Override
            public void clicked(@NotNull Player player, int slot, @NotNull ClickType clickType) {
                // first = PLAYERS balance || second = cost of items
                if (balancePricePair.first >= balancePricePair.second) {
                    action.accept(amount);
                } else {
                    player.sendMessage(ChatColor.RED + "You don't have enough money to purchase that item. Your balance is: " + NumberFormat.getCurrencyInstance(Locale.US).format(balancePricePair.first) + " and the cost is: " + NumberFormat.getCurrencyInstance(Locale.US).format(balancePricePair.second));
                }
            }
        }
    }
}
