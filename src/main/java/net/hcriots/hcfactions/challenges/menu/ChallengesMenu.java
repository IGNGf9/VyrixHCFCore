/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.challenges.menu;

import cc.fyre.stark.engine.menu.Button;
import cc.fyre.stark.engine.menu.Menu;
import net.hcriots.hcfactions.challenges.buttons.TierOneButton;
import net.hcriots.hcfactions.challenges.buttons.TierThreeButton;
import net.hcriots.hcfactions.challenges.buttons.TierTwoButton;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class ChallengesMenu extends Menu {

    {
        setPlaceholder(true);
        setAutoUpdate(true);
    }

    @Override
    public String getTitle(Player player) {
        return ChatColor.GOLD + "Challenges";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        buttons.put(2, new TierOneButton());
        buttons.put(4, new TierTwoButton());
        buttons.put(6, new TierThreeButton());

        return buttons;
    }
}
