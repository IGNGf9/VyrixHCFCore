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
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class KitMapIsGayCommand {

    @Command(names = "kitmapisdaddy", permission = "op")
    public static void kitmapIsGay(Player sender, @Param(name = "kitname") String name) {
        if (!Hulu.getInstance().getMapHandler().isKitMap()) {
            sender.sendMessage(HuluLang.KIT_MAP_ONLY);
        } else {
            if (Hulu.getInstance().getMapHandler().getKitManager().getUseAll().contains(name.toLowerCase())) {
                Hulu.getInstance().getMapHandler().getKitManager().getUseAll().remove(name.toLowerCase());
                sender.sendMessage(ChatColor.RED + "Removed " + name + " from bypass list!");
            } else {
                Hulu.getInstance().getMapHandler().getKitManager().getUseAll().add(name.toLowerCase());
                sender.sendMessage(ChatColor.GREEN + "Added " + name + " to bypass list!");
            }
        }
    }
}
