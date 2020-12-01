/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.commands;

import cc.fyre.stark.engine.command.Command;
import net.hcriots.hcfactions.Hulu;
import org.bukkit.ChatColor;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;

public class WipeDeathbansCommand {

    @Command(names = {"cleardeathbans"}, permission = "op")
    public static void wipeDeathbans(Player sender) {
        ConversationFactory factory = new ConversationFactory(Hulu.getInstance()).withModality(true).withPrefix(new NullConversationPrefix()).withFirstPrompt(new StringPrompt() {

            public String getPromptText(ConversationContext context) {
                return "§aAre you sure you want to wipe all deathbans? This action CANNOT be reversed. Type §byes§a to confirm or §cno§a to quit.";
            }

            @Override
            public Prompt acceptInput(ConversationContext cc, String s) {
                if (s.equalsIgnoreCase("yes")) {
                    Hulu.getInstance().getDeathbanMap().wipeDeathbans();
                    cc.getForWhom().sendRawMessage(ChatColor.GREEN + "Deathbans have been wiped.");
                    return Prompt.END_OF_CONVERSATION;
                }

                if (s.equalsIgnoreCase("no")) {
                    cc.getForWhom().sendRawMessage(ChatColor.GREEN + "Deathban wipe aborted.");
                    return Prompt.END_OF_CONVERSATION;
                }

                cc.getForWhom().sendRawMessage(ChatColor.GREEN + "Unrecognized response. Type §byes§a to confirm or §cno§a to quit.");
                return Prompt.END_OF_CONVERSATION;
            }

        }).withLocalEcho(false).withEscapeSequence("/no").withTimeout(10).thatExcludesNonPlayersWithMessage("Go away evil console!");
        Conversation con = factory.buildConversation(sender);
        sender.beginConversation(con);
    }

}