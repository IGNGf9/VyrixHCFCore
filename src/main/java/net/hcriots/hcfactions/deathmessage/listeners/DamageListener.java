/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.deathmessage.listeners;

import com.google.common.collect.Maps;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.deathmessage.DeathMessageHandler;
import net.hcriots.hcfactions.deathmessage.event.CustomPlayerDamageEvent;
import net.hcriots.hcfactions.deathmessage.event.PlayerKilledEvent;
import net.hcriots.hcfactions.deathmessage.objects.Damage;
import net.hcriots.hcfactions.deathmessage.objects.PlayerDamage;
import net.hcriots.hcfactions.deathmessage.util.UnknownDamage;
import net.hcriots.hcfactions.map.killstreaks.Killstreak;
import net.hcriots.hcfactions.map.killstreaks.PersistentKillstreak;
import net.hcriots.hcfactions.map.stats.StatsEntry;
import net.hcriots.hcfactions.team.Team;
import net.hcriots.hcfactions.util.Players;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class DamageListener implements Listener {

    // kit-map only
    private final Map<UUID, UUID> lastKilled = Maps.newHashMap();
    private final Map<UUID, Integer> boosting = Maps.newHashMap();

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            CustomPlayerDamageEvent customEvent = new CustomPlayerDamageEvent(event, new UnknownDamage(player.getName(), event.getDamage()));

            Hulu.getInstance().getServer().getPluginManager().callEvent(customEvent);
            DeathMessageHandler.addDamage(player, customEvent.getTrackerDamage());
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        DeathMessageHandler.clearDamage(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        List<Damage> record = DeathMessageHandler.getDamage(event.getEntity());

        event.setDeathMessage(null);

        String deathMessage;

        if (record != null) {
            Damage deathCause = record.get(record.size() - 1);

            // Hacky NMS to change the player's killer
            // System.out.println("The milliseconds since death is: " +
            // deathCause.getTimeDifference() + " this should be less than " +
            // TimeUnit.MINUTES.toMillis(1) );
            if (deathCause instanceof PlayerDamage && deathCause.getTimeDifference() < TimeUnit.MINUTES.toMillis(1)) {
                // System.out.println("Its a playerdamage thing");
                String killerName = ((PlayerDamage) deathCause).getDamager();
                Player killer = Hulu.getInstance().getServer().getPlayerExact(killerName);

                if (killer != null) {
                    ((CraftPlayer) event.getEntity()).getHandle().killer = ((CraftPlayer) killer).getHandle();

                    // kit-map death handling
                    if (Hulu.getInstance().getMapHandler().isKitMap()) {
                        Player victim = event.getEntity();

                        // Call event
                        PlayerKilledEvent killedEvent = new PlayerKilledEvent(killer, victim);
                        Hulu.getInstance().getServer().getPluginManager().callEvent(killedEvent);

                        // Prevent kill boosting
                        // Check if the victim's UUID is the same as the killer's last victim UUID
                        // Check if the victim's IP matches the killer's IP
                        if (lastKilled.containsKey(killer.getUniqueId()) && lastKilled.get(killer.getUniqueId()) == victim.getUniqueId()) {
                            boosting.putIfAbsent(killer.getUniqueId(), 0);
                            boosting.put(killer.getUniqueId(), boosting.get(killer.getUniqueId()) + 1);
                        } else {
                            boosting.put(killer.getUniqueId(), 0);
                        }

                        if (killer.equals(victim) || Players.isNaked(victim)) {
                            StatsEntry victimStats = Hulu.getInstance().getMapHandler().getStatsHandler().getStats(victim);

                            victimStats.addDeath();
                        } else if (killer.getAddress().getAddress().getHostAddress().equalsIgnoreCase(victim.getAddress().getAddress().getHostAddress())) {
                            killer.sendMessage(ChatColor.RED + "Boost Check: You've killed a player on the same IP address as you.");
                        } else if (boosting.containsKey(killer.getUniqueId()) && boosting.get(killer.getUniqueId()) > 1) {
                            killer.sendMessage(ChatColor.RED + "Boost Check: You've killed " + victim.getName() + " " + boosting.get(killer.getUniqueId()) + " times.");

                            StatsEntry victimStats = Hulu.getInstance().getMapHandler().getStatsHandler().getStats(victim);

                            victimStats.addDeath();
                        } else {
                            StatsEntry victimStats = Hulu.getInstance().getMapHandler().getStatsHandler().getStats(victim);
                            StatsEntry killerStats = Hulu.getInstance().getMapHandler().getStatsHandler().getStats(killer);

                            victimStats.addDeath();
                            killerStats.addKill();

                            lastKilled.put(killer.getUniqueId(), victim.getUniqueId());

                            Killstreak killstreak = Hulu.getInstance().getMapHandler().getKillstreakHandler().check(killerStats.getKillstreak());

                            if (killstreak != null) {
                                killstreak.apply(killer);

                                Bukkit.broadcastMessage(killer.getDisplayName() + ChatColor.YELLOW + " has received " + ChatColor.GOLD.toString() + ChatColor.BOLD + killstreak.getName() + ChatColor.YELLOW + " killstreak for killing " + ChatColor.RED + killerStats.getKills() + " players" + ChatColor.YELLOW + "!");

                                List<PersistentKillstreak> persistent = Hulu.getInstance().getMapHandler().getKillstreakHandler().getPersistentKillstreaks(killer, killerStats.getKillstreak());

                                for (PersistentKillstreak persistentStreak : persistent) {
                                    if (persistentStreak.matchesExactly(killerStats.getKillstreak())) {
                                        Bukkit.broadcastMessage(killer.getDisguisedName() + ChatColor.YELLOW + " has received " + ChatColor.GOLD.toString() + ChatColor.BOLD + killstreak.getName() + ChatColor.YELLOW + " killstreak for killing " + ChatColor.RED + killerStats.getKills() + " players" + ChatColor.YELLOW + "!");
                                    }

                                    persistentStreak.apply(killer);
                                }
                            }

                            Hulu.getInstance().getKillsMap().setKills(killer.getUniqueId(), killerStats.getKills());
                            Hulu.getInstance().getDeathsMap().setDeaths(victim.getUniqueId(), victimStats.getDeaths());
                        }
                    } else {
                        Hulu.getInstance().getKillsMap().setKills(killer.getUniqueId(), Hulu.getInstance().getKillsMap().getKills(killer.getUniqueId()) + 1);

                        if (Hulu.getInstance().getServerHandler().isHardcore()) {
                            event.getDrops().add(Hulu.getInstance().getServerHandler().generateDeathSign(event.getEntity().getName(), killer.getName()));
                        }
                    }
                }
            }

            deathMessage = deathCause.getDeathMessage();
        } else {
            deathMessage = new UnknownDamage(event.getEntity().getName(), 1).getDeathMessage();
        }

        Player killer = event.getEntity().getKiller();

        Team killerTeam = killer == null ? null : Hulu.getInstance().getTeamHandler().getTeam(killer);
        Team deadTeam = Hulu.getInstance().getTeamHandler().getTeam(event.getEntity());

        if (killerTeam != null) {
            killerTeam.setKills(killerTeam.getKills() + 1);
        }

        if (deadTeam != null) {
            deadTeam.setDeaths(deadTeam.getDeaths() + 1);
        }

        Bukkit.getScheduler().scheduleAsyncDelayedTask(Hulu.getInstance(), () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (Hulu.getInstance().getToggleDeathMessageMap().areDeathMessagesEnabled(player.getUniqueId())) {
                    player.sendMessage(deathMessage);
                } else {
                    if (Hulu.getInstance().getTeamHandler().getTeam(player) == null) {
                        continue;
                    }

                    // send them the message if the player who died was on their team
                    if (Hulu.getInstance().getTeamHandler().getTeam(event.getEntity()) != null && Hulu.getInstance().getTeamHandler().getTeam(player).equals(Hulu.getInstance().getTeamHandler().getTeam(event.getEntity()))) {
                        player.sendMessage(deathMessage);
                    }

                    // send them the message if the killer was on their team
                    if (killer != null) {
                        if (Hulu.getInstance().getTeamHandler().getTeam(killer) != null && Hulu.getInstance().getTeamHandler().getTeam(player).equals(Hulu.getInstance().getTeamHandler().getTeam(killer))) {
                            player.sendMessage(deathMessage);
                        }
                    }
                }
            }
        });

        // DeathTracker.logDeath(event.getEntity(), event.getEntity().getKiller());
        DeathMessageHandler.clearDamage(event.getEntity());
        Hulu.getInstance().getDeathsMap().setDeaths(event.getEntity().getUniqueId(), Hulu.getInstance().getDeathsMap().getDeaths(event.getEntity().getUniqueId()) + 1);
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        if (Hulu.getInstance().getMapHandler().isKitMap()) {
            checkKillstreaks(event.getPlayer());
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (Hulu.getInstance().getMapHandler().isKitMap()) {
            checkKillstreaks(event.getPlayer());
        }
    }

    private void checkKillstreaks(Player player) {
        Bukkit.getScheduler().runTaskLater(Hulu.getInstance(), () -> {
            int killstreak = Hulu.getInstance().getMapHandler().getStatsHandler().getStats(player).getKillstreak();
            List<PersistentKillstreak> persistent = Hulu.getInstance().getMapHandler().getKillstreakHandler().getPersistentKillstreaks(player, killstreak);

            for (PersistentKillstreak persistentStreak : persistent) {
                persistentStreak.apply(player);
            }
        }, 5L);
    }

    @EventHandler(ignoreCancelled = false)
    public void onRightClick(PlayerInteractEvent event) {
        if (!event.getAction().name().startsWith("RIGHT_CLICK")) {
            return;
        }

        ItemStack inHand = event.getPlayer().getItemInHand();
        if (inHand == null) {
            return;
        }

        if (inHand.getType() != Material.NETHER_STAR) {
            return;
        }

        if (!inHand.hasItemMeta() || !inHand.getItemMeta().hasDisplayName() || !inHand.getItemMeta().getDisplayName().startsWith(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "Potion Refill Token")) {
            return;
        }

        event.getPlayer().setItemInHand(null);

        ItemStack pot = new ItemStack(Material.POTION, 1, (short) 16421);
        while (event.getPlayer().getInventory().addItem(pot).isEmpty()) {
        }
    }
}