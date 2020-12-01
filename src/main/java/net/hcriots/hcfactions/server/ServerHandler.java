/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.server;

import cc.fyre.stark.util.ItemUtils;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.events.Event;
import net.hcriots.hcfactions.events.EventType;
import net.hcriots.hcfactions.server.idle.IdleCheckRunnable;
import net.hcriots.hcfactions.server.uhc.UHCListener;
import net.hcriots.hcfactions.team.Team;
import net.hcriots.hcfactions.team.claims.LandBoard;
import net.hcriots.hcfactions.team.dtr.DTRBitmask;
import net.hcriots.hcfactions.util.InventoryUtils;
import net.hcriots.hcfactions.util.Logout;
import org.bukkit.*;
import org.bukkit.World.Environment;
import org.bukkit.block.Sign;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("deprecation")
public class ServerHandler {

    @Getter
    private static final Map<String, Logout> tasks = new HashMap<>();
    @Getter
    private static final Map<String, Long> homeTimer = new ConcurrentHashMap<>();
    public static int WARZONE_RADIUS = 1000;
    public static int WARZONE_BORDER = 3000;
    // NEXT MAP //
    // http://minecraft.gamepedia.com/Potion#Data_value_table
    private final Map<PotionType, PotionStatus> potionStatus = new HashMap<>();
    @Getter
    private final String serverName;
    @Getter
    private final String networkWebsite;
    @Getter
    private final String statsWebsiteRoot;
    @Getter
    private final String tabServerName;
    @Getter
    private final String tabSectionColor;
    @Getter
    private final String tabInfoColor;
    @Getter
    private final boolean squads;
    @Getter
    private final boolean idleCheckEnabled;
    @Getter
    private final boolean startingTimerEnabled;
    @Getter
    private final boolean forceInvitesEnabled;
    @Getter
    private final boolean uhcHealing;
    @Getter
    private final boolean passiveTagEnabled;
    @Getter
    private final boolean allowBoosting;
    @Getter
    private final boolean waterPlacementInClaimsAllowed;
    @Getter
    private final boolean blockRemovalEnabled;
    @Getter
    private final boolean rodPrevention;
    @Getter
    private final boolean skybridgePrevention;
    @Getter
    private final boolean teamHQInEnemyClaims;
    @Getter
    private final boolean reduceArmorDamage;
    @Getter
    private final boolean blockEntitiesThroughPortals;
    @Getter
    private final ChatColor archerTagColor;
    @Getter
    private final ChatColor stunTagColor;
    @Getter
    private final ChatColor defaultRelationColor;
    @Getter
    private final boolean hardcore;
    @Getter
    private final boolean placeBlocksInCombat;
    private final Map<UUID, Long> playerDamageRestrictMap = Maps.newHashMap();
    private final HashMap<Sign, BukkitRunnable> showSignTasks = new HashMap<>();
    @Getter
    @Setter
    private boolean EOTW = false;
    @Getter
    @Setter
    private boolean PreEOTW = false;

