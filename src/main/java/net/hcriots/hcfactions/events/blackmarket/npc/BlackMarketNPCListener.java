/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.events.blackmarket.npc;

import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.events.blackmarket.menu.InitialBlackMarketMenu;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;

/**
 * @author Missionary (missionarymc@gmail.com)
 * @since 8/4/2020
 */
public class BlackMarketNPCListener implements Listener {

    private final Hulu plugin;

    public BlackMarketNPCListener(Hulu plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onNPCInteractEvent(NPCRightClickEvent event) {
        if (event.getNPC().getName().equalsIgnoreCase(ChatColor.GOLD + ChatColor.BOLD.toString() + "Black-Market") && plugin.getBlackMarketHandler().isActive()) {
            new InitialBlackMarketMenu().openMenu(event.getClicker());
        }
    }

    @EventHandler
    public void onChunk(ChunkUnloadEvent event) {
        if (plugin.getBlackMarketHandler().getLocation() != null && plugin.getBlackMarketHandler().isActive()) {
            Location location = plugin.getBlackMarketHandler().getLocation();
            int x = location.getChunk().getX(), z = location.getChunk().getZ();
            if (event.getChunk().getX() == x && event.getChunk().getZ() == z) {
                plugin.getLogger().info("[Blackmarket] Cancelled unloading chunk at " + event.getChunk().toString());
                event.setCancelled(true);
            }
        }
    }
}
