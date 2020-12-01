/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.pvpclasses.pvpclasses.archer.menu.button;

import cc.fyre.stark.engine.menu.Button;
import lombok.AllArgsConstructor;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.map.kits.KitListener;
import net.hcriots.hcfactions.pvpclasses.pvpclasses.archer.ArcherUpgrade;
import net.hcriots.hcfactions.util.ProgressBarBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by InspectMC
 * Date: 7/6/2020
 * Time: 3:56 PM
 */

@AllArgsConstructor
public class UpgradeProgressButton extends Button {

    private final ArcherUpgrade upgrade;

    @Override
    public String getName(Player player) {
        return ChatColor.YELLOW + upgrade.getUpgradeName();
    }

    @Override
    public Material getMaterial(Player player) {
        return Material.INK_SACK;
    }

    @Override
    public byte getDamageValue(Player player) {
        return (byte) upgrade.getMaterialData();
    }

    @Override
    public List<String> getDescription(Player player) {
        int progress = Hulu.getInstance().getArcherKillsMap().getArcherKills(player.getUniqueId());
        double percentage = ProgressBarBuilder.percentage(progress, upgrade.getKillsNeeded());

        if (progress >= upgrade.getKillsNeeded()) {
            progress = upgrade.getKillsNeeded();
        }

        List<String> lore = new ArrayList<>();
        lore.add("");

        for (String descriptionLine : upgrade.getDescription()) {
            lore.add(ChatColor.GRAY + ChatColor.ITALIC.toString() + descriptionLine);
        }

        lore.add("");
        lore.add(ChatColor.DARK_GRAY.toString() + "[" + new ProgressBarBuilder().build(percentage) + ChatColor.DARK_GRAY + "] (" + ChatColor.GREEN + progress + ChatColor.DARK_GRAY + "/" + ChatColor.GRAY + upgrade.getKillsNeeded() + ChatColor.DARK_GRAY + ")");

        if (percentage >= 100.0D) {
            lore.add(ChatColor.GREEN + "You unlocked this upgrade");
            lore.add("");
            lore.add(ChatColor.YELLOW + "Click to load this kit");
        } else {
            lore.add(ChatColor.GREEN + "Kill " + upgrade.getKillsNeeded() + " people to unlock this upgrade");
        }

        return lore;
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
        int progress = Hulu.getInstance().getArcherKillsMap().getArcherKills(player.getUniqueId());

        if (progress >= upgrade.getKillsNeeded()) {
            KitListener.attemptApplyKit(player, Hulu.getInstance().getMapHandler().getKitManager().get(player.getUniqueId(), "Archer" + upgrade.getUpgradeName()));
        } else {
            player.sendMessage(ChatColor.RED + "You need " + upgrade.getKillsNeeded() + " more kills");
        }
    }

}