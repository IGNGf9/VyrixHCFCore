/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.scoreboard;

import cc.fyre.stark.Stark;
import cc.fyre.stark.core.util.TimeUtils;
import cc.fyre.stark.engine.scoreboard.ScoreFunction;
import cc.fyre.stark.engine.scoreboard.ScoreGetter;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.abilities.Ability;
import net.hcriots.hcfactions.commands.CustomTimerCreateCommand;
import net.hcriots.hcfactions.commands.EOTWCommand;
import net.hcriots.hcfactions.events.Event;
import net.hcriots.hcfactions.events.EventType;
import net.hcriots.hcfactions.events.conquest.game.ConquestGame;
import net.hcriots.hcfactions.events.dtc.DTC;
import net.hcriots.hcfactions.events.koth.KOTH;
import net.hcriots.hcfactions.events.purge.commands.PurgeCommands;
import net.hcriots.hcfactions.listener.GoldenAppleListener;
import net.hcriots.hcfactions.map.stats.StatsEntry;
import net.hcriots.hcfactions.pvpclasses.PvPClass;
import net.hcriots.hcfactions.pvpclasses.PvPClassHandler;
import net.hcriots.hcfactions.pvpclasses.pvpclasses.ArcherClass;
import net.hcriots.hcfactions.pvpclasses.pvpclasses.BardClass;
import net.hcriots.hcfactions.pvpclasses.pvpclasses.MageClass;
import net.hcriots.hcfactions.server.EnderpearlCooldownHandler;
import net.hcriots.hcfactions.server.ServerHandler;
import net.hcriots.hcfactions.server.SpawnTagHandler;
import net.hcriots.hcfactions.staffmode.StaffModeHandler;
import net.hcriots.hcfactions.team.Team;
import net.hcriots.hcfactions.team.claims.LandBoard;
import net.hcriots.hcfactions.team.commands.team.TeamStuckCommand;
import net.hcriots.hcfactions.team.dtr.DTRBitmask;
import net.hcriots.hcfactions.tournaments.Tournament;
import net.hcriots.hcfactions.tournaments.TournamentState;
import net.hcriots.hcfactions.tournaments.TournamentType;
import net.hcriots.hcfactions.tournaments.handler.TournamentHandler;
import net.hcriots.hcfactions.util.DurationFormatter;
import net.hcriots.hcfactions.util.Logout;
import org.bson.types.ObjectId;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public class HuluScorboard implements ScoreGetter {


    public void getScores(@NotNull LinkedList<String> scores, @NotNull Player player) {
        Team team = Hulu.getInstance().getTeamHandler().getTeam(player);

        String spawnTagScore = getSpawnTagScore(player);
        String enderpearlScore = getEnderpearlScore(player);
        String pvpTimerScore = getPvPTimerScore(player);
        String archerMarkScore = getArcherMarkScore(player);
        String bardEffectScore = getBardEffectScore(player);
        String bardEnergyScore = getBardEnergyScore(player);
        String mageEffectScore = getMageEffectScore(player);
        String mageEnergyScore = getMageEnergyScore(player);
        String fstuckScore = getFStuckScore(player);
        String logoutScore = getLogoutScore(player);
        String homeScore = getHomeScore(player);
        String appleScore = getAppleScore(player);


        TournamentHandler tournamentManager = Hulu.getInstance().getTournamentHandler();
        Tournament tournament = Hulu.getInstance().getTournamentHandler().getTournament();

        if (player.hasMetadata("frozen")) {
            scores.add("&4&lYou are Frozen");
            scores.add("&cJoin our TeamSpeak");
            scores.add("&f(ts.vyrix.us)");
            scores.add("");
        }

        if (Hulu.getInstance().getMapHandler().isKitMap()) {
            StatsEntry stats = Hulu.getInstance().getMapHandler().getStatsHandler().getStats(player.getUniqueId());

            scores.add(" &e» &fKills&7: &e" + stats.getKills());
            scores.add(" &e» &fDeaths&7: &e" + stats.getDeaths());
            scores.add(" &e» &fKillStreak&7: &e" + stats.getKillstreak());
        }

        if (Hulu.getInstance().getToggleClaimMap().isClaimedToggled(player.getUniqueId())) {
            Location loc = player.getLocation();
            Team ownerTeam = LandBoard.getInstance().getTeam(loc);
            String location;
            if (ownerTeam != null) {
                location = ownerTeam.getName(player.getPlayer());
            } else if (!Hulu.getInstance().getServerHandler().isWarzone(loc)) {
                location = ChatColor.GRAY + "Wilderness";
            } else if (LandBoard.getInstance().getTeam(loc) != null && LandBoard.getInstance().getTeam(loc).getName().equalsIgnoreCase("citadel")) {
                location = ChatColor.DARK_RED + "Citadel";
            } else {
                location = ChatColor.RED + "Warzone";
            }
            if (!Hulu.getInstance().getServerHandler().isUnclaimed(loc)) {
                scores.add("&6&lClaim&7: " + location);
            }
        }

        if (player.hasMetadata("ModMode")) {
            scores.add("&6&lStaff Mode");
            scores.add(" &e» &fVanish&7: &f" + (StaffModeHandler.getStaffModeMap().get(player).isHidden() ? "&aEnabled" : "&cDisabled"));
            scores.add(" &e» &fPlayers&7: &e" + Bukkit.getOnlinePlayers().size());
            if (player.getName().equalsIgnoreCase("Gf9") || player.hasPermission("stark.admin") && !player.getName().equals("YoloSanta")) {
                scores.add(" &e» &fTPS&7: &e" + this.formatTPS(Bukkit.getServer().spigot().getTPS()[0]));
            }
        }

        if (Hulu.getInstance().getCooldownsMap().isCooldownToggled(player.getUniqueId())) {
            final PvPClass classs = PvPClassHandler.getPvPClass(player);
            if (classs != null) {
                scores.add("&e&lClass&7: &f" + classs.getName());
            }
            if (spawnTagScore != null) {
                scores.add(("&c&lSpawn Tag&7: &f" + spawnTagScore));
                if (team != null) {
                    if (team.getHQ() != null) {
                        final Location loc = team.getHQ();
                        scores.add((" &7» &cHQ &f" + loc.getBlockX() + ", " + loc.getBlockZ()));
                    } else {
                        scores.add(" &7» &cHQ &fNot set.");
                    }
                }
            }

            if (homeScore != null) {
                scores.add("&9&lHome§7: **&f" + homeScore);
            }

            if (appleScore != null) {
                scores.add("&6&lApple&7: **&f" + appleScore);
            }

            if (enderpearlScore != null) {
                scores.add("&e&lEnderpearl&7: &f" + enderpearlScore);
            }

            if (pvpTimerScore != null) {
                scores.add("&a&lStarting Timer&7: &f" + pvpTimerScore);
            }
        }

        Iterator<Map.Entry<String, Long>> iterator = CustomTimerCreateCommand.getCustomTimers().entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Long> timer = iterator.next();
            if (timer.getValue() < System.currentTimeMillis()) {
                iterator.remove();
                continue;
            }

            if (timer.getKey().equals("&e&lSOTW")) {
                if (CustomTimerCreateCommand.hasSOTWEnabled(player.getUniqueId())) {
                    scores.add(ChatColor.translateAlternateColorCodes('&', "&e&lSOTW &f&m" + getTimerScore(timer)));
                } else {
                    scores.add(ChatColor.translateAlternateColorCodes('&', "&e&lSOTW &f" + getTimerScore(timer)));
                }
            } else {
                scores.add(ChatColor.translateAlternateColorCodes('&', timer.getKey()) + "&7: &f" + getTimerScore(timer));
            }
        }

        String dtrColored;
        double dtr;
        if (team != null) {
            if (team.isRally()) {
                Location rallyP = team.getRallyPlayer().getLocation();
                scores.add("&6&lFaction Rally");
                scores.add(" &7» &f" + rallyP.getBlockX() + ", " + rallyP.getBlockY() + ", " + rallyP.getBlockZ() + " &7" + (player.getWorld().getName().contains("End") || player.getWorld().getName().contains("Nether") ? "(" + getNiceWorldName(player.getWorld().getName()) + ")" : ""));
            }

            if (team.getFactionFocused() != null) {
                scores.add("&d&lFaction Focus");
                Team focusedFaction = team.getFactionFocused();
                scores.add(" &7» &fName: &d" + focusedFaction.getName());
                scores.add(" &7» &fOnline: &d" + focusedFaction.getOnlineMemberAmount() + "/" + focusedFaction.getMembers().size());

                dtr = Double.parseDouble((new DecimalFormat("#.##")).format(focusedFaction.getDTR()));
                if (dtr >= 1.01D) {
                    dtrColored = ChatColor.GREEN + String.valueOf(dtr);
                } else if (dtr <= 0.0D) {
                    dtrColored = ChatColor.RED + String.valueOf(dtr);
                } else {
                    dtrColored = ChatColor.YELLOW + String.valueOf(dtr);
                }

                scores.add(" &7» &fDTR: " + dtrColored + focusedFaction.getDTRSuffix());
                if (focusedFaction.getHQ() != null) {
                    scores.add(" &7» &fHome: &d" + focusedFaction.getHQ().getBlockX() + ", " + focusedFaction.getHQ().getBlockZ());
                } else {
                    scores.add(" &7» &fHome: &cNot set.");
                }
            }
        }


        Iterator<Map.Entry<String, Long>> purge = PurgeCommands.getCustomTimers().entrySet().iterator();
        while (purge.hasNext()) {
            Map.Entry<String, Long> timer = purge.next();
            if (timer.getValue() < System.currentTimeMillis()) {
                purge.remove();
                continue;
            }

            if (timer.getKey().equals("&4&lPurge")) {
                scores.add(ChatColor.translateAlternateColorCodes('&', "&4&lPurge&7: &f" + getTimerScore(timer)));
            }
        }

        for (Event event : Hulu.getInstance().getEventHandler().getEvents()) {
            if (!event.isActive() || event.isHidden()) {
                continue;
            }

            String displayName;

            switch (event.getName()) {
                case "EOTW":
                    displayName = ChatColor.DARK_RED.toString() + ChatColor.BOLD + "EOTW";
                    break;
                case "Citadel":
                    displayName = ChatColor.DARK_PURPLE.toString() + ChatColor.BOLD + "Citadel";
                    break;
                default:
                    displayName = ChatColor.BLUE.toString() + ChatColor.BOLD + event.getName();
                    break;
            }

            if (event.getType() == EventType.DTC) {
                scores.add(displayName + "&7: &f" + ((DTC) event).getCurrentPoints());
            } else {
                scores.add(displayName + "&7: &f" + ScoreFunction.Companion.TIME_FANCY(((KOTH) event).getRemainingCapTime()));
            }
        }

        if (EOTWCommand.isFfaEnabled()) {
            long ffaEnabledAt = EOTWCommand.getFfaActiveAt();
            if (System.currentTimeMillis() < ffaEnabledAt) {
                long difference = ffaEnabledAt - System.currentTimeMillis();
                scores.add("&4&lFFA&7: &f" + ScoreFunction.Companion.TIME_FANCY(difference / 1000F));
            }
        }

        if (Hulu.getInstance().getKillTheKing() != null) {
            Player king = Hulu.getInstance().getKillTheKing().getActiveKing();
            scores.add("&4&lKill the King");
            scores.add(" &fKing:&f " + king.getName());
            scores.add(" &cHealth:&f " + Math.round(king.getHealth()) / 2 + "&4❤");
            scores.add(" &cLocation:&f " + king.getLocation().getBlockX() + ", " + king.getLocation().getBlockZ());
        }

        if (archerMarkScore != null) {
            scores.add("&6&lArcher Mark&7: &f" + archerMarkScore);
        }

        if (bardEffectScore != null) {
            scores.add("&a&lBard Effect&7: &f" + bardEffectScore);
        }

        if (bardEnergyScore != null) {
            scores.add("&b&lBard Energy&7: &f" + bardEnergyScore);
        }

        if (mageEffectScore != null) {
            scores.add("&5&lMage Effect&7: &f" + mageEffectScore);
        }

        if (mageEnergyScore != null) {
            scores.add("&d&lMage Energy&7: &f" + mageEnergyScore);
        }

        if (Hulu.getInstance().getCooldownsMap().isCooldownToggled(player.getUniqueId())) {
            if (fstuckScore != null) {
                scores.add("&b&lStuck&7: &f" + fstuckScore);
            }

            if (logoutScore != null) {
                scores.add("&4&lLogout&7: &f" + logoutScore);
            }

        }
        if (DTRBitmask.SAFE_ZONE.appliesAt(player.getLocation()) && Hulu.getInstance().getConfig().getBoolean("coordsStatus.spawn")) {
            scores.add("&6&lSpawn Coords");
            scores.add((" &e» &fShop:&e " + Hulu.getInstance().getConfig().getString("scoreCoords.shop")));
            scores.add((" &e» &fCrates:&e " + Hulu.getInstance().getConfig().getString("scoreCoords.crates")));
        }

        if (player.getLocation().getWorld().getName().endsWith("_the_end")) {
            scores.add("&9End Exit&7: &f" + Hulu.getInstance().getConfig().getString("scoreCoords.endexit"));
        }

        {
            for (Ability ability : Hulu.getInstance().getAbilityHandler().getAbilities().values()) {
                if (ability.hasCooldown(player)) {
                    scores.add(ability.getDisplayName() + ChatColor.GRAY + ": " + ChatColor.WHITE + DurationFormatter.getRemaining(ability.getRemaining(player), true));
                }
            }
        }

        ConquestGame conquest = Hulu.getInstance().getConquestHandler().getGame();

        if (conquest != null) {
            if (scores.size() != 0) {
                scores.add("&c&7&m--------------------");
            }

            scores.add("&e&lConquest:");
            int displayed = 0;

            for (Map.Entry<ObjectId, Integer> entry : conquest.getTeamPoints().entrySet()) {
                Team resolved = Hulu.getInstance().getTeamHandler().getTeam(entry.getKey());

                if (resolved != null) {
                    scores.add("  " + resolved.getName(player) + "&7: &f" + entry.getValue());
                    displayed++;
                }

                if (displayed == 3) {
                    break;
                }
            }

            if (displayed == 0) {
                scores.add("  &7No scores yet");
            }
        }

        if (tournamentManager.isInTournament(player) && !player.hasMetadata("frozen")) {
            int announceCountdown = tournament.getDesecrentAnn();
            scores.add("&4&l" + tournament.getType().getName() + " Event");
            if (tournament.getType() == TournamentType.SUMO) {
                scores.add(" &cPlayers&7: &f" + tournament.getPlayers().size() + "/" + tournament.getSize());
                if (announceCountdown > 0) {
                    scores.add(" &cRound Begins&7: &f" + announceCountdown + "s");
                }
                if (tournament.getTournamentState() == TournamentState.WAITING) {
                    scores.add(" &cState&7: &fWaiting...");
                } else if (tournament.getTournamentState() == TournamentState.FIGHTING) {
                    scores.add(" &cState&7: &fFighting...");
                } else {
                    scores.add(" &cState&7: &fSelecting...");
                }
            } else if (tournament.getType() == TournamentType.DIAMOND ||
                    tournament.getType() == TournamentType.AXE ||
                    tournament.getType() == TournamentType.ARCHER ||
                    tournament.getType() == TournamentType.ROGUE) {
                scores.add(" &cPlayers&7: &f" + tournament.getPlayers().size() + "/" + tournament.getSize());
                if (announceCountdown > 0) {
                    scores.add(" &cFFA Begins&7: &f" + announceCountdown + "s");
                }
                if (tournament.getTournamentState() == TournamentState.WAITING) {
                    scores.add(" &cState&7: &fWaiting...");
                } else if (tournament.isActiveProtection()) {
                    scores.add(" &cState&7: &fImmune");
                    scores.add(" &cImmunity&7: &f" + (tournament.getProtection() / 1000) + "s");
                } else {
                    scores.add(" &cState&7: &fFighting...");
                    scores.add(" &cImmunity&7: &fDeactivated");
                }
            }
        }

        if (Stark.instance.getRebootHandler().isRebooting()) {
            scores.add("&4&lRebooting: " + TimeUtils.formatIntoMMSS(Stark.instance.getRebootHandler().rebootSecondsRemaining()));
        }


        if (!scores.isEmpty()) {
            scores.addFirst("&a&7&m--------------------");
            scores.add("");
            scores.add("&7&o" + Hulu.getInstance().getServerHandler().getNetworkWebsite());
            scores.add("&b&7&m--------------------");
        }
    }

    public String getAppleScore(Player player) {
        if (GoldenAppleListener.getCrappleCooldown().containsKey(player.getUniqueId()) && GoldenAppleListener.getCrappleCooldown().get(player.getUniqueId()) >= System.currentTimeMillis()) {
            float diff = GoldenAppleListener.getCrappleCooldown().get(player.getUniqueId()) - System.currentTimeMillis();

            if (diff >= 0) {
                return (ScoreFunction.Companion.TIME_FANCY(diff / 1000F));
            }
        }

        return (null);
    }

    public String getHomeScore(Player player) {
        if (ServerHandler.getHomeTimer().containsKey(player.getName()) && ServerHandler.getHomeTimer().get(player.getName()) >= System.currentTimeMillis()) {
            float diff = ServerHandler.getHomeTimer().get(player.getName()) - System.currentTimeMillis();

            if (diff >= 0) {
                return (ScoreFunction.Companion.TIME_FANCY(diff / 1000F));
            }
        }

        return (null);
    }

    public String getFStuckScore(Player player) {
        if (TeamStuckCommand.getWarping().containsKey(player.getName())) {
            float diff = TeamStuckCommand.getWarping().get(player.getName()) - System.currentTimeMillis();

            if (diff >= 0) {
                return (ScoreFunction.Companion.TIME_FANCY(diff / 1000F));
            }
        }

        return null;
    }

    public String getLogoutScore(Player player) {
        Logout logout = ServerHandler.getTasks().get(player.getName());

        if (logout != null) {
            float diff = logout.getLogoutTime() - System.currentTimeMillis();

            if (diff >= 0) {
                return (ScoreFunction.Companion.TIME_FANCY(diff / 1000F));
            }
        }

        return null;
    }

    public String getSpawnTagScore(Player player) {
        if (SpawnTagHandler.isTagged(player)) {
            float diff = SpawnTagHandler.getTag(player);

            if (diff >= 0) {
                return (ScoreFunction.Companion.TIME_FANCY(diff / 1000F));
            }
        }

        return null;
    }

    public String getEnderpearlScore(Player player) {
        if (EnderpearlCooldownHandler.getEnderpearlCooldown().containsKey(player.getName()) && EnderpearlCooldownHandler.getEnderpearlCooldown().get(player.getName()) >= System.currentTimeMillis()) {
            float diff = EnderpearlCooldownHandler.getEnderpearlCooldown().get(player.getName()) - System.currentTimeMillis();

            if (diff >= 0) {
                return (ScoreFunction.Companion.TIME_FANCY(diff / 1000F));
            }
        }

        return null;
    }

    public String getPvPTimerScore(Player player) {
        if (Hulu.getInstance().getPvPTimerMap() != null) {
            if (Hulu.getInstance().getPvPTimerMap().hasTimer(player.getUniqueId())) {
                int secondsRemaining = Hulu.getInstance().getPvPTimerMap().getSecondsRemaining(player.getUniqueId());

                if (secondsRemaining >= 0) {
                    return (ScoreFunction.Companion.TIME_FANCY(secondsRemaining));
                }
            }
        }
        return null;
    }

    public String getTimerScore(Map.Entry<String, Long> timer) {
        long diff = timer.getValue() - System.currentTimeMillis();

        if (diff > 0) {
            return (ScoreFunction.Companion.TIME_FANCY(diff / 1000F));
        } else {
            return null;
        }
    }

    public String getArcherMarkScore(Player player) {
        if (ArcherClass.isMarked(player)) {
            long diff = ArcherClass.getMarkedPlayers().get(player.getName()) - System.currentTimeMillis();

            if (diff > 0) {
                return (ScoreFunction.Companion.TIME_FANCY(diff / 1000F));
            }
        }

        return null;
    }

    public String getBardEffectScore(Player player) {
        if (BardClass.getLastEffectUsage().containsKey(player.getName()) && BardClass.getLastEffectUsage().get(player.getName()) >= System.currentTimeMillis()) {
            float diff = BardClass.getLastEffectUsage().get(player.getName()) - System.currentTimeMillis();

            if (diff > 0) {
                return (ScoreFunction.Companion.TIME_FANCY(diff / 1000F));
            }
        }

        return (null);
    }

    public String getBardEnergyScore(Player player) {
        if (BardClass.getEnergy().containsKey(player.getName())) {
            float energy = BardClass.getEnergy().get(player.getName());

            if (energy > 0) {
                return (String.valueOf(BardClass.getEnergy().get(player.getName())));
            }
        }

        return (null);
    }

    public String getMageEffectScore(Player player) {
        if (MageClass.getLastEffectUsage().containsKey(player.getName()) && MageClass.getLastEffectUsage().get(player.getName()) >= System.currentTimeMillis()) {
            float diff = MageClass.getLastEffectUsage().get(player.getName()) - System.currentTimeMillis();

            if (diff > 0) {
                return (ScoreFunction.Companion.TIME_FANCY(diff / 1000F));
            }
        }

        return (null);
    }

    public String getMageEnergyScore(Player player) {
        if (MageClass.getEnergy().containsKey(player.getName())) {
            float energy = MageClass.getEnergy().get(player.getName());

            if (energy > 0) {
                return (String.valueOf(MageClass.getEnergy().get(player.getName())));
            }
        }

        return (null);
    }

    private String formatTPS(final double tps) {
        return ((tps > 18.0) ? ChatColor.GREEN : ((tps > 16.0) ? ChatColor.YELLOW : ChatColor.RED)).toString() + ((tps > 20.0) ? "*" : "") + Math.min(Math.round(tps * 100.0) / 100.0, 20.0);
    }

    private String getNiceWorldName(String worldName) {
        if (worldName.endsWith("_the_end")) {
            return "End";
        } else if (worldName.endsWith("_nether")) {
            return "Nether";
        }
        return "Overworld";
    }
}