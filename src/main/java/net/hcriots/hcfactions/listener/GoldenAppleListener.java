/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.listener;

import cc.fyre.stark.core.util.TimeUtils;
import com.cheatbreaker.api.CheatBreakerAPI;
import com.cheatbreaker.api.object.CBCooldown;
import lombok.Getter;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.commands.EOTWCommand;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class GoldenAppleListener implements Listener {

    @Getter
    private static final Map<UUID, Long> crappleCooldown = new HashMap<>();

    @EventHandler(ignoreCancelled = false)
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {

        Player player = event.getPlayer();

        if (event.getItem() == null || event.getItem().getType() != Material.GOLDEN_APPLE) {
            return;
        }

        if (event.getItem().getDurability() == 0 && EOTWCommand.realFFAStarted()) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "Crapples are disabled during FFA.");
            return;
        }


        long cooldown = 10900;

        if (!Hulu.getInstance().getServerHandler().isUhcHealing()) {
            if (event.getItem().getDurability() == 0 && !crappleCooldown.containsKey(player.getUniqueId())) {
                crappleCooldown.put(player.getUniqueId(), System.currentTimeMillis() + (cooldown));
                CheatBreakerAPI.getInstance().sendCooldown(player, new CBCooldown("Crapple", 11, TimeUnit.SECONDS, Material.DIAMOND_SWORD));
                return;
            }

            if (event.getItem().getDurability() == 0 && crappleCooldown.containsKey(player.getUniqueId())) {
                long millisRemaining = crappleCooldown.get(player.getUniqueId()) - System.currentTimeMillis();
                double value = (millisRemaining / 1000D);
                double sec = value > 0.1 ? Math.round(10.0 * value) / 10.0 : 0.1;

                if (crappleCooldown.get(player.getUniqueId()) > System.currentTimeMillis()) {
                    player.sendMessage(ChatColor.RED + "You cannot use this for another " + ChatColor.BOLD + sec + ChatColor.RED + " seconds!");
                    event.setCancelled(true);
                    return;
                } else {
                    crappleCooldown.put(player.getUniqueId(), System.currentTimeMillis() + cooldown);
                    CheatBreakerAPI.getInstance().sendCooldown(player, new CBCooldown("Crapple", 11, TimeUnit.SECONDS, Material.DIAMOND_SWORD));
                    return;
                }
            }
        }

        if (event.getItem().getType() == Material.GOLDEN_APPLE && event.getItem().getDurability() == 0) return;

        if (Hulu.getInstance().getMapHandler().getGoppleCooldown() == -1) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "Super golden apples are currently disabled.");
            return;
        }

        long cooldownUntil = Hulu.getInstance().getOppleMap().getCooldown(event.getPlayer().getUniqueId());

        if (cooldownUntil > System.currentTimeMillis()) {
            long millisLeft = cooldownUntil - System.currentTimeMillis();
            String msg = TimeUtils.formatIntoDetailedString((int) millisLeft / 1000);

            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "You cannot use this for another §c§l" + msg + "§c.");
            return;
        }

        Hulu.getInstance().getOppleMap().useGoldenApple(
                event.getPlayer().getUniqueId(),
                Hulu.getInstance().getMapHandler().isKitMap() ?
                        TimeUnit.MINUTES.toSeconds(5) :
                        (Hulu.getInstance().getMapHandler().getGoppleCooldown() * 60) // minutes to seconds
        );

        long millisLeft = Hulu.getInstance().getOppleMap().getCooldown(event.getPlayer().getUniqueId()) - System.currentTimeMillis();

        event.getPlayer().sendMessage(ChatColor.DARK_GREEN + "███" + ChatColor.BLACK + "██" + ChatColor.DARK_GREEN + "███");
        event.getPlayer().sendMessage(ChatColor.DARK_GREEN + "███" + ChatColor.BLACK + "█" + ChatColor.DARK_GREEN + "████");
        event.getPlayer().sendMessage(ChatColor.DARK_GREEN + "██" + ChatColor.GOLD + "████" + ChatColor.DARK_GREEN + "██" + ChatColor.GOLD + " Super Golden Apple:");
        event.getPlayer().sendMessage(ChatColor.DARK_GREEN + "█" + ChatColor.GOLD + "██" + ChatColor.WHITE + "█" + ChatColor.GOLD + "███" + ChatColor.DARK_GREEN + "█" + ChatColor.DARK_GREEN + "   Consumed");
        event.getPlayer().sendMessage(ChatColor.DARK_GREEN + "█" + ChatColor.GOLD + "█" + ChatColor.WHITE + "█" + ChatColor.GOLD + "████" + ChatColor.DARK_GREEN + "█" + ChatColor.YELLOW + " Cooldown Remaining:");
        event.getPlayer().sendMessage(ChatColor.DARK_GREEN + "█" + ChatColor.GOLD + "██████" + ChatColor.DARK_GREEN + "█" + ChatColor.BLUE + "   " + TimeUtils.formatIntoDetailedString((int) millisLeft / 1000));
        event.getPlayer().sendMessage(ChatColor.DARK_GREEN + "█" + ChatColor.GOLD + "██████" + ChatColor.DARK_GREEN + "█");
        event.getPlayer().sendMessage(ChatColor.DARK_GREEN + "██" + ChatColor.GOLD + "████" + ChatColor.DARK_GREEN + "██");
    }

}
