/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.team.menu.button;

import cc.fyre.stark.engine.menu.Button;
import lombok.AllArgsConstructor;
import net.hcriots.hcfactions.commands.TeamManageCommand;
import net.hcriots.hcfactions.team.Team;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class RenameButton extends Button {

    private final Team team;

    @Override
    public void clicked(Player player, int i, ClickType clickType) {
        player.closeInventory();
        TeamManageCommand.renameTeam(player, team);
    }

    @Override
    public String getName(Player player) {
        return "§eRename Team";
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
        return Material.SIGN;
    }
}
