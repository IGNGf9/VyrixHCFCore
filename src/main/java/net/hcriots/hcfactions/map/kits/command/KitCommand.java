/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.map.kits.command;

import cc.fyre.stark.engine.command.Command;
import cc.fyre.stark.engine.command.data.parameter.Param;
import cc.fyre.stark.engine.scoreboard.ScoreFunction;
import com.google.common.collect.Maps;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.HuluLang;
import net.hcriots.hcfactions.map.kits.Kit;
import net.hcriots.hcfactions.server.SpawnTagHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class KitCommand {

    private static final Map<String, Map<UUID, Long>> cooldownsMap = Maps.newHashMap();

    @Command(names = {"kits"}, permission = "")
    public static void kits_command(Player sender, @Param(name = "<kit>", defaultValue = "some-unused-value", wildcard = true) String kitName) {
        if (!Hulu.getInstance().getMapHandler().isKitMap()) {
            sender.sendMessage(HuluLang.KIT_MAP_ONLY);
        } else {

            kitName = kitName.toLowerCase();
            if (kitName.equalsIgnoreCase("some-unused-value")) {
                StringBuilder kitsBuilder = new StringBuilder();
                for (Kit kit : Hulu.getInstance().getMapHandler().getKitManager().getKits()) {
                    if (!canUse(sender, kit)) continue;

                    if (kitsBuilder.length() != 0) {
                        kitsBuilder.append(',');
                        kitsBuilder.append(' ');
                    }

                    kitsBuilder.append(kit.getName());
                }

                if (kitsBuilder.length() == 0) {
                    sender.sendMessage(ChatColor.RED + "You have no usable donor kits!");
                    return;
                }

                sender.sendMessage(ChatColor.RED + "Your usable donor kits: " + kitsBuilder.toString());
                return;
            }

            Kit targetKit = Hulu.getInstance().getMapHandler().getKitManager().get(kitName);

            if (targetKit == null) {
                sender.sendMessage(ChatColor.RED + "Unable to locate kit '" + kitName + "'.");
                return;
            }

            if (SpawnTagHandler.isTagged(sender)) {
                sender.sendMessage(ChatColor.RED + "You're ineligible to use donator kits whilst you have an active Spawn Tag.");
                return;
            }

            if (sender.hasMetadata("ModMode")) {
                sender.sendMessage(ChatColor.RED + "You can't do this whilst in mod mode.");
                return;
            }

            if (!canUse(sender, targetKit)) {
                sender.sendMessage(ChatColor.RED + "You're ineligible to use the donator kit '" + kitName + "'.");
                return;
            }

            Map<UUID, Long> cooldownMap = cooldownsMap.computeIfAbsent(kitName, k -> Maps.newHashMap());

            if (0 <= cooldownMap.getOrDefault(sender.getUniqueId(), -1L) - System.currentTimeMillis()) {
                long difference = cooldownMap.get(sender.getUniqueId()) - System.currentTimeMillis();
                sender.sendMessage(ChatColor.RED + "You're ineligible to use this kit for " + ScoreFunction.Companion.TIME_SIMPLE(Math.round(difference / 1000F)));
                return;
            } else {
                cooldownMap.put(sender.getUniqueId(), System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(30));
            }

            targetKit.apply(sender);
        }
    }

    public static boolean canUse(Player player, Kit kit) {
        String kitName = kit.getName();

        // Don't show any of these kits as donator kits
        if (kitName.equals("PvP") || kitName.equals("Archer") || kitName.equals("Bard") || kitName.equals("Rogue") || kitName.equals("Miner") || kitName.equals("Builder")) {
            return false;
        }

        return player.hasPermission("stark.staff") || player.hasPermission("hulu.youtuber") || player.hasPermission("hulu.famous");

    }


}
