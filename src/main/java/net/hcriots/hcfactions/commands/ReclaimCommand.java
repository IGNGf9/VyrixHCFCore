/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.commands;

import cc.fyre.stark.Stark;
import cc.fyre.stark.core.profile.Profile;
import cc.fyre.stark.core.rank.Rank;
import cc.fyre.stark.engine.command.Command;
import cc.fyre.stark.engine.command.data.parameter.Param;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.reclaim.Group;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ReclaimCommand {

    @Command(names = {"reclaim", "claim"}, permission = "")
    public static void reclaim(CommandSender sender) {
        Player p = (Player) sender;
        Profile profile = Stark.instance.core.getProfileHandler().getByUUID(p.getUniqueId());
        assert profile != null;
        Rank rank = profile.getRank();
        String group = rank.getDisplayName();

        boolean needsReclaim = false;

        for (Group definedGroup : Hulu.getInstance().getReclaimHandler().getGroups()) {
            if (definedGroup.getName().equalsIgnoreCase(group)) {
                needsReclaim = true;
            }
        }

        if (needsReclaim) {

            if (Hulu.getInstance().getReclaimHandler().hasUsedReclaim(p)) {
                p.sendMessage(ChatColor.RED + "You have already reclaimed your rank.");
                return;
            }

            List<String> commands = new ArrayList<>();

            for (Group definedGroup : Hulu.getInstance().getReclaimHandler().getGroups()) {
                if (definedGroup.getName().equalsIgnoreCase(group)) {
                    for (String command1 : definedGroup.getCommands()) {
                        commands.add(command1);
                    }
                }
            }

            for (String c : commands) {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), c.replace("%player%", p.getName()));
            }

            Hulu.getInstance().getReclaimHandler().setUsedReclaim(p, true);
        } else {
            p.sendMessage(ChatColor.RED + "You have nothing to reclaim.");
        }
    }

    @Command(names = {"reclaim reset", "claim reset"}, permission = "op")
    public static void reclaimReset(CommandSender sender, @Param(name = "player", defaultValue = "self") Player target) {
        Player player = (Player) sender;
        sender.sendMessage(player.getDisplayName() + ChatColor.GOLD + " reclaim has been reset and now can use their reclaim.");
        Hulu.getInstance().getReclaimHandler().setUsedReclaim(target, false);

    }
}