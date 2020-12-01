/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.killtheking.listener;

import net.hcriots.hcfactions.Hulu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;

/**
 * Created by InspectMC
 * Date: 6/23/2020
 * Time: 4:51 AM
 */

public class KillTheKingListener implements Listener {

    private final String prefix = ChatColor.RED + "Kill the King" + ChatColor.DARK_GRAY + " » ";

    @EventHandler
    public void on(PlayerDeathEvent e) {
        if (Hulu.getInstance().getKillTheKing() == null) {
            return;
        }
        Player player = Hulu.getInstance().getKillTheKing().getActiveKing();
        Player killer = e.getEntity().getKiller();
        if (e.getEntity().equals(player)) {
            e.getDrops().clear();
            if (killer == null) {
                Bukkit.broadcastMessage(prefix + ChatColor.WHITE + player.getName() + " has been killed.");
                Hulu.getInstance().setKillTheKing(null);
                return;
            }
            Hulu.getInstance().setKillTheKing(null);
            Bukkit.broadcastMessage(prefix + ChatColor.WHITE + player.getName() + " has been killed by " + killer.getName() + ".");
        }
    }

    @EventHandler
    public void on(PlayerQuitEvent e) {
        if (Hulu.getInstance().getKillTheKing() == null) {
            return;
        }
        Player player = Hulu.getInstance().getKillTheKing().getActiveKing();
        if (e.getPlayer().equals(player)) {
            Hulu.getInstance().setKillTheKing(null);
            player.getActivePotionEffects().stream().map(PotionEffect::getType).forEach(player::removePotionEffect);
            Bukkit.broadcastMessage(prefix + ChatColor.WHITE + player.getName() + " has disconnected!");
        }
    }
}
