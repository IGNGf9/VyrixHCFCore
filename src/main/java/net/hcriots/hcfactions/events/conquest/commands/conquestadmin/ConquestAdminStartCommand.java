/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.events.conquest.commands.conquestadmin;

import cc.fyre.stark.engine.command.Command;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.events.conquest.game.ConquestGame;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ConquestAdminStartCommand {

    @Command(names = {"conquestadmin start"}, permission = "op")
    public static void conquestAdminStart(CommandSender sender) {
        ConquestGame game = Hulu.getInstance().getConquestHandler().getGame();

        if (game != null) {
            sender.sendMessage(ChatColor.RED + "Conquest is already active.");
            return;
        }

        new ConquestGame();
    }

}