/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.events.conquest.commands.conquest;

import cc.fyre.stark.engine.command.Command;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.events.conquest.ConquestHandler;
import net.hcriots.hcfactions.events.conquest.game.ConquestGame;
import net.hcriots.hcfactions.team.Team;
import org.bson.types.ObjectId;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Map;

public class ConquestCommand {

    @Command(names = {"conquest"}, permission = "")
    public static void conquest(Player sender) {
        ConquestGame game = Hulu.getInstance().getConquestHandler().getGame();

        if (game == null) {
            sender.sendMessage(ChatColor.RED + "Conquest is not active.");
            return;
        }

        Map<ObjectId, Integer> caps = game.getTeamPoints();

        sender.sendMessage(ChatColor.YELLOW + "Conquest Scores:");
        boolean sent = false;

        for (Map.Entry<ObjectId, Integer> capEntry : caps.entrySet()) {
            Team resolved = Hulu.getInstance().getTeamHandler().getTeam(capEntry.getKey());

            if (resolved != null) {
                sender.sendMessage(resolved.getName(sender) + ": " + ChatColor.WHITE + capEntry.getValue() + " point" + (capEntry.getValue() == 1 ? "" : "s"));
                sent = true;
            }
        }

        if (!sent) {
            sender.sendMessage(ChatColor.GRAY + "No points have been scored!");
        }

        sender.sendMessage("");
        sender.sendMessage(ChatColor.YELLOW.toString() + ConquestHandler.getPointsToWin() + " points are required to win.");
    }

}