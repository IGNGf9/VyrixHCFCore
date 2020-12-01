/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.abilities.parameter;

import cc.fyre.stark.engine.command.data.parameter.ParameterType;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.abilities.Ability;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AbilityParameter implements ParameterType<Ability> {

    @Override
    public Ability transform(CommandSender commandSender, String s) {

        final Ability toReturn = Hulu.getInstance().getAbilityHandler().fromName(s);

        if (toReturn == null) {
            commandSender.sendMessage(ChatColor.RED + "Ability " + ChatColor.YELLOW + s + ChatColor.RED + " not found.");
            return null;
        }

        return toReturn;
    }

    @Override
    public List<String> tabComplete(Player sender, Set<String> flags, String source) {
        return Hulu.getInstance().getAbilityHandler().getAbilities().values().stream().filter(ability -> StringUtils.startsWithIgnoreCase(ability.getName(), source)).map(Ability::getName).collect(Collectors.toList());
    }
}