/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.deathmessage;

import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.deathmessage.objects.Damage;
import net.hcriots.hcfactions.deathmessage.trackers.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeathMessageHandler {

    private static final Map<String, List<Damage>> damage = new HashMap<>();

    public static void init() {

        Hulu.getInstance().getServer().getPluginManager().registerEvents(new GeneralTracker(), Hulu.getInstance());
        Hulu.getInstance().getServer().getPluginManager().registerEvents(new PVPTracker(), Hulu.getInstance());
        Hulu.getInstance().getServer().getPluginManager().registerEvents(new EntityTracker(), Hulu.getInstance());
        Hulu.getInstance().getServer().getPluginManager().registerEvents(new FallTracker(), Hulu.getInstance());
        Hulu.getInstance().getServer().getPluginManager().registerEvents(new ArrowTracker(), Hulu.getInstance());
        Hulu.getInstance().getServer().getPluginManager().registerEvents(new VoidTracker(), Hulu.getInstance());
        Hulu.getInstance().getServer().getPluginManager().registerEvents(new BurnTracker(), Hulu.getInstance());
    }

    public static List<Damage> getDamage(Player player) {
        return (damage.get(player.getName()));
    }

    public static void addDamage(Player player, Damage addedDamage) {
        if (!damage.containsKey(player.getName())) {
            damage.put(player.getName(), new ArrayList<Damage>());
        }

        List<Damage> damageList = damage.get(player.getName());

        while (damageList.size() > 30) {
            damageList.remove(0);
        }

        damageList.add(addedDamage);
    }

    public static void clearDamage(Player player) {
        damage.remove(player.getName());
    }

}