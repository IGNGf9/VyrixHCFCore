/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.language.menu.button;

import cc.fyre.stark.engine.menu.Button;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import net.hcriots.hcfactions.language.Language;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.List;

/**
 * Created by InspectMC
 * Date: 7/27/2020
 * Time: 11:03 PM
 */
@AllArgsConstructor
public class LanguageButton extends Button {

    private final Language language;

    @Override
    public String getName(Player player) {
        return language.getName();
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> description = Lists.newArrayList();

        description.add("");
        description.addAll(language.getDescription());
        description.add("");
        return description;
    }

    @Override
    public Material getMaterial(Player player) {
        return language.getIcon();
    }

    @Override
    public byte getDamageValue(Player player) {
        return 0;
    }

    @Override
    public void clicked(Player player, int i, ClickType clickType) {
        language.toggle(player);
    }


}
