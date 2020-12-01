/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.deathmessage.objects;

import cc.fyre.stark.Stark;
import lombok.Getter;
import net.hcriots.hcfactions.Hulu;
import org.bukkit.ChatColor;

public abstract class Damage {

    @Getter
    private final String damaged;
    @Getter
    private final double damage;
    @Getter
    private final long time;

    public Damage(String damaged, double damage) {
        this.damaged = damaged;
        this.damage = damage;
        this.time = System.currentTimeMillis();
    }

    public abstract String getDeathMessage();

    public String wrapName(String player) {
        int kills = Hulu.getInstance().getMapHandler().isKitMap() ? Hulu.getInstance().getMapHandler().getStatsHandler().getStats(player).getKills() : Hulu.getInstance().getKillsMap().getKills(Stark.instance.getCore().getUuidCache().uuid(player));

        return (ChatColor.RED + player + ChatColor.DARK_RED + "[" + kills + "]" + ChatColor.YELLOW);
    }

    public long getTimeDifference() {
        return System.currentTimeMillis() - time;
    }

}