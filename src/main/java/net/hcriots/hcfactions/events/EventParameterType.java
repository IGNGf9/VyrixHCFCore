/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.events;

import cc.fyre.stark.engine.command.data.parameter.ParameterType;
import com.mysql.jdbc.StringUtils;
import net.hcriots.hcfactions.Hulu;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class EventParameterType implements ParameterType<Event> {

    public Event transform(CommandSender sender, String source) {
        if (source.equals("active")) {
            for (Event event : Hulu.getInstance().getEventHandler().getEvents()) {
                if (event.isActive() && !event.isHidden()) {
                    return event;
                }
            }

            sender.sendMessage(ChatColor.RED + "There is no active Event at the moment.");

            return null;
        }

        Event event = Hulu.getInstance().getEventHandler().getEvent(source);

        if (event == null) {
            sender.sendMessage(ChatColor.RED + "No Event with the name " + source + " found.");
            return (null);
        }

        return (event);
    }

    public List<String> tabComplete(Player sender, Set<String> flags, String source) {
        List<String> completions = new ArrayList<>();

        for (Event event : Hulu.getInstance().getEventHandler().getEvents()) {
            if (StringUtils.startsWithIgnoreCase(event.getName(), source)) {
                completions.add(event.getName());
            }
        }

        return (completions);
    }

}