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
import net.hcriots.hcfactions.team.commands.ForceLeaderCommand;
import net.hcriots.hcfactions.team.menu.ConfirmMenu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class MakeLeaderButton extends Button {

    @NonNull
    private final UUID uuid;
    @NonNull
    private final Team team;

    @Override
    public String getName(Player player) {
        return (team.isOwner(uuid) ? "§a§l" : "§7") + Stark.instance.getCore().getUuidCache().name(uuid);
    }

    @Override
    public List<String> getDescription(Player player) {
        ArrayList<String> lore = new ArrayList<>();

        if (team.isOwner(uuid)) {
            lore.add("§aThis player is already the leader!");
        } else {
            lore.add("§eClick to change §b" + team.getName() + "§b's§e leader");
            lore.add("§eto §6" + Stark.instance.getCore().getUuidCache().name(uuid));
        }

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
        if (team.isOwner(uuid)) {
            player.sendMessage(ChatColor.RED + "That player is already the leader!");
            return;
        }

        new ConfirmMenu("Make " + Stark.instance.getCore().getUuidCache().name(uuid) + " leader?", (b) -> {
            if (b) {
                ForceLeaderCommand.forceLeader(player, uuid);

            }
        }).openMenu(player);


    }


}
