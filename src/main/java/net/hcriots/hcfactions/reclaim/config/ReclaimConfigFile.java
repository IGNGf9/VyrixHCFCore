/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.reclaim.config;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

/**
 * Created by InspectMC
 * Date: 7/3/2020
 * Time: 12:24 AM
 */

public class ReclaimConfigFile {

    @Getter
    @Setter
    private File file;
    @Getter
    @Setter
    private String name, directory;
    @Getter
    @Setter
    private YamlConfiguration configuration;

    public ReclaimConfigFile(JavaPlugin plugin, String name, String directory) {
        setName(name);
        setDirectory(directory);
        file = new File(directory, name + ".yml");
        if (!file.exists()) {
            plugin.saveResource(name + ".yml", false);
        }
        this.configuration = YamlConfiguration.loadConfiguration(this.getFile());
    }

    public void save() {
        try {
            configuration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
