/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.reclaim;

import net.hcriots.hcfactions.Hulu;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by InspectMC
 * Date: 6/23/2020
 * Time: 4:45 AM
 */

public class ReclaimHandler {

    public List<Group> getGroups() {
        List<Group> groups = new ArrayList<Group>();
        for (String s : Hulu.getInstance().getReclaimConfig().getConfiguration().getConfigurationSection("groups").getKeys(false)) {
            Group group = new Group();
            group.setName(s);
            group.setCommands(Hulu.getInstance().getReclaimConfig().getConfiguration().getStringList("groups." + s + ".commands"));

            groups.add(group);
        }
        return groups;
    }


    public boolean hasUsedReclaim(Player player) {
        return Hulu.getInstance().getReclaimConfig().getConfiguration().getBoolean("reclaimed." + player.getUniqueId().toString());
    }

    public void setUsedReclaim(Player p, boolean used) {
        Hulu.getInstance().getReclaimConfig().getConfiguration().set("reclaimed." + p.getUniqueId().toString(), used);
        Hulu.getInstance().getReclaimConfig().save();
    }
}
