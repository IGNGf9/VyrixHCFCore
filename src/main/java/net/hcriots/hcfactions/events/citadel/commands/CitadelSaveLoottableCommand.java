/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.events.citadel.commands;

import cc.fyre.stark.engine.command.Command;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.events.citadel.CitadelHandler;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CitadelSaveLoottableCommand {

    @Command(names = {"citadel saveloottable"}, permission = "op")
    public static void citadelSaveLoottable(Player sender) {
        Hulu.getInstance().getCitadelHandler().getCitadelLoot().clear();

        for (ItemStack itemStack : sender.getInventory().getContents()) {
            if (itemStack != null && itemStack.getType() != Material.AIR) {
                Hulu.getInstance().getCitadelHandler().getCitadelLoot().add(itemStack);
            }
        }

        Hulu.getInstance().getCitadelHandler().saveCitadelInfo();
        sender.sendMessage(CitadelHandler.PREFIX + " " + ChatColor.YELLOW + "Saved Citadel loot from your inventory.");
    }

}