/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.commands;

import cc.fyre.stark.engine.command.Command;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.util.Color;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * ---------- hcteams ----------
 * Created by Fraser.Cumming on 29/03/2016.
 * © 2016 Fraser Cumming All Rights Reserved
 */
public class LivesCommand {

    @Command(names = {"lives"}, permission = "")
    public static void lives(CommandSender commandSender) {
        String gray = net.md_5.bungee.api.ChatColor.GRAY.toString() + net.md_5.bungee.api.ChatColor.STRIKETHROUGH + StringUtils.repeat("-", 35);


        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatColor.RED + "Bad console.");
            return;
        }

        Player sender = (Player) commandSender;

        int shared = Hulu.getInstance().getFriendLivesMap().getLives(sender.getUniqueId());
        int soulbound = Hulu.getInstance().getSoulboundLivesMap().getLives(sender.getUniqueId());
        sender.sendMessage(gray);
        sender.sendMessage(Color.translate("&fYou can purchase more lives at: &6&ostore.vyrix.us"));
        sender.sendMessage(Color.translate(" &e» &fLives: &6" + shared));
        sender.sendMessage(gray);

    }
}
