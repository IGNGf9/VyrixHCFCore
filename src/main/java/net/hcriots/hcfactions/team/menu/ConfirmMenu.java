/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.team.menu;

import cc.fyre.stark.engine.menu.Button;
import cc.fyre.stark.engine.menu.Menu;
import cc.fyre.stark.util.Callback;
import lombok.AllArgsConstructor;
import net.hcriots.hcfactions.team.menu.button.BooleanButton;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class ConfirmMenu extends Menu {

    private final String title;
    private final Callback<Boolean> response;

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = new HashMap<>();

        for (int i = 0; i < 9; i++) {
            if (i == 3) {
                buttons.put(i, new BooleanButton(true, response));

            } else if (i == 5) {
                buttons.put(i, new BooleanButton(false, response));

            } else {
                buttons.put(i, Button.placeholder(Material.STAINED_GLASS_PANE, (byte) 14));

            }
        }

        return buttons;
    }

    @Override
    public String getTitle(Player player) {
        return title;
    }
}
