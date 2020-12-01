/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.abilities;

import cc.fyre.stark.Stark;
import cc.fyre.stark.engine.command.Command;
import cc.fyre.stark.engine.command.data.parameter.Param;
import cc.fyre.stark.util.ClassUtils;
import lombok.Getter;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.abilities.parameter.AbilityParameter;
import net.hcriots.hcfactions.team.Team;
import net.hcriots.hcfactions.util.Cooldown;
import net.minecraft.util.com.google.common.collect.HashBasedTable;
import net.minecraft.util.com.google.common.collect.Table;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AbilityHandler {

    @Getter
    private final Map<String, Ability> abilities = new HashMap<>();

    @Getter
    private final Table<UUID, Ability, Cooldown> cooldown = HashBasedTable.create();

    public AbilityHandler() {

        for (Class<?> clazz : ClassUtils.getClassesInPackage(Hulu.getInstance(), "net.hcriots.hcfactions.abilities.type")) {

            if (!Ability.class.isAssignableFrom(clazz)) {
                continue;
            }

            try {

                final Ability ability = (Ability) clazz.newInstance();

                this.abilities.put(ability.getName(), ability);
            } catch (InstantiationException | IllegalAccessException ex) {
                ex.printStackTrace();
            }
        }

        Stark.instance.getCommandHandler().registerParameterType(Ability.class, new AbilityParameter());
        Stark.instance.getCommandHandler().registerClass(this.getClass());
    }

    @Command(names = {"ability"}, permission = "hulu.command.ability")
    public static void execute(CommandSender sender, @Param(name = "ability") Ability ability, @Param(name = "amount", defaultValue = "1") int amount, @Param(name = "player", defaultValue = "self") Player target) {
        final ItemStack itemStack = ability.getItemStack();

        itemStack.setAmount(amount);

        target.getInventory().addItem(itemStack);

    }

    public Ability fromName(String name) {
        return this.abilities.values().stream().filter(ability -> ability.getName().equalsIgnoreCase(name)).findAny().orElse(null);
    }

    public void applyCooldown(Ability ability, Player player) {

        if (ability.getCooldown() <= 0) {
            return;
        }
        Team team = Hulu.getInstance().getTeamHandler().getTeam(player);
        if (team != null) {
            if (team.getAbilityCooldownUpgrade() >= 2) {
                int level = team.getAbilityCooldownUpgrade();
                this.cooldown.put(player.getUniqueId(), ability, new Cooldown((ability.getCooldown() / level)));
            } else {
                this.cooldown.put(player.getUniqueId(), ability, new Cooldown(ability.getCooldown()));
            }
        } else {
            this.cooldown.put(player.getUniqueId(), ability, new Cooldown(ability.getCooldown()));
        }
//        int cooldownInt = (int) (ability.getCooldown() / 1000);
//        CheatBreakerAPI.getInstance().sendCooldown(player, new CBCooldown(ability.getName() + " Ability", cooldownInt, TimeUnit.MINUTES, Material.NETHER_STAR));
    }

    public boolean hasCooldown(Ability ability, Player player) {

        if (!this.cooldown.contains(player.getUniqueId(), ability)) {
            return false;
        }

        return !this.cooldown.get(player.getUniqueId(), ability).hasExpired();
    }

    public long getRemaining(Ability ability, Player player) {
        return this.cooldown.get(player.getUniqueId(), ability).getRemaining();
    }

}
