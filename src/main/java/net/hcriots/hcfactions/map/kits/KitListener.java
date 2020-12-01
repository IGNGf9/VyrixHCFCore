/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.map.kits;

import com.google.common.collect.Maps;
import net.hcriots.hcfactions.Hulu;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class KitListener implements Listener {

    private static final Map<UUID, Long> lastClicked = Maps.newHashMap();

    public static void attemptApplyKit(Player player, Kit kit) {
        if (kit == null) {
            player.sendMessage(ChatColor.RED + "Unknown kit.");
            return;
        }

        if (player.hasMetadata("ModMode")) {
            player.sendMessage(ChatColor.RED + "You cannot use this while in mod mode.");
            return;
        }

        if (lastClicked.containsKey(player.getUniqueId()) && (System.currentTimeMillis() - lastClicked.get(player.getUniqueId()) < TimeUnit.SECONDS.toMillis(15))) {
            player.sendMessage(ChatColor.RED + "Please wait before using this again.");
            return;
        }

        if (!Hulu.getInstance().getMapHandler().getKitManager().canUseKit(player, kit.getName())) {
            player.sendMessage(ChatColor.RED + "You don't have permission to use this kit.");
            return;
        }

        kit.apply(player);

        lastClicked.put(player.getUniqueId(), System.currentTimeMillis());
    }

    @EventHandler
    public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof Wolf) {
            ((Wolf) event.getRightClicked()).setSitting(false);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getClickedBlock() == null || !(event.getClickedBlock().getState() instanceof Sign)) {
            return;
        }

        Sign sign = (Sign) event.getClickedBlock().getState();

        if (!sign.getLine(0).startsWith(ChatColor.BLUE + "- Kit")) {
            return;
        }

        Kit kit = Hulu.getInstance().getMapHandler().getKitManager().get(player.getUniqueId(), sign.getLine(1));

        attemptApplyKit(player, kit);
    }

}
