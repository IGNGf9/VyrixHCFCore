/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.team.commands;

import cc.fyre.stark.engine.command.Command;
import cc.fyre.stark.engine.command.data.parameter.Param;
import net.hcriots.hcfactions.team.Team;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SetTeamBalanceCommand {

    @Command(names = {"team setbal", "team setbal"}, permission = "foxtrot.setteambalance")
    public static void setTeamBalance(Player sender, @Param(name = "team") Team team, @Param(name = "balance") float balance) {
        team.setBalance(balance);
        sender.sendMessage(ChatColor.LIGHT_PURPLE + team.getName() + ChatColor.YELLOW + "'s balance is now " + ChatColor.LIGHT_PURPLE + team.getBalance() + ChatColor.YELLOW + ".");
    }

}