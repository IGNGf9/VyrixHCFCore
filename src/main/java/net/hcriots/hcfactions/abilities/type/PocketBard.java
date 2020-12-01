/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.abilities.type;

import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.abilities.Ability;
import net.hcriots.hcfactions.team.Team;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class PocketBard extends Ability {

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public Material getMaterial() {
        return Material.INK_SACK;
    }

    @Override
    public String getDisplayName() {
        return ChatColor.GOLD.toString() + ChatColor.BOLD + "Pocket Bard";
    }

    @Override
    public List<String> getLore() {
        List<String> toReturn = new ArrayList();
        toReturn.add("&7Right-Click to give all positive");
        toReturn.add("&7effects to yourself and your faction!");
        return toReturn;
    }

    @Override
    public long getCooldown() {
        return (long) (60.0 * 1000L);
    }

    @EventHandler
    public void onPocketBard(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (!this.isSimilar(player.getItemInHand())) {
                return;
            }

            if (this.hasCooldown(e.getPlayer())) {
                this.sendCooldownMessage(e.getPlayer());
                e.setCancelled(true);
                return;
            }

            if (this.isSimilar(player.getItemInHand())) {
                List<Player> valid = new ArrayList<>();
                Team team = Hulu.getInstance().getTeamHandler().getTeam(player);

                this.applyCooldown(player);
                if (Hulu.getInstance().getTeamHandler().getTeam(player) == null) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 120, 1), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 120, 2), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 120, 2), true);
                    player.sendMessage(new String[]{
                            ChatColor.translateAlternateColorCodes('&', "&7&m----------------------------------------------------"),
                            ChatColor.GRAY + "You have used your " + this.getDisplayName(),
                            "",
                            ChatColor.GRAY + "(No Team Found) You have been given: ",
                            ChatColor.YELLOW + "Strength II: " + ChatColor.WHITE + "5 seconds",
                            ChatColor.YELLOW + "Resistance III: " + ChatColor.WHITE + "5 seconds",
                            ChatColor.YELLOW + "Regeneration III: " + ChatColor.WHITE + "5 seconds",
                            ChatColor.translateAlternateColorCodes('&', "&7&m----------------------------------------------------"),

                    });
                    player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
                } else {
                    team.getOnlineMembers().forEach(beaner -> beaner.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 120, 1)));
                    team.getOnlineMembers().forEach(beaner -> beaner.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 120, 2)));
                    team.getOnlineMembers().forEach(beaner -> beaner.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 120, 1)));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 120, 1), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 120, 2), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 120, 2), true);
                    player.sendMessage(new String[]{
                            ChatColor.translateAlternateColorCodes('&', "&7&m----------------------------------------------------"),
                            ChatColor.GRAY + "You have used your " + this.getDisplayName(),
                            "",
                            ChatColor.GRAY + "Your faction has been given:",
                            ChatColor.YELLOW + "Strength II: " + ChatColor.WHITE + "5 seconds",
                            ChatColor.YELLOW + "Resistance III: " + ChatColor.WHITE + "5 seconds",
                            ChatColor.YELLOW + "Regeneration III: " + ChatColor.WHITE + "5 seconds",
                            ChatColor.translateAlternateColorCodes('&', "&7&m----------------------------------------------------"),

                    });
                    team.sendMessage(player.getDisplayName() + ChatColor.YELLOW + " has just used " + ChatColor.GOLD + this.getDisplayName() + ChatColor.YELLOW + "!");

                    if (player.getItemInHand().getAmount() == 1) player.setItemInHand(null);
                    player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
                }
            }
        }
    }
}
