/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.commands;

import cc.fyre.stark.engine.command.Command;
import net.hcriots.hcfactions.util.Color;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MediaCommand {

    @Command(names = {"youtube", "youtuber", "media"}, permission = "")
    public static void youtube(Player sender) {
        sender.sendMessage(new String[]{
                ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "------------------------",
                ChatColor.LIGHT_PURPLE.toString() + "YouTuber Requirements",
                Color.translate(" &f» &d75 Subscribers"),
                Color.translate(" &f» &d50 Average Views"),
                Color.translate(" &f» &dHCF related content"),
                Color.translate(" &f» &d1 exisiting video on Vyrix"),
                Color.translate(""),
                Color.translate("&7&oApply on the forums or create a ticket in discord!"),
                ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "------------------------",
        });
    }

    @Command(names = {"famous"}, permission = "")
    public static void famous(Player sender) {
        sender.sendMessage(new String[]{
                ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "------------------------",
                ChatColor.GREEN.toString() + ChatColor.BOLD + "Famous Requirements",
                Color.translate(" &f» &a250 Subscribers"),
                Color.translate(" &f» &a75 Average Views"),
                Color.translate(" &f» &aHCF related content"),
                Color.translate(" &f» &a1 exisiting video on Vyrix"),
                Color.translate(""),
                Color.translate("&7&oApply on the forums or create a ticket in discord!"),
                ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "------------------------",
        });
    }

    @Command(names = {"partner"}, permission = "")
    public static void partner(Player sender) {
        sender.sendMessage(new String[]{
                ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "------------------------",
                ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "Partner Requirements",
                Color.translate(" &f» &dIf you feel like you qualify for partner"),
                Color.translate(" &f» &dplease create a ticket in the discord!"),
                Color.translate(""),
                Color.translate("&7&oApply on the forums or create a ticket in discord!"),
                ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "------------------------",
        });
    }
}
