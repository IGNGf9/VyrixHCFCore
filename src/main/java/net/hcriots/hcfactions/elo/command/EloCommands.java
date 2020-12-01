/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.elo.command;

import cc.fyre.stark.engine.command.Command;
import cc.fyre.stark.engine.command.data.parameter.Param;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.elo.EloHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by InspectMC
 * Date: 6/23/2020
 * Time: 3:08 AM
 */

public class EloCommands {

    @Command(names = {"elo", "myelo"}, permission = "")
    public static void elo(CommandSender sender, @Param(name = "target", defaultValue = "Self") Player target) {
        sender.sendMessage(target.getDisplayName() + ChatColor.YELLOW + " has " + ChatColor.BLUE + EloHandler.getPlayerELO(target) + ChatColor.YELLOW + " ELO.");
    }

    @Command(names = {"elo reset"}, permission = "hulu.command.admin")
    public static void eloReset(CommandSender sender, @Param(name = "target", defaultValue = "Self") Player target) {
        sender.sendMessage(ChatColor.GREEN + target.getName() + " elo has been reset.");
        Hulu.getInstance().getEloMap().setELO(target.getUniqueId(), 60);

    }

    @Command(names = {"elo set"}, permission = "hulu.command.admin")
    public static void eloSet(CommandSender sender, @Param(name = "player") Player player, @Param(name = "amount") int amount) {
        if (amount < 0) {
            sender.sendMessage(ChatColor.RED + "Please enter a valid number.");
            return;
        }

        Hulu.getInstance().getEloMap().setELO(player.getUniqueId(), amount);
        sender.sendMessage(ChatColor.YELLOW + "You have updated " + player.getDisplayName() + ChatColor.YELLOW + " elo!");
    }
}