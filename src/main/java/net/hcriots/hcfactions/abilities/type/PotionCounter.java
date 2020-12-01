/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.abilities.type;

import net.hcriots.hcfactions.abilities.Ability;
import net.hcriots.hcfactions.util.Color;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;

public class PotionCounter extends Ability {

    public static Predicate<ItemStack> INSTANT_HEALTH = itemStack -> {
        if (itemStack.getType() != Material.POTION) {
            return false;
        }

        PotionType potionType = Potion.fromItemStack(itemStack).getType();
        return potionType == PotionType.INSTANT_HEAL;
    };
    private final HashMap<Player, Integer> hits = new HashMap<>();

    public static int countStacksMatching(ItemStack[] items, Predicate<ItemStack> predicate) {
        if (items == null) {
            return 0;
        }

        int amountMatching = 0;

        for (ItemStack item : items) {
            if (item != null && predicate.test(item)) {
                amountMatching++;
            }
        }

        return amountMatching;
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public Material getMaterial() {
        return Material.STICK;
    }

    @Override
    public String getDisplayName() {
        return ChatColor.RED.toString() + ChatColor.BOLD + "Potion Counter";
    }

    @Override
    public List<String> getLore() {
        List<String> toReturn = new ArrayList();
        toReturn.add("&7Hit a player 3 times to");
        toReturn.add("&7check their potions!");
        return toReturn;
    }

    @Override
    public long getCooldown() {
        return (long) (60.0 * 1000L);
    }

    @EventHandler
    public void onbeanerattack(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        if (!(event.getEntity() instanceof Player)) return;

        Player attacker = (Player) event.getDamager();
        Player attacked = (Player) event.getEntity();

        if (!(attacker.getItemInHand().getType() == getMaterial())) return;
        if (!(attacker.getItemInHand().getItemMeta().getLore().contains(Color.translate("&7Hit a player 3 times to check their potions!"))))
            return;

        if (this.hasCooldown(attacker)) {
            this.sendCooldownMessage(attacker);
            event.setCancelled(true);
            return;
        }

        hits.putIfAbsent(attacker, 0);
        hits.put(attacker, hits.get(attacker) + 1);

        if (hits.get(attacker) == 3) {


            this.applyCooldown(attacker);
            int total = countStacksMatching(attacked.getInventory().getContents(), INSTANT_HEALTH);
            attacker.sendMessage(Color.translate("&c" + attacked.getName() + " &fhas &c" + total + " &fpotions left!"));
        }
    }
}
