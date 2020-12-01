/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.settings;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.settings.menu.button.SettingButton;
import net.hcriots.hcfactions.tab.TabListMode;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Collection;

@AllArgsConstructor
public enum Setting {

    DTR(
            ChatColor.BLUE + "DTR",
            ImmutableList.of(
                    ChatColor.GRAY.toString() + ChatColor.ITALIC + "Toggle DTR"
            ),
            Material.NAME_TAG,
            ChatColor.LIGHT_PURPLE + "Show dtr in nametags",
            ChatColor.LIGHT_PURPLE + "Hide dtr in nametags",
            true
    ) {
        @Override
        public void toggle(Player player) {
            boolean value = !Hulu.getInstance().getDtrMap().isDtrToggled(player.getUniqueId());

            Hulu.getInstance().getDtrMap().setToggled(player.getUniqueId(), value);
            player.sendMessage(ChatColor.YELLOW + "You have " + (value ? ChatColor.GREEN + "enabled" : ChatColor.RED + "disabled") + ChatColor.YELLOW + " dtr nametags.");
        }

        @Override
        public boolean isEnabled(Player player) {
            return Hulu.getInstance().getDtrMap().isDtrToggled(player.getUniqueId());
        }
    },

    PUBLIC_CHAT(
            ChatColor.BLUE + "Global Chat",
            ImmutableList.of(
                    ChatColor.GRAY.toString() + ChatColor.ITALIC + "Do you want to see players",
                    ChatColor.GRAY.toString() + ChatColor.ITALIC + "chat messages while playing?"
            ),
            Material.SIGN,
            ChatColor.LIGHT_PURPLE + "Show public chat",
            ChatColor.LIGHT_PURPLE + "Hide public chat",
            true
    ) {
        @Override
        public void toggle(Player player) {
            boolean value = !Hulu.getInstance().getToggleGlobalChatMap().isGlobalChatToggled(player.getUniqueId());

            Hulu.getInstance().getToggleGlobalChatMap().setGlobalChatToggled(player.getUniqueId(), value);
            player.sendMessage(ChatColor.YELLOW + "You have " + (value ? ChatColor.GREEN + "enabled" : ChatColor.RED + "disabled") + ChatColor.YELLOW + " global chat messages.");
        }

        @Override
        public boolean isEnabled(Player player) {
            return Hulu.getInstance().getToggleGlobalChatMap().isGlobalChatToggled(player.getUniqueId());
        }
    },

    TAB_LIST(
            ChatColor.BLUE + "Tab List",
            ImmutableList.of(
                    ChatColor.GRAY.toString() + ChatColor.ITALIC + "Do you want to see",
                    ChatColor.GRAY.toString() + ChatColor.ITALIC + "extra info on your",
                    ChatColor.GRAY.toString() + ChatColor.ITALIC + "tab list?"
            ),
            Material.PAINTING,
            ChatColor.LIGHT_PURPLE + "Normal",
            ChatColor.LIGHT_PURPLE + "Normal w/ Team List",
            true
    ) {
        @Override
        public void toggle(Player player) {
            TabListMode mode = SettingButton.next(Hulu.getInstance().getTabListModeMap().getTabListMode(player.getUniqueId()));

            Hulu.getInstance().getTabListModeMap().setTabListMode(player.getUniqueId(), mode);
            player.sendMessage(ChatColor.YELLOW + "You have set your tab to " + ChatColor.LIGHT_PURPLE + mode.getName() + ChatColor.YELLOW + ".");
        }

        @Override
        public boolean isEnabled(Player player) {
            return true;
        }

    },

    CLAIM_SHOW(
            ChatColor.BLUE + "Display Claim On Scoreboard",
            ImmutableList.of(
                    ChatColor.GRAY.toString() + ChatColor.ITALIC + "Do you want to see the claim",
                    ChatColor.GRAY.toString() + ChatColor.ITALIC + "you're on in on your scoreboard"
            ),
            Material.COMPASS,
            ChatColor.LIGHT_PURPLE + "Show claim",
            ChatColor.LIGHT_PURPLE + "Hide claim",
            true
    ) {
        @Override
        public void toggle(Player player) {
            boolean value = !Hulu.getInstance().getToggleClaimMap().isClaimedToggled(player.getUniqueId());

            Hulu.getInstance().getToggleClaimMap().setClaimsToggled(player.getUniqueId(), value);
            player.sendMessage(ChatColor.YELLOW + "You are now " + (value ? ChatColor.GREEN + "seeing" : ChatColor.RED + "hiding") + ChatColor.YELLOW + " the claim scoreboard display.");
        }

        @Override
        public boolean isEnabled(Player player) {
            return Hulu.getInstance().getToggleClaimMap().isClaimedToggled(player.getUniqueId());
        }
    },

