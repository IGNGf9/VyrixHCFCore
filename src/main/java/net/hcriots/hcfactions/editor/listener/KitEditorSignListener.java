/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.editor.listener;

import net.hcriots.hcfactions.editor.EditorMenu;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Created by InspectMC
 * Date: 7/6/2020
 * Time: 12:37 AM
 */
public class KitEditorSignListener implements Listener {

    @EventHandler
    public void onSignChange(SignChangeEvent e) {
        Player player = e.getPlayer();
        if (!player.hasPermission("hcf.sign.create")) {
            if (e.getLine(0).equalsIgnoreCase("[KitEditor]")) {
                e.setCancelled(true);
                return;
            }
        }
        if (e.getLine(0).equalsIgnoreCase("[KitEditor]")) {
            e.setLine(0, "");
            e.setLine(1, ChatColor.GOLD + "Kit Editor");
            e.setLine(2, ChatColor.BLACK + "Click me");
            e.setLine(3, "");
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (!(e.getAction() == Action.RIGHT_CLICK_BLOCK)) return;
        if (e.getClickedBlock().getState() instanceof Sign) {
            Sign s = (Sign) e.getClickedBlock().getState();
            if (s.getLine(1).equalsIgnoreCase(ChatColor.GOLD + "Kit Editor")) {
                new EditorMenu().openMenu(player);
            }
        }
    }
}