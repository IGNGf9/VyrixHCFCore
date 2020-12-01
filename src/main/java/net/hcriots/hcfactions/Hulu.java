/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions;

import cc.fyre.stark.Stark;
import cc.fyre.stark.engine.economy.EconomyHandler;
import com.comphenix.protocol.ProtocolLibrary;
import com.mongodb.MongoClient;
import lombok.Getter;
import lombok.Setter;
import net.hcriots.hcfactions.abilities.AbilityHandler;
import net.hcriots.hcfactions.bosses.eggs.BossEggHandler;
import net.hcriots.hcfactions.bosses.eggs.listener.DaggerListener;
import net.hcriots.hcfactions.bosses.listener.BossDeathListener;
import net.hcriots.hcfactions.challenges.listener.ChallengeListener;
import net.hcriots.hcfactions.chat.ChatHandler;
import net.hcriots.hcfactions.credit.CreditHandler;
import net.hcriots.hcfactions.credit.listener.CreditListener;
import net.hcriots.hcfactions.deathmessage.DeathMessageHandler;
import net.hcriots.hcfactions.deathmessage.listeners.DamageListener;
import net.hcriots.hcfactions.editor.listener.KitEditorSignListener;
import net.hcriots.hcfactions.elo.EloListener;
import net.hcriots.hcfactions.events.EventHandler;
import net.hcriots.hcfactions.events.blackmarket.BlackMarketHandler;
import net.hcriots.hcfactions.events.citadel.CitadelHandler;
import net.hcriots.hcfactions.events.conquest.ConquestHandler;
import net.hcriots.hcfactions.events.purge.listener.PurgeListener;
import net.hcriots.hcfactions.events.region.cavern.CavernHandler;
import net.hcriots.hcfactions.events.region.glowmtn.GlowHandler;
import net.hcriots.hcfactions.killtheking.KillTheKing;
import net.hcriots.hcfactions.killtheking.listener.KillTheKingListener;
import net.hcriots.hcfactions.listener.*;
import net.hcriots.hcfactions.map.MapHandler;
import net.hcriots.hcfactions.packetborder.PacketBorderThread;
import net.hcriots.hcfactions.persist.RedisSaveTask;
import net.hcriots.hcfactions.persist.maps.*;
import net.hcriots.hcfactions.persist.maps.statistics.*;
import net.hcriots.hcfactions.protocol.ClientCommandPacketAdaper;
import net.hcriots.hcfactions.protocol.SignGUIPacketAdaper;
import net.hcriots.hcfactions.pvpclasses.PvPClassHandler;
import net.hcriots.hcfactions.reclaim.ReclaimHandler;
import net.hcriots.hcfactions.reclaim.config.ReclaimConfigFile;
import net.hcriots.hcfactions.server.EnderpearlCooldownHandler;
import net.hcriots.hcfactions.server.ServerHandler;
import net.hcriots.hcfactions.spawners.SpawnerShopListener;
import net.hcriots.hcfactions.staffmode.StaffModeHandler;
import net.hcriots.hcfactions.staffmode.listener.StaffModeListener;
import net.hcriots.hcfactions.task.ServerFakeFreezeTask;
import net.hcriots.hcfactions.team.TeamHandler;
import net.hcriots.hcfactions.team.claims.LandBoard;
import net.hcriots.hcfactions.team.commands.team.TeamClaimCommand;
import net.hcriots.hcfactions.team.commands.team.subclaim.TeamSubclaimCommand;
import net.hcriots.hcfactions.team.dtr.DTRHandler;
import net.hcriots.hcfactions.team.upgrades.TeamUpgrade;
import net.hcriots.hcfactions.tips.TipsHandler;
import net.hcriots.hcfactions.tournaments.handler.TournamentHandler;
import net.hcriots.hcfactions.tournaments.listener.TournamentListener;
import net.hcriots.hcfactions.util.Color;
import net.hcriots.hcfactions.util.Cooldowns;
import net.hcriots.hcfactions.util.RegenUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.spigotmc.SpigotConfig;
import redis.clients.jedis.BinaryJedis;

import java.util.Arrays;
import java.util.stream.Collectors;

@Getter
@Setter
public class Hulu extends JavaPlugin {

