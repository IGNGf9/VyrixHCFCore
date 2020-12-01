/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.elo;

import net.hcriots.hcfactions.Hulu;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

/**
 * Created by InspectMC
 * Date: 6/23/2020
 * Time: 3:00 AM
 */

public class EloListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        if (EloHandler.getPlayerELO(player) == 0) {
            return;
        }

        if (event.getEntity().getKiller() == null) {
            return;
        }

        Hulu.getInstance().getEloMap().setELO(player.getUniqueId(), Hulu.getInstance().getEloMap().getELO(player.getUniqueId()) - EloHandler.getLostELO());
        player.sendMessage(ChatColor.WHITE + "You have lost " + ChatColor.RED + ChatColor.BOLD + EloHandler.getLostELO() + ChatColor.WHITE + " elo from dying.");

        if (event.getEntity() != null && event.getEntity().getKiller() != null) {
            Player killer = event.getEntity().getKiller();
            Hulu.getInstance().getEloMap().setELO(killer.getUniqueId(), Hulu.getInstance().getEloMap().getELO(killer.getUniqueId()) + EloHandler.getWinELO());
            killer.sendMessage(ChatColor.WHITE + "You have killed " + player.getDisplayName() + ChatColor.WHITE + " and earned " + ChatColor.GREEN + EloHandler.getWinELO() + ChatColor.WHITE + " elo");
        }
    }
}