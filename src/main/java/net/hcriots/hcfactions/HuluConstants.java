/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions;

import cc.fyre.stark.Stark;
import cc.fyre.stark.core.tags.Tag;
import cc.fyre.stark.util.CC;
import net.hcriots.hcfactions.team.Team;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public final class HuluConstants {

    public static String DEATHBAN_MESSAGE = CC.INSTANCE.translate(
            "&4&lYou are currently deathbanned!\n" +
                    "&cYou will be able to join back in\n" +
                    "&7%s");

    public static String teamChatFormat(Player player, String message) {
        return (ChatColor.DARK_AQUA + "(Team) " + player.getName() + ": " + ChatColor.YELLOW + message);
    }

    public static String officerChatFormat(Player player, String message) {
        return (ChatColor.LIGHT_PURPLE + "(Officer) " + player.getName() + ": " + ChatColor.YELLOW + message);
    }

    public static String teamChatSpyFormat(Team team, Player player, String message) {
        return (ChatColor.GOLD + "[" + ChatColor.DARK_AQUA + "TC: " + ChatColor.YELLOW + team.getName() + ChatColor.GOLD + "]" + ChatColor.DARK_AQUA + player.getName() + ": " + message);
    }

    public static String allyChatFormat(Player player, String message) {
        return (Team.ALLY_COLOR + "(Ally) " + player.getName() + ": " + ChatColor.YELLOW + message);
    }

    public static String allyChatSpyFormat(Team team, Player player, String message) {
        return (ChatColor.GOLD + "[" + Team.ALLY_COLOR + "AC: " + ChatColor.YELLOW + team.getName() + ChatColor.GOLD + "]" + Team.ALLY_COLOR + player.getName() + ": " + message);
    }

    public static String publicChatFormatTwoPointOhBaby(Player player, Team team, String rankPrefix) {
        String starting = "";

        if (team != null) {
            if (rankPrefix.toLowerCase().contains("famous") || rankPrefix.toLowerCase().contains("youtube")) {
                rankPrefix = "";
            }

            starting = ChatColor.GOLD + "[" + Hulu.getInstance().getServerHandler().getDefaultRelationColor() + team.getName() + ChatColor.GOLD + "] ";
        }

        Tag tag = Stark.instance.core.getProfileHandler().getByUUID(player.getUniqueId()).getTag();
        if (tag == null) {
            return starting + ChatColor.LIGHT_PURPLE + rankPrefix + ChatColor.WHITE + "%s" + ChatColor.WHITE + ": %s";
        } else {
            return starting + ChatColor.LIGHT_PURPLE + rankPrefix + ChatColor.WHITE + "%s " + tag.getDisplay() + ChatColor.WHITE + ": %s";
        }
    }
}
