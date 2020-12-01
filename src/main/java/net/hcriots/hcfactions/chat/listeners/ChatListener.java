/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.chat.listeners;

import com.google.common.collect.ImmutableMap;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.HuluConstants;
import net.hcriots.hcfactions.chat.ChatHandler;
import net.hcriots.hcfactions.chat.enums.ChatMode;
import net.hcriots.hcfactions.team.Team;
import net.hcriots.hcfactions.team.commands.team.TeamMuteCommand;
import net.hcriots.hcfactions.team.commands.team.TeamShadowMuteCommand;
import net.hcriots.hcfactions.team.track.TeamActionTracker;
import net.hcriots.hcfactions.team.track.TeamActionType;
import org.bson.types.ObjectId;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class ChatListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    // this handler prevents people from getting banned for spam in faction (or ally) chat
    public void onAsyncPlayerChatEarly(AsyncPlayerChatEvent event) {
        ChatMode playerChatMode = Hulu.getInstance().getChatModeMap().getChatMode(event.getPlayer().getUniqueId());
        ChatMode forcedChatMode = ChatMode.findFromForcedPrefix(event.getMessage().charAt(0));
        ChatMode finalChatMode;

        if (forcedChatMode != null) {
            finalChatMode = forcedChatMode;
        } else {
            finalChatMode = playerChatMode;
        }

        if (finalChatMode != ChatMode.PUBLIC) {
            event.getPlayer().setMetadata("NoSpamCheck", new FixedMetadataValue(Hulu.getInstance(), true));
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        event.getPlayer().removeMetadata("NoSpamCheck", Hulu.getInstance());

        Team playerTeam = Hulu.getInstance().getTeamHandler().getTeam(event.getPlayer());
        String rankPrefix = event.getPlayer().hasMetadata("RankPrefix") ? event.getPlayer().getMetadata("RankPrefix").get(0).asString() : "";
        ChatMode playerChatMode = Hulu.getInstance().getChatModeMap().getChatMode(event.getPlayer().getUniqueId());
        ChatMode forcedChatMode = ChatMode.findFromForcedPrefix(event.getMessage().charAt(0));
        ChatMode finalChatMode;

        // If they provided us with a forced chat mode, we have to remove it from the final message.
        // We also .trim() the message because people will do things like '! hi', which just looks annoying in chat.
        if (forcedChatMode != null) {
            event.setMessage(event.getMessage().substring(1).trim());
        }

        if (forcedChatMode != null) {
            finalChatMode = forcedChatMode;
        } else {
            finalChatMode = playerChatMode;
        }

        // If another plugin cancelled this event before it got to us (we are on MONITOR, so it'll happen)
        if (event.isCancelled() && finalChatMode == ChatMode.PUBLIC) { // Only respect cancelled events if this is public chat. Who cares what their team says.
            return;
        }

        // Any route we go down will cancel the event eventually.
        // Let's just do it here.
        event.setCancelled(true);

        // If someone's not in a team, instead of forcing their 'channel' to public,
        // we just tell them they can't.
        if (finalChatMode != ChatMode.PUBLIC && playerTeam == null) {
            event.getPlayer().sendMessage(ChatColor.RED + "You can't speak in non-public chat if you're not in a team!");
            return;
        }

        if (finalChatMode != ChatMode.PUBLIC) {
            if (playerTeam == null) {
                event.getPlayer().sendMessage(ChatColor.RED + "You can't speak in non-public chat if you're not in a team!");
                return;
            } else if (finalChatMode == ChatMode.OFFICER && !playerTeam.isCaptain(event.getPlayer().getUniqueId()) && !playerTeam.isCoLeader(event.getPlayer().getUniqueId()) && !playerTeam.isOwner(event.getPlayer().getUniqueId())) {
                event.getPlayer().sendMessage(ChatColor.RED + "You can't speak in officer chat if you're not an officer!");
                return;
            }
        }

        // and here starts the big logic switch
        switch (finalChatMode) {
            case PUBLIC:
                if (TeamMuteCommand.getTeamMutes().containsKey(event.getPlayer().getUniqueId())) {
                    event.getPlayer().sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "Your team is muted!");
                    return;
                }

                String publicChatFormat = HuluConstants.publicChatFormatTwoPointOhBaby(event.getPlayer(), playerTeam, rankPrefix);
                String finalMessage = String.format(publicChatFormat, event.getPlayer().getDisplayName(), event.getMessage());

                // Loop those who are to receive the message (which they won't if they have the sender /ignore'd or something),
                // not online players
                for (Player player : event.getRecipients()) {
                    if (playerTeam == null) {
                        // If the player sending the message is shadowmuted (if their team was and they left it)
                        // then we don't allow them to. We probably could move this check "higher up", but oh well.
                        if (TeamShadowMuteCommand.getTeamShadowMutes().containsKey(event.getPlayer().getUniqueId())) {
                            continue;
                        }

                        // If their chat is enabled (which it is by default) or the sender is op, send them the message
                        // The isOp() fragment is so OP messages are sent regardless of if the player's chat is toggled
                        if (event.getPlayer().isOp() || Hulu.getInstance().getToggleGlobalChatMap().isGlobalChatToggled(player.getUniqueId())) {
                            player.sendMessage(finalMessage);
                        }
                    } else {
                        if (playerTeam.isMember(player.getUniqueId())) {
                            // Gypsie way to get a custom color if they're allies/teammates
                            player.sendMessage(finalMessage.replace(ChatColor.GOLD + "[" + Hulu.getInstance().getServerHandler().getDefaultRelationColor(), ChatColor.GOLD + "[" + ChatColor.DARK_GREEN));
                        } else if (playerTeam.isAlly(player.getUniqueId())) {
                            // Gypsie way to get a custom color if they're allies/teammates
                            player.sendMessage(finalMessage.replace(ChatColor.GOLD + "[" + Hulu.getInstance().getServerHandler().getDefaultRelationColor(), ChatColor.GOLD + "[" + Team.ALLY_COLOR));
                        } else {
                            // We only check this here as...
                            // Team members always see their team's messages
                            // Allies always see their allies' messages, 'cause they'll probably be in a TS or something
                            // and they could figure out this code even exists
                            if (TeamShadowMuteCommand.getTeamShadowMutes().containsKey(event.getPlayer().getUniqueId())) {
                                continue;
                            }

                            // If their chat is enabled (which it is by default) or the sender is op, send them the message
                            // The isOp() fragment is so OP messages are sent regardless of if the player's chat is toggled
                            if (event.getPlayer().isOp() || Hulu.getInstance().getToggleGlobalChatMap().isGlobalChatToggled(player.getUniqueId())) {
                                player.sendMessage(finalMessage);
                            }
                        }
                    }
                }

                ChatHandler.getPublicMessagesSent().incrementAndGet();
                Hulu.getInstance().getServer().getConsoleSender().sendMessage(finalMessage);
                break;
            case ALLIANCE:
                String allyChatFormat = HuluConstants.allyChatFormat(event.getPlayer(), event.getMessage());
                String allyChatSpyFormat = HuluConstants.allyChatSpyFormat(playerTeam, event.getPlayer(), event.getMessage());

                // Loop online players and not recipients just in case you're weird and
                // /ignore your teammates/allies
                for (Player player : Hulu.getInstance().getServer().getOnlinePlayers()) {
                    if (playerTeam.isMember(player.getUniqueId()) || playerTeam.isAlly(player.getUniqueId())) {
                        player.sendMessage(allyChatFormat);
                    } else if (Hulu.getInstance().getChatSpyMap().getChatSpy(player.getUniqueId()).contains(playerTeam.getUniqueId())) {
                        player.sendMessage(allyChatSpyFormat);
                    }
                }

                // Log to ally's allychat log.
                for (ObjectId allyId : playerTeam.getAllies()) {
                    Team ally = Hulu.getInstance().getTeamHandler().getTeam(allyId);

                    if (ally != null) {
                        TeamActionTracker.logActionAsync(ally, TeamActionType.ALLY_CHAT_MESSAGE, ImmutableMap.<String, Object>builder()
                                .put("allyTeamId", playerTeam.getUniqueId())
                                .put("allyTeamName", playerTeam.getName())
                                .put("playerId", event.getPlayer().getUniqueId())
                                .put("playerName", event.getPlayer().getName())
                                .put("message", event.getMessage())
                                .build()
                        );
                    }
                }

                TeamActionTracker.logActionAsync(playerTeam, TeamActionType.ALLY_CHAT_MESSAGE, ImmutableMap.of(
                        "playerId", event.getPlayer().getUniqueId(),
                        "playerName", event.getPlayer().getName(),
                        "message", event.getMessage()
                ));

                Hulu.getInstance().getServer().getLogger().info("[Ally Chat] [" + playerTeam.getName() + "] " + event.getPlayer().getName() + ": " + event.getMessage());
                break;
            case TEAM:
                String teamChatFormat = HuluConstants.teamChatFormat(event.getPlayer(), event.getMessage());
                String teamChatSpyFormat = HuluConstants.teamChatSpyFormat(playerTeam, event.getPlayer(), event.getMessage());

                // Loop online players and not recipients just in case you're weird and
                // /ignore your teammates
                for (Player player : Hulu.getInstance().getServer().getOnlinePlayers()) {
                    if (playerTeam.isMember(player.getUniqueId())) {
                        player.sendMessage(teamChatFormat);
                    } else if (Hulu.getInstance().getChatSpyMap().getChatSpy(player.getUniqueId()).contains(playerTeam.getUniqueId())) {
                        player.sendMessage(teamChatSpyFormat);
                    }
                }

                // Log to our teamchat log.
                TeamActionTracker.logActionAsync(playerTeam, TeamActionType.TEAM_CHAT_MESSAGE, ImmutableMap.of(
                        "playerId", event.getPlayer().getUniqueId(),
                        "playerName", event.getPlayer().getName(),
                        "message", event.getMessage()
                ));

                Hulu.getInstance().getServer().getLogger().info("[Team Chat] [" + playerTeam.getName() + "] " + event.getPlayer().getName() + ": " + event.getMessage());
                break;
            case OFFICER:
                String officerChatFormat = HuluConstants.officerChatFormat(event.getPlayer(), event.getMessage());

                // Loop online players and not recipients just in case you're weird and
                // /ignore your teammates
                for (Player player : Hulu.getInstance().getServer().getOnlinePlayers()) {
                    if (playerTeam.isCaptain(player.getUniqueId()) || playerTeam.isCoLeader(player.getUniqueId()) || playerTeam.isOwner(player.getUniqueId())) {
                        player.sendMessage(officerChatFormat);
                    }
                }

                // Log to our teamchat log.
                TeamActionTracker.logActionAsync(playerTeam, TeamActionType.OFFICER_CHAT_MESSAGE, ImmutableMap.of(
                        "playerId", event.getPlayer().getUniqueId(),
                        "playerName", event.getPlayer().getName(),
                        "message", event.getMessage()
                ));

                Hulu.getInstance().getServer().getLogger().info("[Officer Chat] [" + playerTeam.getName() + "] " + event.getPlayer().getName() + ": " + event.getMessage());
                break;
        }
    }
}