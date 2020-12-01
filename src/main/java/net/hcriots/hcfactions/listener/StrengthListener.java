/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PotionEffectAddEvent;
import org.bukkit.event.entity.PotionEffectExtendEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Created by Chris
 * Date: 16/10/2020
 * Time: 13:20
 */
public class StrengthListener implements Listener {

    @EventHandler(priority = EventPriority.NORMAL)

    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {

            Player p = (Player) event.getDamager();
            if (p.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
                for (PotionEffect eff : p.getActivePotionEffects()) {
                    double div = (eff.getAmplifier() + 1) * 1.3 + 1.0;

                    int dmg;
                    if (event.getDamage() / div <= 1.0) {
                        dmg = (eff.getAmplifier() + 1) * 3 + 1;
                    } else {
                        double strongdmg = 1.5;
                        dmg = (int) (event.getDamage() / div + (int) ((strongdmg + 1) * strongdmg));
                    }

                    event.setDamage(dmg);
                    break;
                }
            }
        }
    }

    @EventHandler
    public void on(PotionEffectAddEvent event) {
        if (event.getCause() == PotionEffectAddEvent.EffectCause.BEACON
                && event.getEffect().getType() == PotionEffectType.INCREASE_DAMAGE
                && event.getEffect().getAmplifier() >= 1) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void on(PotionEffectExtendEvent event) {
        if (event.getCause() == PotionEffectAddEvent.EffectCause.BEACON
                && event.getEffect().getType() == PotionEffectType.INCREASE_DAMAGE
                && event.getEffect().getAmplifier() >= 1) {
            event.setCancelled(true);
        }
    }
}
