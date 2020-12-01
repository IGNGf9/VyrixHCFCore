/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.team.commands.team;

import cc.fyre.stark.engine.command.Command;
import cc.fyre.stark.engine.command.data.parameter.Param;
import com.google.common.collect.ImmutableMap;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.team.Team;
import net.hcriots.hcfactions.team.track.TeamActionTracker;
import net.hcriots.hcfactions.team.track.TeamActionType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TeamDepositCommand {

    @Command(names = {"team deposit", "t deposit", "f deposit", "faction deposit", "fac deposit", "team d", "t d", "f d", "faction d", "fac d", "team m d", "t m d", "f m d", "faction m d", "fac m d"}, permission = "")
    public static void teamDeposit(Player sender, @Param(name = "amount") float amount) {
        Team team = Hulu.getInstance().getTeamHandler().getTeam(sender);

        if (team == null) {
            sender.sendMessage(ChatColor.GRAY + "You are not on a team!");
            return;
        }

        if (amount <= 0 || Float.isNaN(amount)) {
            sender.sendMessage(ChatColor.RED + "You can't deposit $0.0 (or less)!");
            return;
        }

        if (Float.isNaN(amount)) {
            sender.sendMessage(ChatColor.RED + "Nope.");
            return;
        }

        if (Hulu.getInstance().getEconomyHandler().getBalance(sender.getUniqueId()) < amount) {
            sender.sendMessage(ChatColor.RED + "You don't have enough money to do this!");
            return;
        }

        Hulu.getInstance().getEconomyHandler().withdraw(sender.getUniqueId(), amount);

        sender.sendMessage(ChatColor.YELLOW + "You have added " + ChatColor.LIGHT_PURPLE + amount + ChatColor.YELLOW + " to the team balance!");

        TeamActionTracker.logActionAsync(team, TeamActionType.PLAYER_DEPOSIT_MONEY, ImmutableMap.of(
                "playerId", sender.getUniqueId(),
                "playerName", sender.getName(),
                "amount", amount,
                "oldBalance", team.getBalance(),
                "newBalance", team.getBalance() + amount
        ));

        team.setBalance(team.getBalance() + amount);
        team.sendMessage(ChatColor.YELLOW + sender.getName() + " deposited " + ChatColor.LIGHT_PURPLE + amount + ChatColor.YELLOW + " into the team balance.");

        Hulu.getInstance().getWrappedBalanceMap().setBalance(sender.getUniqueId(), Hulu.getInstance().getEconomyHandler().getBalance(sender.getUniqueId()));
    }

    @Command(names = {"team deposit all", "t deposit all", "f deposit all", "faction deposit all", "fac deposit all", "team d all", "t d all", "f d all", "faction d all", "fac d all", "team m d all", "t m d all", "f m d all", "faction m d all", "fac m d all"}, permission = "")
    public static void teamDepositAll(Player sender) {
        teamDeposit(sender, (float) Hulu.getInstance().getEconomyHandler().getBalance(sender.getUniqueId()));
    }

}