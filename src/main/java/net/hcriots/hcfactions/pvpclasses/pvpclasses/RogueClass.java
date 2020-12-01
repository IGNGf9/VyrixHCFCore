/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.pvpclasses.pvpclasses;

import cc.fyre.stark.Stark;
import cc.fyre.stark.core.util.TimeUtils;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.deathmessage.DeathMessageHandler;
import net.hcriots.hcfactions.deathmessage.objects.PlayerDamage;
import net.hcriots.hcfactions.pvpclasses.PvPClass;
import net.hcriots.hcfactions.pvpclasses.PvPClassHandler;
import net.hcriots.hcfactions.server.event.BackstabKillEvent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RogueClass extends PvPClass {

    private static final Map<String, Long> lastSpeedUsage = new HashMap<>();
    private static final Map<String, Long> lastJumpUsage = new HashMap<>();
    private static final Map<String, Long> backstabCooldown = new HashMap<>();

    public RogueClass() {
        super("Rogue", 15, Arrays.asList(Material.SUGAR, Material.FEATHER));
    }

    @Override
    public boolean qualifies(PlayerInventory armor) {
        return wearingAllArmor(armor) &&
                armor.getHelmet().getType() == Material.CHAINMAIL_HELMET &&
                armor.getChestplate().getType() == Material.CHAINMAIL_CHESTPLATE &&
                armor.getLeggings().getType() == Material.CHAINMAIL_LEGGINGS &&
                armor.getBoots().getType() == Material.CHAINMAIL_BOOTS;
    }

    @Override
    public void apply(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 1));
    }

    @Override
    public void tick(Player player) {
        if (!player.hasPotionEffect(PotionEffectType.SPEED)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));
        }

        if (!player.hasPotionEffect(PotionEffectType.JUMP)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 1));
        }

        if (!player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0));
        }
    }

    @Override
    public void remove(Player player) {
        removeInfiniteEffects(player);
    }

    @Override
    public boolean itemConsumed(Player player, Material material) {
        if (material == Material.SUGAR) {
            if (lastSpeedUsage.containsKey(player.getName()) && lastSpeedUsage.get(player.getName()) > System.currentTimeMillis()) {
                Long millisLeft = ((lastSpeedUsage.get(player.getName()) - System.currentTimeMillis()) / 1000L) * 1000L;
                String msg = TimeUtils.formatIntoDetailedString((int) (millisLeft / 1000));

                player.sendMessage(ChatColor.RED + "You cannot use this for another §c§l" + msg + "§c.");
                return (false);
            }

            lastSpeedUsage.put(player.getName(), System.currentTimeMillis() + (1000L * 60 * 2));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 4), true);
        } else {
            if (lastJumpUsage.containsKey(player.getName()) && lastJumpUsage.get(player.getName()) > System.currentTimeMillis()) {
                Long millisLeft = ((lastJumpUsage.get(player.getName()) - System.currentTimeMillis()) / 1000L) * 1000L;
                String msg = TimeUtils.formatIntoDetailedString((int) (millisLeft / 1000));

                player.sendMessage(ChatColor.RED + "You cannot use this for another §c§l" + msg + "§c.");
                return (false);
            }

            lastJumpUsage.put(player.getName(), System.currentTimeMillis() + (1000L * 60 * 2));
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 200, 6), true);
        }

        return (true);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityArrowHit(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            Player damager = (Player) event.getDamager();
            Player victim = (Player) event.getEntity();

            if (damager.getItemInHand() != null && damager.getItemInHand().getType() == Material.GOLD_SWORD && PvPClassHandler.hasKitOn(damager, this)) {
                if (backstabCooldown.containsKey(damager.getName()) && backstabCooldown.get(damager.getName()) > System.currentTimeMillis()) {
                    return;
                }

                backstabCooldown.put(damager.getName(), System.currentTimeMillis() + 1500L);

                Vector playerVector = damager.getLocation().getDirection();
                Vector entityVector = victim.getLocation().getDirection();

                playerVector.setY(0F);
                entityVector.setY(0F);

                double degrees = playerVector.angle(entityVector);

                if (Math.abs(degrees) < 1.4) {
                    damager.setItemInHand(new ItemStack(Material.AIR));

                    damager.playSound(damager.getLocation(), Sound.ITEM_BREAK, 1F, 1F);
                    damager.getWorld().playEffect(victim.getEyeLocation(), Effect.STEP_SOUND, Material.REDSTONE_BLOCK);

                    if (victim.getHealth() - 7D <= 0) {
                        event.setCancelled(true);
                    } else {
                        event.setDamage(0D);
                    }

                    DeathMessageHandler.addDamage(victim, new BackstabDamage(victim.getName(), 7D, damager.getName()));
                    victim.setHealth(Math.max(0D, victim.getHealth() - 7D));
                    if (victim.getHealth() <= 0.0D) {
                        Bukkit.getPluginManager().callEvent(new BackstabKillEvent(damager, victim));
                    }

                    damager.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 2 * 20, 2));
                } else {
                    damager.sendMessage(ChatColor.RED + "Failed to backstab!");
                }
            }
        }
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        lastJumpUsage.remove(event.getPlayer().getName());
        lastSpeedUsage.remove(event.getPlayer().getName());
        backstabCooldown.remove(event.getPlayer().getName());
    }

    public class BackstabDamage extends PlayerDamage {

        //***************************//

        public BackstabDamage(String damaged, double damage, String damager) {
            super(damaged, damage, damager);
        }

        //***************************//

        public String getDescription() {
            return ("Backstabbed by " + getDamager());
        }

        public String getDeathMessage() {
            return (ChatColor.RED + getDamaged() + ChatColor.DARK_RED + "[" + Hulu.getInstance().getKillsMap().getKills(Stark.instance.getCore().getUuidCache().uuid(getDamaged())) + "]" + ChatColor.YELLOW + " was backstabbed by " + ChatColor.RED +
                    getDamager() + ChatColor.DARK_RED + "[" + Hulu.getInstance().getKillsMap().getKills(Stark.instance.getCore().getUuidCache().uuid(getDamager())) + "]" + ChatColor.YELLOW + ".");
        }

        //***************************//

    }

}
