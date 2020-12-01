/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.events.koth.listeners;

import cc.fyre.stark.core.util.TimeUtils;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.events.EventType;
import net.hcriots.hcfactions.events.koth.KOTH;
import net.hcriots.hcfactions.events.koth.events.EventControlTickEvent;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class KOTHListener implements Listener {

    @EventHandler
    public void onKOTHControlTick(EventControlTickEvent event) {

        if (event.getKOTH().getType() != EventType.KOTH) {
            return;
        }

        KOTH koth = event.getKOTH();
        if (koth.getRemainingCapTime() % 180 == 0 && koth.getRemainingCapTime() <= (koth.getCapTime() - 30)) {
            Hulu.getInstance().getServer().broadcastMessage(ChatColor.BLUE + "KOTH - " + ChatColor.YELLOW + koth.getName() + ChatColor.GOLD + " is trying to be controlled.");
            Hulu.getInstance().getServer().broadcastMessage(ChatColor.GRAY + " - Time left: " + ChatColor.YELLOW + TimeUtils.formatIntoMMSS(koth.getRemainingCapTime()));
        }
    }

}