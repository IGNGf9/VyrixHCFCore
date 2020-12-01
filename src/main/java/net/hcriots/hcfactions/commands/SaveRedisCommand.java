/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.commands;

import cc.fyre.stark.engine.command.Command;
import net.hcriots.hcfactions.persist.RedisSaveTask;
import org.bukkit.command.CommandSender;

public class SaveRedisCommand {

    @Command(names = {"SaveRedis", "Save"}, permission = "op")
    public static void saveRedis(CommandSender sender) {
        RedisSaveTask.save(sender, false);
    }

    @Command(names = {"SaveRedis ForceAll", "Save ForceAll"}, permission = "op")
    public static void saveRedisForceAll(CommandSender sender) {
        RedisSaveTask.save(sender, true);
    }

}