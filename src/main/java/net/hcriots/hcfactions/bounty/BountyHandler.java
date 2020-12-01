/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.bounty;

import cc.fyre.stark.Stark;
import cc.fyre.stark.engine.command.Command;
import cc.fyre.stark.engine.command.data.parameter.Param;
import cc.fyre.stark.util.ItemBuilder;
import lombok.Getter;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.commands.CustomTimerCreateCommand;
import net.hcriots.hcfactions.team.Team;
import net.hcriots.hcfactions.team.claims.LandBoard;
import net.hcriots.hcfactions.team.dtr.DTRBitmask;
import net.minecraft.util.com.google.common.collect.ImmutableList;
import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.PotionEffectAddEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by InspectMC
 * Date: 7/16/2020
 * Time: 8:57 PM
 */

public class BountyHandler implements Listener {

    public static final Random rand = new Random();
    private static final String bountyPrefix = "&7[&4&lBounty&7] ";
    @Getter
    private static UUID currentBountyPlayer;
    boolean pickingNewBounty = false;
    private long lastPositionBroadcastMessage = -1L;
    private long lastSuitablePositionTime = -1L;
    private int secondsUnsuitable = 0;
    private Reward reward;

    public BountyHandler() {
        Stark.instance.getCommandHandler().registerClass(this.getClass());

        Bukkit.getScheduler().runTaskTimer(Hulu.getInstance(), this::checkBounty, 20L, 20L);
    }

    @Command(names = "bounty set", permission = "bounty.set", async = true)
    public static void setBounty(@Param(name = "target") Player target) {
        currentBountyPlayer = target.getUniqueId();
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', bountyPrefix + " &eA &cBounty &ehas been placed on " + target.getDisplayName() + "&e."));
    }

    @Command(names = {"bounty", "bounty coords"}, permission = "")
    public static void coords(Player sender) {
        Player player = currentBountyPlayer == null ? null : Bukkit.getPlayer(currentBountyPlayer);

        if (player == null) {
            sender.sendMessage(ChatColor.RED + "There's no bounty active right now.");
            return;
        }

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', bountyPrefix + player.getDisplayName() + " &ehas been spotted at &c" + player.getLocation().getBlockX() + "," + player.getLocation().getBlockY() + ", " + player.getLocation().getBlockZ() + "&e."));
    }

    private void checkBounty() {

        if (CustomTimerCreateCommand.isSOTWTimer()) {
            currentBountyPlayer = null;
            return;
        }

        Player targetBountyPlayer = currentBountyPlayer == null ? null : Bukkit.getPlayer(currentBountyPlayer);

        if ((targetBountyPlayer == null || !targetBountyPlayer.isOnline()) && !pickingNewBounty) {
            newBounty();
            return;
        }

        if (!isSuitable(targetBountyPlayer)) {
            if (1000 < System.currentTimeMillis() - lastSuitablePositionTime) {
                if (30 <= secondsUnsuitable++) {
                    currentBountyPlayer = null;
                    secondsUnsuitable = 0;
                    newBounty();
                }
            }
        } else {
            lastSuitablePositionTime = System.currentTimeMillis();
            secondsUnsuitable = 0;
        }

        checkBroadcast();
    }

    private void newBounty() {

        if (CustomTimerCreateCommand.isSOTWTimer()) {
            currentBountyPlayer = null;
            return;
        }

        this.pickingNewBounty = true;

        if (Bukkit.getOnlinePlayers().size() < 10) {
            return;
        }

        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', bountyPrefix + "&eA &cBounty &ewill be placed on a random player in &a30 seconds&e."));
        Bukkit.getScheduler().runTaskLater(Hulu.getInstance(), () -> {
            pickNewBounty();
        }, 30 * 20);
    }

    private void pickNewBounty() {
        List<Player> suitablePlayers = Bukkit.getOnlinePlayers().stream().filter(this::isSuitable).collect(Collectors.toList());
        if (suitablePlayers.isEmpty()) {
            Bukkit.getScheduler().runTaskLater(Hulu.getInstance(), this::pickNewBounty, 20L);
            return;
        }

        if (!pickingNewBounty) {
            return;
        }

        Player bountyPlayer = suitablePlayers.get(rand.nextInt(suitablePlayers.size()));
        pickingNewBounty = false;
        setBounty(bountyPlayer);
        this.reward = Reward.values()[rand.nextInt(Reward.values().length)];
    }