    @Getter
    @Setter
    public static String MONGO_DB_NAME;
    @Getter
    private static Hulu instance;
    private MongoClient mongoPool;
    private EconomyHandler economyHandler;
    private ChatHandler chatHandler;
    private PvPClassHandler pvpClassHandler;
    private TeamHandler teamHandler;
    private ServerHandler serverHandler;
    private TournamentHandler tournamentHandler;
    private TipsHandler tipsHandler;
    private MapHandler mapHandler;
    private CitadelHandler citadelHandler;
    private EventHandler eventHandler;
    private ConquestHandler conquestHandler;
    private CavernHandler cavernHandler;
    private GlowHandler glowHandler;
    private ELOMap eloMap;
    private CreditsMap creditsMap;
    private ArcherKillsMap archerKillsMap;

    private PlaytimeMap playtimeMap;
    private OppleMap oppleMap;
    private DeathbanMap deathbanMap;
    private PvPTimerMap PvPTimerMap;
    private StartingPvPTimerMap startingPvPTimerMap;
    private DeathsMap deathsMap;
    private KillsMap killsMap;
    private ChatModeMap chatModeMap;
    private FishingKitMap fishingKitMap;
    private ToggleGlobalChatMap toggleGlobalChatMap;
    private ToggleClaimMap toggleClaimMap;
    private LanguageMap languageMap;
    private TipsMap tipsMap;
    private CooldownsMap cooldownsMap;
    private TabListModeMap tabListModeMap;
    private DtrMap dtrMap;

    private ChatSpyMap chatSpyMap;
    private DiamondMinedMap diamondMinedMap;
    private GoldMinedMap goldMinedMap;
    private IronMinedMap ironMinedMap;
    private CoalMinedMap coalMinedMap;
    private RedstoneMinedMap redstoneMinedMap;
    private LapisMinedMap lapisMinedMap;
    private EmeraldMinedMap emeraldMinedMap;
    private FirstJoinMap firstJoinMap;
    private LastJoinMap lastJoinMap;
    private SoulboundLivesMap soulboundLivesMap;
    private FriendLivesMap friendLivesMap;
    private BaseStatisticMap enderPearlsUsedMap;
    private BaseStatisticMap expCollectedMap;
    private BaseStatisticMap itemsRepairedMap;
    private BaseStatisticMap splashPotionsBrewedMap;
    private BaseStatisticMap splashPotionsUsedMap;
    private WrappedBalanceMap wrappedBalanceMap;
    private ToggleFoundDiamondsMap toggleFoundDiamondsMap;
    private ToggleDeathMessageMap toggleDeathMessageMap;
    private IPMap ipMap;
    private WhitelistedIPMap whitelistedIPMap;
    private CobblePickupMap cobblePickupMap;
    private KDRMap kdrMap;
    private KitmapTokensMap tokensMap;
    private LFFMap lffMap;

    private AbilityHandler abilityHandler;
    private BossEggHandler bossEggHandler;

    private CombatLoggerListener combatLoggerListener;
    private KillTheKing killTheKing;
    private ReclaimConfigFile reclaimConfig;
    private ReclaimHandler reclaimHandler;
    private CreditHandler creditHandler;
    private BlackMarketHandler blackMarketHandler;

