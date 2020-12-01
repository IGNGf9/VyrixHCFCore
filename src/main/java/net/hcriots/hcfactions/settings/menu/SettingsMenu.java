/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.settings.menu;

import cc.fyre.stark.engine.menu.Button;
import cc.fyre.stark.engine.menu.Menu;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import net.hcriots.hcfactions.settings.Setting;
import org.bukkit.entity.Player;

import java.util.Map;

@AllArgsConstructor
public class SettingsMenu extends Menu {

    {
        setUpdateAfterClick(true);
        setAutoUpdate(true);
    }

    @Override
    public String getTitle(Player player) {
        return "Your Settings";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = Maps.newHashMap();
        // I'm going to manually set the positions for now as we only have three options to make it pretty,
        // but once we add more we should probably use a for loop to add the buttons.
        buttons.put(1, Setting.FOUND_DIAMONDS.toButton());
        buttons.put(3, Setting.DEATH_MESSAGES.toButton());
        buttons.put(5, Setting.PUBLIC_CHAT.toButton());
        buttons.put(7, Setting.CLAIM_SHOW.toButton());
        buttons.put(11, Setting.TIPS.toButton());
//        buttons.put(13, Setting.COOLDOWN.toButton());
        buttons.put(13, Setting.DTR.toButton());
        buttons.put(15, Setting.TAB_LIST.toButton());

        return buttons;
    }

}
