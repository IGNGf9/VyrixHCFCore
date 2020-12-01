package net.hcriots.hcfactions.blockshop.button;

import cc.fyre.stark.engine.menu.Button;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.blockshop.BlockShopType;
import net.hcriots.hcfactions.util.CC;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class BuyItemButton extends Button {

    private final Player player;
    private final BlockShopType blockShopType;

    public BuyItemButton(Player player, BlockShopType blockShopType) {
        this.player = player;
        this.blockShopType = blockShopType;
    }

    @Override
    public List<String> getDescription(@NotNull Player player) {
        return CC.translateLines(
                Arrays.asList(
                        "",
                        " &e* &fPrice: &e$" + this.blockShopType.getPrice(),
                        " &e* &fAmount: &ex" + this.blockShopType.getAmount(),
                        " &e* &fLeft click to purchase this item.",
                        ""
                )
        );
    }

    @Override
    public Material getMaterial(@NotNull Player player) {
        return this.blockShopType.getItem().getType();
    }

    @Override
    public ItemStack getButtonItem(@NotNull Player player) {
        ItemStack item = this.blockShopType.getItem();
        ItemMeta meta = item.getItemMeta();

        meta.setLore(getDescription(player));
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public String getName(@NotNull Player player) {
        return CC.translate(
                "&f" + WordUtils.capitalize(this.blockShopType.getItem().getType().name()).replaceAll("_", " ") + " &ex" + this.blockShopType.getAmount()
        );
    }

    @Override
    public void clicked(@NotNull Player player, int slot, @NotNull ClickType clickType) {
        if (clickType.isLeftClick()) {
            double bal = Hulu.getInstance().getEconomyHandler().getBalance(player.getUniqueId());
            double price = this.blockShopType.getPrice();

            if (bal >= price) {
                Hulu.getInstance().getEconomyHandler().withdraw(player.getUniqueId(), price);

                if (player.getInventory().firstEmpty() != -1) {
                    for (int i = 1; i <= blockShopType.getAmount(); i++) {
                        player.getInventory().addItem(this.blockShopType.getItem());
                    }
                } else {
                    for (int i = 1; i <= blockShopType.getAmount(); i++) {
                        player.getWorld().dropItemNaturally(player.getLocation().add(0, 1, 0), this.blockShopType.getItem());
                    }
                }

                player.sendMessage(ChatColor.GREEN + "You have purchased " + this.getName(player) + ChatColor.GREEN + ".");
            } else {
                player.sendMessage(ChatColor.RED + "You do not have enough to purchase this item.");
            }
        }
    }
}