    @Override
    public void onEnable() {
        SpigotConfig.onlyCustomTab = true;
        instance = this;
        saveDefaultConfig();
        reclaimConfig = new ReclaimConfigFile(this, "reclaims", this.getDataFolder().getAbsolutePath());

        try {
            mongoPool = new MongoClient(getConfig().getString("Mongo.Host", "127.0.0.1"));
            MONGO_DB_NAME = getConfig().getString("Mongo.DBName");
        } catch (Exception e) {
            e.printStackTrace();
        }

        (new DTRHandler()).runTaskTimer(this, 20L, 1200L);
        (new RedisSaveTask()).runTaskTimerAsynchronously(this, 1200L, 1200L);
        (new PacketBorderThread()).start();

        setupHandlers();

        setupPersistence();
        setupListeners();
        createCoolDowns();

        ProtocolLibrary.getProtocolManager().addPacketListener(new SignGUIPacketAdaper());
        ProtocolLibrary.getProtocolManager().addPacketListener(new ClientCommandPacketAdaper());
        for (World world : Bukkit.getWorlds()) {
            world.setThundering(false);
            world.setStorm(false);
            world.setWeatherDuration(Integer.MAX_VALUE);
            world.setGameRuleValue("doFireTick", "false");
            world.setGameRuleValue("mobGriefing", "false");
        }

        new BukkitRunnable() {
            public void run() {
                String players = Bukkit.getOnlinePlayers().stream().filter(player -> player.hasPermission("top.rank") && !player.isOp() && !player.hasPermission("*")).map(HumanEntity::getName).collect(Collectors.joining(", "));
                if (players.isEmpty()) {
                    players = "No online users";
                }
                Bukkit.broadcastMessage(Color.translate("&6&lOnline &f&k! &4&lMaster &f&k! &6&lUsers ") + ChatColor.GRAY + "» " + ChatColor.WHITE + players);
                Bukkit.broadcastMessage(Color.translate(" &e» &fYou can purchase the rank at &6&ostore.vyrix.us"));
            }
        }.runTaskTimer(this, 200L, 18000L);
        new ServerFakeFreezeTask().runTaskTimerAsynchronously(this, 20L, 20L);
        new HuluTasks().runTaskTimerAsynchronously(this, 0L, 20L);
        new RallyTasks();

        DeathMessageHandler.init();

        Cooldowns.createCooldown("bs1");
        Cooldowns.createCooldown("bs2");
        Cooldowns.createCooldown("bs3");
        Cooldowns.createCooldown("restricted");
    }

    @Override
    public void onDisable() {
        getEventHandler().saveEvents();
        this.getServer().getOnlinePlayers().stream().filter(StaffModeHandler::hasStaffMode).forEach(it -> StaffModeHandler.getStaffModeMap().get(it).remove());
        for (Player player : Hulu.getInstance().getServer().getOnlinePlayers()) {
            getPlaytimeMap().playerQuit(player.getUniqueId(), false);
            player.setMetadata("loggedout", new FixedMetadataValue(this, true));
        }

        for (String playerName : PvPClassHandler.getEquippedKits().keySet()) {
            PvPClassHandler.getEquippedKits().get(playerName).remove(getServer().getPlayerExact(playerName));
        }

        for (Entity e : this.combatLoggerListener.getCombatLoggers()) {
            if (e != null) {
                e.remove();
            }
        }

        RedisSaveTask.save(null, false);
        if (getInstance().getMapHandler().isKitMap()) {
            getInstance().getMapHandler().getStatsHandler().save();
        }

        RedisSaveTask.save(null, false);
        if (getInstance().getMapHandler().isKitMap()) {
            Hulu.getInstance().getMapHandler().getStatsHandler().save();
        }

        RegenUtils.resetAll();
        Stark.instance.getCore().redis.runRedisCommand(BinaryJedis::save);

        blackMarketHandler.disable();
    }

    private void setupHandlers() {
        serverHandler = new ServerHandler();
        tipsHandler = new TipsHandler();

        mapHandler = new MapHandler();
        mapHandler.load();

        teamHandler = new TeamHandler();
        tournamentHandler = new TournamentHandler();

        LandBoard.getInstance().loadFromTeams();

        economyHandler = new EconomyHandler();
        economyHandler.load();
        reclaimHandler = new ReclaimHandler();
        blackMarketHandler = new BlackMarketHandler(this);

        if (getServer().getPluginManager().getPlugin("WorldEdit") != null && getConfig().getBoolean("blackMarket.enabled", false)) {
            this.blackMarketHandler = new BlackMarketHandler(this);
            String[] location = getConfig().getString("blackMarket.location").split(",");
            Double[] locationNumbers = Arrays.stream(location).map(Double::parseDouble).toArray(Double[]::new);
            this.blackMarketHandler.setLocation(new Location(getServer().getWorld("world"), locationNumbers[0], locationNumbers[1], locationNumbers[2]));
        }

        chatHandler = new ChatHandler();
        citadelHandler = new CitadelHandler();
        pvpClassHandler = new PvPClassHandler();

        eventHandler = new EventHandler();
        conquestHandler = new ConquestHandler();

        if (getConfig().getBoolean("glowstoneMountain", false)) {
            glowHandler = new GlowHandler();
        }

        if (getConfig().getBoolean("cavern", false)) {
            cavernHandler = new CavernHandler();
        }
        abilityHandler = new AbilityHandler();
        bossEggHandler = new BossEggHandler();

        Stark.instance.getCommandHandler().registerAll(this);
        DTRHandler.loadDTR();
    }

