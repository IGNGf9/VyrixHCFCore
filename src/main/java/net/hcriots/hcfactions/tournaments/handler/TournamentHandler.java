/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.tournaments.handler;

import cc.fyre.stark.util.ItemBuilder;
import lombok.Getter;
import lombok.Setter;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.server.SpawnTagHandler;
import net.hcriots.hcfactions.tournaments.Tournament;
import net.hcriots.hcfactions.tournaments.TournamentState;
import net.hcriots.hcfactions.tournaments.TournamentType;
import net.hcriots.hcfactions.tournaments.handler.runnable.TournamentRunnable;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

/**
 * Created by InspectMC
 * Date: 8/3/2020
 * Time: 6:01 PM
 */
@Getter
@Setter
public class TournamentHandler {

    public final Map<UUID, Integer> matches;
    private final Hulu plugin;
    private final List<UUID> players;
    private Tournament tournament;
    private boolean created;

    public TournamentHandler() {
        this.plugin = Hulu.getInstance();
        this.matches = new HashMap<>();
        this.players = new ArrayList<>();
        this.created = false;
    }

    public boolean isInTournament(UUID uuid) {
        return this.players.contains(uuid);
    }

    public boolean isInTournament(Player player) {
        return this.players.contains(player.getUniqueId());
    }

    public void kickPlayer(UUID uuid) {
        this.players.remove(uuid);
    }

    public void createTournament(CommandSender commandSender, int size, TournamentType type, Player player) {
        Tournament tournament = new Tournament(size, type, player);
        this.tournament = tournament;
        TournamentRunnable runnable = new TournamentRunnable(tournament);
        if (type == TournamentType.SUMO) {
            runnable.startSumo();
        }
        this.created = true;
    }

