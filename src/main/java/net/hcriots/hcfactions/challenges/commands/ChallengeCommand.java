/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.challenges.commands;

import cc.fyre.stark.Stark;
import cc.fyre.stark.engine.command.Command;
import cc.fyre.stark.engine.command.data.parameter.Param;
import cc.fyre.stark.util.CC;
import net.hcriots.hcfactions.challenges.ChallengeTypes;
import net.hcriots.hcfactions.challenges.maps.ChallengeCooldownMap;
import net.hcriots.hcfactions.challenges.maps.ChallengeMap;
import net.hcriots.hcfactions.challenges.menu.ChallengesMenu;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ChallengeCommand {

    @Command(names = {"challenge", "challenges", "ch"}, permission = "")
    public static void progress(Player sender) {
        if (sender != null) {
            new ChallengesMenu().openMenu(sender);
        }
    }

    @Command(names = {"challenge reset", "challenges reset", "ch reset"}, permission = "hulu.reset")
    public static void reset(Player sender, @Param(name = "target") UUID player, @Param(name = "progress|cooldowns") String type) {
        for (ChallengeTypes challengeTypes : ChallengeTypes.values()) {
            ChallengeCooldownMap cooldownMap = challengeTypes.getChallengeCooldownMap();
            ChallengeMap challengeMap = challengeTypes.getChallengeMap();

            if (type.equalsIgnoreCase("progress")) {
                challengeMap.setAmount(player, 0);
            } else if (type.equalsIgnoreCase("cooldowns")) {
                cooldownMap.setCooldown(player, 0);
            } else {
                sender.sendMessage(CC.INSTANCE.getRED() + "Please put a valid reset option. You can only reset a players progress and cooldowns.");
                return;
            }
        }
        sender.sendMessage(CC.INSTANCE.getGREEN() + "You have successfully reset " + Stark.instance.getCore().getUuidCache().name(player) + "'s challenge " + type.toLowerCase() + ".");
    }
}
