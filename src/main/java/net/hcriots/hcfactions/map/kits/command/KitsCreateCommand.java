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

public class KitsCreateCommand {

    @Command(names = {"kits create"}, permission = "op")
    public static void kits_create(Player sender, @Param(name = "name", wildcard = true) String name) {
        if (!Hulu.getInstance().getMapHandler().isKitMap()) {
            sender.sendMessage(HuluLang.KIT_MAP_ONLY);
        } else {
            if (Hulu.getInstance().getMapHandler().getKitManager().get(name) != null) {
                sender.sendMessage(ChatColor.RED + "That kit already exists.");
                return;
            }

            Kit kit = Hulu.getInstance().getMapHandler().getKitManager().getOrCreate(name);

            kit.update(sender.getInventory());
            Hulu.getInstance().getMapHandler().getKitManager().save();

            sender.sendMessage(ChatColor.YELLOW + "The " + ChatColor.BLUE + kit.getName() + ChatColor.YELLOW
                    + " kit has been created from your inventory.");
        }
    }
}
