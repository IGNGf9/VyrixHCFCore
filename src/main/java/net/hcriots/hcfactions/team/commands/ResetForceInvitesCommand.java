/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.team.commands;

import cc.fyre.stark.engine.command.Command;
import cc.fyre.stark.engine.command.data.parameter.Param;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.team.Team;
import org.bukkit.ChatColor;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;

public class ResetForceInvitesCommand {

    @Command(names = {"resetforceinvites all"}, permission = "op")
    public static void resetforceinvites_all(Player sender) {
        ConversationFactory factory = new ConversationFactory(Hulu.getInstance()).withModality(true).withPrefix(new NullConversationPrefix()).withFirstPrompt(new StringPrompt() {

            public String getPromptText(ConversationContext context) {
                return (ChatColor.GREEN + "Are you sure you want to reset all force invites? Type §byes§a to confirm or §cno§a to quit.");
            }

            @Override
            public Prompt acceptInput(ConversationContext cc, String s) {
                if (s.equalsIgnoreCase("yes")) {
                    for (Team team : Hulu.getInstance().getTeamHandler().getTeams()) {
                        team.setForceInvites(Team.MAX_FORCE_INVITES);
                    }

                    Hulu.getInstance().getServer().broadcastMessage(ChatColor.RED.toString() + ChatColor.BOLD + "All force invites have been reset!");
                    cc.getForWhom().sendRawMessage(ChatColor.RED.toString() + ChatColor.BOLD + "All force invites have been reset!");
                    return (Prompt.END_OF_CONVERSATION);
                }

                if (s.equalsIgnoreCase("no")) {
                    cc.getForWhom().sendRawMessage(ChatColor.GREEN + "Resetting cancelled.");
                    return (Prompt.END_OF_CONVERSATION);
                }

                cc.getForWhom().sendRawMessage(ChatColor.GREEN + "Unrecognized response. Type §b/yes§a to confirm or §c/no§a to quit.");
                return (Prompt.END_OF_CONVERSATION);
            }

        }).withEscapeSequence("/no").withLocalEcho(false).withTimeout(10).thatExcludesNonPlayersWithMessage("Go away evil console!");

        Conversation con = factory.buildConversation(sender);
        sender.beginConversation(con);
    }

    @Command(names = "resetforceinvites", permission = "op")
    public static void resetforceinvites(Player sender, @Param(name = "team") Team team) {
        team.setForceInvites(Team.MAX_FORCE_INVITES);
        sender.sendMessage(ChatColor.GREEN + team.getName() + " now has " + team.getForceInvites() + " force invites.");
    }

}
