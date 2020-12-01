/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.events.citadel.tasks;

import net.hcriots.hcfactions.Hulu;
import org.bukkit.scheduler.BukkitRunnable;

public class CitadelSaveTask extends BukkitRunnable {

    public void run() {
        Hulu.getInstance().getCitadelHandler().saveCitadelInfo();
    }

}