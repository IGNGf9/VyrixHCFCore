/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.map.stats.command;

import cc.fyre.stark.engine.command.Command;
import cc.fyre.stark.engine.command.data.parameter.Param;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.map.stats.StatsEntry;
import net.hcriots.hcfactions.team.Team;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;

public class StatsCommand {

    @Command(names = {"stats"}, permission = "")
    public static void stats(CommandSender sender, @Param(name = "player", defaultValue = "self") Player uuid) {
        StatsEntry stats = Hulu.getInstance().getMapHandler().getStatsHandler().getStats(uuid.getUniqueId());

        if (stats == null) {
            sender.sendMessage(ChatColor.RED + "Player not found.");
            return;
        }

        sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat('-', 53));
        sender.sendMessage(uuid.getDisplayName() + ChatColor.GOLD + " Stats");
        sender.sendMessage("");
        sender.sendMessage(ChatColor.YELLOW + "Kills: " + ChatColor.WHITE + stats.getKills());
        sender.sendMessage(ChatColor.YELLOW + "Deaths: " + ChatColor.WHITE + stats.getDeaths());
        sender.sendMessage(ChatColor.YELLOW + "Killstreak: " + ChatColor.WHITE + stats.getKillstreak());
        sender.sendMessage(ChatColor.YELLOW + "Highest Killstreak: " + ChatColor.WHITE + stats.getHighestKillstreak());
        sender.sendMessage(ChatColor.YELLOW + "KD: " + ChatColor.WHITE + (stats.getDeaths() == 0 ? "N/A" : Team.DTR_FORMAT.format((double) stats.getKills() / (double) stats.getDeaths())));

        sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat('-', 53));
    }

    @Command(names = {"statsclearall"}, permission = "op")
    public static void clearallstats(Player sender) {
        ConversationFactory factory = new ConversationFactory(Hulu.getInstance()).withModality(true).withPrefix(new NullConversationPrefix()).withFirstPrompt(new StringPrompt() {

            public String getPromptText(ConversationContext context) {
                return "§aAre you sure you want to clear all stats? Type §byes§a to confirm or §cno§a to quit.";
            }

            @Override
            public Prompt acceptInput(ConversationContext cc, String s) {
                if (s.equalsIgnoreCase("yes")) {
                    Hulu.getInstance().getMapHandler().getStatsHandler().clearAll();
                    cc.getForWhom().sendRawMessage(ChatColor.GREEN + "All stats cleared!");
                    return Prompt.END_OF_CONVERSATION;
                }

                if (s.equalsIgnoreCase("no")) {
                    cc.getForWhom().sendRawMessage(ChatColor.GREEN + "Cancelled.");
                    return Prompt.END_OF_CONVERSATION;
                }

                cc.getForWhom().sendRawMessage(ChatColor.GREEN + "Unrecognized response. Type §b/yes§a to confirm or §c/no§a to quit.");
                return Prompt.END_OF_CONVERSATION;
            }

        }).withLocalEcho(false).withEscapeSequence("/no").withTimeout(10).thatExcludesNonPlayersWithMessage("Go away evil console!");

        Conversation con = factory.buildConversation(sender);
        sender.beginConversation(con);
    }
}