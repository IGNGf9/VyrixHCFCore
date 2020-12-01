/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.perks;

import cc.fyre.stark.engine.command.Command;
import net.hcriots.hcfactions.util.Color;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PotionCommands {
    @Command(names = {"fire", "fres", "fr"}, permission = "foxtrot.fire")
    public static void fire(final Player sender) {
        if (sender.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE)) {
            sender.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
            sender.sendMessage(Color.translate(" &6» &eYou have &cdisabled &6Fire Resistance II&e."));
            return;
        }
        sender.sendMessage(Color.translate(" &6» &eYou have &aenabled &6Fire Resistance II&e."));
        sender.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0));
    }

    @Command(names = {"speed", "sp"}, permission = "foxtrot.speed")
    public static void speed(final Player sender) {
        if (sender.hasPotionEffect(PotionEffectType.SPEED)) {
            sender.removePotionEffect(PotionEffectType.SPEED);
            sender.sendMessage(Color.translate(" &6» &eYou have &cdisabled &bSpeed II&e."));
            return;
        }
        sender.sendMessage(Color.translate(" &6» &eYou have &aenabled &bSpeed II&e."));
        sender.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
    }

    @Command(names = {"nv", "nightvision"}, permission = "foxtrot.night")
    public static void night(final Player sender) {
        if (sender.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
            sender.removePotionEffect(PotionEffectType.NIGHT_VISION);
            sender.sendMessage(Color.translate(" &6» &eYou have &cdisabled &9Night Vision II&e."));
            return;
        }
        sender.sendMessage(Color.translate(" &6» &eYou have &aenabled &9Night Vision II&e."));
        sender.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0));
    }

    @Command(names = {"invis", "invisibility"}, permission = "foxtrot.invis")
    public static void inv(final Player sender) {
        if (sender.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
            sender.removePotionEffect(PotionEffectType.INVISIBILITY);
            sender.sendMessage(Color.translate(" &6» &eYou have &cdisabled &fInvisbility II&e."));
            return;
        }
        sender.sendMessage(Color.translate(" &6» &eYou have &aenabled &fInvisbility II&e."));
        sender.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0));
    }
}