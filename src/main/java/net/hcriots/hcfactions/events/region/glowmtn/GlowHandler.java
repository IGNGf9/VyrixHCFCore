/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.events.region.glowmtn;

import cc.fyre.stark.Stark;
import lombok.Getter;
import lombok.Setter;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.events.region.glowmtn.listeners.GlowListener;
import net.hcriots.hcfactions.team.claims.Claim;
import net.minecraft.util.org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.io.File;
import java.io.IOException;

public class GlowHandler {

    @Getter
    private final static String glowTeamName = "Glowstone";
    private static File file;
    @Getter
    @Setter
    private GlowMountain glowMountain;

    public GlowHandler() {
        try {
            file = new File(Hulu.getInstance().getDataFolder(), "glowmtn.json");

            if (!file.exists()) {
                glowMountain = null;

                if (file.createNewFile()) {
                    Hulu.getInstance().getLogger().warning("Created a new glow mountain json file.");
                }
            } else {
                glowMountain = Stark.getGson().fromJson(FileUtils.readFileToString(file), GlowMountain.class);
                Hulu.getInstance().getLogger().info("Successfully loaded the glow mountain from file");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        int secs = Hulu.getInstance().getServerHandler().isHardcore() ? (90 * 60 * 20) : Hulu.getInstance().getServerHandler().getTabServerName().contains("cane") ? Hulu.getInstance().getMapHandler().getTeamSize() == 8 ? 20 * 25 * 60 : 20 * 45 * 60 : 12000;
        Hulu.getInstance().getServer().getScheduler().runTaskTimer(Hulu.getInstance(), () -> {
            getGlowMountain().reset();

            // Broadcast the reset
            Bukkit.broadcastMessage(ChatColor.GOLD + "[Glowstone Mountain]" + ChatColor.GREEN + " All glowstone has been reset!");
        }, secs, secs);

        Hulu.getInstance().getServer().getPluginManager().registerEvents(new GlowListener(), Hulu.getInstance());
    }

    public static Claim getClaim() {
        return Hulu.getInstance().getTeamHandler().getTeam(glowTeamName).getClaims().get(0); // null if no glowmtn is set!
    }

    public void save() {
        try {
            FileUtils.write(file, Stark.getGson().toJson(glowMountain));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean hasGlowMountain() {
        return glowMountain != null;
    }
}