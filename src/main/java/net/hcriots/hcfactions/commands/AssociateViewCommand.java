/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.commands;

import cc.fyre.stark.engine.command.Command;
import cc.fyre.stark.engine.command.data.parameter.Param;
import net.hcriots.hcfactions.Hulu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * ---------- hcteams ----------
 * Created by Fraser.Cumming on 29/03/2016.
 * © 2016 Fraser Cumming All Rights Reserved
 */
public class AssociateViewCommand {

    @Command(names = {"assview", "associateview"}, permission = "op")
    public static void associate(Player sender, @Param(name = "player") UUID player) {
        if (Hulu.getInstance().getWhitelistedIPMap().contains(player)) {
            player = Hulu.getInstance().getWhitelistedIPMap().get(player);
        }

        Map<UUID, UUID> map = Hulu.getInstance().getWhitelistedIPMap().getMap();
        List<String> list = new ArrayList<String>();
        for (UUID id : map.keySet()) {
            UUID found = map.get(id);
            if (found.equals(player)) {
                sender.sendMessage(ChatColor.RED + Bukkit.getOfflinePlayer(id).getName() + ChatColor.YELLOW + " is associated!");
            }
        }
    }
}
