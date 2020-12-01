/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.tournaments.config;

import net.hcriots.hcfactions.Hulu;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

/**
 * Created by InspectMC
 * Date: 8/3/2020
 * Time: 5:50 PM
 */

public class TournamentFile extends YamlConfiguration {

    private static TournamentFile config;
    private final Plugin plugin;
    private final java.io.File configFile;

    public TournamentFile() {
        this.plugin = main();
        this.configFile = new java.io.File(this.plugin.getDataFolder(), "tournament.yml");
        saveDefault();
        reload();
    }

    public static TournamentFile getConfig() {
        if (config == null) {
            config = new TournamentFile();
        }
        return config;
    }

    private Plugin main() {
        return Hulu.getInstance();
    }

    public void reload() {
        try {
            super.load(this.configFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            super.save(this.configFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveDefault() {
        this.plugin.saveResource("tournament.yml", false);
    }
}
