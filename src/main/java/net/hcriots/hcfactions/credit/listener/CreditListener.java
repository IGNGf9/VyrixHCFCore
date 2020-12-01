/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.credit.listener;

import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.credit.CreditHandler;
import net.hcriots.hcfactions.util.Color;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

/**
 * Created by InspectMC
 * Date: 6/28/2020
 * Time: 4:55 PM
 */

public class CreditListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        if (event.getEntity() != null && event.getEntity().getKiller() != null) {
            Player killer = event.getEntity().getKiller();
            Hulu.getInstance().getCreditsMap().setCredits(killer.getUniqueId(), Hulu.getInstance().getCreditsMap().getCredits(killer.getUniqueId()) + CreditHandler.getWinCredits());
            killer.sendMessage(Color.translate(" &6» &fYou have received &e+1 charm &ffor killing " + ChatColor.RED + player.getDisplayName()));
        }
    }
}