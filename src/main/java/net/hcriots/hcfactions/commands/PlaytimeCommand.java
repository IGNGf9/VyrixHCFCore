/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.commands;

import cc.fyre.stark.Stark;
import cc.fyre.stark.core.util.TimeUtils;
import cc.fyre.stark.engine.command.Command;
import cc.fyre.stark.engine.command.data.parameter.Param;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.persist.maps.PlaytimeMap;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlaytimeCommand {

    @Command(names = {"Playtime", "PTime"}, permission = "")
    public static void playtime(Player sender, @Param(name = "player", defaultValue = "self") UUID player) {
        PlaytimeMap playtime = Hulu.getInstance().getPlaytimeMap();
        int playtimeTime = (int) playtime.getPlaytime(player);
        Player bukkitPlayer = Hulu.getInstance().getServer().getPlayer(player);

        if (bukkitPlayer != null && sender.canSee(bukkitPlayer)) {
            playtimeTime += playtime.getCurrentSession(bukkitPlayer.getUniqueId()) / 1000;
        }

        sender.sendMessage(ChatColor.DARK_RED
                + Stark.instance.getCore().getUuidCache().name(player) +
                ChatColor.WHITE + "'s total playtime is " +
                ChatColor.GOLD + TimeUtils.formatIntoDetailedString(playtimeTime) + ChatColor.WHITE + ".");
    }

}