/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.team.menu.button;

import cc.fyre.stark.engine.menu.Button;
import lombok.AllArgsConstructor;
import net.hcriots.hcfactions.team.Team;
import net.hcriots.hcfactions.team.commands.ForceDisbandCommand;
import net.hcriots.hcfactions.team.menu.ConfirmMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class DisbandTeamButton extends Button {

    private final Team team;

    @Override
    public void clicked(Player player, int i, ClickType clickType) {
        new ConfirmMenu("Disband?", (b) -> {
            if (b) {
                ForceDisbandCommand.forceDisband(player, team);
            }
        }).openMenu(player);
    }

    @Override
    public String getName(Player player) {
        return "§c§lDisband Team";
    }

    @Override
    public List<String> getDescription(Player player) {
        return new ArrayList<>();
    }

    @Override
    public byte getDamageValue(Player player) {
        return 0;
    }

    @Override
    public Material getMaterial(Player player) {
        return Material.TNT;
    }
}
