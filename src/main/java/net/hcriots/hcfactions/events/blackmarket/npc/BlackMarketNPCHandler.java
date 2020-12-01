/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.events.blackmarket.npc;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.DespawnReason;
import net.citizensnpcs.api.npc.NPC;
import net.hcriots.hcfactions.Hulu;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

/**
 * @author Missionary (missionarymc@gmail.com)
 * @since 8/1/2020
 */
public class BlackMarketNPCHandler {

    private NPC shopkeeper;

    public BlackMarketNPCHandler(Hulu plugin) {
        plugin.getServer().getPluginManager().registerEvents(new BlackMarketNPCListener(plugin), plugin);
    }

    public static boolean gettingRobbed() {
        return Math.random() <= 0.20;
    }

    public void spawnShopKeeper(Location location) {
        if (shopkeeper != null && shopkeeper.isSpawned()) {
            return;
        }

        Chunk chunk = location.getChunk();

        if (!chunk.isLoaded()) {
            chunk.load();
        }

        this.shopkeeper = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, "Black-Market");
        this.shopkeeper.setProtected(true);
        this.shopkeeper.data().setPersistent(NPC.PLAYER_SKIN_UUID_METADATA, "SemirKnol"); // has the skin I want https://namemc.com/skin/67030021e8e0b011
        this.shopkeeper.data().setPersistent(NPC.PLAYER_SKIN_USE_LATEST, false);
        this.shopkeeper.setName(ChatColor.GOLD + ChatColor.BOLD.toString() + "Black-Market");
        location.setYaw((float) -180);
        location.setPitch((float) 0);
        this.shopkeeper.spawn(location.add(0.5, 0, 0.5));
    }

    public void despawnShopKeeper() {
        if (shopkeeper == null || !shopkeeper.isSpawned()) {
            return;
        }

        shopkeeper.despawn(DespawnReason.REMOVAL);
        shopkeeper.destroy();
    }

}
