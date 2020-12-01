/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.team.commands.pvp;

import cc.fyre.stark.Stark;
import cc.fyre.stark.engine.command.Command;
import cc.fyre.stark.engine.command.data.parameter.Param;
import net.hcriots.hcfactions.Hulu;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PvPSetLivesCommand {

    @Command(names = {"pvptimer setlives", "timer setlives", "pvp setlives", "pvptimer setlives", "timer setlives", "pvp setlives"}, permission = "foxtrot.setlives")
    public static void pvpSetLives(Player sender, @Param(name = "player") UUID player, @Param(name = "life type") String lifeType, @Param(name = "amount") int amount) {
        if (lifeType.equalsIgnoreCase("soulbound")) {
            Hulu.getInstance().getSoulboundLivesMap().setLives(player, amount);
            sender.sendMessage(ChatColor.YELLOW + "Set " + ChatColor.GREEN + Stark.instance.getCore().getUuidCache().name(player) + ChatColor.YELLOW + "'s soulbound life count to " + amount + ".");
        } else if (lifeType.equalsIgnoreCase("friend")) {
            Hulu.getInstance().getFriendLivesMap().setLives(player, amount);
            sender.sendMessage(ChatColor.YELLOW + "Set " + ChatColor.GREEN + Stark.instance.getCore().getUuidCache().name(player) + ChatColor.YELLOW + "'s friend life count to " + amount + ".");
        } else {
            sender.sendMessage(ChatColor.RED + "Not a valid life type: Options are soulbound or friend.");
        }
    }

}