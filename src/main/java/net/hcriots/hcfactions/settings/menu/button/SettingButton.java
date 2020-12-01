/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.settings.menu.button;

import cc.fyre.stark.engine.menu.Button;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.settings.Setting;
import net.hcriots.hcfactions.tab.TabListMode;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.List;

@AllArgsConstructor
public class SettingButton extends Button {

    private final Setting setting;

    public static TabListMode next(TabListMode current) {
        switch (current) {
            case DETAILED:
                return TabListMode.DETAILED_WITH_FACTION_INFO;
            case DETAILED_WITH_FACTION_INFO:
                return TabListMode.DETAILED;
            default:
                return TabListMode.DETAILED;
        }
    }

    @Override
    public String getName(Player player) {
        return setting.getName();
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> description = Lists.newArrayList();

        description.add("");
        description.addAll(setting.getDescription());
        description.add("");
        if (setting != Setting.TAB_LIST) {
            if (setting.isEnabled(player)) {
                description.add(ChatColor.BLUE.toString() + ChatColor.BOLD + "  ► " + setting.getEnabledText());
                description.add("    " + setting.getDisabledText());
            } else {
                description.add("    " + setting.getEnabledText());
                description.add(ChatColor.BLUE.toString() + ChatColor.BOLD + "  ► " + setting.getDisabledText());
            }
        } else {
            TabListMode current = Hulu.getInstance().getTabListModeMap().getTabListMode(player.getUniqueId());
            for (TabListMode mode : TabListMode.values()) {
                if (mode != current) {
                    description.add("    " + ChatColor.LIGHT_PURPLE + mode.getName());
                } else {
                    description.add(ChatColor.BLUE.toString() + ChatColor.BOLD + "  ► " + ChatColor.LIGHT_PURPLE + mode.getName());
                }
            }
        }
        return description;
    }

    @Override
    public Material getMaterial(Player player) {
        return setting.getIcon();
    }

    @Override
    public byte getDamageValue(Player player) {
        return 0;
    }

    @Override
    public void clicked(Player player, int i, ClickType clickType) {
        setting.toggle(player);
    }
}
