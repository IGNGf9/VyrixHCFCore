/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.commands;

import cc.fyre.stark.engine.command.Command;
import net.hcriots.hcfactions.listener.BorderListener;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class HelpCommand {

    @Command(names = {"Help"}, permission = "")
    public static void help(Player sender) {
        //String website = Hulu.getInstance().getServerHandler().getNetworkWebsite();
        //String sharp = "Sharpness " + Enchantment.DAMAGE_ALL.getMaxLevel();
        //String prot = "Protection " + Enchantment.PROTECTION_ENVIRONMENTAL.getMaxLevel();
        //String bow = "Power " + Enchantment.ARROW_DAMAGE.getMaxLevel();
        //String endPortalLocation = Hulu.getInstance().getMapHandler().getEndPortalLocation();
        //if (endPortalLocation != null && (!endPortalLocation.equals("N/A") && !endPortalLocation.isEmpty())) {
        sender.sendMessage(new String[]{
                "",
                ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "------------------------------------------------",
                ChatColor.GOLD.toString() + ChatColor.BOLD + "Champions Information",
                ChatColor.GRAY + "Map Border: " + ChatColor.WHITE + BorderListener.BORDER_SIZE,
                ChatColor.GRAY + "MapKit: " + ChatColor.WHITE + "Sharpness 1 and Protection 1",
                "",
                ChatColor.GOLD.toString() + ChatColor.BOLD + "Useful Commands",
                ChatColor.GRAY + "/report: " + ChatColor.WHITE + "A command to report rule breakers",
                ChatColor.GRAY + "/helpop: " + ChatColor.WHITE + "Alerts online staff members that you need assistance",
                "",
                ChatColor.GOLD.toString() + ChatColor.BOLD + "Other Information",
                ChatColor.GRAY + "Website: " + ChatColor.WHITE + "https://vyrix.us",
                ChatColor.GRAY + "Store: " + ChatColor.WHITE + "https://store.vyrix.us",
                ChatColor.GRAY + "Teamspeak: " + ChatColor.WHITE + "ts.vyrix.us",
                ChatColor.GRAY + "Discord: " + ChatColor.WHITE + "https://invite.gg/vyrix",
                ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "------------------------------------------------",
                "",
        });
    }
}
