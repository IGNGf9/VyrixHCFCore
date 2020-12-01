/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.settings.commands;

import cc.fyre.stark.engine.command.Command;
import net.hcriots.hcfactions.settings.menu.SettingsMenu;
import org.bukkit.entity.Player;

public class SettingsCommand {

    @Command(names = {"settings", "options"}, permission = "")
    public static void settings(Player sender) {
        new SettingsMenu().openMenu(sender);
    }

}
