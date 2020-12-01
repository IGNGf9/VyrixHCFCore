/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.events.conquest.commands.conquestadmin;

import cc.fyre.stark.engine.command.Command;
import cc.fyre.stark.engine.command.data.parameter.Param;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.events.conquest.ConquestHandler;
import net.hcriots.hcfactions.events.conquest.game.ConquestGame;
import net.hcriots.hcfactions.team.Team;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ConquestAdminSetScoreCommand {

    @Command(names = {"conquestadmin setscore"}, permission = "op")
    public static void conquestAdminSetScore(CommandSender sender, @Param(name = "team") Team team, @Param(name = "score") int score) {
        ConquestGame game = Hulu.getInstance().getConquestHandler().getGame();

        if (game == null) {
            sender.sendMessage(ChatColor.RED + "Conquest is not active.");
            return;
        }

        game.getTeamPoints().put(team.getUniqueId(), score);
        sender.sendMessage(ConquestHandler.PREFIX + " " + ChatColor.GOLD + "Updated the score for " + team.getName() + ChatColor.GOLD + ".");
    }

}