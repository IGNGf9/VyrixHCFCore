/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.events.region.cavern.commands;

import cc.fyre.stark.engine.command.Command;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.events.region.cavern.Cavern;
import net.hcriots.hcfactions.events.region.cavern.CavernHandler;
import net.hcriots.hcfactions.team.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import static org.bukkit.ChatColor.*;

public class CavernCommand {

    @Command(names = "cavern scan", permission = "op")
    public static void cavernScan(Player sender) {
        if (!Hulu.getInstance().getConfig().getBoolean("cavern", false)) {
            sender.sendMessage(RED + "Cavern is currently disabled. Check config.yml to toggle.");
            return;
        }

        Team team = Hulu.getInstance().getTeamHandler().getTeam(CavernHandler.getCavernTeamName());

        // Make sure we have a team
        if (team == null) {
            sender.sendMessage(ChatColor.RED + "You must first create the team (" + CavernHandler.getCavernTeamName() + ") and claim it!");
            return;
        }

        // Make sure said team has a claim
        if (team.getClaims().isEmpty()) {
            sender.sendMessage(ChatColor.RED + "You must claim land for '" + CavernHandler.getCavernTeamName() + "' before scanning it!");
            return;
        }

        // We have a claim, and a team, now do we have a glowstone?
        if (!Hulu.getInstance().getCavernHandler().hasCavern()) {
            Hulu.getInstance().getCavernHandler().setCavern(new Cavern());
        }

        // We have a glowstone now, we're gonna scan and save the area
        Hulu.getInstance().getCavernHandler().getCavern().scan();
        Hulu.getInstance().getCavernHandler().save(); // save to file :D

        sender.sendMessage(GREEN + "[Cavern] Scanned all ores and saved Cavern to file!");
    }

    @Command(names = "cavern reset", permission = "op")
    public static void cavernReset(Player sender) {
        Team team = Hulu.getInstance().getTeamHandler().getTeam(CavernHandler.getCavernTeamName());

        // Make sure we have a team, claims, and a mountain!
        if (team == null || team.getClaims().isEmpty() || !Hulu.getInstance().getCavernHandler().hasCavern()) {
            sender.sendMessage(RED + "Create the team '" + CavernHandler.getCavernTeamName() + "', then make a claim for it, finally scan it! (/cavern scan)");
            return;
        }

        // Check, check, check, LIFT OFF! (reset the mountain)
        Hulu.getInstance().getCavernHandler().getCavern().reset();

        Bukkit.broadcastMessage(AQUA + "[Cavern]" + GREEN + " All ores have been reset!");
    }
}