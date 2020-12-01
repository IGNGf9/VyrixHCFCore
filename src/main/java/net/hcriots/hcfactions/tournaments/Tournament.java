/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.tournaments;

import cc.fyre.stark.util.ItemBuilder;
import lombok.Getter;
import lombok.Setter;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.tournaments.config.TournamentFile;
import net.hcriots.hcfactions.tournaments.util.LocationUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Created by InspectMC
 * Date: 8/3/2020
 * Time: 5:46 PM
 */

@Getter
@Setter
public class Tournament {

    private Hulu plugin;
    private Set<UUID> players;
    private HashMap<Player, ItemStack[]> playerArmor;
    private HashMap<Player, ItemStack[]> playerInventory;
    private Set<UUID> matches;
    private int id;
    private int size;

    private TournamentState tournamentState = TournamentState.WAITING;
    private int currentRound = 1;
    private int countdown;
    private int announceCountdown;
    private TournamentType type;
    private Player hoster;
    private TournamentFile file = TournamentFile.getConfig();
    private Player firstPlayer;
    private Player secondPlayer;
    private long protection;
    private int pregame;

    public Tournament(int size, TournamentType type, Player player) {
        this.plugin = Hulu.getInstance();
        this.players = new HashSet<>();
        this.playerArmor = new HashMap<>();
        this.playerInventory = new HashMap<>();
        this.matches = new HashSet<>();
        this.tournamentState = TournamentState.WAITING;
        this.currentRound = 1;
        this.file = TournamentFile.getConfig();
        this.size = size;
        this.type = type;
        this.hoster = player;
        this.countdown = 11;
        this.pregame = 6;
        this.announceCountdown = 60;
    }

    public boolean isActiveProtection() {
        return getProtection() / 1000.0D > 0.0D;
    }

    public long getProtection() {
        return this.protection - System.currentTimeMillis();
    }

    public void setProtection(int time) {
        this.protection = (System.currentTimeMillis() + (time + 1) * 1000L);
    }

    public void teleport(Player player, String location) {
        player.teleport(LocationUtils.getLocation(this.file.getString("Locations." + location)));
    }

    public void addPlayer(UUID uuid) {
        this.players.add(uuid);
    }

    public void removePlayer(UUID uuid) {
        this.players.remove(uuid);
    }

    public void saveInventory(Player player) {
        this.playerArmor.put(player, player.getInventory().getArmorContents());
        this.playerInventory.put(player, player.getInventory().getContents());
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
    }

    public void rollbackInventory(Player player) {
        player.getInventory().setArmorContents(this.playerArmor.get(player));
        player.getInventory().setContents(this.playerInventory.get(player));
    }

    public void giveItemWait(Player player) {
        player.getInventory().setItem(8, new ItemBuilder(Material.NETHER_STAR).name(ChatColor.translateAlternateColorCodes('&', "&cLeave the Event")).addToLore(ChatColor.translateAlternateColorCodes('&', "&7Right click to leave")).build());

    }

    public void broadcast(String message) {
        for (UUID uuid : this.players) {
            Player player = this.plugin.getServer().getPlayer(uuid);

            player.sendMessage(message);
        }
    }

    public void broadcastWithSound(String message, Sound sound) {
        for (UUID uuid : this.players) {
            Player player = this.plugin.getServer().getPlayer(uuid);

            player.sendMessage(message);
            player.playSound(player.getLocation(), sound, 10.0F, 1.0F);
        }
    }

    public int decrementCountdown() {
        return --this.countdown;
    }

    public int decrementAnnounce() {
        return --this.announceCountdown;
    }

    public int getDesecrentAnn() {
        return this.announceCountdown;
    }

    public int getCooldown() {
        return this.countdown;
    }
}