/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.pvpclasses.pvpclasses.archer.commands;

import cc.fyre.stark.engine.command.Command;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.HuluLang;
import net.hcriots.hcfactions.pvpclasses.pvpclasses.archer.menu.ArcherUpgradeProgressMenu;
import org.bukkit.entity.Player;

/**
 * Created by InspectMC
 * Date: 7/6/2020
 * Time: 3:58 PM
 */

public class ArcherUpgradesCommand {

    @Command(names = {"archerupgrades"}, permission = "")
    public static void showProgress(Player player) {
        if (Hulu.getInstance().getMapHandler().isKitMap()) {
            new ArcherUpgradeProgressMenu().openMenu(player);
        } else {
            player.sendMessage(HuluLang.KIT_MAP_ONLY);
        }
    }

}
