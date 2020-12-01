/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.tips;

import net.hcriots.hcfactions.Hulu;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Created by InspectMC
 * Date: 7/29/2020
 * Time: 4:30 PM
 */
public class TipsHandler {

    public TipsHandler() {
        new BukkitRunnable() {

            final List<String> messages = Hulu.getInstance().getConfig().getStringList("TIPS.MESSAGES");

            final List<String> clonedMessages = new ArrayList<>(messages);
            Iterator<String> iterator = clonedMessages.iterator();

            public void run() {

                if (iterator.hasNext()) {
                    for (Player player : Hulu.getInstance().getServer().getOnlinePlayers()) {
                        if (Hulu.getInstance().getTipsMap().isTipsToggled(player.getUniqueId())) {
                            try {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', iterator.next()));
                            } catch (NoSuchElementException ignored) {
                            }
                        }
                    }
                } else {
                    iterator = new ArrayList<>(messages).iterator();
                }
            }

        }.runTaskTimer(Hulu.getInstance(), 20L, 20L * Hulu.getInstance().getConfig().getInt("TIPS.DELAY"));

    }
}