/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.killtheking.menu;

import cc.fyre.stark.engine.menu.Button;
import cc.fyre.stark.engine.menu.Menu;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.killtheking.menu.button.HostButton;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * Created by InspectMC
 * Date: 7/12/2020
 * Time: 2:35 AM
 */

@AllArgsConstructor
public class HostMenu extends Menu {

    {
        setPlaceholder(true);
        setAutoUpdate(true);

    }

    @Override
    public String getTitle(Player player) {
        boolean value = Hulu.getInstance().getLanguageMap().isNewLanguageToggle(player.getUniqueId());
//        return (value ? ChatColor.GREEN + "English" : ChatColor.RED + "Español") + ChatColor.YELLOW + ".");
        return (value ? ChatColor.RED + "Select an event..." : ChatColor.RED + "Seleccione un evento");
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = Maps.newHashMap();
        buttons.put(1, Host.DIAMOND.toButton());
        buttons.put(4, new HostButton());
        buttons.put(7, Host.SUMO.toButton());
        return buttons;
    }
}