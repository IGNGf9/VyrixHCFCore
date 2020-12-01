/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.events.conquest.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.ChatColor;

@AllArgsConstructor
public enum ConquestCapzone {

    GREEN(ChatColor.DARK_GREEN, "Green"),
    YELLOW(ChatColor.YELLOW, "Yellow"),
    BLUE(ChatColor.BLUE, "Blue"),
    RED(ChatColor.RED, "Red"),
    MAIN(ChatColor.GOLD, "Main");

    @Getter
    private final ChatColor color;
    @Getter
    private final String name;

}