/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.credit.menu;

import cc.fyre.stark.engine.menu.Button;
import cc.fyre.stark.engine.menu.Menu;
import lombok.AllArgsConstructor;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.credit.menu.button.FireButton;
import net.hcriots.hcfactions.credit.menu.button.GodButton;
import net.hcriots.hcfactions.credit.menu.button.KingCashButton;
import net.hcriots.hcfactions.credit.menu.button.MoneyButton;
import net.hcriots.hcfactions.credit.menu.button.hcf.*;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by InspectMC
 * Date: 6/28/2020
 * Time: 4:59 PM
 */
@AllArgsConstructor
public class CreditMenu extends Menu {

    {
        setPlaceholder(true);
        setAutoUpdate(true);
    }

    @Override
    public String getTitle(Player player) {
        return ChatColor.GOLD + "Charm Shop";
    }


    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        if (Hulu.getInstance().getMapHandler().isKitMap()) {
            buttons.put(3, new GodButton()); // 35 Credits
            buttons.put(4, new FireButton()); // 40 Credits
            buttons.put(5, new MoneyButton()); // 100 Credits
            buttons.put(6, new KingCashButton()); // 100 Credits
        } else {
            buttons.put(2, new LivesButton()); // 10 Credits
            buttons.put(3, new SummerKeyButton()); // 15 Credits
            buttons.put(4, new PartnerPackageButton()); // 25 Credits
            buttons.put(5, new AirdropsButton()); // 30 Credits
            buttons.put(6, new LootboxesButton()); // 35 Credits
            buttons.put(11, new BardGkitButton()); // 40 Credits
            buttons.put(12, new DiamondGKitButton()); // 50 Credits
            buttons.put(13, new MasterGkitButton()); // 70 Credits
            buttons.put(14, new CheckmarkPrefixButton()); // 100 Credits
            buttons.put(15, new ChampionRankButton()); // 120 Credits
            buttons.put(22, new MasterRankButton()); // 150 Credits
        }

        return buttons;
    }
}
