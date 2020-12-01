/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.team.commands.team;

import cc.fyre.stark.engine.command.Command;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.team.Team;
import net.hcriots.hcfactions.team.claims.VisualClaim;
import net.hcriots.hcfactions.team.claims.VisualClaimType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;

public class TeamResizeCommand {

    public static final ItemStack SELECTION_WAND = new ItemStack(Material.WOOD_AXE);

    static {
        ItemMeta meta = SELECTION_WAND.getItemMeta();

        meta.setDisplayName("§a§oResizing Wand");
        meta.setLore(Arrays.asList(

                "",
                "§eRight/Left Click§6 Block",
                "§b- §fSelect resize's corners",
                "",
                "§eRight Click §6Air",
                "§b- §fCancel current claim",
                "",
                "§9Shift §eLeft Click §6Block/Air",
                "§b- §fPurchase current claim"

        ));

        SELECTION_WAND.setItemMeta(meta);
    }

    //TODO: Remove permission node to deploy
    @Command(names = {"team resize", "t resize", "f resize", "faction resize", "fac resize"}, permission = "op")
    public static void teamResize(final Player sender) {
        Team team = Hulu.getInstance().getTeamHandler().getTeam(sender);

        if (team == null) {
            sender.sendMessage(ChatColor.GRAY + "You are not on a team!");
            return;
        }

        if (team.isOwner(sender.getUniqueId()) || team.isCoLeader(sender.getUniqueId()) || team.isCaptain(sender.getUniqueId())) {
            sender.getInventory().remove(SELECTION_WAND);

            if (team.isRaidable()) {
                sender.sendMessage(ChatColor.RED + "You may not resize land while your faction is raidable!");
                return;
            }

            new BukkitRunnable() {

                public void run() {
                    sender.getInventory().addItem(SELECTION_WAND.clone());
                }

            }.runTaskLater(Hulu.getInstance(), 1L);

            new VisualClaim(sender, VisualClaimType.RESIZE, false).draw(false);

            if (!VisualClaim.getCurrentMaps().containsKey(sender.getName())) {
                new VisualClaim(sender, VisualClaimType.MAP, false).draw(true);
            }
        } else {
            sender.sendMessage(ChatColor.DARK_AQUA + "Only team captains can do this.");
        }
    }

}