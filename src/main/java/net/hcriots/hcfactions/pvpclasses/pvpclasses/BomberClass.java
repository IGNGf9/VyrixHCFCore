/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.pvpclasses.pvpclasses;

import cc.fyre.stark.core.util.TimeUtils;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.pvpclasses.PvPClass;
import net.hcriots.hcfactions.pvpclasses.PvPClassHandler;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BomberClass extends PvPClass {

    private static final Map<String, Long> lastAbilityUse = new HashMap<>();

    public BomberClass() {
        super("Bomber", 15, new ArrayList<>());
    }

    @Override
    public void apply(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1), true);
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 0), true);
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1), true);
    }

    @Override
    public void tick(Player player) {
        if (!player.hasPotionEffect(PotionEffectType.SPEED)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
        }

        if (!player.hasPotionEffect(PotionEffectType.REGENERATION)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 0));

        }

        if (!player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1));
        }

        super.tick(player);
    }

    @EventHandler
    public void onHit(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!PvPClassHandler.hasKitOn(player, this)) {
            return;
        }
        if (event.getPlayer().getItemInHand().getType() == Material.STICK && event.getAction().toString().startsWith("RIGHT_")) {
            if (lastAbilityUse.containsKey(player.getName()) && lastAbilityUse.get(player.getName()) > System.currentTimeMillis()) {
                long millisLeft = ((lastAbilityUse.get(player.getName()) - System.currentTimeMillis()) / 1000L) * 1000L;
                String msg = TimeUtils.formatIntoDetailedString((int) (millisLeft / 1000));

                player.sendMessage(ChatColor.RED + "You cannot use this for another §c§l" + msg + "§c.");
            } else {
                lastAbilityUse.put(player.getName(), System.currentTimeMillis() + (1000L * 30));

                // throw
                Item primedTnt = player.getWorld().dropItem(player.getEyeLocation(), new ItemStack(Material.TNT));
                primedTnt.setPickupDelay(Integer.MAX_VALUE);
                primedTnt.setVelocity(player.getEyeLocation().getDirection().multiply(3));
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        primedTnt.remove();
                        primedTnt.getWorld().createExplosion(primedTnt.getLocation(), 4.0F, false);
                    }
                }.runTaskLater(Hulu.getInstance(), 60L);
            }
        }
    }

    @EventHandler
    public void onTntExplode(EntityExplodeEvent event) {
        Entity entity = event.getEntity();
        if (entity == null) {
            event.blockList().clear();
        }
    }

    @Override
    public boolean qualifies(PlayerInventory armor) {
        return wearingAllArmor(armor) &&
                armor.getHelmet().getType() == Material.GOLD_HELMET &&
                armor.getChestplate().getType() == Material.DIAMOND_CHESTPLATE &&
                armor.getLeggings().getType() == Material.GOLD_LEGGINGS &&
                armor.getBoots().getType() == Material.GOLD_BOOTS;
    }
}
