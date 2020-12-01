/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.events.blackmarket.commands;

import cc.fyre.stark.engine.command.Command;
import cc.fyre.stark.util.Cuboid;
import net.hcriots.hcfactions.Hulu;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 * @author Missionary (missionarymc@gmail.com)
 * @since 8/4/2020
 */
public class BlackMarketEventDebugCommands {

    @Command(names = {"blackmarketdebug debugpaste"}, permission = "op")
    public static void debugPaste(Player player) {
        try {
            Hulu.getInstance().getBlackMarketHandler().pasteBlackMarketShop(player.getLocation());
        } catch (Exception e) {
            e.printStackTrace();
        }
        player.sendMessage("pasted in the schematic");
        Cuboid cuboid = Hulu.getInstance().getBlackMarketHandler().getBlackMarketRegion();
        for (Block wall : cuboid.getWalls()) {
            player.sendBlockChange(wall.getLocation(), Material.STAINED_GLASS, (byte) 14);
        }
        player.sendMessage("should be able to see cuboid with red glass");
    }

    @Command(names = {"blackmarketdebug debugnpc"}, permission = "op")
    public static void debugNPC(Player player) {
        Hulu.getInstance().getBlackMarketHandler().setActive(true);
        Hulu.getInstance().getBlackMarketHandler().getNpcHandler().spawnShopKeeper(player.getLocation());
        player.sendMessage("spawned shopkeeper at ur location | try right-clicking to see if it opens gui");
    }

    @Command(names = {"blackmarketdebug despawnnpc"}, permission = "op")
    public static void despawnNPC(Player player) {
        Hulu.getInstance().getBlackMarketHandler().setActive(false);
        Hulu.getInstance().getBlackMarketHandler().getNpcHandler().despawnShopKeeper();
        player.sendMessage("removed shopkeeper");
    }

    @Command(names = {"blackmarketdebug undoedit"}, permission = "op")
    public static void undoEditSession(Player player) {
        if (Hulu.getInstance().getBlackMarketHandler().getEditSession() == null) {
            player.sendMessage("null edit session, run this first: /blackmarket debugpaste");
            return;
        }

        Hulu.getInstance().getBlackMarketHandler().getEditSession().first.undo(Hulu.getInstance().getBlackMarketHandler().getEditSession().first);
        player.sendMessage("hopefully this works :)");
    }
}
