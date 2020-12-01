/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.team.commands.team;

import cc.fyre.stark.engine.command.Command;
import cc.fyre.stark.util.CC;
import org.bukkit.entity.Player;

public class TeamCommand {

    @Command(names = {"team", "t", "f", "faction", "fac"}, permission = "")
    public static void team(Player sender) {

        String[] msg = {

                CC.INSTANCE.translate("§7§m-----------------------------------------------------"),
                CC.INSTANCE.translate("§6§lTeam Help §7- §fTeam Help"),
                CC.INSTANCE.translate("§7§m-----------------------------------------------------"),

                CC.INSTANCE.translate("§6&lGeneral Commands:"),
                CC.INSTANCE.translate(" §e* &f/f create <teamName> §7- §eCreate a new team"),
                CC.INSTANCE.translate(" §e* &f/f accept <teamName> §7- §eAccept a pending invitation"),
                CC.INSTANCE.translate(" §e* &f/f lives add <amount> §7- §eIrreversibly add lives to your faction"),
                CC.INSTANCE.translate(" §e* &f/f rally §7- §eDisplays your coords to your faction"),
                CC.INSTANCE.translate(" §e* &f/f upgrades §7- §ePurchase upgrades for your faction"),
                CC.INSTANCE.translate(" §e* &f/f vault §7- §eOpens a vault that only your faction can access"),
                CC.INSTANCE.translate(" §e* &f/f leave §7- §eLeave your current team"),
                CC.INSTANCE.translate(" §e* &f/f focus <factiom> §7- §eFocus a faction and their information will display"),
                CC.INSTANCE.translate(" §e* &f/f home §7- §eTeleport to your team home"),
                CC.INSTANCE.translate(" §e* &f/f stuck §7- §eTeleport out of enemy territory"),
                CC.INSTANCE.translate(" §e* &f/f deposit <amount§7|§eall> §7- §eDeposit money into your team balance"),

                "",
                CC.INSTANCE.translate("§6&lInformation Commands:"),
                CC.INSTANCE.translate(" §e* &f/f who [player§7|§eteamName] §7- §eDisplay team information"),
                CC.INSTANCE.translate(" §e* &f/f map §7- §eShow nearby claims (identified by pillars)"),
                CC.INSTANCE.translate(" §e* &f/f list §7- §eShow list of teams online (sorted by most online)"),

                "",
                CC.INSTANCE.translate("§6&lCaptain Commands:"),
                CC.INSTANCE.translate(" §e* &f/f invite <player> §7- §eInvite a player to your team"),
                CC.INSTANCE.translate(" §e* &f/f uninvite <player> §7- §eRevoke an invitation"),
                CC.INSTANCE.translate(" §e* &f/f invites §7- §eList all open invitations"),
                CC.INSTANCE.translate(" §e* &f/f kick <player> §7- §eKick a player from your team"),
                CC.INSTANCE.translate(" §e* &f/f claim §7- §eStart a claim for your team"),
                CC.INSTANCE.translate(" §e* &f/f subclaim §7- §eShow the subclaim help page"),
                CC.INSTANCE.translate(" §e* &f/f sethome §7- §eSet your team's home at your current location"),
                CC.INSTANCE.translate(" §e* &f/f withdraw <amount> §7- §eWithdraw money from your team's balance"),
                CC.INSTANCE.translate(" §e* &f/f announcement [message here] §7- §eSet your team's announcement"),

                "",
                CC.INSTANCE.translate("§6&lLeader Commands:"),
                CC.INSTANCE.translate(" §e* &f/f coleader <add|remove> <player> §7- §eAdd or remove a co-leader"),
                CC.INSTANCE.translate(" §e* &f/f captain <add|remove> <player> §7- §eAdd or remove a captain"),
                CC.INSTANCE.translate(" §e* &f/f revive <player> §7- §eRevive a teammate using team lives"),
                CC.INSTANCE.translate(" §e* &f/f unclaim [all] §7- §eUnclaim land"),
                CC.INSTANCE.translate(" §e* &f/f rename <newName> §7- §eRename your team"),
                CC.INSTANCE.translate(" §e* &f/f disband §7- §eDisband your team"),
                CC.INSTANCE.translate("§7§m-----------------------------------------------------"),
        };
        sender.sendMessage(msg);
    }
}