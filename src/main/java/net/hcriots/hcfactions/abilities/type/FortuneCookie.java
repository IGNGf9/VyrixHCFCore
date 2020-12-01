package net.hcriots.hcfactions.abilities.type;

import net.hcriots.hcfactions.abilities.Ability;
import net.hcriots.hcfactions.team.dtr.DTRBitmask;
import net.hcriots.hcfactions.util.CC;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FortuneCookie extends Ability {
    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public Material getMaterial() {
        return Material.COOKIE;
    }

    @Override
    public String getDisplayName() {
        return ChatColor.GOLD.toString() + ChatColor.BOLD + "Fortune Cookie";
    }

    @Override
    public List<String> getLore() {
        List<String> toReturn = new ArrayList();
        toReturn.add("&7Eat this item to have a chance");
        toReturn.add("&7to receive confusion or strength!");
        return toReturn;
    }

    @Override
    public long getCooldown() {
        return 180000L;
    }

    @EventHandler
    public void onConsume(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            if (event.getItem().getType() != Material.COOKIE) {
                return;
            }

            if (!event.getItem().getItemMeta().getLore().contains(CC.translate("&7Eat this item to have a chance"))) {
                return;
            }

            if (DTRBitmask.SAFE_ZONE.appliesAt(player.getLocation())) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "You can't use this in spawn!");
                return;
            }

            if (this.hasCooldown(player)) {
                this.sendCooldownMessage(player);
                event.setCancelled(true);
                return;
            }

            this.applyCooldown(player);
            player.setItemInHand(new ItemStack(Material.AIR, 1));
            Random object = new Random();

            for (int counter = 1; counter <= 1; ++counter) {
                int chance = 1 + object.nextInt(100);
                ArrayList message;
                if (chance > 50) {
                    message = new ArrayList();
                    message.add(CC.translate("&7&m---------------------------"));
                    message.add(CC.translate(" &6» &fYou have ate a &6&lFortune Cookie"));
                    message.add(CC.translate(" &6» &fand received &eStrength 2&f!"));
                    message.add(CC.translate(" "));
                    message.add(CC.translate(" &6» &fDuration: &e7 Seconds"));
                    message.add(CC.translate("&7&m---------------------------"));
                    message.forEach(str -> player.sendMessage((String) str));
                    player.setItemInHand(new ItemStack(Material.AIR, 1));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 170, 1));
                } else {
                    message = new ArrayList();
                    message.add(CC.translate("&7&m---------------------------"));
                    message.add(CC.translate(" &6» &fYou have ate a &6&lFortune Cookie"));
                    message.add(CC.translate(" &6» &fand received &eConfusion 2&f!"));
                    message.add(CC.translate(" "));
                    message.add(CC.translate(" &6» &fDuration: &e7 Seconds"));
                    message.add(CC.translate("&7&m---------------------------"));
                    message.forEach(str -> player.sendMessage((String) str));
                    player.setItemInHand(new ItemStack(Material.AIR, 1));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 170, 1));
                }
            }
        }

    }
}