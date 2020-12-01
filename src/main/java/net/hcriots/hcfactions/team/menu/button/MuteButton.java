/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.team.menu.button;

import cc.fyre.stark.engine.menu.Button;
import lombok.AllArgsConstructor;
import net.hcriots.hcfactions.team.Team;
import net.hcriots.hcfactions.team.commands.team.TeamShadowMuteCommand;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class MuteButton extends Button {

    private final int minutes;
    private final Team team;

    @Override
    public void clicked(Player player, int i, ClickType clickType) {
        TeamShadowMuteCommand.teamShadowMute(player, team, minutes);
    }

    @Override
    public String getName(Player player) {
        return "§e" + minutes + "m mute";
    }

    @Override
    public List<String> getDescription(Player player) {
        return new ArrayList<>();
    }

    @Override
    public byte getDamageValue(Player player) {
        return (byte) 0;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        ItemStack it = new ItemStack(getMaterial(player));
        ItemMeta im = it.getItemMeta();

        im.setLore(getDescription(player));
        im.setDisplayName(getName(player));
        it.setItemMeta(im);
        it.setAmount(minutes);

        return it;
    }

    @Override
    public Material getMaterial(Player player) {
        return Material.CHEST;
    }
}
