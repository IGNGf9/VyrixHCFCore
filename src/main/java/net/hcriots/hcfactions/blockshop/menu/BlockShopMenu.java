package net.hcriots.hcfactions.blockshop.menu;

import cc.fyre.stark.engine.menu.Button;
import cc.fyre.stark.engine.menu.Menu;
import net.hcriots.hcfactions.blockshop.BlockShopType;
import net.hcriots.hcfactions.blockshop.button.BuyItemButton;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class BlockShopMenu extends Menu {

    {
        setPlaceholder(true);
        setAutoUpdate(true);
    }

    @Override
    public String getTitle(Player player) {
        return ChatColor.GREEN + "Block Shop";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        for (BlockShopType blockShopType : BlockShopType.values()) {
            if (blockShopType.isSelling()) {
                buttons.put(buttons.size(), new BuyItemButton(player, blockShopType));
            }
        }
        return buttons;
    }
}