    private void setupListeners() {
        getServer().getPluginManager().registerEvents(new MapListener(), this);
        getServer().getPluginManager().registerEvents(new AntiGlitchListener(), this);
        getServer().getPluginManager().registerEvents(new BasicPreventionListener(), this);
        getServer().getPluginManager().registerEvents(new BorderListener(), this);
        getServer().getPluginManager().registerEvents((combatLoggerListener = new CombatLoggerListener()), this);
        getServer().getPluginManager().registerEvents(new CrowbarListener(), this);
        getServer().getPluginManager().registerEvents(new DeathbanListener(), this);
        getServer().getPluginManager().registerEvents(new EnchantmentLimiterListener(), this);
        getServer().getPluginManager().registerEvents(new EnderpearlCooldownHandler(), this);
        getServer().getPluginManager().registerEvents(new EndListener(), this);
        getServer().getPluginManager().registerEvents(new FoundDiamondsListener(), this);
        getServer().getPluginManager().registerEvents(new FoxListener(), this);
        getServer().getPluginManager().registerEvents(new GoldenAppleListener(), this);
        getServer().getPluginManager().registerEvents(new KOTHRewardKeyListener(), this);
        getServer().getPluginManager().registerEvents(new PvPTimerListener(), this);
        getServer().getPluginManager().registerEvents(new PotionLimiterListener(), this);
        getServer().getPluginManager().registerEvents(new NetherPortalListener(), this);
        getServer().getPluginManager().registerEvents(new PortalTrapListener(), this);
        getServer().getPluginManager().registerEvents(new SignSubclaimListener(), this);
        getServer().getPluginManager().registerEvents(new SpawnListener(), this);
        getServer().getPluginManager().registerEvents(new SpawnTagListener(), this);
        getServer().getPluginManager().registerEvents(new StaffUtilsListener(), this);
        getServer().getPluginManager().registerEvents(new TeamListener(), this);
        getServer().getPluginManager().registerEvents(new WebsiteListener(), this);
        getServer().getPluginManager().registerEvents(new TeamSubclaimCommand(), this);
        getServer().getPluginManager().registerEvents(new TeamClaimCommand(), this);
        getServer().getPluginManager().registerEvents(new StatTrakListener(), this);
        getServer().getPluginManager().registerEvents(new TeamRequestSpamListener(), this);
        getServer().getPluginManager().registerEvents(new StaffModeListener(), this);
        getServer().getPluginManager().registerEvents(new PotRefillSignListener(), this);
        getServer().getPluginManager().registerEvents(new EloListener(), this);
        getServer().getPluginManager().registerEvents(new CreditListener(), this);
        getServer().getPluginManager().registerEvents(new SpawnerShopListener(this), this);
        getServer().getPluginManager().registerEvents(new KitEditorSignListener(), this);
        getServer().getPluginManager().registerEvents(new DamageListener(), this);
        getServer().getPluginManager().registerEvents(new KillTheKingListener(), this);
        getServer().getPluginManager().registerEvents(new SkullDropListener(), this);
        getServer().getPluginManager().registerEvents(new MobSackerListener(), this);
        getServer().getPluginManager().registerEvents(new ElevatorListener(), this);
        getServer().getPluginManager().registerEvents(new StrengthListener(), this);
        getServer().getPluginManager().registerEvents(new TournamentListener(), this);
        getServer().getPluginManager().registerEvents(new PurgeListener(), this);

        if (getServerHandler().isReduceArmorDamage()) {
            getServer().getPluginManager().registerEvents(new ArmorDamageListener(), this);
        }

        if (getServerHandler().isBlockEntitiesThroughPortals()) {
            getServer().getPluginManager().registerEvents(new EntityPortalListener(), this);
        }

        if (getServerHandler().isBlockRemovalEnabled()) {
            getServer().getPluginManager().registerEvents(new BlockRegenListener(), this);
        }

        if (getMapHandler().isKitMap()) {
            getServer().getPluginManager().registerEvents(new KitMapListener(), this);
        }
        getServer().getPluginManager().registerEvents(new BossDeathListener(), this);
        TeamUpgrade.register();
        getServer().getPluginManager().registerEvents(new BlockConvenienceListener(), this);
        getServer().getPluginManager().registerEvents(new DaggerListener(), this);

        // senta
        getServer().getPluginManager().registerEvents(new ChallengeListener(), this);
        getServer().getPluginManager().registerEvents(new SentaListener(), this);
    }

