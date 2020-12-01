/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.tournaments.handler.runnable;

import mkremins.fanciful.FancyMessage;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.tournaments.Tournament;
import net.hcriots.hcfactions.tournaments.TournamentState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by InspectMC
 * Date: 8/3/2020
 * Time: 6:00 PM
 */

public class TournamentRunnable {

    private final Hulu plugin;
    private final Tournament tournament;

    public TournamentRunnable(Tournament tournament) {
        this.plugin = Hulu.getInstance();
        this.tournament = tournament;
    }

    @SuppressWarnings("deprecation")
    public Player getPlayerByUuid(UUID uuid) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getUniqueId().equals(uuid)) {
                return p;
            }
        }
        throw new IllegalArgumentException();
    }

    public void startSumo() {
        new BukkitRunnable() {
            public void run() {
                if (!TournamentRunnable.this.plugin.getTournamentHandler().isCreated()) {
                    this.cancel();
                    return;
                }
                if (TournamentRunnable.this.plugin.getTournamentHandler().getPlayers().isEmpty()) {
                    this.cancel();
                    return;
                }
                if (TournamentRunnable.this.tournament.getTournamentState() == TournamentState.STARTING || TournamentRunnable.this.tournament.getTournamentState() == TournamentState.FIGHTING) {
                    int countdown = TournamentRunnable.this.tournament.decrementCountdown();
                    Player first = TournamentRunnable.this.tournament.getFirstPlayer();
                    Player second = TournamentRunnable.this.tournament.getSecondPlayer();
                    if (countdown == 0) {
                        for (UUID players : TournamentRunnable.this.tournament.getPlayers()) {
                            try {
                                Player player = TournamentRunnable.this.getPlayerByUuid(players);
                                player.sendMessage(
                                        ChatColor.translateAlternateColorCodes('&', first.getDisplayName() + " &7vs " + second.getDisplayName()));
                            } catch (IllegalArgumentException ex) {
                            }
                        }
                        TournamentRunnable.this.tournament.teleport(TournamentRunnable.this.tournament.getFirstPlayer(), "Sumo.First");
                        TournamentRunnable.this.tournament.teleport(TournamentRunnable.this.tournament.getSecondPlayer(), "Sumo.Second");
                        TournamentRunnable.this.tournament.setTournamentState(TournamentState.FIGHTING);
                    } else if ((countdown % 5 == 0 || countdown < 5) && countdown > 0) {
                        TournamentRunnable.this.tournament.broadcastWithSound(
                                ChatColor.translateAlternateColorCodes('&', "&7The Round will start in &f" + countdown + " seconds!"), Sound.CLICK);
                        if (countdown == 1) {
                            TournamentRunnable.this.searchPlayers();
                            TournamentRunnable.this.tournament.getFirstPlayer().getInventory().clear();
                            TournamentRunnable.this.tournament.getSecondPlayer().getInventory().clear();
                        }
                    } else if (countdown < 0) {
                        this.cancel();
                    }
                }
            }
        }.runTaskTimer(this.plugin, 20L, 20L);
    }

    public void runAnnounce() {
        new BukkitRunnable() {
            @SuppressWarnings("deprecation")
            public void run() {
                if (!TournamentRunnable.this.plugin.getTournamentHandler().isCreated()) {
                    this.cancel();
                    return;
                }
                if (TournamentRunnable.this.plugin.getTournamentHandler().getPlayers().isEmpty()) {
                    this.cancel();
                    return;
                }
                if (TournamentRunnable.this.tournament != null && TournamentRunnable.this.tournament.getTournamentState() == TournamentState.WAITING) {
                    int countdown = TournamentRunnable.this.tournament.decrementAnnounce();
                    if (countdown == 0) {
                        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&4&l" + TournamentRunnable.this.tournament.getType().getName() + " &7has started with &f" + TournamentRunnable.this.tournament.getPlayers().size() + " players."));
                    } else if ((countdown % 10 == 0 || countdown < 5) && countdown > 0) {
                        Player player = TournamentRunnable.this.tournament.getHoster();
                        String name = player.getDisplayName();
                        FancyMessage message = new FancyMessage("").then(ChatColor.translateAlternateColorCodes('&', "&4&l" + TournamentRunnable.this.tournament.getType().getName() + " &7hosted by &r" + name + " " + "&7will start in &f" + countdown + " second" + ((countdown == 1) ? "" : "s") + " &f(" + "&a" + TournamentRunnable.this.tournament.getPlayers().size() + "&f/&a" + TournamentRunnable.this.tournament.getSize() + "&f)"));
                        for (Player online : Bukkit.getOnlinePlayers()) {
                            message.send(online);
                            online.playSound(online.getLocation(), Sound.CLICK, 3, 1);
                        }
                    } else if (countdown < 0) {
                        if (TournamentRunnable.this.tournament.getPlayers().size() < 2) {
                            TournamentRunnable.this.plugin.getTournamentHandler().setCreated(false);
                            for (Player online2 : Bukkit.getOnlinePlayers()) {
                                if (TournamentRunnable.this.plugin.getTournamentHandler().isInTournament(online2.getUniqueId())) {
                                    TournamentRunnable.this.tournament.rollbackInventory(online2);
                                    TournamentRunnable.this.plugin.getTournamentHandler().kickPlayer(online2.getUniqueId());
                                    online2.sendMessage(ChatColor.RED + "You have been kicked from the event because there isn't enough players.");
                                    online2.teleport(Bukkit.getWorld("world").getSpawnLocation());
                                }
                            }
                        } else {
                            TournamentRunnable.this.plugin.getTournamentHandler().forceStart();
                        }
                        this.cancel();
                    }
                }
            }
        }.runTaskTimer(this.plugin, 20L, 20L);
    }

    @SuppressWarnings("deprecation")
    private void searchPlayers() {
        List<Player> players = new ArrayList<Player>();
        if (!players.isEmpty()) {
            players.clear();
        }
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (this.plugin.getTournamentHandler().isInTournament(online.getUniqueId())) {
                players.add(online);
            }
        }
        if (players.size() > 1) {
            this.tournament.setFirstPlayer(players.get(0));
            this.tournament.setSecondPlayer(players.get(1));
        }
    }
}
