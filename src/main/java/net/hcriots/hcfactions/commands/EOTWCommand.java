/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.commands;

import cc.fyre.stark.engine.command.Command;
import lombok.Getter;
import lombok.Setter;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.listener.EndListener;
import net.hcriots.hcfactions.team.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public class EOTWCommand {

    @Getter
    @Setter
    private static boolean ffaEnabled = false;
    @Getter
    @Setter
    private static long ffaActiveAt = -1L;

    @Command(names = {"eotwsetallraidable"}, permission = "op")
    public static void eotwSetAllRaidable(Player sender) {
        for (Team team : Hulu.getInstance().getTeamHandler().getTeams()) {
            team.setDTR(-20.0);
            team.setNeedsSave(true);
        }
        sender.sendMessage(ChatColor.GOLD + "All teams have been set raidable!");
    }

    @Command(names = {"EOTW"}, permission = "foxtrot.eotw")
    public static void eotw(Player sender) {
        if (sender.getGameMode() != GameMode.CREATIVE) {
            sender.sendMessage(ChatColor.RED + "This command must be ran in creative.");
            return;
        }

        Hulu.getInstance().getServerHandler().setEOTW(!Hulu.getInstance().getServerHandler().isEOTW());

        EndListener.endActive = !Hulu.getInstance().getServerHandler().isEOTW();

        if (Hulu.getInstance().getServerHandler().isEOTW()) {
            for (Player player : Hulu.getInstance().getServer().getOnlinePlayers()) {
                player.playSound(player.getLocation(), Sound.WITHER_SPAWN, 1F, 1F);
            }

            Hulu.getInstance().getServer().broadcastMessage(ChatColor.RED + "███████");
            Hulu.getInstance().getServer().broadcastMessage(ChatColor.RED + "█" + ChatColor.DARK_RED + "█████" + ChatColor.RED + "█");
            Hulu.getInstance().getServer().broadcastMessage(ChatColor.RED + "█" + ChatColor.DARK_RED + "█" + ChatColor.RED + "█████" + " " + ChatColor.DARK_RED + "[EOTW]");
            Hulu.getInstance().getServer().broadcastMessage(ChatColor.RED + "█" + ChatColor.DARK_RED + "████" + ChatColor.RED + "██" + " " + ChatColor.RED.toString() + ChatColor.BOLD + "EOTW has commenced.");
            Hulu.getInstance().getServer().broadcastMessage(ChatColor.RED + "█" + ChatColor.DARK_RED + "█" + ChatColor.RED + "█████" + " " + ChatColor.RED + "All SafeZones are now Deathban.");
            Hulu.getInstance().getServer().broadcastMessage(ChatColor.RED + "█" + ChatColor.DARK_RED + "█████" + ChatColor.RED + "█");
            Hulu.getInstance().getServer().broadcastMessage(ChatColor.RED + "███████");
        } else {
            sender.sendMessage(ChatColor.RED + "The server is no longer in EOTW mode.");
        }
    }

    @Command(names = {"EOTW TpAll"}, permission = "foxtrot.eotw")
    public static void eotwTpAll(Player sender) {
        if (sender.getGameMode() != GameMode.CREATIVE) {
            sender.sendMessage(ChatColor.RED + "This command must be ran in creative.");
            return;
        }

        if (!Hulu.getInstance().getServerHandler().isEOTW()) {
            sender.sendMessage(ChatColor.RED + "This command must be ran during EOTW. (/eotw)");
            return;
        }

        for (Player onlinePlayer : Hulu.getInstance().getServer().getOnlinePlayers()) {
            onlinePlayer.teleport(sender.getLocation());
        }

        sender.sendMessage(ChatColor.RED + "Players teleported.");
    }

    @Command(names = {"PreEOTW"}, permission = "foxtrot.eotw")
    public static void preeotw(Player sender) {
        if (sender.getGameMode() != GameMode.CREATIVE) {
            sender.sendMessage(ChatColor.RED + "This command must be ran in creative.");
            return;
        }

        Hulu.getInstance().getServerHandler().setPreEOTW(!Hulu.getInstance().getServerHandler().isPreEOTW());

        Hulu.getInstance().getDeathbanMap().wipeDeathbans();

        if (Hulu.getInstance().getServerHandler().isPreEOTW()) {
            for (Player player : Hulu.getInstance().getServer().getOnlinePlayers()) {
                player.playSound(player.getLocation(), Sound.WITHER_SPAWN, 1F, 1F);
            }

            Hulu.getInstance().getServer().broadcastMessage(ChatColor.RED + "███████");
            Hulu.getInstance().getServer().broadcastMessage(ChatColor.RED + "█" + ChatColor.DARK_RED + "█████" + ChatColor.RED + "█" + " " + ChatColor.DARK_RED + "[Pre-EOTW]");
            Hulu.getInstance().getServer().broadcastMessage(ChatColor.RED + "█" + ChatColor.DARK_RED + "█" + ChatColor.RED + "█████" + " " + ChatColor.RED.toString() + ChatColor.BOLD + "EOTW is about to commence.");
            Hulu.getInstance().getServer().broadcastMessage(ChatColor.RED + "█" + ChatColor.DARK_RED + "████" + ChatColor.RED + "██" + " " + ChatColor.RED + "PvP Protection is disabled.");
            Hulu.getInstance().getServer().broadcastMessage(ChatColor.RED + "█" + ChatColor.DARK_RED + "█" + ChatColor.RED + "█████" + " " + ChatColor.RED + "All players have been un-deathbanned.");
            Hulu.getInstance().getServer().broadcastMessage(ChatColor.RED + "█" + ChatColor.DARK_RED + "█████" + ChatColor.RED + "█" + " " + ChatColor.RED + "All deathbans are now permanent.");
            Hulu.getInstance().getServer().broadcastMessage(ChatColor.RED + "███████");
        } else {
            sender.sendMessage(ChatColor.RED + "The server is no longer in Pre-EOTW mode.");
        }
    }

    @Command(names = {"eotw ffa"}, permission = "foxtrot.eotw")
    public static void ffa(Player sender) {
        if (sender.getGameMode() != GameMode.CREATIVE) {
            sender.sendMessage(ChatColor.RED + "This command must be ran in creative.");
            return;
        }

        ConversationFactory factory = new ConversationFactory(Hulu.getInstance()).withModality(true).withPrefix(new NullConversationPrefix()).withFirstPrompt(new StringPrompt() {

            public String getPromptText(ConversationContext context) {
                return "§aAre you sure you want to enter FFA mode? This will start a countdown that cannot be cancelled. Type yes or no to confirm.";
            }

            @Override
            public Prompt acceptInput(ConversationContext cc, String s) {
                if (s.equalsIgnoreCase("yes")) {
                    ffaEnabled = true;
                    ffaActiveAt = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5);
                    cc.getForWhom().sendRawMessage(ChatColor.GREEN + "FFA countdown initiated.");

                    Bukkit.getScheduler().runTask(Hulu.getInstance(), () -> {
                        Hulu.getInstance().getServer().broadcastMessage(ChatColor.RED + "███████");
                        Hulu.getInstance().getServer().broadcastMessage(ChatColor.RED + "█" + ChatColor.GOLD + "█████" + ChatColor.RED + "█");
                        Hulu.getInstance().getServer().broadcastMessage(ChatColor.RED + "█" + ChatColor.GOLD + "█" + ChatColor.RED + "█████" + " " + ChatColor.DARK_RED + "[EOTW]");
                        Hulu.getInstance().getServer().broadcastMessage(ChatColor.RED + "█" + ChatColor.GOLD + "████" + ChatColor.RED + "██" + " " + ChatColor.RED.toString() + ChatColor.BOLD + "EOTW " + ChatColor.GOLD.toString() + ChatColor.BOLD + "FFA" + ChatColor.RED.toString() + ChatColor.BOLD + " will commence in: 5:00.");
                        Hulu.getInstance().getServer().broadcastMessage(ChatColor.RED + "█" + ChatColor.GOLD + "█" + ChatColor.RED + "█████" + " " + ChatColor.RED.toString() + "If you ally, you will be punished.");
                        Hulu.getInstance().getServer().broadcastMessage(ChatColor.RED + "█" + ChatColor.GOLD + "█" + ChatColor.RED + "█████");
                        Hulu.getInstance().getServer().broadcastMessage(ChatColor.RED + "███████");
                    });

                    Bukkit.getScheduler().runTaskLater(Hulu.getInstance(), () -> {
                        Hulu.getInstance().getServer().broadcastMessage(ChatColor.RED + "███████");
                        Hulu.getInstance().getServer().broadcastMessage(ChatColor.RED + "█" + ChatColor.GOLD + "█████" + ChatColor.RED + "█");
                        Hulu.getInstance().getServer().broadcastMessage(ChatColor.RED + "█" + ChatColor.GOLD + "█" + ChatColor.RED + "█████" + " " + ChatColor.DARK_RED + "[EOTW]");
                        Hulu.getInstance().getServer().broadcastMessage(ChatColor.RED + "█" + ChatColor.GOLD + "████" + ChatColor.RED + "██" + " " + ChatColor.RED.toString() + ChatColor.BOLD + "EOTW " + ChatColor.GOLD.toString() + ChatColor.BOLD + "FFA" + ChatColor.RED.toString() + ChatColor.BOLD + " has now commenced!");
                        Hulu.getInstance().getServer().broadcastMessage(ChatColor.RED + "█" + ChatColor.GOLD + "█" + ChatColor.RED + "█████" + " " + ChatColor.RED + "Good luck and have fun!");
                        Hulu.getInstance().getServer().broadcastMessage(ChatColor.RED + "█" + ChatColor.GOLD + "█" + ChatColor.RED + "█████");
                        Hulu.getInstance().getServer().broadcastMessage(ChatColor.RED + "███████");
                    }, 5 * 60 * 20);

                    return Prompt.END_OF_CONVERSATION;
                }

                if (s.equalsIgnoreCase("no")) {
                    cc.getForWhom().sendRawMessage(ChatColor.GREEN + "FFA initation aborted.");
                    return Prompt.END_OF_CONVERSATION;
                }

                cc.getForWhom().sendRawMessage(ChatColor.GREEN + "Unrecognized response. Type §byes§a to confirm or §cno§a to quit.");
                return Prompt.END_OF_CONVERSATION;
            }

        }).withLocalEcho(false).withEscapeSequence("/no").withTimeout(10).thatExcludesNonPlayersWithMessage("Go away evil console!");
        Conversation con = factory.buildConversation(sender);
        sender.beginConversation(con);
    }

    public static boolean realFFAStarted() {
        return ffaEnabled && ffaActiveAt < System.currentTimeMillis();
    }

}