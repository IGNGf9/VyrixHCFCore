/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.language;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.language.menu.button.LanguageButton;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Collection;

/**
 * Created by InspectMC
 * Date: 7/27/2020
 * Time: 10:57 PM
 */

@AllArgsConstructor
public enum Language {

    LANGUAGE(
            ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Server Language",
            ImmutableList.of(
                    ChatColor.GRAY.toString() + ChatColor.ITALIC + "Do you want to see chat",
                    ChatColor.GRAY.toString() + ChatColor.ITALIC + "English or Español"
            ),
            Material.SIGN,
            ChatColor.YELLOW + "English chat",
            ChatColor.YELLOW + "Español chat",
            true
    ) {
        @Override
        public void toggle(Player player) {
            boolean value = !Hulu.getInstance().getLanguageMap().isNewLanguageToggle(player.getUniqueId());

            Hulu.getInstance().getLanguageMap().setNewLanguage(player.getUniqueId(), value);
            if (Hulu.getInstance().getLanguageMap().isNewLanguageToggle(player.getUniqueId())) {
                player.sendMessage(ChatColor.YELLOW + "Your chat is now " + (value ? ChatColor.GREEN + "English" : ChatColor.RED + "Español") + ChatColor.YELLOW + ".");
            } else {
                player.sendMessage(ChatColor.YELLOW + "Tu chat está ahora en " + ChatColor.GREEN + "Español");
            }
        }

        @Override
        public boolean isEnabled(Player player) {
            return Hulu.getInstance().getLanguageMap().isNewLanguageToggle(player.getUniqueId());
        }
    };

    @Getter
    private final String name;
    @Getter
    private final Collection<String> description;
    @Getter
    private final Material icon;
    @Getter
    private final String enabledText;
    @Getter
    private final String disabledText;
    private final boolean defaultValue;

    // Using @Getter means the method would be 'isDefaultValue',
    // which doesn't correctly represent this variable.
    public boolean getDefaultValue() {
        return (defaultValue);
    }

    public LanguageButton toButton() {
        return new LanguageButton(this);
    }

    public abstract void toggle(Player player);

    public abstract boolean isEnabled(Player player);

}
