/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.challenges.menu;

import cc.fyre.stark.engine.menu.Button;
import cc.fyre.stark.engine.menu.Menu;
import net.hcriots.hcfactions.challenges.ChallengeTypes;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class TierTwoMenu extends Menu {

    {
        setPlaceholder(true);
        setAutoUpdate(true);
    }

    @Override
    public String getTitle(Player player) {
        return ChatColor.YELLOW + "Tier 2 Challenges";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        for (ChallengeTypes challengeType : ChallengeTypes.values()) {
            if (challengeType.getChallengeTier() == 2) {
                buttons.put(buttons.size(), Button.fromItem(challengeType.getCusomItem(player)));
            }
        }
        return buttons;
    }
}
