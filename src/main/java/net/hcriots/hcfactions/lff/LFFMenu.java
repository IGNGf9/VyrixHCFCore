/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.lff;

import cc.fyre.stark.engine.menu.Button;
import cc.fyre.stark.engine.menu.Menu;
import cc.fyre.stark.engine.scoreboard.ScoreFunction;
import lombok.Getter;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.lff.button.ClassButton;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public class LFFMenu extends Menu {
    private final List<ClassButton> classes = new ArrayList<>();

    {
        setAutoUpdate(true);
    }

    @Override
    public String getTitle(Player player) {
        return ChatColor.DARK_GRAY + "Select Classes";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(buttons.size(), new ClassButton(ChatColor.AQUA, "Diamond", Material.DIAMOND_HELMET, this));
        buttons.put(buttons.size(), new ClassButton(ChatColor.GOLD, "Bard", Material.GOLD_HELMET, this));
        buttons.put(buttons.size(), new ClassButton(ChatColor.LIGHT_PURPLE, "Archer", Material.LEATHER_HELMET, this));
        buttons.put(buttons.size(), new ClassButton(ChatColor.RED, "Rogue", Material.CHAINMAIL_HELMET, this));
        buttons.put(buttons.size(), new ClassButton(ChatColor.GREEN, "Base Bitch", Material.FENCE_GATE, this));
        if (Hulu.getInstance().getMapHandler().isKitMap()) {
            buttons.put(buttons.size(), new ClassButton(ChatColor.DARK_RED, "Bomber", Material.TNT, this));
            buttons.put(buttons.size(), new ClassButton(ChatColor.GRAY, "Serpent", Material.IRON_HELMET, this));
            buttons.put(buttons.size(), new ClassButton(ChatColor.WHITE, "Ghost", Material.QUARTZ, this));
        }
        buttons.put(8, new Button() {

            @Override
            public String getName(Player player) {
                return ChatColor.GREEN + "Confirm Selection";
            }

            @Override
            public List<String> getDescription(Player player) {
                return Collections.singletonList("&7&oClick to confirm your selected classes.");
            }

            @Override
            public Material getMaterial(Player player) {
                return Material.EMERALD_BLOCK;
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType) {
                if (classes.isEmpty()) {
                    player.closeInventory();
                    player.sendMessage(ChatColor.RED + "You didn't select any classes.");
                    return;
                }

                if (Hulu.getInstance().getLffMap().hasCooldown(player.getUniqueId()) && System.currentTimeMillis() < Hulu.getInstance().getLffMap().getCooldown(player.getUniqueId())) {
                    player.sendMessage(ChatColor.RED + "You still have a cooldown of " + ScoreFunction.Companion.TIME_FANCY((float) (Hulu.getInstance().getLffMap().getCooldown(player.getUniqueId()) - System.currentTimeMillis()) / 1000));
                    return;
                }

                player.closeInventory();

                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7&m----------------------------------"));
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', player.getDisplayName() + " &eis looking for a faction."));
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', " &f* &eClasses&7: " + StringUtils.join(classes.stream().map(claz -> claz.getColor() + claz.getName()).collect(Collectors.toList()), "&7, ")));
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7&m----------------------------------"));


                Hulu.getInstance().getLffMap().setCurrentTime(player.getUniqueId());
            }
        });

        return buttons;
    }


    public boolean checkIfClassIsSelected(String name) {
        return getClassButton(name) != null;
    }

    public ClassButton getClassButton(String name) {
        return classes.stream().filter(playerselection -> playerselection.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }


}
