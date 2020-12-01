/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.listener;

import cc.fyre.stark.util.PlayerUtils;
import cc.fyre.stark.util.serialization.PlayerInventorySerializer;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import net.hcriots.hcfactions.Hulu;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Date;
import java.util.UUID;

public class WebsiteListener implements Listener {

    public WebsiteListener() {
        Bukkit.getLogger().info("Creating indexes...");
        DBCollection mongoCollection = Hulu.getInstance().getMongoPool().getDB(Hulu.MONGO_DB_NAME).getCollection("Deaths");

        mongoCollection.createIndex(new BasicDBObject("uuid", 1));
        mongoCollection.createIndex(new BasicDBObject("killerUUID", 1));
        mongoCollection.createIndex(new BasicDBObject("ip", 1));
        Bukkit.getLogger().info("Creating indexes done.");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        final BasicDBObject playerDeath = new BasicDBObject();

        playerDeath.put("_id", UUID.randomUUID().toString().substring(0, 7));

        if (event.getEntity().getKiller() != null) {
            playerDeath.append("healthLeft", (int) event.getEntity().getKiller().getHealth());
            playerDeath.append("killerUUID", event.getEntity().getKiller().getUniqueId().toString().replace("-", ""));
            playerDeath.append("killerLastUsername", event.getEntity().getKiller().getName());
            playerDeath.append("killerInventory", PlayerInventorySerializer.getInsertableObject(event.getEntity().getKiller()));
            playerDeath.append("killerPing", PlayerUtils.getPing(event.getEntity().getKiller()));
            playerDeath.append("killerHunger", event.getEntity().getKiller().getFoodLevel());
        } else {
            try {
                playerDeath.append("reason", event.getEntity().getLastDamageCause().getCause().toString());
            } catch (NullPointerException ignored) {
            }
        }

        playerDeath.append("playerInventory", PlayerInventorySerializer.getInsertableObject(event.getEntity()));
        playerDeath.append("ip", event.getEntity().getAddress().toString().split(":")[0].replace("/", ""));
        playerDeath.append("uuid", event.getEntity().getUniqueId().toString().replace("-", ""));
        playerDeath.append("lastUsername", event.getEntity().getName());
        playerDeath.append("hunger", event.getEntity().getFoodLevel());
        playerDeath.append("ping", PlayerUtils.getPing(event.getEntity()));
        playerDeath.append("when", new Date());

        new BukkitRunnable() {
            public void run() {
                Hulu.getInstance().getMongoPool().getDB(Hulu.MONGO_DB_NAME).getCollection("Deaths").insert(playerDeath);
            }
        }.runTaskAsynchronously(Hulu.getInstance());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        switch (event.getBlock().getType()) {
            case DIAMOND_ORE:
            case GOLD_ORE:
            case IRON_ORE:
            case COAL_ORE:
            case REDSTONE_ORE:
            case GLOWING_REDSTONE_ORE:
            case LAPIS_ORE:
            case EMERALD_ORE:
                event.getBlock().setMetadata("PlacedByPlayer", new FixedMetadataValue(Hulu.getInstance(), true));
                break;
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        if ((event.getPlayer().getItemInHand() != null && event.getPlayer().getItemInHand().containsEnchantment(Enchantment.SILK_TOUCH)) || event.getBlock().hasMetadata("PlacedByPlayer")) {
            return;
        }

        switch (event.getBlock().getType()) {
            case DIAMOND_ORE:
                Hulu.getInstance().getDiamondMinedMap().setMined(event.getPlayer(), Hulu.getInstance().getDiamondMinedMap().getMined(event.getPlayer().getUniqueId()) + 1);
                break;
            case GOLD_ORE:
                Hulu.getInstance().getGoldMinedMap().setMined(event.getPlayer().getUniqueId(), Hulu.getInstance().getGoldMinedMap().getMined(event.getPlayer().getUniqueId()) + 1);
                break;
            case IRON_ORE:
                Hulu.getInstance().getIronMinedMap().setMined(event.getPlayer().getUniqueId(), Hulu.getInstance().getIronMinedMap().getMined(event.getPlayer().getUniqueId()) + 1);
                break;
            case COAL_ORE:
                Hulu.getInstance().getCoalMinedMap().setMined(event.getPlayer().getUniqueId(), Hulu.getInstance().getCoalMinedMap().getMined(event.getPlayer().getUniqueId()) + 1);
                break;
            case REDSTONE_ORE:
                Hulu.getInstance().getRedstoneMinedMap().setMined(event.getPlayer().getUniqueId(), Hulu.getInstance().getRedstoneMinedMap().getMined(event.getPlayer().getUniqueId()) + 1);
            case GLOWING_REDSTONE_ORE:
                Hulu.getInstance().getRedstoneMinedMap().setMined(event.getPlayer().getUniqueId(), Hulu.getInstance().getRedstoneMinedMap().getMined(event.getPlayer().getUniqueId()) + 1);
                break;
            case LAPIS_ORE:
                Hulu.getInstance().getLapisMinedMap().setMined(event.getPlayer().getUniqueId(), Hulu.getInstance().getLapisMinedMap().getMined(event.getPlayer().getUniqueId()) + 1);
                break;
            case EMERALD_ORE:
                Hulu.getInstance().getEmeraldMinedMap().setMined(event.getPlayer().getUniqueId(), Hulu.getInstance().getEmeraldMinedMap().getMined(event.getPlayer().getUniqueId()) + 1);
                break;
            case GLOWSTONE:
            case LEAVES:
            case LOG:
            case STONE:
                break;
        }
    }

}