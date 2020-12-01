/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.bosses.menu;

import cc.fyre.stark.engine.menu.Button;
import cc.fyre.stark.engine.menu.Menu;
import net.hcriots.hcfactions.bosses.menu.button.DaggerButton;
import net.hcriots.hcfactions.bosses.menu.button.GoliathButton;
import net.hcriots.hcfactions.bosses.menu.button.ReaperButton;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by InspectMC
 * Date: 7/21/2020
 * Time: 2:31 PM
 */
public class BossEggsMenu extends Menu {

    {
        setPlaceholder(true);
        setAutoUpdate(true);

    }

    @Override
    public String getTitle(Player player) {
        return ChatColor.DARK_GRAY + "Boss Eggs";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        buttons.put(2, new DaggerButton());
        buttons.put(4, new GoliathButton());
        buttons.put(6, new ReaperButton());

        return buttons;
    }
}
