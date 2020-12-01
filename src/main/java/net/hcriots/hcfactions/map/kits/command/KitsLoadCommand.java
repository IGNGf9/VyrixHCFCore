/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.map.kits.command;

import cc.fyre.stark.engine.command.Command;
import cc.fyre.stark.engine.command.data.parameter.Param;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.HuluLang;
import net.hcriots.hcfactions.map.kits.Kit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class KitsLoadCommand {

    @Command(names = {"kits load"}, permission = "op")
    public static void kits_load(Player sender, @Param(name = "kit", wildcard = true) Kit kit) {
        if (!Hulu.getInstance().getMapHandler().isKitMap()) {
            sender.sendMessage(HuluLang.KIT_MAP_ONLY);
        } else {
            kit.apply(sender);

            sender.sendMessage(ChatColor.YELLOW + "Applied the " + ChatColor.BLUE + kit.getName() + ChatColor.YELLOW + ".");
        }
    }
}
