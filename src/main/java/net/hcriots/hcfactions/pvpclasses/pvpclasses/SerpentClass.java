/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.pvpclasses.pvpclasses;

import cc.fyre.stark.core.util.TimeUtils;
import net.hcriots.hcfactions.pvpclasses.PvPClass;
import net.hcriots.hcfactions.pvpclasses.PvPClassHandler;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SerpentClass extends PvPClass {

    private static final Map<String, Long> lastAbilityUse = new HashMap<>();

    public SerpentClass() {
        super("Serpent", 15, null);
    }

    @Override
    public void apply(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1), true);
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0), true);
    }

    @Override
    public void tick(Player player) {
        if (!player.hasPotionEffect(PotionEffectType.SPEED)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
        }

        if (!player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0));

        }
        super.tick(player);
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            Player damager = (Player) event.getDamager();
            Player damagee = (Player) event.getEntity();

            if (!PvPClassHandler.hasKitOn(damager, this)) {
                return;
            }

            if (damager.getItemInHand().getType() == Material.SPIDER_EYE) {
                if (lastAbilityUse.containsKey(damager.getName()) && lastAbilityUse.get(damager.getName()) > System.currentTimeMillis()) {
                    long millisLeft = ((lastAbilityUse.get(damager.getName()) - System.currentTimeMillis()) / 1000L) * 1000L;
                    String msg = TimeUtils.formatIntoDetailedString((int) (millisLeft / 1000));

                    damager.sendMessage(ChatColor.RED + "You cannot use this for another §c§l" + msg + "§c.");
                } else {
                    damager.getItemInHand().setAmount(damager.getItemInHand().getAmount() - 1);
                    damager.updateInventory();
                    lastAbilityUse.put(damager.getName(), System.currentTimeMillis() + (1000L * 30));
                    damagee.addPotionEffects(Arrays.asList(
                            new PotionEffect(PotionEffectType.WITHER, 20 * 5, 1),
                            new PotionEffect(PotionEffectType.SLOW, 20 * 5, 1),
                            new PotionEffect(PotionEffectType.BLINDNESS, 20 * 5, 1)
                    ));
                }
            }
        }
    }

    @Override
    public boolean qualifies(PlayerInventory armor) {
        return wearingAllArmor(armor) &&
                armor.getHelmet().getType() == Material.IRON_HELMET &&
                armor.getChestplate().getType() == Material.CHAINMAIL_CHESTPLATE &&
                armor.getLeggings().getType() == Material.CHAINMAIL_LEGGINGS &&
                armor.getBoots().getType() == Material.IRON_BOOTS;
    }
}
