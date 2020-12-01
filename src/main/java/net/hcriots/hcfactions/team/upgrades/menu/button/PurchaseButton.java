/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.team.upgrades.menu.button;

import cc.fyre.stark.engine.menu.Button;
import lombok.AllArgsConstructor;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.credit.CreditHandler;
import net.hcriots.hcfactions.team.Team;
import net.hcriots.hcfactions.team.upgrades.TeamUpgrade;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by InspectMC
 * Date: 7/16/2020
 * Time: 9:36 PM
 */

@AllArgsConstructor
public class PurchaseButton extends Button {

    private final Team team;
    private final TeamUpgrade upgrade;

    @Override
    public String getName(Player player) {
        return ChatColor.YELLOW + upgrade.getUpgradeName();
    }

    @Override
    public List<String> getDescription(Player player) {
        int nextTier = upgrade.getTier(team) + 1;

        List<String> lore = new ArrayList<>();

        if (nextTier > upgrade.getTierLimit()) {
            lore.add("");
            lore.add(ChatColor.GRAY + "This upgrade is at maximum tier");
        } else {
            if (upgrade.getTierLimit() > 1) {
                lore.add(ChatColor.GRAY + "(" + ChatColor.BLUE + "Tier " + nextTier + ChatColor.GRAY + ")");
            }

            lore.add(ChatColor.GRAY + "(" + ChatColor.BLUE + "Price: " + ChatColor.GREEN + upgrade.getPrice(nextTier) + " charms" + ChatColor.GRAY + ")");
            lore.add("");
            lore.add(ChatColor.GRAY + ChatColor.ITALIC.toString() + upgrade.getDescription());
            lore.add("");
            lore.add(ChatColor.YELLOW + "Click to purchase this upgrade");
        }

        return lore;
    }

    @Override
    public Material getMaterial(Player player) {
        return upgrade.getIcon().getType();
    }

    @Override
    public byte getDamageValue(Player player) {
        return (byte) upgrade.getIcon().getDurability();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
        if (!team.isOwner(player.getUniqueId()) && !team.isCoLeader(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "You must be the owner or co-leader of your team to purchase upgrades.");
            return;
        }

        int nextTier = upgrade.getTier(team) + 1;

        if (nextTier > upgrade.getTierLimit()) {
            player.sendMessage(ChatColor.RED + "You cannot purchase a higher tier for this upgrade.");
            return;
        }

        int price = upgrade.getPrice(nextTier);

        if (price > CreditHandler.getPlayerCredits(player)) {
            player.sendMessage(ChatColor.RED + "You do not have enough charms to purchase this for your team.");
            return;
        }

        Hulu.getInstance().getCreditsMap().setCredits(player.getUniqueId(), CreditHandler.getPlayerCredits(player) - price);
        team.getUpgradeToTier().put(upgrade.getUpgradeName(), nextTier);
        team.flagForSave();
        upgrade.onPurchase(player, team, nextTier, price);

        player.sendMessage(ChatColor.GREEN + "You purchased the " + ChatColor.AQUA + ChatColor.BOLD.toString() + upgrade.getUpgradeName() + (upgrade.getTierLimit() > 1 ? " Tier " + nextTier : "") + ChatColor.GREEN + " upgrade for " + ChatColor.LIGHT_PURPLE + price + " charms" + ChatColor.GREEN + ".");
        player.closeInventory();
    }

}
