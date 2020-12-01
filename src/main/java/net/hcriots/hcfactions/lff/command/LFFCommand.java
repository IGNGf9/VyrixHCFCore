/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.lff.command;

import cc.fyre.stark.engine.command.Command;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.lff.LFFMenu;
import net.hcriots.hcfactions.team.Team;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class LFFCommand {

    @Command(names = {"lff", "lookingforfaction"}, permission = "")
    public static void lff(Player player) {
        Team playerTeam = Hulu.getInstance().getTeamHandler().getTeam(player);
        if (playerTeam != null) {
            player.sendMessage(ChatColor.RED + "You are already in a Team!");
            return;
        }
        new LFFMenu().openMenu(player);
    }
}