    FOUND_DIAMONDS(
            ChatColor.BLUE + "Found Diamonds",
            ImmutableList.of(
                    ChatColor.GRAY.toString() + ChatColor.ITALIC + "Do you want to see",
                    ChatColor.GRAY.toString() + ChatColor.ITALIC + "found-diamonds messages?"
            ),
            Material.DIAMOND,
            ChatColor.LIGHT_PURPLE + "Show messages",
            ChatColor.LIGHT_PURPLE + "Hide messages",
            true
    ) {
        @Override
        public void toggle(Player player) {
            boolean value = !Hulu.getInstance().getToggleFoundDiamondsMap().isFoundDiamondToggled(player.getUniqueId());

            Hulu.getInstance().getToggleFoundDiamondsMap().setFoundDiamondToggled(player.getUniqueId(), value);
            player.sendMessage(ChatColor.YELLOW + "You have " + (value ? ChatColor.GREEN + "enabled" : ChatColor.RED + "disabled") + ChatColor.YELLOW + " found diamond messages.");
        }

        @Override
        public boolean isEnabled(Player player) {
            return Hulu.getInstance().getToggleFoundDiamondsMap().isFoundDiamondToggled(player.getUniqueId());
        }

    },
    DEATH_MESSAGES(
            ChatColor.BLUE + "Death Messages",
            ImmutableList.of(
                    ChatColor.GRAY.toString() + ChatColor.ITALIC + "Do you want to see",
                    ChatColor.GRAY.toString() + ChatColor.ITALIC + "death messages?"
            ),
            Material.SKULL_ITEM,
            ChatColor.LIGHT_PURPLE + "Show messages",
            ChatColor.LIGHT_PURPLE + "Hide messages",
            true
    ) {
        @Override
        public void toggle(Player player) {
            boolean value = !Hulu.getInstance().getToggleDeathMessageMap().areDeathMessagesEnabled(player.getUniqueId());

            Hulu.getInstance().getToggleDeathMessageMap().setDeathMessagesEnabled(player.getUniqueId(), value);
            player.sendMessage(ChatColor.YELLOW + "You have " + (value ? ChatColor.GREEN + "enabled" : ChatColor.RED + "disabled") + ChatColor.YELLOW + " death messages.");
        }

        @Override
        public boolean isEnabled(Player player) {
            return Hulu.getInstance().getToggleDeathMessageMap().areDeathMessagesEnabled(player.getUniqueId());
        }
    },

    TIPS(
            ChatColor.BLUE + "Tips",
            ImmutableList.of(
                    ChatColor.GRAY.toString() + ChatColor.ITALIC + "Do you want to see",
                    ChatColor.GRAY.toString() + ChatColor.ITALIC + "tips messages?"
            ),
            Material.PAPER,
            ChatColor.LIGHT_PURPLE + "Show tips",
            ChatColor.LIGHT_PURPLE + "Hide tips",
            true
    ) {
        @Override
        public void toggle(Player player) {
            boolean value = !Hulu.getInstance().getTipsMap().isTipsToggled(player.getUniqueId());

            Hulu.getInstance().getTipsMap().setToggled(player.getUniqueId(), value);
            player.sendMessage(ChatColor.YELLOW + "You have " + (value ? ChatColor.GREEN + "enabled" : ChatColor.RED + "disabled") + ChatColor.YELLOW + " in-game tips!");
        }

        @Override
        public boolean isEnabled(Player player) {
            return Hulu.getInstance().getTipsMap().isTipsToggled(player.getUniqueId());
        }

    },

    COOLDOWN(
            ChatColor.BLUE + "Cooldowns",
            ImmutableList.of(
                    ChatColor.GRAY.toString() + ChatColor.ITALIC + "Do you want to see",
                    ChatColor.GRAY.toString() + ChatColor.ITALIC + "cooldowns on scoreboard"
            ),
            Material.ENDER_PEARL,
            ChatColor.LIGHT_PURPLE + "Show cooldowns",
            ChatColor.LIGHT_PURPLE + "Hide cooldowns",
            true
    ) {
        @Override
        public void toggle(Player player) {
            boolean value = !Hulu.getInstance().getCooldownsMap().isCooldownToggled(player.getUniqueId());

            Hulu.getInstance().getCooldownsMap().setCooldowns(player.getUniqueId(), value);
            player.sendMessage(ChatColor.YELLOW + "You have " + (value ? ChatColor.GREEN + "enabled" : ChatColor.RED + "disabled") + ChatColor.YELLOW + " cooldowns on scoreboard");
        }

        @Override
        public boolean isEnabled(Player player) {
            return Hulu.getInstance().getCooldownsMap().isCooldownToggled(player.getUniqueId());
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

    public SettingButton toButton() {
        return new SettingButton(this);
    }

    public abstract void toggle(Player player);

    public abstract boolean isEnabled(Player player);

}
