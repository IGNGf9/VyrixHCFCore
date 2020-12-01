/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.listener;

import cc.fyre.stark.core.util.TimeUtils;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.HuluConstants;
import net.hcriots.hcfactions.commands.LastInvCommand;
import net.hcriots.hcfactions.server.EnderpearlCooldownHandler;
import net.hcriots.hcfactions.util.Cooldown;
import net.hcriots.hcfactions.util.Cooldowns;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class DeathbanListener implements Listener {

    public static Cooldown possibleRevive = new Cooldown(30000);

    @EventHandler
    public void onPlayerDeath(final PlayerDeathEvent event) {
        LastInvCommand.recordInventory(event.getEntity());

        EnderpearlCooldownHandler.getEnderpearlCooldown().remove(event.getEntity().getName()); // cancel enderpearls

        if (Hulu.getInstance().getMapHandler().isKitMap()) {
            return;
        }

        int seconds = (int) Hulu.getInstance().getServerHandler().getDeathban(event.getEntity());
        Hulu.getInstance().getDeathbanMap().deathban(event.getEntity().getUniqueId(), seconds);

        final String time = TimeUtils.formatIntoDetailedString(seconds);

        new BukkitRunnable() {

            public void run() {
                if (!event.getEntity().isOnline()) {
                    return;
                }

                if (Hulu.getInstance().getServerHandler().isPreEOTW()) {
                    event.getEntity().kickPlayer(ChatColor.YELLOW + "Stay tuned for the next SOTW!");
                } else {
                    String message = String.format(HuluConstants.DEATHBAN_MESSAGE, time);
                    event.getEntity().kickPlayer(message);
                }
            }

        }.runTaskLater(Hulu.getInstance(), 10 * 20L);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        boolean shouldBypass = event.getPlayer().isOp();

        if (shouldBypass) {
            Hulu.getInstance().getDeathbanMap().revive(player.getUniqueId());
            return;
        }

/*        if (Hulu.getInstance().getDeathbanMap().isDeathbanned(player.getUniqueId()) && Hulu.getInstance().getServerHandler().isEOTW()) {
            player.kickPlayer(ChatColor.YELLOW + "Deathbanned for the rest of the map due to it being 'EOTW' Come back for SOTW!");
            return;
        }

        if (Hulu.getInstance().getFriendLivesMap().getLives(player.getUniqueId()) == 0 && Hulu.getInstance().getDeathbanMap().isDeathbanned(player.getUniqueId())) {
            int seconds = (int) Hulu.getInstance().getServerHandler().getDeathban(player);
            final String time = TimeUtils.formatIntoDetailedString(seconds);
            event.getPlayer().kickPlayer(ChatColor.RED + "You are currently deathbanned for another" + time + "! \nYou can donate at store.vyrix.us for lives or purchase a rank to shorten this.");
        } else {
            if (!Hulu.getInstance().getDeathbanMap().isDeathbanned(player.getUniqueId())) {
                return;
            }

            new BukkitRunnable() {
                public void run() {
                    Hulu.getInstance().getFriendLivesMap().setLives(player.getUniqueId(), Hulu.getInstance().getSoulboundLivesMap().getLives(player.getUniqueId()));
                    event.getPlayer().sendMessage(ChatColor.GREEN + "1x Life has been used, Reconnect within 30 seconds to join the server!");
                }
            }.runTaskLater(Hulu.getInstance(), 40L);
        }*/
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();

        // Checks if player has enough friend lives, if so it will use one if the player joins within 30 seconds + will revive them.
        if (Hulu.getInstance().getDeathbanMap().isDeathbanned(player.getUniqueId())) {

            // If it's EOTW this stopps people from being able to login using lives
            if (Hulu.getInstance().getServerHandler().isEOTW()) {
                player.kickPlayer(ChatColor.YELLOW + "Deathbanned for the rest of the map due to it being 'EOTW' Come back for SOTW!");
                return;
            }

            int friend = Hulu.getInstance().getFriendLivesMap().getLives(player.getUniqueId());
            if (friend > 0) {
                if (!Cooldowns.isOnCooldown("possibleRevive", player.getUniqueId())) {
                    event.disallow(PlayerLoginEvent.Result.KICK_BANNED, ChatColor.YELLOW + "Reconnect within 30 seconds to use a life. " + ChatColor.GRAY + "(You will have " + friend-- + " remaining)");
                    Cooldowns.addCooldown("possibleRevive", player, 30);
                    return;
                } else {
                    event.allow();
                    Hulu.getInstance().getDeathbanMap().revive(player.getUniqueId());
                    Hulu.getInstance().getFriendLivesMap().setLives(player.getUniqueId(), friend - 1);
                    player.sendMessage(ChatColor.YELLOW + "You have used x1 life to revive yourself.");
                    return;
                }
            } else {
                // If the player is deathbannes and doesn't have any lives
                int seconds = (int) Hulu.getInstance().getServerHandler().getDeathban(player);
                final String time = TimeUtils.formatIntoDetailedString(seconds);
                event.disallow(PlayerLoginEvent.Result.KICK_BANNED, ChatColor.RED + "You are currently deathbanned for another" + time + "! \nYou can donate at store.vyrix.us for lives or purchase a rank to shorten this.");
            }
        }
    }
}