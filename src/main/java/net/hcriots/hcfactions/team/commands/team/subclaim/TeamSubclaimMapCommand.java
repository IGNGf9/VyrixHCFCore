/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.team.commands.team.subclaim;

import cc.fyre.stark.engine.command.Command;
import net.hcriots.hcfactions.team.claims.VisualClaim;
import net.hcriots.hcfactions.team.claims.VisualClaimType;
import org.bukkit.entity.Player;

public class TeamSubclaimMapCommand {

    @Command(names = {"team subclaim map", "t subclaim map", "f subclaim map", "faction subclaim map", "fac subclaim map", "team sub map", "t sub map", "f sub map", "faction sub map", "fac sub map"}, permission = "")
    public static void teamSubclaimMap(Player sender) {
        (new VisualClaim(sender, VisualClaimType.SUBCLAIM_MAP, false)).draw(false);
    }

}