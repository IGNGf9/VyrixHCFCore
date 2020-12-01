package net.hcriots.hcfactions.blockshop;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@Getter
public enum BlockShopType {

    // Wool
    WOOL(new ItemStack(Material.WOOL), 32, 250, true),
    ORANGE_WOOL(new ItemStack(Material.WOOL, 1, (short) 1), 32, 250, true),
    RED_WOOL(new ItemStack(Material.WOOL, 1, (short) 14), 32, 250, true),
    LIME_WOOL(new ItemStack(Material.WOOL, 1, (short) 5), 32, 250, true),
    CYAN_WOOL(new ItemStack(Material.WOOL, 1, (short) 9), 32, 250, true),
    GRAY_WOOL(new ItemStack(Material.WOOL, 1, (short) 8), 32, 250, true),
    PURPLE_WOOL(new ItemStack(Material.WOOL, 1, (short) 10), 32, 250, true),
    BLACK_WOOL(new ItemStack(Material.WOOL, 1, (short) 15), 32, 250, true),
    LBUE_WOOL(new ItemStack(Material.WOOL, 1, (short) 3), 32, 250, true),

    // Clay
    CLAY(new ItemStack(Material.CLAY), 32, 800, true),
    ORANGE_CLAY(new ItemStack(Material.STAINED_CLAY, 1, (short) 1), 32, 800, true),
    RED_CLAY(new ItemStack(Material.STAINED_CLAY, 1, (short) 14), 32, 800, true),
    LIME_CLAY(new ItemStack(Material.STAINED_CLAY, 1, (short) 5), 32, 800, true),
    CYAN_CLAY(new ItemStack(Material.STAINED_CLAY, 1, (short) 9), 32, 800, true),
    GRAY_CLAY(new ItemStack(Material.STAINED_CLAY, 1, (short) 8), 32, 800, true),
    PURPLE_CLAY(new ItemStack(Material.STAINED_CLAY, 1, (short) 10), 32, 800, true),
    BLACK_CLAY(new ItemStack(Material.STAINED_CLAY, 1, (short) 15), 32, 800, true),
    LBUE_CLAY(new ItemStack(Material.STAINED_CLAY, 1, (short) 3), 32, 800, true),

    // Glass
    GLASS(new ItemStack(Material.GLASS), 32, 500, true),
    ORANGE_GLASS(new ItemStack(Material.STAINED_GLASS, 1, (short) 1), 32, 500, true),
    RED_GLASS(new ItemStack(Material.STAINED_GLASS, 1, (short) 14), 32, 500, true),
    LIME_GLASS(new ItemStack(Material.STAINED_GLASS, 1, (short) 5), 32, 500, true),
    CYAN_GLASS(new ItemStack(Material.STAINED_GLASS, 1, (short) 9), 32, 500, true),
    GRAY_GLASS(new ItemStack(Material.STAINED_GLASS, 1, (short) 8), 32, 500, true),
    PURPLE_GLASS(new ItemStack(Material.STAINED_GLASS, 1, (short) 10), 32, 500, true),
    BLACK_GLASS(new ItemStack(Material.STAINED_GLASS, 1, (short) 15), 32, 500, true),
    LBUE_GLASS(new ItemStack(Material.STAINED_GLASS, 1, (short) 3), 32, 500, true);

    private final ItemStack item;
    private final int amount;
    private final double price;
    private final boolean selling;

    BlockShopType(ItemStack item, int amount, double price, boolean selling) {
        this.item = item;
        this.amount = amount;
        this.price = price;
        this.selling = selling;
    }
}
