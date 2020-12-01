/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.team.menu;

import cc.fyre.stark.Stark;
import cc.fyre.stark.engine.menu.Button;
import cc.fyre.stark.engine.menu.Menu;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.hcriots.hcfactions.team.Team;
import net.hcriots.hcfactions.team.menu.button.MakeLeaderButton;
import org.bukkit.entity.Player;

import java.util.*;

@RequiredArgsConstructor
public class SelectNewLeaderMenu extends Menu {

    @NonNull
    @Getter
    Team team;

    @Override
    public String getTitle(Player player) {
        return "Leader for " + team.getName();
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = new HashMap<>();
        int index = 0;

        ArrayList<UUID> uuids = new ArrayList<>();
        uuids.addAll(team.getMembers());

        Collections.sort(uuids, Comparator.comparing(u2 -> Stark.instance.getCore().getUuidCache().name(u2).toLowerCase()));

        for (UUID u : uuids) {
            buttons.put(index, new MakeLeaderButton(u, team));
            index++;
        }

        return buttons;
    }

}
