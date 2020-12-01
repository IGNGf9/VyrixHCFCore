package net.hcriots.hcfactions.abilities.type;

import net.hcriots.hcfactions.abilities.Ability;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;

public class FakeLogger extends Ability {

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
        return ChatColor.GOLD.toString() + ChatColor.BOLD + "Fake Logger";
    }

    @Override
    public List<String> getLore() {
        List<String> toReturn = new ArrayList();
        toReturn.add("&7Right-Click to summon a fake");
        toReturn.add("&7villager logger that looks like yours!");
        return toReturn;
    }

    @Override
    public long getCooldown() {
        return (long) (120.0 * 1000L);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!(event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            return;
        }

        if (!this.isSimilar(player.getItemInHand())) {
            return;
        }

        if (this.hasCooldown(player)) {
            this.sendCooldownMessage(player);
            event.setCancelled(true);
        }

        Villager villager = player.getWorld().spawn(
                new Location(player.getWorld(),
                        player.getLocation().getX(),
                        player.getLocation().getY()
                                + 20, player.getLocation().getZ()),
                Villager.class);

        villager.setCustomName(ChatColor.YELLOW + player.getName());
        villager.setCustomNameVisible(true);
        this.applyCooldown(player);

        if (player.getItemInHand().getAmount() == 1) player.setItemInHand(null);
        player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
    }
}
