/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.pvpclasses.pvpclasses.archer.menu;

import cc.fyre.stark.engine.menu.Button;
import cc.fyre.stark.engine.menu.Menu;
import net.hcriots.hcfactions.pvpclasses.pvpclasses.ArcherClass;
import net.hcriots.hcfactions.pvpclasses.pvpclasses.archer.ArcherUpgrade;
import net.hcriots.hcfactions.pvpclasses.pvpclasses.archer.menu.button.UpgradeProgressButton;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by InspectMC
 * Date: 7/6/2020
 * Time: 3:51 PM
 */

public class ArcherUpgradeProgressMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return ChatColor.GOLD + "Archer Upgrades";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        int startAt = 1;

        for (ArcherUpgrade upgrade : ArcherClass.getArcherUpgrades()) {
            buttons.put(startAt, new UpgradeProgressButton(upgrade));
            startAt += 2;
        }

        return buttons;
    }

}