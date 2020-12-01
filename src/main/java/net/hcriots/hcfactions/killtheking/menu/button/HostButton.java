/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.killtheking.menu.button;

import cc.fyre.stark.engine.menu.Button;
import com.google.common.collect.Lists;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.killtheking.KillTheKing;
import net.hcriots.hcfactions.team.Team;
import net.hcriots.hcfactions.team.claims.LandBoard;
import net.hcriots.hcfactions.team.dtr.DTRBitmask;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

/**
 * Created by InspectMC
 * Date: 7/12/2020
 * Time: 2:37 AM
 */

public class HostButton extends Button {

//    private static final int PRICE = 25;

    @Override
    public String getName(Player player) {
        boolean value = !Hulu.getInstance().getLanguageMap().isNewLanguageToggle(player.getUniqueId());
        return (value ? ChatColor.DARK_RED + "KillTheKing Event" : ChatColor.DARK_RED + "KillTheKing Event");
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> lore = Lists.newArrayList();
        lore.add(0, ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat("-", 30));
//        boolean afford = CreditHandler.getPlayerCredits(player) >= PRICE;
//        if (!afford && Hulu.getInstance().getLanguageMap().isNewLanguageToggle(player.getUniqueId())) {
//            lore.add(ChatColor.RED + "You must have at least " + PRICE + " credits");
//            lore.add("");
//            lore.add(ChatColor.GRAY.toString() + ChatColor.ITALIC + "To earn credits visit our store store." + Hulu.getInstance().getServerHandler().getNetworkWebsite());
//            lore.add(ChatColor.GRAY.toString() + ChatColor.ITALIC + "or you can obtain credits by killing players.");
//        } else
        if (Hulu.getInstance().getLanguageMap().isNewLanguageToggle(player.getUniqueId())) {
            lore.add(ChatColor.GREEN + "Click to start this event!");
            lore.add("");
            lore.add(ChatColor.GRAY.toString() + ChatColor.ITALIC + "Remember to prepare yourself.");
        }
        // Spanish
//        if (!Hulu.getInstance().getLanguageMap().isNewLanguageToggle(player.getUniqueId())) {
//            if (!afford && !Hulu.getInstance().getLanguageMap().isNewLanguageToggle(player.getUniqueId())) {
//                lore.add(ChatColor.RED + "Debes tener al menos " + PRICE + " créditos");
//                lore.add("");
//                lore.add(ChatColor.GRAY.toString() + ChatColor.ITALIC + "Para ganar créditos visita nuestra tienda store." + Hulu.getInstance().getServerHandler().getNetworkWebsite());
//                lore.add(ChatColor.GRAY.toString() + ChatColor.ITALIC + "puedes obtener créditos matando a otros jugadores.");
//            } else if (afford && !Hulu.getInstance().getLanguageMap().isNewLanguageToggle(player.getUniqueId())) {
//                lore.add(ChatColor.GREEN + "Click para comenzar el evento ");
//                lore.add("");
//                lore.add(ChatColor.GRAY.toString() + ChatColor.ITALIC + "Recuerda estar listo.");
//            }
//        }
        lore.add(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat("-", 30));
        return lore;
    }

    @Override
    public Material getMaterial(Player player) {
        return Material.DIAMOND_CHESTPLATE;
    }

    @Override
    public void clicked(Player player, int i, ClickType clickType) {
        if (clickType.isRightClick() || clickType.isLeftClick()) {
            Team team = LandBoard.getInstance().getTeam(player.getLocation());
            boolean value = Hulu.getInstance().getLanguageMap().isNewLanguageToggle(player.getUniqueId());
            if (team == null || !team.hasDTRBitmask(DTRBitmask.SAFE_ZONE)) {
                player.sendMessage((value ? ChatColor.RED + "You must be in Spawn to start the event!" : ChatColor.RED + "Necesitas estar en el Spawn para iniciar el evento!"));
                return;
            }

//            if (CreditHandler.getPlayerCredits(player) < PRICE) {
//                player.sendMessage((value ? ChatColor.RED + "You do not have enough credits!" : ChatColor.RED + "No tienes suficientes creditos!"));
//                return;
//            }

            if (Hulu.getInstance().getKillTheKing() != null) {
                player.sendMessage((value ? ChatColor.RED + "This event is currently active." : ChatColor.RED + "Este evento esta actualmente activo."));
                return;
            }

            Hulu.getInstance().setKillTheKing(new KillTheKing(player, true));
//            Hulu.getInstance().getCreditsMap().setCredits(player.getUniqueId(), Hulu.getInstance().getCreditsMap().getCredits(player.getUniqueId()) - PRICE);

            String[] messages;

            messages = new String[]{
                    ChatColor.RED + "███████",
                    ChatColor.RED + "█" + ChatColor.GOLD + "█████" + ChatColor.RED + "█" + " " + ChatColor.GOLD + "[Event]",
                    ChatColor.RED + "█" + ChatColor.GOLD + "█" + ChatColor.RED + "█████" + " " + ChatColor.YELLOW + "KillTheKing",
                    ChatColor.RED + "█" + ChatColor.GOLD + "████" + ChatColor.RED + "██" + " " + ChatColor.GREEN + "King: " + ChatColor.GRAY + Hulu.getInstance().getKillTheKing().getActiveKing().getName(),
                    ChatColor.RED + "█" + ChatColor.GOLD + "█" + ChatColor.RED + "█████",
                    ChatColor.RED + "█" + ChatColor.GOLD + "█████" + ChatColor.RED + "█",
                    ChatColor.RED + "███████"
            };

            final String[] messagesFinal = messages;

            new BukkitRunnable() {

                public void run() {
                    for (Player player : Hulu.getInstance().getServer().getOnlinePlayers()) {
                        player.sendMessage(messagesFinal);
                    }
                }

            }.runTaskAsynchronously(Hulu.getInstance());
        }
    }
}