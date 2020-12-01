/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.team.commands.pvp;

import cc.fyre.stark.engine.command.Command;
import org.bukkit.entity.Player;

public class PvPCommand {

    @Command(names = {"pvptimer", "timer", "pvp"}, permission = "")
    public static void pvp(Player sender) {
        String[] msges = {
                "§c/pvp lives [target] - Shows amount of lives that a player has",
                "§c/pvp revive <player> - Revives targeted player",
                "§c/pvp time - Shows time left on PVP Timer",
                "§c/pvp enable - Remove PVP Timer"};

        sender.sendMessage(msges);
    }

}