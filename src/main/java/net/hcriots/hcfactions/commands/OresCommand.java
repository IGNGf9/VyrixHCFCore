/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.commands;

import cc.fyre.stark.engine.command.Command;
import cc.fyre.stark.engine.command.data.parameter.Param;
import net.hcriots.hcfactions.Hulu;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class OresCommand {

    @Command(names = {"Ores"}, permission = "")
    public static void ores(Player sender, @Param(name = "player", defaultValue = "self") UUID player) {
        sender.sendMessage(sender.getDisplayName() + ChatColor.YELLOW + " Ores Stats");
        sender.sendMessage(ChatColor.AQUA + "Diamond mined: " + ChatColor.WHITE + Hulu.getInstance().getDiamondMinedMap().getMined(player));
        sender.sendMessage(ChatColor.GREEN + "Emerald mined: " + ChatColor.WHITE + Hulu.getInstance().getEmeraldMinedMap().getMined(player));
        sender.sendMessage(ChatColor.RED + "Redstone mined: " + ChatColor.WHITE + Hulu.getInstance().getRedstoneMinedMap().getMined(player));
        sender.sendMessage(ChatColor.GOLD + "Gold mined: " + ChatColor.WHITE + Hulu.getInstance().getGoldMinedMap().getMined(player));
        sender.sendMessage(ChatColor.GRAY + "Iron mined: " + ChatColor.WHITE + Hulu.getInstance().getIronMinedMap().getMined(player));
        sender.sendMessage(ChatColor.BLUE + "Lapis mined: " + ChatColor.WHITE + Hulu.getInstance().getLapisMinedMap().getMined(player));
        sender.sendMessage(ChatColor.DARK_GRAY + "Coal mined: " + ChatColor.WHITE + Hulu.getInstance().getCoalMinedMap().getMined(player));
    }

}