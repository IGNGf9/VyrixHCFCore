/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.team.commands.team;

import cc.fyre.stark.engine.command.Command;
import net.hcriots.hcfactions.team.claims.VisualClaim;
import net.hcriots.hcfactions.team.claims.VisualClaimType;
import org.bukkit.entity.Player;

public class TeamMapCommand {

    @Command(names = {"team map", "t map", "f map", "faction map", "fac map", "map"}, permission = "")
    public static void teamMap(Player sender) {
        (new VisualClaim(sender, VisualClaimType.MAP, false)).draw(false);
    }

//    @Command(names={ "team map surface", "t map surface", "f map surface", "faction map surface", "fac map surface", "map surface" }, permission="")
//    public static void teamMapSurface(Player sender) {
//        (new VisualClaim(sender, VisualClaimType.SURFACE_MAP, false)).draw(false);
//    }

}