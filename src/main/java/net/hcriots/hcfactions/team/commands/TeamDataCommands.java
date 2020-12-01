/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.team.commands;

import cc.fyre.stark.Stark;
import cc.fyre.stark.engine.command.Command;
import cc.fyre.stark.engine.command.data.parameter.Param;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.persist.RedisSaveTask;
import net.hcriots.hcfactions.team.Team;
import net.hcriots.hcfactions.team.claims.LandBoard;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.io.*;
import java.util.Date;

public class TeamDataCommands {

    @Command(names = {"team exportdata"}, permission = "op")
    public static void exportTeamData(CommandSender sender, @Param(name = "file") String fileName) {
        File file = new File(fileName);

        if (file.exists()) {
            sender.sendMessage(ChatColor.RED + "An export under that name already exists.");
            return;
        }

        try {
            DataOutputStream out = new DataOutputStream(new FileOutputStream(file));

            out.writeUTF(sender.getName());
            out.writeUTF(new Date().toString());
            out.writeInt(Hulu.getInstance().getTeamHandler().getTeams().size());

            for (Team team : Hulu.getInstance().getTeamHandler().getTeams()) {
                out.writeUTF(team.getName());
                out.writeUTF(team.saveString(false));
            }

            sender.sendMessage(ChatColor.GOLD + "Saved " + Hulu.getInstance().getTeamHandler().getTeams().size() + " teams to " + ChatColor.GREEN + file.getAbsolutePath() + ChatColor.GOLD + ".");
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            sender.sendMessage(ChatColor.RED + "Could not import teams! Check console for errors.");
        }
    }

    @Command(names = {"team importdata"}, permission = "op")
    public static void importTeamData(CommandSender sender, @Param(name = "file") String fileName) {
        long startTime = System.currentTimeMillis();
        File file = new File(fileName);

        if (!file.exists()) {
            sender.sendMessage(ChatColor.RED + "An export under that name does not exist.");
            return;
        }

        try {
            Stark.instance.getCore().getRedis().runRedisCommand((jedis) -> {
                jedis.flushAll();
                return null;
            });

            DataInputStream in = new DataInputStream(new FileInputStream(file));
            String author = in.readUTF();
            String created = in.readUTF();
            int teamsToRead = in.readInt();

            for (int i = 0; i < teamsToRead; i++) {
                String teamName = in.readUTF();
                String teamData = in.readUTF();

                Team team = new Team(teamName);
                team.load(teamData, true);

                Hulu.getInstance().getTeamHandler().setupTeam(team, true);
            }

            LandBoard.getInstance().loadFromTeams(); // to update land board shit
            Hulu.getInstance().getTeamHandler().recachePlayerTeams();
            RedisSaveTask.save(sender, true);
            sender.sendMessage(ChatColor.GOLD + "Loaded " + teamsToRead + " teams from an export created by " + ChatColor.GREEN + author + ChatColor.GOLD + " at " + ChatColor.GREEN + created + ChatColor.GOLD + " and recached claims in " + ChatColor.GREEN.toString() + (System.currentTimeMillis() - startTime) + "ms.");
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            sender.sendMessage(ChatColor.RED + "Could not import teams! Check console for errors.");
        }
    }

}