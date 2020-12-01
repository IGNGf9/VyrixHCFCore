/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.killtheking.menu;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.hcriots.hcfactions.killtheking.menu.button.TournamentButton;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Collection;

/**
 * Created by InspectMC
 * Date: 8/3/2020
 * Time: 9:06 PM
 */
@AllArgsConstructor
public enum Host {

    DIAMOND(ChatColor.DARK_RED + "FFA Diamond Event",
            ImmutableList.of(
                    ChatColor.GRAY.toString() + ChatColor.ITALIC + "Event Type: " + ChatColor.WHITE + "PvP",
                    ChatColor.GRAY.toString() + ChatColor.ITALIC + "Max Players: " + ChatColor.WHITE + "25",
                    ChatColor.GRAY.toString() + ChatColor.ITALIC + "",
                    ChatColor.GRAY.toString() + ChatColor.ITALIC + "Friendly Fire: " + ChatColor.GREEN + "enabled",
                    ChatColor.GRAY.toString() + ChatColor.ITALIC + "DTR Loss: " + ChatColor.RED + "disabled"
            ),
            Material.DIAMOND_SWORD, true) {
        @Override
        public void toggle(Player player) {
            player.chat("/tournament create 25 diamond");

        }
    },

    SUMO(ChatColor.DARK_RED + "Sumo Event",
            ImmutableList.of(
                    ChatColor.GRAY.toString() + ChatColor.ITALIC + "Event Type: " + ChatColor.WHITE + "Sumo",
                    ChatColor.GRAY.toString() + ChatColor.ITALIC + "Max Players: " + ChatColor.WHITE + "25",
                    ChatColor.GRAY.toString() + ChatColor.ITALIC + "",
                    ChatColor.GRAY.toString() + ChatColor.ITALIC + "Friendly Fire: " + ChatColor.GREEN + "enabled",
                    ChatColor.GRAY.toString() + ChatColor.ITALIC + "DTR Loss: " + ChatColor.RED + "disabled"
            ),
            Material.STICK, true) {
        @Override
        public void toggle(Player player) {
            player.chat("/tournament create 25 sumo");

        }
    };


    @Getter
    private final String name;
    @Getter
    private final Collection<String> description;
    @Getter
    private final Material icon;
    private final boolean defaultValue;

    // Using @Getter means the method would be 'isDefaultValue',
    // which doesn't correctly represent this variable.
    public boolean getDefaultValue() {
        return (defaultValue);
    }

    public TournamentButton toButton() {
        return new TournamentButton(this);
    }

    public abstract void toggle(Player player);

}