    private void checkBroadcast() {
        Player player = currentBountyPlayer == null ? null : Bukkit.getPlayer(currentBountyPlayer);

        if (player == null) {
            return;
        }

        if (15000 <= System.currentTimeMillis() - lastPositionBroadcastMessage) {
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', bountyPrefix + currentBountyPlayer + " &ehas been spotted @ &c" + player.getLocation().getBlockX() + ", " + player.getLocation().getBlockY() + ", " + player.getLocation().getBlockZ() + "&e."));
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&aReward&7: &f" + reward.name));
            lastPositionBroadcastMessage = System.currentTimeMillis();
        }
    }

    private boolean isSuitable(Player player) {

        if (player == null) {
            return false;
        }

        if (player.getGameMode() == GameMode.CREATIVE) {
            return false;
        }

        if (player.hasMetadata("ModMode") || player.hasMetadata("modmode")) {
            return false;
        }

        if (150 <= player.getLocation().getY()) {
            return false;
        }

        if (player.getWorld().getEnvironment() != World.Environment.NORMAL) {
            return false;
        }

        Team teamAt = LandBoard.getInstance().getTeam(player.getLocation());

        if (teamAt != null && !teamAt.hasDTRBitmask(DTRBitmask.ROAD)) {
            return false;
        }


        if (player.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
            return false;
        }

        if (500 < Math.abs(player.getLocation().getX()) || 500 < Math.abs(player.getLocation().getZ())) {
            return false;
        }

        return Hulu.getInstance().getServerHandler().isWarzone(player.getLocation());
    }

    @EventHandler(ignoreCancelled = true)
    public void onPotionAdd(PotionEffectAddEvent event) {
        LivingEntity player = event.getEntity();
        if (!player.getUniqueId().equals(currentBountyPlayer)) {
            return;
        }

        if (event.getEffect().getType().equals(PotionEffectType.INVISIBILITY)) {
            event.setCancelled(true);

            if (player instanceof Player) {
                ((Player) player).sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou currently have a bounty on and cannot pot an invis."));
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onKill(PlayerDeathEvent event) {
        Player died = event.getEntity();
        Player killer = event.getEntity().getKiller();

        if (killer == null) {
            return;
        }

        if (!died.getUniqueId().equals(currentBountyPlayer)) {
            return;
        }

        currentBountyPlayer = null;

        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', bountyPrefix + died.getDisplayName() + " &ehas been slain by &f" + killer.getDisplayName() + "&e."));

        reward.action.reward(killer);
    }

    private enum Reward {
        TWO_HUNDRED_FIFTY_DOLLARS("$250", player -> {
            Hulu.getInstance().getEconomyHandler().deposit(player.getUniqueId(), 250);
        }),

        FIVE_HUNDRED_DOLLARS("$500", player -> {
            Hulu.getInstance().getEconomyHandler().deposit(player.getUniqueId(), 500);
        }),

        SEVEN_HUNDRED_FIFTY_DOLLARS("$750", player -> {
            Hulu.getInstance().getEconomyHandler().deposit(player.getUniqueId(), 750);
        }),

        ONE_BOUNTY_KEY("1 Bounty Key", player -> {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "cr givekey " + player.getName() + " bounty 1");
        }),

        TWO_BOUNTY_KEYS("2 Bounty Keys", player -> {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "cr givekey " + player.getName() + " bounty 2");
        }),

        THREE_BOUNTY_KEYS("3 Bounty Keys", player -> {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "cr givekey " + player.getName() + " bounty 3");
        }),

        CRAPPLES("1 Golden Apple", player -> {
            player.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE));
        }),

        GOD_APPLE("1 God Apple", player -> {
            player.getInventory().addItem(ItemBuilder.of(Material.GOLDEN_APPLE).data((short) 1).build());
        }),

        COBWEBS("8 Cobwebs", player -> {
            player.getInventory().addItem(ItemBuilder.of(Material.WEB).amount(8).build());
        }),

        REFILL_POTS("Potion Refill Token", player -> {
            player.getInventory().addItem(ItemBuilder.of(Material.NETHER_STAR).name("&c&lPotion Refill Token").setUnbreakable(true).setLore(ImmutableList.of("&cRight click this to fill your inventory with potions!")).build());
        });

        private final String name;
        private final RewardAction action;

        Reward(String name, RewardAction action) {
            this.name = name;
            this.action = action;
        }
    }

    private interface RewardAction {
        void reward(Player player);
    }
}