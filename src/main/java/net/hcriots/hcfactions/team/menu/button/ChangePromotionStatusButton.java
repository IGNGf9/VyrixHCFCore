/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.team.menu.button;

import cc.fyre.stark.Stark;
import cc.fyre.stark.engine.menu.Button;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.hcriots.hcfactions.team.Team;
import net.hcriots.hcfactions.team.menu.ConfirmMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class ChangePromotionStatusButton extends Button {

    @NonNull
    private final UUID uuid;
    @NonNull
    private final Team team;
    @NonNull
    private final boolean promote;

    @Override
    public String getName(Player player) {
        return promote ? "§aPromote §e" + Stark.instance.getCore().getUuidCache().name(uuid) : "§cDemote §e" + Stark.instance.getCore().getUuidCache().name(uuid);
    }

    @Override
    public List<String> getDescription(Player player) {
        ArrayList<String> lore = new ArrayList<>();
        lore.add(promote ? "§eClick to promote §b" + Stark.instance.getCore().getUuidCache().name(uuid) + "§e to captain" : "§eClick to demote §b" + Stark.instance.getCore().getUuidCache().name(uuid) + "§e to member");
        return lore;
    }

    @Override
    public byte getDamageValue(Player player) {
        return (byte) 3;
    }

    @Override
    public Material getMaterial(Player player) {
        return Material.SKULL_ITEM;
    }

    @Override
    public void clicked(Player player, int i, ClickType clickType) {
        if (promote) {
            String newRank;
            if (team.isCaptain(uuid)) {
                newRank = "co-leader";
            } else {
                newRank = "captain";
            }
            new ConfirmMenu("Make " + Stark.instance.getCore().getUuidCache().name(uuid) + " " + newRank + "?", (b) -> {
                if (b) {
                    if (team.isCaptain(uuid)) {
                        team.removeCaptain(uuid);
                        team.addCoLeader(uuid);
                    } else {
                        team.addCaptain(uuid);
                    }
                    Player bukkitPlayer = Bukkit.getPlayer(uuid);

                    if (bukkitPlayer != null && bukkitPlayer.isOnline()) {
                        bukkitPlayer.sendMessage(ChatColor.YELLOW + "A staff member has made you a §a" + newRank + " §eof your team.");
                    }

                    player.sendMessage(ChatColor.YELLOW + Stark.instance.getCore().getUuidCache().name(uuid) + " has been made a " + newRank + " of " + team.getName() + ".");
                }
            }).openMenu(player);
        } else {
            new ConfirmMenu("Make " + Stark.instance.getCore().getUuidCache().name(uuid) + " member?", (b) -> {
                if (b) {
                    team.removeCaptain(uuid);
                    team.removeCoLeader(uuid);

                    Player bukkitPlayer = Bukkit.getPlayer(uuid);

                    if (bukkitPlayer != null && bukkitPlayer.isOnline()) {
                        bukkitPlayer.sendMessage(ChatColor.YELLOW + "A staff member has made you a §bmember §eof your team.");
                    }

                    player.sendMessage(ChatColor.YELLOW + Stark.instance.getCore().getUuidCache().name(uuid) + " has been made a member of " + team.getName() + ".");
                }
            }).openMenu(player);
        }
    }
}
