/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.perks;

import cc.fyre.stark.engine.command.Command;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.team.Team;
import net.minecraft.util.com.google.common.base.Joiner;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by InspectMC
 * Date: 7/8/2020
 * Time: 3:35 PM
 */
public class NearCommand {

    @Command(names = {"near", "nearme"}, permission = "hulu.command.near")
    public static void near(CommandSender sender) {
        int RADIUS = 30;
        Player player = (Player) sender;
        List<Player> playerList = getNearbyEnemies(player);
        if (playerList.isEmpty()) {
            sender.sendMessage(ChatColor.RED + "No visible enemies in a " + RADIUS + " block radius");
        } else {
            sender.sendMessage(ChatColor.GOLD + "Nearby visible enemies within " + RADIUS + " block radius");
            sender.sendMessage(ChatColor.GRAY + Joiner.on(", ").join(playerList.stream().map(Player::getName).collect(Collectors.toList())));
        }
    }

    public static List<Player> getNearbyEnemies(Player player) {
        int RADIUS = 30;
        List<Player> players = new ArrayList<>();
        Team playerFaction = Hulu.getInstance().getTeamHandler().getTeam(player.getUniqueId());
        Collection<Entity> nearby = player.getNearbyEntities(RADIUS, RADIUS, RADIUS);
        for (final Entity entity : nearby) {
            if (entity instanceof Player) {
                final Player target = (Player) entity;
                if (!target.canSee(player)) {
                    continue;
                }
                if (!player.canSee(target)) {
                    continue;
                }
                if (target.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                    continue;
                }
                final Team targetFaction;
                if (playerFaction != null && (targetFaction = Hulu.getInstance().getTeamHandler().getTeam(target.getUniqueId())) != null && targetFaction.equals(playerFaction)) {
                    continue;
                }
                players.add(target);
            }
        }
        return players;
    }
}