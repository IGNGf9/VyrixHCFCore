/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.pvpclasses.pvpclasses;

import cc.fyre.stark.Stark;
import cc.fyre.stark.core.util.Pair;
import cc.fyre.stark.core.util.TimeUtils;
import lombok.Getter;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.deathmessage.DeathMessageHandler;
import net.hcriots.hcfactions.deathmessage.event.PlayerKilledEvent;
import net.hcriots.hcfactions.deathmessage.trackers.ArrowTracker;
import net.hcriots.hcfactions.pvpclasses.PvPClass;
import net.hcriots.hcfactions.pvpclasses.PvPClassHandler;
import net.hcriots.hcfactions.pvpclasses.pvpclasses.archer.ArcherUpgrade;
import net.hcriots.hcfactions.pvpclasses.pvpclasses.archer.commands.ArcherUpgradesCommand;
import net.hcriots.hcfactions.pvpclasses.pvpclasses.archer.commands.SetArcherKillsCommand;
import net.hcriots.hcfactions.pvpclasses.pvpclasses.archer.upgrades.*;
import net.hcriots.hcfactions.server.SpawnTagHandler;
import net.hcriots.hcfactions.team.Team;
import net.hcriots.hcfactions.team.dtr.DTRBitmask;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class ArcherClass extends PvPClass {

    @Getter
    private static final List<ArcherUpgrade> archerUpgrades = Arrays.asList(
            new PythonArcherUpgrade(),
            new MedusaArcherUpgrade(),
            new HadesArcherUpgrade(),

            new ApolloArcherUpgrade(),
            new ZeusArcherUpgrade(),
            new SicknessArcherUpgrade(),
            new StealerArcherUpgrade()

    );


    private static final int MARK_SECONDS = 5;

    private static final Map<String, Long> lastSpeedUsage = new HashMap<>();
    private static final Map<String, Long> lastJumpUsage = new HashMap<>();
    @Getter
    private static final Map<String, Long> markedPlayers = new ConcurrentHashMap<>();
    @Getter
    private static final Map<String, Set<Pair<String, Long>>> markedBy = new HashMap<>();

    public ArcherClass() {
        super("Archer", 15, Arrays.asList(Material.SUGAR, Material.FEATHER));
        if (Hulu.getInstance().getMapHandler().isKitMap()) {
            Stark.instance.getCommandHandler().registerClass(ArcherUpgradesCommand.class);
            Stark.instance.getCommandHandler().registerClass(SetArcherKillsCommand.class);
        }
    }

    public static boolean isMarked(Player player) {
        return (getMarkedPlayers().containsKey(player.getName()) && getMarkedPlayers().get(player.getName()) > System.currentTimeMillis());
    }

    @Override
    public boolean qualifies(PlayerInventory armor) {
        return wearingAllArmor(armor) &&
                armor.getHelmet().getType() == Material.LEATHER_HELMET &&
                armor.getChestplate().getType() == Material.LEATHER_CHESTPLATE &&
                armor.getLeggings().getType() == Material.LEATHER_LEGGINGS &&
                armor.getBoots().getType() == Material.LEATHER_BOOTS;
    }

    @Override
    public void apply(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2), true);
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0), true);

        if (Hulu.getInstance().getMapHandler().isKitMap()) {
            for (ArcherUpgrade ability : archerUpgrades) {
                if (ability.applies(player) && Hulu.getInstance().getArcherKillsMap().getArcherKills(player.getUniqueId()) < ability.getKillsNeeded()) {
                    player.sendMessage(ChatColor.RED + "You have an upgraded Archer set equipped but do not meet the requirements to use it's abilities.");
                    player.sendMessage(ChatColor.RED + "To track your unlock progress, use the /archerupgrades command.");
                }
            }
        }
    }

    @Override
    public void tick(Player player) {
        if (!player.hasPotionEffect(PotionEffectType.SPEED)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));
        }

        if (!player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0));
        }

        super.tick(player);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityArrowHit(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getDamager();
            final Player victim = (Player) event.getEntity();

            if (!(arrow.getShooter() instanceof Player)) {
                return;
            }

            Player shooter = (Player) arrow.getShooter();
            float pullback = arrow.getMetadata("Pullback").get(0).asFloat();

            if (!PvPClassHandler.hasKitOn(shooter, this)) {
                return;
            }

            // 2 hearts for a marked shot
            // 1.5 hearts for a marking / unmarked shot.
            int damage = isMarked(victim) ? 4 : 3; // Ternary for getting damage!

            // If the bow isn't 100% pulled back we do 1 heart no matter what.
            if (pullback < 0.5F) {
                damage = 2; // 1 heart
            }

            if (victim.getHealth() - damage <= 0D) {
                event.setCancelled(true);
            } else {
                event.setDamage(0D);
            }

            // The 'ShotFromDistance' metadata is applied in the deathmessage module.
            Location shotFrom = (Location) arrow.getMetadata("ShotFromDistance").get(0).value();
            double distance = shotFrom.distance(victim.getLocation());

            DeathMessageHandler.addDamage(victim, new ArrowTracker.ArrowDamageByPlayer(victim.getName(), damage, ((Player) arrow.getShooter()).getName(), shotFrom, distance));
            victim.setHealth(Math.max(0D, victim.getHealth() - damage));

            if (PvPClassHandler.hasKitOn(victim, this)) {
                shooter.sendMessage(ChatColor.YELLOW + "[" + ChatColor.BLUE + "Arrow Range" + ChatColor.YELLOW + " (" + ChatColor.RED + (int) distance + ChatColor.YELLOW + ")] " + ChatColor.RED + "Cannot mark other Archers. " +
                        ChatColor.BLUE.toString() + ChatColor.BOLD + "(" + damage / 2 + " heart" + ((damage / 2 == 1) ? "" : "s") + ")");
            } else if (pullback >= 0.5F) {
                shooter.sendMessage(
                        ChatColor.YELLOW + "[" + ChatColor.BLUE + "Arrow Range" + ChatColor.YELLOW + " (" + ChatColor.RED + (int) distance + ChatColor.YELLOW + ")] " + ChatColor.GOLD + "Marked player for " + MARK_SECONDS + " seconds. " +
                                ChatColor.BLUE.toString() + ChatColor.BOLD + "(" + damage / 2 + " heart" + ((damage / 2 == 1) ? "" : "s") + ")");

                // Only send the message if they're not already marked.
                if (!isMarked(victim)) {
                    victim.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "Marked! " + ChatColor.YELLOW + "An archer has shot you and marked you (+25% damage) for " + MARK_SECONDS + " seconds.");
                }

                PotionEffect invis = null;

                for (PotionEffect potionEffect : victim.getActivePotionEffects()) {
                    if (potionEffect.getType().equals(PotionEffectType.INVISIBILITY)) {
                        invis = potionEffect;
                        break;
                    }
                }

                if (invis != null) {
                    PvPClass playerClass = PvPClassHandler.getPvPClass(victim);

                    victim.removePotionEffect(invis.getType());

                    final PotionEffect invisFinal = invis;

                    /* Handle returning their invisibility after the archer tag is done */
                    if (playerClass instanceof MinerClass) {
                        /* Queue player to have invis returned. (MinerClass takes care of this) */
                        ((MinerClass) playerClass).getInvis().put(victim.getName(), MARK_SECONDS);
                    } else {
                        /* player has no class but had invisibility, return it after their tag expires */
                        new BukkitRunnable() {

                            public void run() {
                                if (invisFinal.getDuration() > 1_000_000) {
                                    return; // Ensure we never apply an infinite invis to a non miner
                                }

                                victim.addPotionEffect(invisFinal);
                            }

                        }.runTaskLater(Hulu.getInstance(), (MARK_SECONDS * 20) + 5);
                    }
                }

                getMarkedPlayers().put(victim.getName(), System.currentTimeMillis() + (MARK_SECONDS * 1000));

                getMarkedBy().putIfAbsent(shooter.getName(), new HashSet<>());
                getMarkedBy().get(shooter.getName()).add(new Pair<>(victim.getName(), System.currentTimeMillis() + (MARK_SECONDS * 1000)));

                Stark.instance.getNametagEngine().reloadPlayer(victim);

                new BukkitRunnable() {
                    public void run() {
                        Stark.instance.getNametagEngine().reloadPlayer(victim);
                    }
                }.runTaskLater(Hulu.getInstance(), (MARK_SECONDS * 20) + 5);

                // Check for special Archer archerUpgrades if kitmap
                if (Hulu.getInstance().getMapHandler().isKitMap()) {
                    if (!ArcherUpgrade.canUseAbility(shooter)) {
                        shooter.sendMessage(ChatColor.RED + "You can't use your Archer Upgrade ability for another " + ChatColor.BOLD + TimeUtils.formatIntoDetailedString((int) (ArcherUpgrade.getRemainingTime(shooter) / 1000)) + ChatColor.RED + ".");
                        return;
                    }

                    for (ArcherUpgrade ability : archerUpgrades) {
                        if (Hulu.getInstance().getArcherKillsMap().getArcherKills(shooter.getUniqueId()) >= ability.getKillsNeeded()) {
                            if (ability.applies(shooter)) {
                                ability.onHit(shooter, victim);
                                ArcherUpgrade.setCooldown(shooter, 10);
                                break;
                            }
                        }
                    }
                }
            } else {
                shooter.sendMessage(ChatColor.YELLOW + "[" + ChatColor.BLUE + "Arrow Range" + ChatColor.YELLOW + " (" + ChatColor.RED + (int) distance + ChatColor.YELLOW + ")] " + ChatColor.RED + "Bow wasn't fully drawn back. " +
                        ChatColor.BLUE.toString() + ChatColor.BOLD + "(" + damage / 2 + " heart" + ((damage / 2 == 1) ? "" : "s") + ")");
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            if (isMarked(player)) {
                Player damager = null;
                if (event.getDamager() instanceof Player) {
                    damager = (Player) event.getDamager();
                } else if (event.getDamager() instanceof Projectile && ((Projectile) event.getDamager()).getShooter() instanceof Player) {
                    damager = (Player) ((Projectile) event.getDamager()).getShooter();
                }

                if (damager != null && !canUseMark(damager, player)) {
                    return;
                }

                // Apply 125% damage if they're 'marked'
                event.setDamage(event.getDamage() * 1.25D);
            }
        }
    }

    @EventHandler
    public void onEntityShootBow(EntityShootBowEvent event) {
        event.getProjectile().setMetadata("Pullback", new FixedMetadataValue(Hulu.getInstance(), event.getForce()));
    }

    @EventHandler
    public void onPlayerKilledEvent(PlayerKilledEvent event) {
        if (PvPClassHandler.hasKitOn(event.getKiller(), this)) {
            Hulu.getInstance().getArcherKillsMap().increment(event.getKiller().getUniqueId());
        }

        if (Hulu.getInstance().getMapHandler().isKitMap()) {
            for (ArcherUpgrade upgrade : archerUpgrades) {
                if (Hulu.getInstance().getArcherKillsMap().getArcherKills(event.getKiller().getUniqueId()) == upgrade.getKillsNeeded()) {
                    event.getKiller().sendMessage(ChatColor.GREEN + "You've unlocked the " + ChatColor.AQUA + ChatColor.BOLD + upgrade.getUpgradeName() + ChatColor.GREEN + " Archer Upgrade!");
                    break;
                }
            }
        }
    }

    @Override
    public boolean itemConsumed(Player player, Material material) {
        if (material == Material.SUGAR) {
            if (lastSpeedUsage.containsKey(player.getName()) && lastSpeedUsage.get(player.getName()) > System.currentTimeMillis()) {
                long millisLeft = lastSpeedUsage.get(player.getName()) - System.currentTimeMillis();
                String msg = TimeUtils.formatIntoDetailedString((int) millisLeft / 1000);

                player.sendMessage(ChatColor.RED + "You cannot use this for another §c§l" + msg + "§c.");
                return (false);
            }

            lastSpeedUsage.put(player.getName(), System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(30));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 3), true);
            return (true);
        } else {
            if (DTRBitmask.SAFE_ZONE.appliesAt(player.getLocation())) {
                player.sendMessage(ChatColor.RED + "You can't use this in spawn!");
                return (false);
            }

            if (lastJumpUsage.containsKey(player.getName()) && lastJumpUsage.get(player.getName()) > System.currentTimeMillis()) {
                long millisLeft = lastJumpUsage.get(player.getName()) - System.currentTimeMillis();
                String msg = TimeUtils.formatIntoDetailedString((int) millisLeft / 1000);

                player.sendMessage(ChatColor.RED + "You cannot use this for another §c§l" + msg + "§c.");
                return (false);
            }

            lastJumpUsage.put(player.getName(), System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20 * 5, 4));

            SpawnTagHandler.addPassiveSeconds(player, SpawnTagHandler.getMaxTagTime());
            return (false);
        }
    }

    private boolean canUseMark(Player player, Player victim) {
        if (Hulu.getInstance().getTeamHandler().getTeam(player) != null) {
            Team team = Hulu.getInstance().getTeamHandler().getTeam(player);

            int amount = 0;
            for (Player member : team.getOnlineMembers()) {
                if (PvPClassHandler.hasKitOn(member, this)) {
                    amount++;

                    if (amount > 3) {
                        break;
                    }
                }
            }

            if (amount > 3) {
                player.sendMessage(ChatColor.RED + "Your team has too many archers. Archer mark was not applied.");
                return false;
            }
        }

        if (markedBy.containsKey(player.getName())) {
            for (Pair<String, Long> pair : markedBy.get(player.getName())) {
                if (victim.getName().equals(pair.getKey()) && pair.getValue() > System.currentTimeMillis()) {
                    return false;
                }
            }

            return true;
        } else {
            return true;
        }
    }

}
