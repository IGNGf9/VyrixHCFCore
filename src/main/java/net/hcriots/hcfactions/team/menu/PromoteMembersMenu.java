/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.team.menu;

import cc.fyre.stark.engine.menu.Button;
import cc.fyre.stark.engine.menu.Menu;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.hcriots.hcfactions.team.Team;
import net.hcriots.hcfactions.team.menu.button.ChangePromotionStatusButton;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class PromoteMembersMenu extends Menu {

    @NonNull
    @Getter
    Team team;

    @Override
    public String getTitle(Player player) {
        return "Members of " + team.getName();
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = new HashMap<>();
        int index = 0;

        for (UUID uuid : team.getMembers()) {
            if (!team.isOwner(uuid) && !team.isCoLeader(uuid)) {
                buttons.put(index, new ChangePromotionStatusButton(uuid, team, true));
                index++;
            }
        }

        return buttons;
    }

}
