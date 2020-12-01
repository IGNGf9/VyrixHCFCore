/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.team.commands.team;

import cc.fyre.stark.Stark;
import cc.fyre.stark.engine.command.Command;
import cc.fyre.stark.engine.command.data.parameter.Param;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.team.Team;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TeamLivesCommand {

    @Command(names = {"team lives add", "t lives add", "f lives add", "fac lives add", "faction lives add", "t lives deposit", "t lives d", "f lives deposit", "f lives d"}, permission = "")
    public static void livesAdd(Player sender, @Param(name = "lives") int lives) {
        Team team = Hulu.getInstance().getTeamHandler().getTeam(sender);
        if (team == null) {
            sender.sendMessage(ChatColor.RED + "You need a team to use this command.");
            return;
        }

        if (lives <= 0) {
            sender.sendMessage(ChatColor.RED + "You really think we'd fall for that?");
            return;
        }

        int currLives = Hulu.getInstance().getFriendLivesMap().getLives(sender.getUniqueId());

        if (currLives < lives) {
            sender.sendMessage(ChatColor.RED + "You only have " + ChatColor.YELLOW + currLives + ChatColor.RED + " friend lives, you cannot deposit " + ChatColor.YELLOW + lives);
            return;
        }

        Hulu.getInstance().getFriendLivesMap().setLives(sender.getUniqueId(), currLives - lives);
        team.addLives(lives);
        sender.sendMessage(ChatColor.GREEN + "You have deposited " + ChatColor.RED + lives + ChatColor.GREEN + "  friendlives to " + ChatColor.YELLOW + team.getName() + ChatColor.GREEN + ". You now have " + ChatColor.RED + (currLives - lives) + ChatColor.GREEN + " lives and your team now has " + ChatColor.RED + team.getLives() + ChatColor.GREEN + " lives.");
    }

    @Command(names = {"team revive", "t revive", "f revive", "fac revive", "faction revive"}, permission = "")
    public static void livesRevive(Player sender, @Param(name = "player") UUID whom) {
        Team team = Hulu.getInstance().getTeamHandler().getTeam(sender);
        if (team == null) {
            sender.sendMessage(ChatColor.RED + "You need a team to use this command.");
            return;
        }

        if (!team.isCoLeader(sender.getUniqueId()) && !team.isOwner(sender.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + "Only co-leaders and owners can use this command!");
            return;
        }

        if (team.getLives() <= 0) {
            sender.sendMessage(ChatColor.RED + "Your team has no lives to use.");
            return;
        }

        if (!team.isMember(whom)) {
            sender.sendMessage(ChatColor.RED + "This player is not a member of your team.");
            return;
        }

        if (!Hulu.getInstance().getDeathbanMap().isDeathbanned(whom)) {
            sender.sendMessage(ChatColor.RED + "This player is not death banned currently.");
            return;
        }

        team.removeLives(1);
        Hulu.getInstance().getDeathbanMap().revive(whom);
        sender.sendMessage(ChatColor.GREEN + "You have revived " + ChatColor.RED + Stark.instance.getCore().getUuidCache().name(whom) + ChatColor.GREEN + ".");
    }

    @Command(names = {"team lives", "t lives", "f lives", "fac lives", "faction lives"}, permission = "")
    public static void getLives(Player sender) {
        Team team = Hulu.getInstance().getTeamHandler().getTeam(sender);
        if (team == null) {
            sender.sendMessage(ChatColor.RED + "You need a team to use this command.");
            return;
        }

        sender.sendMessage(ChatColor.YELLOW + "Your team has " + ChatColor.RED + team.getLives() + ChatColor.YELLOW + " lives.");
        sender.sendMessage(ChatColor.YELLOW + "To deposit lives, use /t lives add <amount>");
        sender.sendMessage(ChatColor.YELLOW + "Life deposits are FINAL!");
        sender.sendMessage(ChatColor.YELLOW + "Leaders can revive members using " + ChatColor.WHITE + "/t revive <name>");
    }
}
