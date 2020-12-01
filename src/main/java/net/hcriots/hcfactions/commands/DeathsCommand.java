/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.commands;

import cc.fyre.stark.Stark;
import cc.fyre.stark.engine.command.Command;
import cc.fyre.stark.engine.command.data.parameter.Param;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.client.model.DBCollectionFindOptions;
import com.mongodb.util.JSON;
import mkremins.fanciful.FancyMessage;
import net.hcriots.hcfactions.Hulu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class DeathsCommand {

    private final static DateFormat FORMAT = new SimpleDateFormat("M dd yyyy h:mm a");

    @Command(names = {"deaths"}, permission = "foxtrot.deaths")
    public static void deaths(Player sender, @Param(name = "player") UUID player) {
        Hulu.getInstance().getServer().getScheduler().runTaskAsynchronously(Hulu.getInstance(), () -> {
            sender.sendMessage("");
            sender.sendMessage(ChatColor.GRAY + "Grabbing 10 latest deaths of " + Stark.instance.getCore().getUuidCache().name(player) + "...");
            sender.sendMessage("");

            DBCollection mongoCollection = Hulu.getInstance().getMongoPool().getDB(Hulu.MONGO_DB_NAME).getCollection("Deaths");

            boolean empty = true;
            for (DBObject object : mongoCollection.find(new BasicDBObject("uuid", player.toString().replace("-", "")), new DBCollectionFindOptions().limit(10).sort(new BasicDBObject("when", -1)))) {
                empty = false;
                BasicDBObject basicDBObject = (BasicDBObject) object;

                FancyMessage message = new FancyMessage();

                message.text(ChatColor.RED + Stark.instance.getCore().getUuidCache().name(player)).then();

                if (object.get("killerUUID") != null) {
                    message.text(ChatColor.GRAY + " died to " + ChatColor.RED + Stark.instance.getCore().getUuidCache().name(UUIDfromString(object.get("killerUUID").toString())));
                } else {
                    if (object.get("reason") != null) {
                        message.text(ChatColor.GRAY + " died from " + object.get("reason").toString().toLowerCase() + " damage.");
                    } else {
                        message.text(ChatColor.GRAY + " died from unknown causes.");
                    }
                }

                message.then(" (" + FORMAT.format(basicDBObject.getDate("when")) + ") ").color(ChatColor.GOLD);

                if (!(basicDBObject.containsKey("refundedBy"))) {
                    message.then("[UNDO]").color(ChatColor.GREEN).style(ChatColor.BOLD).tooltip(ChatColor.GRAY + "Click to give back inventory.").command("/refund " + object.get("_id").toString());
                }

                message.send(sender);
            }

            if (empty) {
                sender.sendMessage(ChatColor.RED + Stark.instance.getCore().getUuidCache().name(player) + " has no deaths to display.");
            }

            sender.sendMessage("");
        });
    }

    @Command(names = {"refund"}, permission = "foxtrot.deathrefund")
    public static void refund(Player sender, @Param(name = "id") String id) {
        Hulu.getInstance().getServer().getScheduler().runTaskAsynchronously(Hulu.getInstance(), () -> {
            DBCollection mongoCollection = Hulu.getInstance().getMongoPool().getDB(Hulu.MONGO_DB_NAME).getCollection("Deaths");
            DBObject object = mongoCollection.findOne(id);

            if (object != null) {
                BasicDBObject basicDBObject = (BasicDBObject) object;
                Player player = Bukkit.getPlayer(UUIDfromString(object.get("uuid").toString()));

                if (basicDBObject.containsKey("refundedBy")) {
                    sender.sendMessage(ChatColor.RED + "This death was already refunded by " + Stark.instance.getCore().getUuidCache().name(UUIDfromString(basicDBObject.getString("refundedBy"))) + ".");
                    return;
                }

                if (player == null) {
                    sender.sendMessage(ChatColor.RED + "Player isn't on to receive items.");
                    return;
                }

                ItemStack[] contents = Stark.getPlainGson().fromJson(JSON.serialize(((BasicDBObject) basicDBObject.get("playerInventory")).get("contents")), ItemStack[].class);
                ItemStack[] armor = Stark.getPlainGson().fromJson(JSON.serialize(((BasicDBObject) basicDBObject.get("playerInventory")).get("armor")), ItemStack[].class);

                LastInvCommand.cleanLoot(contents);
                LastInvCommand.cleanLoot(armor);

                player.getInventory().setContents(contents);
                player.getInventory().setArmorContents(armor);

                basicDBObject.put("refundedBy", sender.getUniqueId().toString().replace("-", ""));
                basicDBObject.put("refundedAt", new Date());

                mongoCollection.save(basicDBObject);

                player.sendMessage(ChatColor.GREEN + "Your inventory has been reset to an inventory from a previous life.");
                sender.sendMessage(ChatColor.GREEN + "Successfully refunded inventory to " + player.getName() + ".");

            } else {
                sender.sendMessage(ChatColor.RED + "Death not found.");
            }

        });
    }

    private static UUID UUIDfromString(String string) {
        return UUID.fromString(
                string.replaceFirst(
                        "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5"
                )
        );
    }

}