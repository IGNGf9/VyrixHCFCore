/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.nametag;

import cc.fyre.stark.engine.nametag.NametagInfo;
import cc.fyre.stark.engine.nametag.NametagProvider;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.pvpclasses.pvpclasses.ArcherClass;
import net.hcriots.hcfactions.team.Team;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class HuluNametagProvider extends NametagProvider {

    public HuluNametagProvider() {
        super("Hulu Provider", 5);
    }

    @Override
    public NametagInfo fetchNametag(Player toRefresh, Player refreshFor) {
        ChatColor defColor = Hulu.getInstance().getServerHandler().getDefaultRelationColor();
        Team viewerTeam = Hulu.getInstance().getTeamHandler().getTeam(refreshFor);
        Team playerISeee = Hulu.getInstance().getTeamHandler().getTeam(toRefresh);
        NametagInfo nametagInfo = null;

        if (viewerTeam != null) {
            if (viewerTeam.isMember(toRefresh.getUniqueId())) {
                nametagInfo = createNametag(toRefresh, ChatColor.DARK_GREEN.toString(), "");
            } else if (viewerTeam.isAlly(toRefresh.getUniqueId())) {
                nametagInfo = createNametag(toRefresh, Team.ALLY_COLOR.toString(), "");
            }
        }

        if (Hulu.getInstance().getDtrMap() != null) {
            if (Hulu.getInstance().getDtrMap().isDtrToggled(refreshFor.getUniqueId())) {
                if (playerISeee != null) {
                    if (viewerTeam != null) {
                        if (viewerTeam.getFocused() != null && viewerTeam.getFocused().equals(toRefresh.getUniqueId())) {
                            nametagInfo = createNametag(toRefresh, toC(playerISeee.getDTRWithColor() + " &7▏&2 "), "");
                        } else if (viewerTeam.getFactionFocused() != null && viewerTeam.getFactionFocused().equals(playerISeee)) {
                            for (Player player : playerISeee.getOnlineMembers()) {
                                nametagInfo = createNametag(player, toC(playerISeee.getDTRWithColor() + " &7▏&2 "), "");
                            }
                        } else if (viewerTeam.isMember(toRefresh.getUniqueId())) {
                            nametagInfo = createNametag(toRefresh, toC(playerISeee.getDTRWithColor() + " &7▏&2 "), "");
                        } else if (viewerTeam.isAlly(toRefresh.getUniqueId())) {
                            nametagInfo = createNametag(toRefresh, toC(playerISeee.getDTRWithColor() + " &7▏&2 " + Team.ALLY_COLOR.toString()), "");
                        } else if (ArcherClass.getMarkedPlayers().containsKey(toRefresh.getName()) && ArcherClass.getMarkedPlayers().get(toRefresh.getName()) > System.currentTimeMillis()) {
                            nametagInfo = createNametag(toRefresh, toC(playerISeee.getDTRWithColor() + " &7▏&2 " + Hulu.getInstance().getServerHandler().getArcherTagColor().toString()), "");
                        } else {
                            nametagInfo = createNametag(toRefresh, toC(playerISeee.getDTRWithColor() + " &7▏&2 ") + defColor, "");
                        }
                    }
                }
            }
        }


        // If we already found something above they override these, otherwise we can do these checks.
        if (nametagInfo == null) {
            if (ArcherClass.getMarkedPlayers().containsKey(toRefresh.getName()) && ArcherClass.getMarkedPlayers().get(toRefresh.getName()) > System.currentTimeMillis()) {
                nametagInfo = createNametag(toRefresh, Hulu.getInstance().getServerHandler().getArcherTagColor().toString(), "");
            } else if (viewerTeam != null && viewerTeam.getFocused() != null && viewerTeam.getFocused().equals(toRefresh.getUniqueId())) {
                nametagInfo = createNametag(toRefresh, ChatColor.LIGHT_PURPLE.toString(), "");
            }
            if (toRefresh.hasMetadata("ModMode")) {
                nametagInfo = createNametag(refreshFor, toC("&b&L✦&9 "), "");
            }
            if (toRefresh.hasMetadata("frozen")) {
                nametagInfo = createNametag(refreshFor, toC("&7[&4&lF&7] "), "");
            }
        }

        // You always see yourself as green.
        if (refreshFor == toRefresh) {
            nametagInfo = createNametag(toRefresh, ChatColor.DARK_GREEN.toString(), "");
        }


        // If nothing custom was set, fall back on yellow.
        return (nametagInfo == null ? createNametag(toRefresh, Hulu.getInstance().getServerHandler().getDefaultRelationColor().toString(), "") : nametagInfo);
    }

    private NametagInfo createNametag(Player displayed, String prefix, String suffix) {
        return createNametag(prefix, suffix);
    }

    private String toC(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}