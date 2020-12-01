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
import net.hcriots.hcfactions.team.commands.ForceKickCommand;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class KickPlayerButton extends Button {

    @NonNull
    private final UUID uuid;
    @NonNull
    private final Team team;

    @Override
    public String getName(Player player) {
        return "§cKick §e" + Stark.instance.getCore().getUuidCache().name(uuid);
    }

    @Override
    public List<String> getDescription(Player player) {
        ArrayList<String> lore = new ArrayList<>();

        if (team.isOwner(uuid)) {
            lore.add("§e§lLeader");
        } else if (team.isCoLeader(uuid)) {
            lore.add("§e§lCo-Leader");
        } else if (team.isCaptain(uuid)) {
            lore.add("§aCaptain");
        } else {
            lore.add("§7Member");
        }

        lore.add("");
        lore.add("§eClick to kick §b" + Stark.instance.getCore().getUuidCache().name(uuid) + "§e from team.");

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
        ForceKickCommand.forceKick(player, uuid);
    }


}
