/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.staffmode;

import cc.fyre.stark.util.ItemBuilder;
import cc.fyre.stark.util.PlayerUtils;
import lombok.Getter;
import net.hcriots.hcfactions.Hulu;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.HashMap;
import java.util.Map;

@Getter
public class StaffModeHandler {

    @Getter
    private final static Map<Player, StaffModeHandler> staffModeMap = new HashMap<>();

    private final Player player;
    private final ItemStack[] contents;
    private final ItemStack[] armor;
    private final Location location;
    private final GameMode gamemode;
    private boolean hidden;

    public StaffModeHandler(Player player) {

        this.player = player;
        this.contents = player.getInventory().getContents();
        this.armor = player.getInventory().getArmorContents();
        this.gamemode = player.getGameMode();
        this.location = player.getLocation();

        staffModeMap.put(player, this);
        this.setHidden(true);
        this.update();

    }

    public static boolean hasStaffMode(Player player) {
        return staffModeMap.containsKey(player);
    }

    public void update() {
        PlayerUtils.resetInventory(player, GameMode.CREATIVE);
        int i = 0;

        player.getInventory().setItem(i++, new ItemBuilder(Material.COMPASS).name(ChatColor.GOLD + "Teleporter").build());
        player.getInventory().setItem(i++, new ItemBuilder(Material.PACKED_ICE).name(ChatColor.GOLD + "Freeze Tool").build());
        player.getInventory().setItem(i++, new ItemBuilder(Material.BOOK).name(ChatColor.GOLD + "Inspect Inventory").build());

        player.getInventory().setItem(6, new ItemBuilder(Material.WATCH).name(ChatColor.GOLD + "Random Teleport").build());
        player.getInventory().setItem(7, new ItemBuilder(Material.SKULL_ITEM).name(ChatColor.GOLD + "Online Staff").build());
        player.getInventory().setItem(8, new ItemBuilder(Material.INK_SACK).data((byte) 8).name(ChatColor.GOLD + "Become Visible").build());
        player.setMetadata("ModMode", new FixedMetadataValue(Hulu.getInstance(), player.getUniqueId()));
        player.updateInventory();
        setHidden(true);

        //CheatBreakerAPI.getInstance().giveAllStaffModules(player);
    }

    public void remove() {
        player.setGameMode(gamemode);
        player.getInventory().setContents(contents);
        player.getInventory().setArmorContents(armor);
        staffModeMap.remove(player);
        player.removeMetadata("ModMode", Hulu.getInstance());
        setHidden(false);

        //CheatBreakerAPI.getInstance().disableAllStaffModules(player);
    }

    public boolean isHidden() {
        return player.hasMetadata("invisible");
    }

    public void setHidden(boolean vanished) {
        this.hidden = vanished;

        if (vanished) {
            this.player.setMetadata("invisible", new FixedMetadataValue(Hulu.getInstance(), true));
            //Hide the player from everyone else
            for (Player other : Bukkit.getOnlinePlayers()) {
                player.showPlayer(other);

                if (other.hasPermission("stark.staff")) continue;

                other.hidePlayer(player);
            }
        } else {
            this.player.removeMetadata("invisible", Hulu.getInstance());
            //Make sure the player is visible to everyone!
            for (Player other : Bukkit.getOnlinePlayers()) {
                other.showPlayer(player);
            }

        }
    }
}