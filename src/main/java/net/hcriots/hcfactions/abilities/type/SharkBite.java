/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.abilities.type;

import net.hcriots.hcfactions.abilities.Ability;
import net.hcriots.hcfactions.bosses.particles.ParticleEffect;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class SharkBite extends Ability {

    private final HashMap<Player, Integer> hits = new HashMap<>();
    public HashMap<UUID, Long> damagepersecond = new HashMap<>(); // lazy code but oh well works xD

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public Material getMaterial() {
        return Material.SHEARS;
    }

    @Override
    public String getDisplayName() {
        return ChatColor.AQUA.toString() + ChatColor.BOLD + "Shark Bite";
    }

    @Override
    public List<String> getLore() {
        List<String> toReturn = new ArrayList();
        toReturn.add("&7Hit someone 3 times to deal");
        toReturn.add("&7a bleeding effect!");
        return toReturn;
    }

    @Override
    public long getCooldown() {
        return (long) (60.0 * 1000L);
    }

    @EventHandler
    public void onEntityHit(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            Player attacker = (Player) e.getDamager();
            Player victim = (Player) e.getEntity();
            if (!this.isSimilar(attacker.getItemInHand())) return;

            if (this.hasCooldown(attacker)) {
                this.sendCooldownMessage(attacker);
                e.setCancelled(true);
                return;
            }

            hits.putIfAbsent(attacker, 0);
            hits.put(attacker, hits.get(attacker) + 1);

            if (hits.get(attacker) == 3) {
                victim.setHealth(victim.getHealth() - 3);
                this.applyCooldown(attacker);

                new Timer().schedule(new TimerTask() {
                    int counter = 0;

                    @Override
                    public void run() {
                        counter++;
                        if (counter >= 8) this.cancel();

                        victim.setHealth(victim.getHealth() - 1);
                        try {
                            ParticleEffect.REDSTONE.sendToPlayers(Bukkit.getOnlinePlayers(), victim.getLocation(), 1, 0, 1, 0, 50);
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }
                }, 0, TimeUnit.SECONDS.toMillis(1));

                hits.remove(attacker);
            }
        }
    }
}