    public ServerHandler() {
        serverName = Hulu.getInstance().getConfig().getString("serverName");
        networkWebsite = Hulu.getInstance().getConfig().getString("networkWebsite");
        statsWebsiteRoot = Hulu.getInstance().getConfig().getString("statsRoot");
        tabServerName = Hulu.getInstance().getConfig().getString("tab.serverName");
        tabSectionColor = Hulu.getInstance().getConfig().getString("tab.sectionColor");
        tabInfoColor = Hulu.getInstance().getConfig().getString("tab.infoColor");
        squads = Hulu.getInstance().getConfig().getBoolean("squads");
        idleCheckEnabled = Hulu.getInstance().getConfig().getBoolean("idleCheck");
        startingTimerEnabled = Hulu.getInstance().getConfig().getBoolean("startingTimer");
        forceInvitesEnabled = Hulu.getInstance().getConfig().getBoolean("forceInvites");
        uhcHealing = Hulu.getInstance().getConfig().getBoolean("uhcHealing");
        passiveTagEnabled = Hulu.getInstance().getConfig().getBoolean("passiveTag");
        allowBoosting = Hulu.getInstance().getConfig().getBoolean("allowBoosting");
        waterPlacementInClaimsAllowed = Hulu.getInstance().getConfig().getBoolean("waterPlacementInClaims");
        blockRemovalEnabled = Hulu.getInstance().getConfig().getBoolean("blockRemoval");
        rodPrevention = Hulu.getInstance().getConfig().getBoolean("rodPrevention", true);
        skybridgePrevention = Hulu.getInstance().getConfig().getBoolean("skybridgePrevention", true);
        teamHQInEnemyClaims = Hulu.getInstance().getConfig().getBoolean("teamHQInEnemyClaims", true);

        for (PotionType type : PotionType.values()) {
            if (type == PotionType.WATER) {
                continue;
            }

            PotionStatus status = new PotionStatus(Hulu.getInstance().getConfig().getBoolean("potions." + type + ".drinkables"), Hulu.getInstance().getConfig().getBoolean("potions." + type + ".splash"), Hulu.getInstance().getConfig().getInt("potions." + type + ".maxLevel", -1));
            potionStatus.put(type, status);
        }

        if (idleCheckEnabled) {
            new IdleCheckRunnable().runTaskTimer(Hulu.getInstance(), 60 * 20L, 60 * 20L);
        }

        if (uhcHealing) {
            Bukkit.getPluginManager().registerEvents(new UHCListener(), Hulu.getInstance());
        }

        this.reduceArmorDamage = Hulu.getInstance().getConfig().getBoolean("reduceArmorDamage", true);
        this.blockEntitiesThroughPortals = Hulu.getInstance().getConfig().getBoolean("blockEntitiesThroughPortals", true);
        this.archerTagColor = ChatColor.valueOf(Hulu.getInstance().getConfig().getString("archerTagColor", "YELLOW"));
        this.stunTagColor = ChatColor.valueOf(Hulu.getInstance().getConfig().getString("stunTagColor", "BLUE"));
        this.defaultRelationColor = ChatColor.valueOf(Hulu.getInstance().getConfig().getString("defaultRelationColor", "RED"));
        this.hardcore = Hulu.getInstance().getConfig().getBoolean("hardcore", false);
        this.placeBlocksInCombat = Hulu.getInstance().getConfig().getBoolean("placeBlocksInCombat", true);

        registerPlayerDamageRestrictionListener();
    }

    public String getEnchants() {
        if (Enchantment.DAMAGE_ALL.getMaxLevel() == 0 && Enchantment.PROTECTION_ENVIRONMENTAL.getMaxLevel() == 0) {
            return "No Enchants";
        } else {
            return "Prot " + Enchantment.PROTECTION_ENVIRONMENTAL.getMaxLevel() + ", Sharp " + Enchantment.DAMAGE_ALL.getMaxLevel();
        }
    }

    public boolean isWarzone(Location loc) {
        if (loc.getWorld().getEnvironment() != Environment.NORMAL) {
            return (false);
        }

        return (Math.abs(loc.getBlockX()) <= WARZONE_RADIUS && Math.abs(loc.getBlockZ()) <= WARZONE_RADIUS) || ((Math.abs(loc.getBlockX()) > WARZONE_BORDER || Math.abs(loc.getBlockZ()) > WARZONE_BORDER));
    }

    public boolean isSplashPotionAllowed(PotionType type) {
        return (!potionStatus.containsKey(type) || potionStatus.get(type).splash);
    }

    public boolean isDrinkablePotionAllowed(PotionType type) {
        return (!potionStatus.containsKey(type) || potionStatus.get(type).drinkables);
    }

    public boolean isPotionLevelAllowed(PotionType type, int amplifier) {
        return (!potionStatus.containsKey(type) || potionStatus.get(type).maxLevel == -1 || potionStatus.get(type).maxLevel >= amplifier);
    }