    @SuppressWarnings("deprecation")
    public void playerLeft(Tournament tournament, Player player, boolean message) {
        if (message) {
            player.sendMessage(ChatColor.RED + "You have left the event..");
            tournament.rollbackInventory(player);
        }
        this.players.remove(player.getUniqueId());
        for (PotionEffect effects : player.getActivePotionEffects()) {
            player.removePotionEffect(effects.getType());
        }
        if (SpawnTagHandler.isTagged(player)) {
            SpawnTagHandler.removeTag(player);
        }
        tournament.removePlayer(player.getUniqueId());
        player.removePotionEffect(PotionEffectType.INVISIBILITY);
        if (message) {
            tournament.broadcast(ChatColor.translateAlternateColorCodes('&', player.getDisplayName() + " &7has left the event. &f(" + tournament.getPlayers().size() + '/' + tournament.getSize() + ')'));
        }
        if (player.isOnline()) {
            tournament.rollbackInventory(player);
            player.teleport(Bukkit.getWorld("world").getSpawnLocation());
        }
        if (this.players.size() == 1) {
            if (tournament.getTournamentState() != TournamentState.FIGHTING) {
                return;
            }
            Player winner = Bukkit.getPlayer(this.players.get(0));
            for (Player online : Bukkit.getOnlinePlayers()) {
                online.sendMessage(new String[2]);
                online.sendMessage("");
                online.sendMessage(ChatColor.translateAlternateColorCodes('&', winner.getDisplayName() + " &7has won the event!"));
                online.sendMessage("");
                online.sendMessage(new String[2]);
                online.playSound(online.getLocation(), Sound.ENDERDRAGON_GROWL, 2.0f, 2.0f);
                if (SpawnTagHandler.isTagged(winner)) {
                    SpawnTagHandler.removeTag(winner);
                }
                tournament.rollbackInventory(winner);
                for (PotionEffect effects2 : winner.getActivePotionEffects()) {
                    winner.removePotionEffect(effects2.getType());
                }
            }
            this.plugin.getTournamentHandler().setCreated(false);
            for (Player online : Bukkit.getOnlinePlayers()) {
                tournament.rollbackInventory(player);
                if (this.plugin.getTournamentHandler().isInTournament(online.getUniqueId())) {
                    this.players.remove(online.getUniqueId());
                    online.teleport(Bukkit.getWorld("world").getSpawnLocation());
                    tournament.rollbackInventory(player);
                    for (PotionEffect effects2 : online.getActivePotionEffects()) {
                        online.removePotionEffect(effects2.getType());
                    }
                }
            }
            if (this.getTournament().getType() == TournamentType.SUMO) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "cr givekey " + winner.getName() + " Event 1");
            } else if (this.getTournament().getType() == TournamentType.DIAMOND) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "cr givekey " + winner.getName() + " Event 2");
            } else if (this.getTournament().getType() == TournamentType.ROGUE) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "cr givekey " + winner.getName() + " Event 2");
            } else if (this.getTournament().getType() == TournamentType.ARCHER) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "cr givekey " + winner.getName() + " Event 2");
            }
        } else if (this.players.size() == 0) {
            this.plugin.getTournamentHandler().setCreated(false);
        } else if (this.players.size() > 1) {
            TournamentRunnable runnable = new TournamentRunnable(tournament);
            if (this.plugin.getTournamentHandler().getTournament().getType() == TournamentType.SUMO) {
                tournament.setCountdown(4);
                tournament.setCurrentRound(tournament.getCurrentRound() + 1);
                runnable.startSumo();
            }
        }
    }

    public void leaveTournament(Player player) {
        if (!this.isInTournament(player.getUniqueId())) {
            return;
        }
        this.playerLeft(this.tournament, player, true);
    }

    private void playerJoined(Tournament tournament, Player player) {
        tournament.addPlayer(player.getUniqueId());
        this.players.add(player.getUniqueId());
        player.setFoodLevel(20);
        player.setHealth(20.0);
        for (PotionEffect effects : player.getActivePotionEffects()) {
            player.removePotionEffect(effects.getType());
        }
        tournament.broadcast(ChatColor.translateAlternateColorCodes('&', player.getDisplayName() + " &7has join the event &f(" + tournament.getPlayers().size() + '/' + tournament.getSize() + ')'));
    }

    public void joinTournament(Player player) {
        Tournament tournament = this.tournament;
        if (this.players.size() >= tournament.getSize()) {
            player.sendMessage(ChatColor.RED + "This event is already full!");
        } else {
            this.playerJoined(tournament, player);
        }
    }

    public void forceStart() {
        if (this.tournament.getType() == TournamentType.DIAMOND ||
                this.tournament.getType() == TournamentType.ROGUE) {
            for (UUID players : this.players) {
                Player online = Bukkit.getPlayer(players);
                this.tournament.teleport(online, "FFA.Spawn");
            }
            this.tournament.setTournamentState(TournamentState.FIGHTING);
            this.tournament.setProtection(10);
            for (UUID players : this.players) {
                Player online = Bukkit.getPlayer(players);
                PlayerInventory inventory = online.getInventory();
                inventory.clear();
                inventory.setItem(0, new ItemBuilder(Material.ENCHANTED_BOOK).name(ChatColor.translateAlternateColorCodes(
                        '&', "&6Default Kit")).addToLore(ChatColor.translateAlternateColorCodes(
                        '&', "&7Right click to equip the Default kit")).build());
                online.updateInventory();
                online.setGameMode(GameMode.SURVIVAL);
                online.setAllowFlight(false);
                online.setFlying(false);
            }
        } else if (this.tournament.getType() == TournamentType.AXE) {
            for (UUID players : this.players) {
                Player online = Bukkit.getPlayer(players);
                this.tournament.teleport(online, "Axe.Spawn");
            }
            this.tournament.setTournamentState(TournamentState.FIGHTING);
            this.tournament.setProtection(10);
            for (UUID players : this.players) {
                Player online = Bukkit.getPlayer(players);
                PlayerInventory inventory = online.getInventory();
                inventory.clear();
                inventory.setItem(0, new ItemBuilder(Material.ENCHANTED_BOOK).name(ChatColor.translateAlternateColorCodes(
                        '&', "&6Default Kit")).addToLore(ChatColor.translateAlternateColorCodes(
                        '&', "&7Right click to equip the Default kit")).build());
                online.updateInventory();
                online.setGameMode(GameMode.SURVIVAL);
                online.setAllowFlight(false);
                online.setFlying(false);
            }
        } else if (this.tournament.getType() == TournamentType.ARCHER) {
            for (UUID players : this.players) {
                Player online = Bukkit.getPlayer(players);
                this.tournament.teleport(online, "Archer.Spawn");
            }
            this.tournament.setTournamentState(TournamentState.FIGHTING);
            this.tournament.setProtection(10);
            for (UUID players : this.players) {
                Player online = Bukkit.getPlayer(players);
                PlayerInventory inventory = online.getInventory();
                inventory.clear();
                inventory.setItem(0, new ItemBuilder(Material.ENCHANTED_BOOK).name(ChatColor.translateAlternateColorCodes(
                        '&', "&6Default Kit")).addToLore(ChatColor.translateAlternateColorCodes(
                        '&', "&7Right click to equip the Default kit")).build());
                online.updateInventory();
                online.setGameMode(GameMode.SURVIVAL);
                online.setAllowFlight(false);
                online.setFlying(false);
            }
        } else if (this.tournament.getType() == TournamentType.SUMO) {
            for (UUID players : this.players) {
                Player online = Bukkit.getPlayer(players);
                this.tournament.teleport(online, "Sumo.Spawn");
                online.getInventory().clear();
                online.setGameMode(GameMode.SURVIVAL);
                online.setAllowFlight(false);
                online.setFlying(false);
            }
            this.tournament.setTournamentState(TournamentState.STARTING);
        }
    }

    public List<UUID> getPlayers() {
        return this.players;
    }

    public boolean isCreated() {
        return this.created;
    }

    public void setCreated(final boolean s) {
        this.created = s;
    }

    public Tournament getTournament() {
        return this.tournament;
    }
}
