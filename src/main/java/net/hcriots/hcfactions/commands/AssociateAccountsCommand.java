/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.commands;

import cc.fyre.stark.engine.command.Command;
import cc.fyre.stark.engine.command.data.parameter.Param;
import net.hcriots.hcfactions.Hulu;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * ---------- hcteams ----------
 * Created by Fraser.Cumming on 29/03/2016.
 * © 2016 Fraser Cumming All Rights Reserved
 */
public class AssociateAccountsCommand {


    @Command(names = {"ass", "associate"}, permission = "op")
    public static void associate(Player sender, @Param(name = "player") UUID player, @Param(name = "associate") UUID playertwo) {
        if (Hulu.getInstance().getWhitelistedIPMap().contains(player)) {
            UUID other = Hulu.getInstance().getWhitelistedIPMap().get(player);
            Hulu.getInstance().getWhitelistedIPMap().add(playertwo, other);
        } else if (Hulu.getInstance().getWhitelistedIPMap().contains(playertwo)) {
            UUID other = Hulu.getInstance().getWhitelistedIPMap().get(playertwo);
            Hulu.getInstance().getWhitelistedIPMap().add(player, other);
        } else {
            if (Hulu.getInstance().getWhitelistedIPMap().containsValue(player)) {
                Hulu.getInstance().getWhitelistedIPMap().add(playertwo, player);
            } else if (Hulu.getInstance().getWhitelistedIPMap().containsValue(playertwo)) {
                Hulu.getInstance().getWhitelistedIPMap().add(player, playertwo);
            } else {
                Hulu.getInstance().getWhitelistedIPMap().add(playertwo, player);
            }
        }
        sender.sendMessage(ChatColor.GREEN + "You have successfully associated these accounts");
    }


}
