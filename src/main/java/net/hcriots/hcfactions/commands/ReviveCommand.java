/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.commands;

import cc.fyre.stark.Stark;
import cc.fyre.stark.engine.command.Command;
import cc.fyre.stark.engine.command.data.parameter.Param;
import com.google.common.io.Files;
import net.hcriots.hcfactions.Hulu;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class ReviveCommand {

    @Command(names = {"Revive"}, permission = "foxtrot.revive")
    public static void revive(CommandSender sender, @Param(name = "player") UUID player, @Param(name = "reason", wildcard = true) String reason) {
        if (reason.equals(".")) {
            sender.sendMessage(ChatColor.RED + ". is not a good reason...");
            return;
        }

        if (Hulu.getInstance().getDeathbanMap().isDeathbanned(player)) {
            File logTo = new File(new File("foxlogs"), "adminrevives.log");

            try {
                logTo.createNewFile();
                Files.append("[" + SimpleDateFormat.getDateTimeInstance().format(new Date()) + "] " + sender.getName() + " revived " + Stark.instance.getCore().getUuidCache().name(player) + " for " + reason + "\n", logTo, Charset.defaultCharset());
            } catch (Exception e) {
                e.printStackTrace();
            }

            Hulu.getInstance().getDeathbanMap().revive(player);
            sender.sendMessage(ChatColor.GREEN + "Revived " + Stark.instance.getCore().getUuidCache().name(player) + "!");
        } else {
            sender.sendMessage(ChatColor.RED + "That player is not deathbanned!");
        }
    }

}
