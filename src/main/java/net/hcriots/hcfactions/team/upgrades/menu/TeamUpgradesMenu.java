/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.team.upgrades.menu;

import cc.fyre.stark.engine.menu.Button;
import cc.fyre.stark.engine.menu.Menu;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.team.Team;
import net.hcriots.hcfactions.team.upgrades.TeamUpgrade;
import net.hcriots.hcfactions.team.upgrades.menu.button.CategoryButton;
import net.hcriots.hcfactions.team.upgrades.menu.button.PurchaseButton;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by InspectMC
 * Date: 7/16/2020
 * Time: 9:35 PM
 */

public class TeamUpgradesMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return ChatColor.GOLD + "Team Upgrades";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Team team = Hulu.getInstance().getTeamHandler().getTeam(player);
        Map<Integer, Button> buttons = new HashMap<>();

        for (TeamUpgrade upgrade : TeamUpgrade.upgrades.values()) {
            buttons.put(buttons.size(), upgrade.isCategory() ? new CategoryButton(upgrade) : new PurchaseButton(team, upgrade));
        }

        return buttons;
    }

    public boolean isUpdateAfterClick() {
        return true;
    }

}