    private void createCoolDowns() {
        Cooldowns.createCooldown("lff");
        Cooldowns.createCooldown("buildegg");
        Cooldowns.createCooldown("TOURNAMENT_COOLDOWN");
    }

    private void setupPersistence() {
        (playtimeMap = new PlaytimeMap()).loadFromRedis();
        (oppleMap = new OppleMap()).loadFromRedis();
        (deathbanMap = new DeathbanMap()).loadFromRedis();
        (PvPTimerMap = new PvPTimerMap()).loadFromRedis();
        (startingPvPTimerMap = new StartingPvPTimerMap()).loadFromRedis();
        (deathsMap = new DeathsMap()).loadFromRedis();
        (eloMap = new ELOMap()).loadFromRedis();
        (creditsMap = new CreditsMap()).loadFromRedis();
        (killsMap = new KillsMap()).loadFromRedis();
        (chatModeMap = new ChatModeMap()).loadFromRedis();
        (toggleGlobalChatMap = new ToggleGlobalChatMap()).loadFromRedis();
        (toggleClaimMap = new ToggleClaimMap()).loadFromRedis();
        (languageMap = new LanguageMap()).loadFromRedis();
        (tipsMap = new TipsMap()).loadFromRedis();
        (cooldownsMap = new CooldownsMap()).loadFromRedis();
        (tabListModeMap = new TabListModeMap()).loadFromRedis();
        (dtrMap = new DtrMap()).loadFromRedis();

        (fishingKitMap = new FishingKitMap()).loadFromRedis();
        (soulboundLivesMap = new SoulboundLivesMap()).loadFromRedis();
        (friendLivesMap = new FriendLivesMap()).loadFromRedis();
        (chatSpyMap = new ChatSpyMap()).loadFromRedis();
        (diamondMinedMap = new DiamondMinedMap()).loadFromRedis();
        (goldMinedMap = new GoldMinedMap()).loadFromRedis();
        (ironMinedMap = new IronMinedMap()).loadFromRedis();
        (coalMinedMap = new CoalMinedMap()).loadFromRedis();
        (redstoneMinedMap = new RedstoneMinedMap()).loadFromRedis();
        (lapisMinedMap = new LapisMinedMap()).loadFromRedis();
        (emeraldMinedMap = new EmeraldMinedMap()).loadFromRedis();
        (firstJoinMap = new FirstJoinMap()).loadFromRedis();
        (lastJoinMap = new LastJoinMap()).loadFromRedis();
        (enderPearlsUsedMap = new EnderPearlsUsedMap()).loadFromRedis();
        (expCollectedMap = new ExpCollectedMap()).loadFromRedis();
        (itemsRepairedMap = new ItemsRepairedMap()).loadFromRedis();
        (splashPotionsBrewedMap = new SplashPotionsBrewedMap()).loadFromRedis();
        (splashPotionsUsedMap = new SplashPotionsUsedMap()).loadFromRedis();
        (wrappedBalanceMap = new WrappedBalanceMap()).loadFromRedis();
        (toggleFoundDiamondsMap = new ToggleFoundDiamondsMap()).loadFromRedis();
        (toggleDeathMessageMap = new ToggleDeathMessageMap()).loadFromRedis();
        (ipMap = new IPMap()).loadFromRedis();
        (whitelistedIPMap = new WhitelistedIPMap()).loadFromRedis();
        (cobblePickupMap = new CobblePickupMap()).loadFromRedis();
        (kdrMap = new KDRMap()).loadFromRedis();
        (lffMap = new LFFMap()).loadFromRedis();

        if (getMapHandler().isKitMap()) {
            (tokensMap = new KitmapTokensMap()).loadFromRedis();
            (archerKillsMap = new ArcherKillsMap()).loadFromRedis();
        }
    }
}