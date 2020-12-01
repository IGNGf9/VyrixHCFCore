/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.language.menu;

import cc.fyre.stark.engine.menu.Button;
import cc.fyre.stark.engine.menu.Menu;
import com.google.common.collect.Maps;
import net.hcriots.hcfactions.language.Language;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * Created by InspectMC
 * Date: 7/27/2020
 * Time: 11:01 PM
 */

public class LanguageMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return "Select a language";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = Maps.newHashMap();
        buttons.put(4, Language.LANGUAGE.toButton());
        return buttons;
    }

}
