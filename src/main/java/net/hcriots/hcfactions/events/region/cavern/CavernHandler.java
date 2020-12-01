/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.events.region.cavern;

import cc.fyre.stark.Stark;
import lombok.Getter;
import lombok.Setter;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.events.region.cavern.listeners.CavernListener;
import net.hcriots.hcfactions.team.claims.Claim;
import net.minecraft.util.org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.io.File;
import java.io.IOException;

public class CavernHandler {

    @Getter
    private final static String cavernTeamName = "Cavern";
    private static File file;
    @Getter
    @Setter
    private Cavern cavern;

    public CavernHandler() {
        try {
            file = new File(Hulu.getInstance().getDataFolder(), "cavern.json");

            if (!file.exists()) {
                cavern = null;

                if (file.createNewFile()) {
                    Hulu.getInstance().getLogger().warning("Created a new Cavern json file.");
                }
            } else {
                cavern = Stark.getGson().fromJson(FileUtils.readFileToString(file), Cavern.class);
                Hulu.getInstance().getLogger().info("Successfully loaded the Cavern from file");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Hulu.getInstance().getServer().getScheduler().runTaskTimer(Hulu.getInstance(), () -> {
            if (getCavern() == null || Hulu.getInstance().getTeamHandler().getTeam(cavernTeamName) == null) return;
            getCavern().reset();
            // Broadcast the reset
            Bukkit.broadcastMessage(ChatColor.AQUA + "[Cavern]" + ChatColor.GREEN + " All ores have been reset!");
        }, 20 * 60 * 60, 20 * 60 * 60);

        Hulu.getInstance().getServer().getPluginManager().registerEvents(new CavernListener(), Hulu.getInstance());
    }

    public static Claim getClaim() {
        return Hulu.getInstance().getTeamHandler().getTeam(cavernTeamName).getClaims().get(0); // null if no glowmtn is set!
    }

    public void save() {
        try {
            FileUtils.write(file, Stark.getGson().toJson(cavern));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean hasCavern() {
        return cavern != null;
    }
}