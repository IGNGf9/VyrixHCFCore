/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.editor;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.hcriots.hcfactions.editor.button.EditorButton;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Collection;

/**
 * Created by InspectMC
 * Date: 7/29/2020
 * Time: 12:34 AM
 */

@AllArgsConstructor
public enum Editor {

    PvP(ChatColor.AQUA + "PvP",
            ImmutableList.of(
                    ChatColor.GRAY + "Click to edit your PvP Kit"),
            Material.DIAMOND_CHESTPLATE, true) {
        @Override
        public void toggle(Player player) {
            player.chat("/kiteditor PvP");

        }
    },

    BARD(ChatColor.GOLD + "Bard",
            ImmutableList.of(
                    ChatColor.GRAY + "Click to edit your Bard Kit"),
            Material.GOLD_CHESTPLATE, true) {
        @Override
        public void toggle(Player player) {
            player.chat("/kiteditor Bard");

        }
    },

    ARCHER(ChatColor.LIGHT_PURPLE + "Archer",
            ImmutableList.of(
                    ChatColor.GRAY + "Click to edit your Archer Kit"),
            Material.LEATHER_CHESTPLATE, true) {
        @Override
        public void toggle(Player player) {
            player.chat("/kiteditor Archer");

        }
    },

    BUILDER(ChatColor.GREEN + "Builder",
            ImmutableList.of(
                    ChatColor.GRAY + "Click to edit your Builder Kit"),
            Material.IRON_CHESTPLATE, true) {
        @Override
        public void toggle(Player player) {
            player.chat("/kiteditor Builder");

        }
    },

    ROGUE(ChatColor.DARK_RED + "Rogue",
            ImmutableList.of(
                    ChatColor.GRAY + "Click to edit your Rogue Kit"),
            Material.CHAINMAIL_CHESTPLATE, true) {
        @Override
        public void toggle(Player player) {
            player.chat("/kiteditor Rogue");

        }
    },

    MAGE(ChatColor.DARK_PURPLE + "Mage",
            ImmutableList.of(
                    ChatColor.GRAY + "Click to edit your Mage Kit"),
            Material.MAGMA_CREAM, true) {
        @Override
        public void toggle(Player player) {
            player.chat("/kiteditor Mage");

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

    public EditorButton toButton() {
        return new EditorButton(this);
    }

    public abstract void toggle(Player player);

}