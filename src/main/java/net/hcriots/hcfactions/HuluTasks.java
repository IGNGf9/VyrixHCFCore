/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions;

import cc.fyre.stark.Stark;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class HuluTasks extends BukkitRunnable {
    @Override
    public void run() {
        for (Player users : Bukkit.getOnlinePlayers()) {
            Stark.instance.getNametagEngine().reloadOthersFor(users);
        }
    }
}
