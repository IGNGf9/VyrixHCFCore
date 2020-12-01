/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.team.upgrades.menu;

import cc.fyre.stark.engine.menu.Button;
import cc.fyre.stark.engine.menu.Menu;
import lombok.AllArgsConstructor;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.team.Team;
import net.hcriots.hcfactions.team.upgrades.TeamUpgrade;
import net.hcriots.hcfactions.team.upgrades.impl.ClaimEffectTeamUpgrade;
import net.hcriots.hcfactions.team.upgrades.menu.button.PurchaseButton;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by InspectMC
 * Date: 7/16/2020
 * Time: 9:29 PM
 */

@AllArgsConstructor
public class CategoryUpgradesMenu extends Menu {

    private final String title;
    private final List<ClaimEffectTeamUpgrade> upgrades;

    @Override
    public String getTitle(Player player) {
        return title;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Team team = Hulu.getInstance().getTeamHandler().getTeam(player);
        Map<Integer, Button> buttons = new HashMap<>();

        for (TeamUpgrade upgrade : upgrades) {
            buttons.put(buttons.size(), new PurchaseButton(team, upgrade));
        }

        return buttons;
    }

    public boolean isUpdateAfterClick() {
        return true;
    }

}
