/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.bosses.eggs.parameter;

import cc.fyre.stark.engine.command.data.parameter.ParameterType;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.bosses.eggs.BossEggAbility;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by InspectMC
 * Date: 7/20/2020
 * Time: 9:07 PM
 */
public class BossEggAbilityParameter implements ParameterType<BossEggAbility> {

    @Override
    public BossEggAbility transform(CommandSender commandSender, String s) {

        final BossEggAbility toReturn = Hulu.getInstance().getBossEggHandler().fromName(s);

        if (toReturn == null) {
            commandSender.sendMessage(ChatColor.RED + "Boss Egg " + ChatColor.YELLOW + s + ChatColor.RED + " not found.");
            return null;
        }

        return toReturn;
    }

    @Override
    public List<String> tabComplete(Player sender, Set<String> flags, String source) {
        return Hulu.getInstance().getBossEggHandler().getEggability().values().stream().filter(bossEggAbility -> StringUtils.startsWithIgnoreCase(bossEggAbility.getName(), source)).map(BossEggAbility::getName).collect(Collectors.toList());
    }
}