/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.perks;

import cc.fyre.stark.engine.command.Command;
import com.google.common.collect.ImmutableMap;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by InspectMC
 * Date: 6/23/2020
 * Time: 2:48 AM
 */
public class CondenseCommand {

    private static final Map<MaterialData, Material> MAP = new ImmutableMap.Builder<MaterialData, Material>()
            .put(new MaterialData(Material.DIAMOND), Material.DIAMOND_BLOCK)
            .put(new MaterialData(Material.IRON_INGOT), Material.IRON_BLOCK)
            .put(new MaterialData(Material.GOLD_INGOT), Material.GOLD_BLOCK)
            .put(new MaterialData(Material.REDSTONE), Material.REDSTONE_BLOCK)
            .put(new MaterialData(Material.EMERALD), Material.EMERALD_BLOCK)
            .put(new MaterialData(Material.INK_SACK, (byte) 4), Material.LAPIS_BLOCK)
            .put(new MaterialData(Material.COAL), Material.COAL_BLOCK)
            .build();

    @Command(names = {"condense", "block"}, permission = "hulu.command.condence")
    public static void condense(CommandSender sender) {
        Player player = (Player) sender;
        PlayerInventory inventory = player.getInventory();
        ItemStack[] contents = inventory.getContents();
        Map<MaterialData, Integer> AMOUNT = new HashMap<>();
        for (int i = 0; i < contents.length; i++) {
            ItemStack stack = contents[i];
            if (stack != null) {
                MaterialData data = stack.getData();
                if (MAP.containsKey(data)) {
                    AMOUNT.put(data, stack.getAmount() + AMOUNT.getOrDefault(data, 0));
                    contents[i] = null;
                }
            }
        }
        inventory.setContents(contents);
        List<ItemStack> refund = new ArrayList<>();
        for (Map.Entry<MaterialData, Integer> entry : AMOUNT.entrySet()) {
            MaterialData data = entry.getKey();
            int amount = entry.getValue();
            Material make = MAP.get(data);
            int refundAmount = amount % 9;
            int makeAmount = (int) Math.floor(amount / 9.0);
            if (makeAmount > 0) {
                refund.add(new ItemStack(make, makeAmount));
            }
            if (refundAmount > 0) {
                refund.add(new ItemStack(data.getItemType(), refundAmount, data.getData()));
            }
        }
        Location location = player.getLocation();
        World world = player.getWorld();
        boolean dropped = false;
        for (ItemStack stack : inventory.addItem(refund.toArray(new ItemStack[refund.size()])).values()) {
            world.dropItem(location, stack);
            dropped = true;
        }
        if (dropped) {
            sender.sendMessage(ChatColor.RED + "Inventory was full. Some items has drop on the floor.");
        } else {
            sender.sendMessage(ChatColor.YELLOW + "You have condense your items in your inventory.");
        }
    }
}
