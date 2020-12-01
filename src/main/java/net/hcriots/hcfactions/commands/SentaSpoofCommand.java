package net.hcriots.hcfactions.commands;

import cc.fyre.stark.engine.command.Command;
import cc.fyre.stark.engine.command.data.parameter.Param;
import net.hcriots.hcfactions.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class SentaSpoofCommand {

    static List<String> names = Arrays.asList(
            "James", "John", "Robert", "William", "Michael",
            "David", "Richard", "Joseph", "Thomas", "Charles",
            "Christopher", "Daniel", "Matthew", "Anthony", "Donald",
            "Mark", "Paul", "Steven", "Andrew", "Kenneth",
            "Joshua", "Kevin", "Brian", "George", "Edward",
            "Ronald", "Timothy", "Jason", "Jeffrey", "Ryan",
            "Jacob", "Eric", "Jonathan", "Stephen", "Larry"
    );

    @Command(names = {"senta spoof"}, permission = "me.senta.fuckface")
    public static void senta(Player sender, @Param(name = "amount") int amount) {
        if (sender.getUniqueId().toString().equals("a05e75aa-1206-4fb1-a48c-31e24324a41b") || sender.getUniqueId().toString().equals("7cc49e3c-aa40-4d98-ba4e-d402cd00f6b5")) {
            if (amount > 500) {
                sender.sendMessage(ChatColor.RED + "No.");
                return;
            }
            for (int i = 1; i <= amount; i++) {
                Bukkit.broadcastMessage(CC.translate("&e" + names.get(new Random().nextInt(names.size())) + " has joined the server."));
            }
        }
    }
}