    public void startLogoutSequence(final Player player) {
        player.sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Logging out... " + ChatColor.YELLOW + "Please wait" + ChatColor.RED + " 30" + ChatColor.YELLOW + " seconds.");

        BukkitTask taskid = new BukkitRunnable() {

            int seconds = 30;

            @Override
            public void run() {
                if (player.hasMetadata("frozen")) {
                    player.sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD + "LOGOUT " + ChatColor.RED.toString() + ChatColor.BOLD + "CANCELLED!");
//                    CheatBreakerKey.LOGOUT.clear(player);
                    cancel();
                    return;
                }

                seconds--;

                if (seconds == 0) {
                    if (tasks.containsKey(player.getName())) {
                        tasks.remove(player.getName());
                        player.setMetadata("loggedout", new FixedMetadataValue(Hulu.getInstance(), true));
                        player.kickPlayer("§cYou have been safely logged out of the server!");
                        cancel();
                    }
                }

            }
        }.runTaskTimer(Hulu.getInstance(), 20L, 20L);

        tasks.put(player.getName(), new Logout(taskid.getTaskId(), System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(30)));
    }

    public RegionData getRegion(Team ownerTo, Location location) {
        if (ownerTo != null && ownerTo.getOwner() == null) {
            if (ownerTo.hasDTRBitmask(DTRBitmask.SAFE_ZONE)) {
                return (new RegionData(RegionType.SPAWN, ownerTo));
            } else if (ownerTo.hasDTRBitmask(DTRBitmask.KOTH)) {
                return (new RegionData(RegionType.KOTH, ownerTo));
            } else if (ownerTo.hasDTRBitmask(DTRBitmask.CITADEL)) {
                return (new RegionData(RegionType.CITADEL, ownerTo));
            } else if (ownerTo.hasDTRBitmask(DTRBitmask.ROAD)) {
                return (new RegionData(RegionType.ROAD, ownerTo));
            } else if (ownerTo.hasDTRBitmask(DTRBitmask.CONQUEST)) {
                return (new RegionData(RegionType.CONQUEST, ownerTo));
            }
        }

        if (ownerTo != null) {
            return (new RegionData(RegionType.CLAIMED_LAND, ownerTo));
        } else if (isWarzone(location)) {
            return (new RegionData(RegionType.WARZONE, null));
        }

        return (new RegionData(RegionType.WILDNERNESS, null));
    }

    public boolean isUnclaimed(Location loc) {
        return (LandBoard.getInstance().getClaim(loc) == null && !isWarzone(loc));
    }

    public boolean isAdminOverride(Player player) {
        return (player.getGameMode() == GameMode.CREATIVE);
    }

    public Location getSpawnLocation() {
        return (Hulu.getInstance().getServer().getWorld("world").getSpawnLocation().add(new Vector(0.5, 1, 0.5)));
    }

    public boolean isUnclaimedOrRaidable(Location loc) {
        Team owner = LandBoard.getInstance().getTeam(loc);
        return (owner == null || owner.isRaidable());
    }

    public double getDTRLoss(Player player) {
        return (getDTRLoss(player.getLocation()));
    }

    public double getDTRLoss(Location location) {
        double dtrLoss = 1.00D;

        if (Hulu.getInstance().getMapHandler().isKitMap()) {
            dtrLoss = Math.min(dtrLoss, 0.01D);
        }

        Team ownerTo = LandBoard.getInstance().getTeam(location);

        if (Hulu.getInstance().getConquestHandler().getGame() != null && location.getWorld().getEnvironment() == Environment.THE_END && ownerTo != null && ownerTo.hasDTRBitmask(DTRBitmask.CONQUEST)) {
            dtrLoss = Math.min(dtrLoss, 0.50D);
        }

        if (ownerTo != null) {
            if (ownerTo.hasDTRBitmask(DTRBitmask.QUARTER_DTR_LOSS)) {
                dtrLoss = Math.min(dtrLoss, 0.25D);
            } else if (ownerTo.hasDTRBitmask(DTRBitmask.REDUCED_DTR_LOSS)) {
                dtrLoss = Math.min(dtrLoss, 0.75D);
            }
        }

        return (dtrLoss);
    }

    public long getDeathban(Player player) {
        return (getDeathban(player.getUniqueId(), player.getLocation()));
    }

    public long getDeathban(UUID playerUUID, Location location) {
        // Things we already know and can easily eliminate.
        if (isPreEOTW()) {
            return (TimeUnit.DAYS.toSeconds(1000));
        } else if (Hulu.getInstance().getMapHandler().isKitMap()) {
            return (TimeUnit.SECONDS.toSeconds(5));
        }

        Team ownerTo = LandBoard.getInstance().getTeam(location);
        Player player = Hulu.getInstance().getServer().getPlayer(playerUUID); // Used in various checks down below.

        // Check DTR flags, which will also take priority over playtime.
        if (ownerTo != null && ownerTo.getOwner() == null) {
            Event linkedKOTH = Hulu.getInstance().getEventHandler().getEvent(ownerTo.getName());

            // Only respect the reduced deathban if
            // The KOTH is non-existant (in which case we're probably
            // something like a 1v1 arena) or it is active.
            // If it's there but not active,
            // the null check will be false, the .isActive will be false, so we'll ignore
            // the reduced DB check.
            if (linkedKOTH == null || linkedKOTH.isActive()) {
                if (ownerTo.hasDTRBitmask(DTRBitmask.FIVE_MINUTE_DEATHBAN)) {
                    return (TimeUnit.MINUTES.toSeconds(5));
                } else if (ownerTo.hasDTRBitmask(DTRBitmask.FIFTEEN_MINUTE_DEATHBAN)) {
                    return (TimeUnit.MINUTES.toSeconds(15));
                }
            }
        }

        return Deathban.getDeathbanSeconds(player);
    }

    public void beginHQWarp(final Player player, final Team team, int warmup) {
        Team inClaim = LandBoard.getInstance().getTeam(player.getLocation());

        // quick fix
        if (team.getBalance() < 0) {
            team.setBalance(0);
        }

        if (inClaim != null) {
            if (Hulu.getInstance().getServerHandler().isHardcore() && inClaim.getOwner() != null && !inClaim.isMember(player.getUniqueId())) {
                player.sendMessage(ChatColor.RED + "You may not go to your team headquarters from an enemy's claim! Use '/team stuck' first.");
                return;
            }

            if (inClaim.getOwner() == null && (inClaim.hasDTRBitmask(DTRBitmask.KOTH) || inClaim.hasDTRBitmask(DTRBitmask.CITADEL))) {
                player.sendMessage(ChatColor.RED + "You may not go to your team headquarters from inside of events!");
                return;
            }

            if (inClaim.hasDTRBitmask(DTRBitmask.SAFE_ZONE)) {
                if (player.getWorld().getEnvironment() != Environment.THE_END) {
                    player.sendMessage(ChatColor.YELLOW + "Warping to " + ChatColor.LIGHT_PURPLE + team.getName() + ChatColor.YELLOW + "'s HQ.");
                    player.teleport(team.getHQ());
                } else {
                    player.sendMessage(ChatColor.RED + "You cannot teleport to your end headquarters while you're in end spawn!");
                }
                return;
            }
        }


        if (SpawnTagHandler.isTagged(player)) {
            player.sendMessage(ChatColor.RED + "You may not go to your team headquarters while spawn tagged!");
            return;
        }

        player.sendMessage(ChatColor.YELLOW + "Teleporting to your team's HQ in " + ChatColor.LIGHT_PURPLE + warmup + " seconds" + ChatColor.YELLOW + "... Stay still and do not take damage.");

        /**
         * Give player heads up now. They should have 10 seconds to move even just an inch to cancel the tp if they want
         */
        if (Hulu.getInstance().getPvPTimerMap().hasTimer(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "Your PvP Timer will be removed if the teleport is not cancelled.");
        }

        homeTimer.put(player.getName(), System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(warmup));

//        CheatBreakerKey.HOME.send(player, homeTimer.get(player.getName()) - System.currentTimeMillis());

        final int finalWarmup = warmup;

        new BukkitRunnable() {

            final Location startLocation = player.getLocation();
            int time = finalWarmup;
            double startHealth = player.getHealth();

            @Override
            public void run() {
                time--;

                if (!player.getLocation().getWorld().equals(startLocation.getWorld()) || player.getLocation().distanceSquared(startLocation) >= 0.1 || player.getHealth() < startHealth) {
                    player.sendMessage(ChatColor.YELLOW + "Teleport cancelled.");
//                    CheatBreakerKey.HOME.clear(player);
                    homeTimer.remove(player.getName());
                    cancel();
                    return;
                }

                // Reset their previous health, so players can't start on 1/2 a heart, splash, and then be able to take damage before warping.
                startHealth = player.getHealth();

                // Prevent server lag from making the home time inaccurate.
                if (homeTimer.containsKey(player.getName()) && homeTimer.get(player.getName()) <= System.currentTimeMillis()) {
                    if (Hulu.getInstance().getPvPTimerMap().hasTimer(player.getUniqueId())) {
                        Hulu.getInstance().getPvPTimerMap().removeTimer(player.getUniqueId());
                    }

                    for (EnderPearl enderPearl : player.getWorld().getEntitiesByClass(EnderPearl.class)) {
                        if (enderPearl.getShooter() != null && enderPearl.getShooter().equals(player)) {
                            enderPearl.remove();
                        }
                    }

                    player.sendMessage(ChatColor.YELLOW + "Warping to " + ChatColor.LIGHT_PURPLE + team.getName() + ChatColor.YELLOW + "'s HQ.");
                    player.teleport(team.getHQ());
                    homeTimer.remove(player.getName());
                    cancel();
                    return;
                }

                // After testing, this code is actually run sometimes. I'm going to leave it. FIXME
                if (time == 0) {
                    // Remove their PvP timer.
                    if (Hulu.getInstance().getPvPTimerMap().hasTimer(player.getUniqueId())) {
                        Hulu.getInstance().getPvPTimerMap().removeTimer(player.getUniqueId());
                    }

                    for (EnderPearl enderPearl : player.getWorld().getEntitiesByClass(EnderPearl.class)) {
                        if (enderPearl.getShooter() != null && enderPearl.getShooter().equals(player)) {
                            enderPearl.remove();
                        }
                    }

                    player.sendMessage(ChatColor.YELLOW + "Warping to " + ChatColor.RED + team.getName() + ChatColor.YELLOW + "'s HQ.");
                    player.teleport(team.getHQ());
                    homeTimer.remove(player.getName());
                    cancel();
                }
            }

        }.runTaskTimer(Hulu.getInstance(), 20L, 20L);
    }

    public void disablePlayerAttacking(final Player player, int seconds) {
        if (seconds == 10) {
            player.sendMessage(ChatColor.GRAY + "You cannot attack for " + seconds + " seconds.");
        }

        playerDamageRestrictMap.put(player.getUniqueId(), System.currentTimeMillis() + (seconds * 1000));
    }

    private void registerPlayerDamageRestrictionListener() {
        Hulu.getInstance().getServer().getPluginManager().registerEvents(new Listener() {
            @EventHandler(ignoreCancelled = true)
            public void onDamage(EntityDamageByEntityEvent event) {
                Long expiry = playerDamageRestrictMap.get(event.getDamager().getUniqueId());
                if (expiry != null && System.currentTimeMillis() < expiry) {
                    event.setCancelled(true);
                }
            }

            @EventHandler
            public void onQuit(PlayerQuitEvent event) {
                playerDamageRestrictMap.remove(event.getPlayer().getUniqueId());
            }
        }, Hulu.getInstance());
    }

    public boolean isSpawnBufferZone(Location loc) {
        if (loc.getWorld().getEnvironment() != Environment.NORMAL) {
            return (false);
        }

        int radius = Hulu.getInstance().getMapHandler().getWorldBuffer();
        int x = loc.getBlockX();
        int z = loc.getBlockZ();

        return ((x < radius && x > -radius) && (z < radius && z > -radius));
    }

    public boolean isNetherBufferZone(Location loc) {
        if (loc.getWorld().getEnvironment() != Environment.NETHER) {
            return (false);
        }

        int radius = Hulu.getInstance().getMapHandler().getNetherBuffer();
        int x = loc.getBlockX();
        int z = loc.getBlockZ();

        return ((x < radius && x > -radius) && (z < radius && z > -radius));
    }

    public void handleShopSign(Sign sign, Player player) {
        ItemStack itemStack = (sign.getLine(2).contains("Crowbar") ? InventoryUtils.CROWBAR : ItemUtils.get(sign.getLine(2).toLowerCase().replace(" ", "")));

        if (itemStack == null && sign.getLine(2).contains("Antidote")) {
            itemStack = InventoryUtils.ANTIDOTE;
        }

        if (itemStack == null) {
            System.err.println(sign.getLine(2).toLowerCase().replace(" ", ""));
            return;
        }

        if (sign.getLine(0).toLowerCase().contains("buy")) {
            int price;
            int amount;

            try {
                price = Integer.parseInt(sign.getLine(3).replace("$", "").replace(",", ""));
                amount = Integer.parseInt(sign.getLine(1));
            } catch (NumberFormatException e) {
                return;
            }

            if (Hulu.getInstance().getEconomyHandler().getBalance(player.getUniqueId()) >= price) {
                if (Hulu.getInstance().getEconomyHandler().getBalance(player.getUniqueId()) > 600000) {
                    player.sendMessage("§cYour balance is too high. Please contact an admin to do this.");
                    Bukkit.getLogger().severe("[ECONOMY] " + player.getName() + " tried to buy shit at spawn with over 100K.");
                    return;
                }


                if (Double.isNaN(Hulu.getInstance().getEconomyHandler().getBalance(player.getUniqueId()))) {
                    Hulu.getInstance().getEconomyHandler().setBalance(player.getUniqueId(), 0);
                    player.sendMessage("§cYour balance was fucked, but we unfucked it.");
                    return;
                }

                if (player.getInventory().firstEmpty() != -1) {
                    Hulu.getInstance().getEconomyHandler().withdraw(player.getUniqueId(), price);

                    itemStack.setAmount(amount);
                    player.getInventory().addItem(itemStack);
                    player.updateInventory();

                    showSignPacket(player, sign,
                            "§aBOUGHT§r " + amount,
                            "for §a$" + NumberFormat.getNumberInstance(Locale.US).format(price),
                            "New Balance:",
                            "§a$" + NumberFormat.getNumberInstance(Locale.US).format((int) Hulu.getInstance().getEconomyHandler().getBalance(player.getUniqueId()))
                    );
                } else {
                    showSignPacket(player, sign,
                            "§c§lError!",
                            "",
                            "§cNo space",
                            "§cin inventory!"
                    );
                }
            } else {
                showSignPacket(player, sign,
                        "§cInsufficient",
                        "§cfunds for",
                        sign.getLine(2),
                        sign.getLine(3)
                );
            }
        } else if (sign.getLine(0).toLowerCase().contains("sell")) {
            double pricePerItem;
            int amount;

            try {
                int price = Integer.parseInt(sign.getLine(3).replace("$", "").replace(",", ""));
                amount = Integer.parseInt(sign.getLine(1));

                pricePerItem = (float) price / (float) amount;
            } catch (NumberFormatException e) {
                return;
            }

            int amountInInventory = Math.min(amount, countItems(player, itemStack.getType(), itemStack.getDurability()));

            if (amountInInventory == 0) {
                showSignPacket(player, sign,
                        "§cYou do not",
                        "§chave any",
                        sign.getLine(2),
                        "§con you!"
                );
            } else {
                int totalPrice = (int) (amountInInventory * pricePerItem);

                removeItem(player, itemStack, amountInInventory);
                player.updateInventory();

                Hulu.getInstance().getEconomyHandler().deposit(player.getUniqueId(), totalPrice);

                showSignPacket(player, sign,
                        "§aSOLD§r " + amountInInventory,
                        "for §a$" + NumberFormat.getNumberInstance(Locale.US).format(totalPrice),
                        "New Balance:",
                        "§a$" + NumberFormat.getNumberInstance(Locale.US).format((int) Hulu.getInstance().getEconomyHandler().getBalance(player.getUniqueId()))
                );
            }
        }
    }

    public void handleKitSign(Sign sign, Player player) {
        String kit = ChatColor.stripColor(sign.getLine(1));

        if (kit.equalsIgnoreCase("Fishing")) {
            int uses = Hulu.getInstance().getFishingKitMap().getUses(player.getUniqueId());

            if (uses == 3) {
                showSignPacket(player, sign, "§aFishing Kit:", "", "§cAlready used", "§c3/3 times!");
            } else {
                ItemStack rod = new ItemStack(Material.FISHING_ROD);

                rod.addEnchantment(Enchantment.LURE, 2);
                player.getInventory().addItem(rod);
                player.updateInventory();
                player.sendMessage(ChatColor.GOLD + "Equipped the " + ChatColor.WHITE + "Fishing" + ChatColor.GOLD + " kit!");
                Hulu.getInstance().getFishingKitMap().setUses(player.getUniqueId(), uses + 1);
                showSignPacket(player, sign, "§aFishing Kit:", "§bEquipped!", "", "§dUses: §e" + (uses + 1) + "/3");
            }
        }
    }

    public void removeItem(Player p, ItemStack it, int amount) {
        boolean specialDamage = it.getType().getMaxDurability() == (short) 0;

        for (int a = 0; a < amount; a++) {
            for (ItemStack i : p.getInventory()) {
                if (i != null) {
                    if (i.getType() == it.getType() && (!specialDamage || it.getDurability() == i.getDurability())) {
                        if (i.getAmount() == 1) {
                            p.getInventory().clear(p.getInventory().first(i));
                            break;
                        } else {
                            i.setAmount(i.getAmount() - 1);
                            break;
                        }
                    }
                }
            }
        }

    }

    public ItemStack generateDeathSign(String killed, String killer) {
        ItemStack deathsign = new ItemStack(Material.SIGN);
        ItemMeta meta = deathsign.getItemMeta();

        ArrayList<String> lore = new ArrayList<>();

        lore.add("§4" + killed);
        lore.add("§eSlain By:");
        lore.add("§a" + killer);

        DateFormat sdf = new SimpleDateFormat("M/d HH:mm:ss");

        lore.add(sdf.format(new Date()).replace(" AM", "").replace(" PM", ""));

        meta.setLore(lore);
        meta.setDisplayName("§dDeath Sign");
        deathsign.setItemMeta(meta);

        return (deathsign);
    }

    public ItemStack generateKOTHSign(String koth, String capper, EventType eventType) {
        ItemStack kothsign = new ItemStack(Material.SIGN);
        ItemMeta meta = kothsign.getItemMeta();

        ArrayList<String> lore = new ArrayList<>();

        lore.add("§9" + koth);
        lore.add("§eCaptured By:");
        lore.add("§a" + capper);

        DateFormat sdf = new SimpleDateFormat("M/d HH:mm:ss");

        lore.add(sdf.format(new Date()).replace(" AM", "").replace(" PM", ""));

        meta.setLore(lore);
        meta.setDisplayName("§d" + eventType.name() + "Capture Sign");
        kothsign.setItemMeta(meta);

        return (kothsign);
    }

    public void showSignPacket(Player player, final Sign sign, String... lines) {
        player.sendSignChange(sign.getLocation(), lines);

        if (showSignTasks.containsKey(sign)) {
            showSignTasks.remove(sign).cancel();
        }

        BukkitRunnable br = new BukkitRunnable() {

            @Override
            public void run() {
                sign.update();
                showSignTasks.remove(sign);
            }

        };

        showSignTasks.put(sign, br);
        br.runTaskLater(Hulu.getInstance(), 90L);
    }

    public int countItems(Player player, Material material, int damageValue) {
        PlayerInventory inventory = player.getInventory();
        ItemStack[] items = inventory.getContents();
        int amount = 0;

        for (ItemStack item : items) {
            if (item != null) {
                boolean specialDamage = material.getMaxDurability() == (short) 0;

                if (item.getType() != null && item.getType() == material && (!specialDamage || item.getDurability() == (short) damageValue)) {
                    amount += item.getAmount();
                }
            }
        }

        return (amount);
    }

    @AllArgsConstructor
    private class PotionStatus {

        private final boolean drinkables;
        private final boolean splash;
        private final int maxLevel;

    }

}
