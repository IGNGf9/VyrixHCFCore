/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.bosses.eggs;

import cc.fyre.stark.Stark;
import cc.fyre.stark.engine.command.Command;
import cc.fyre.stark.engine.command.data.parameter.Param;
import cc.fyre.stark.util.ClassUtils;
import lombok.Getter;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.bosses.eggs.parameter.BossEggAbilityParameter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by InspectMC
 * Date: 7/20/2020
 * Time: 8:52 PM
 */

public class BossEggHandler {

    @Getter
    private final Map<String, BossEggAbility> eggability = new HashMap<>();

    public BossEggHandler() {

        for (Class<?> clazz : ClassUtils.getClassesInPackage(Hulu.getInstance(), "net.hcriots.hcfactions.bosses.eggs.type")) {

            if (!BossEggAbility.class.isAssignableFrom(clazz)) {
                continue;
            }

            try {

                final BossEggAbility bossEggAbility = (BossEggAbility) clazz.newInstance();

                this.eggability.put(bossEggAbility.getName(), bossEggAbility);
            } catch (InstantiationException | IllegalAccessException ex) {
                ex.printStackTrace();
            }
        }

        Stark.instance.getCommandHandler().registerParameterType(BossEggAbility.class, new BossEggAbilityParameter());
        Stark.instance.getCommandHandler().registerClass(this.getClass());
    }

    @Command(names = {"bossegg"}, permission = "hulu.command.bossegg")
    public static void execute(CommandSender sender, @Param(name = "boss") BossEggAbility bossEggAbility, @Param(name = "amount", defaultValue = "1") int amount, @Param(name = "player", defaultValue = "self") Player target) {
        final ItemStack itemStack = bossEggAbility.getItemStack();

        itemStack.setAmount(amount);

        target.getInventory().addItem(itemStack);
    }

    public BossEggAbility fromName(String name) {
        return this.eggability.values().stream().filter(bossEggAbility -> bossEggAbility.getName().equalsIgnoreCase(name)).findAny().orElse(null);
    }

}