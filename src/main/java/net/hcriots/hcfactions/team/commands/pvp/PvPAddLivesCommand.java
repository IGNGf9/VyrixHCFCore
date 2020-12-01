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
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PvPAddLivesCommand {

    @Command(names = {"pvp addlives", "addlives"}, permission = "foxtrot.addlives")
    public static void pvpSetLives(CommandSender sender, @Param(name = "player") UUID player, @Param(name = "life type") String lifeType, @Param(name = "amount") int amount) {
        if (lifeType.equalsIgnoreCase("soulbound")) {
            Hulu.getInstance().getSoulboundLivesMap().setLives(player, Hulu.getInstance().getSoulboundLivesMap().getLives(player) + amount);
            sender.sendMessage(ChatColor.YELLOW + "Gave " + ChatColor.GREEN + Stark.instance.getCore().getUuidCache().name(player) + ChatColor.YELLOW + " " + amount + " soulbound lives.");

            Player bukkitPlayer = Bukkit.getPlayer(player);
            if (bukkitPlayer != null && bukkitPlayer.isOnline()) {
                String suffix = sender instanceof Player ? " from " + sender.getName() : "";
                bukkitPlayer.sendMessage(ChatColor.GREEN + "You have received " + amount + " lives" + suffix);
            }

        } else if (lifeType.equalsIgnoreCase("friend")) {
            Hulu.getInstance().getFriendLivesMap().setLives(player, Hulu.getInstance().getFriendLivesMap().getLives(player) + amount);
            sender.sendMessage(ChatColor.YELLOW + "Gave " + ChatColor.GREEN + Stark.instance.getCore().getUuidCache().name(player) + ChatColor.YELLOW + " " + amount + " friend lives.");

            Player bukkitPlayer = Bukkit.getPlayer(player);
            if (bukkitPlayer != null && bukkitPlayer.isOnline()) {
                String suffix = sender instanceof Player ? " from " + sender.getName() : "";
                bukkitPlayer.sendMessage(ChatColor.GREEN + "You have received " + amount + " lives" + suffix);
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Not a valid life type: Options are soulbound or friend");
        }
    }

}
