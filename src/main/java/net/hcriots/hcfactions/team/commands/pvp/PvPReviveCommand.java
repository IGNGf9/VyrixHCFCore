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

public class PvPReviveCommand {

    @Command(names = {"pvptimer revive", "timer revive", "pvp revive", "pvptimer revive", "timer revive", "pvp revive"}, permission = "")
    public static void pvpRevive(Player sender, @Param(name = "player") UUID player) {
        int friendLives = Hulu.getInstance().getFriendLivesMap().getLives(sender.getUniqueId());

        if (Hulu.getInstance().getServerHandler().isPreEOTW()) {
            sender.sendMessage(ChatColor.RED + "The server is in EOTW Mode: Lives cannot be used.");
            return;
        }

        if (friendLives <= 0) {
            sender.sendMessage(ChatColor.RED + "You have no lives which can be used to revive other players!");
            return;
        }

        if (!Hulu.getInstance().getDeathbanMap().isDeathbanned(player)) {
            sender.sendMessage(ChatColor.RED + "That player is not deathbanned!");
            return;
        }

        // Use a friend life.
        Hulu.getInstance().getFriendLivesMap().setLives(sender.getUniqueId(), friendLives - 1);
        sender.sendMessage(ChatColor.YELLOW + "You have revived " + ChatColor.GREEN + Stark.instance.getCore().getUuidCache().name(player) + ChatColor.YELLOW + " with a friend life!");


        Hulu.getInstance().getDeathbanMap().revive(player);
    }

}