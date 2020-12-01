/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.commands;

import cc.fyre.stark.engine.command.Command;
import net.hcriots.hcfactions.Hulu;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;

/**
 * Created by InspectMC
 * Date: 7/29/2020
 * Time: 3:37 PM
 */

public class TipsCommand {

    @Command(names = "tips", permission = "")
    public static void tips(Player sender) {
        String gray = ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat("-", 35);
        sender.sendMessage(gray);
        sender.sendMessage(ChatColor.RED + "/tips toggle");
        sender.sendMessage(gray);
    }

    @Command(names = {"tips toggle", "toggletips"}, permission = "")
    public static void toggle(Player sender) {
        boolean val = !Hulu.getInstance().getTipsMap().isTipsToggled(sender.getUniqueId());
        sender.sendMessage(ChatColor.YELLOW + "You are now " + (val ? (ChatColor.GREEN + "able") : (ChatColor.RED + "unable")) + ChatColor.YELLOW + " to see in-game tips!");
        Hulu.getInstance().getTipsMap().setToggled(sender.getUniqueId(), val);
    }

}
