/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.map.stats.command;

import cc.fyre.stark.engine.command.Command;
import lombok.Getter;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.HuluLang;
import net.hcriots.hcfactions.team.dtr.DTRBitmask;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ChestCommand {

    @Getter
    private static final Set<UUID> BYPASS = new HashSet<>();

    @Command(names = {"storage"}, permission = "")
    public static void chest(Player sender) {
        if (!Hulu.getInstance().getMapHandler().isKitMap()) {
            sender.sendMessage(HuluLang.KIT_MAP_ONLY);
            return;
        }

        if (!DTRBitmask.SAFE_ZONE.appliesAt(sender.getLocation())) {
            sender.sendMessage(ChatColor.RED + "You can only do this at spawn.");
            return;
        }

        BYPASS.add(sender.getUniqueId());
        sender.openInventory(sender.getEnderChest());
        BYPASS.remove(sender.getUniqueId());
    }

}
