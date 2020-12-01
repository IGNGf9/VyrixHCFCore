package net.hcriots.hcfactions.listener;

import net.hcriots.hcfactions.util.CC;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Arrays;
import java.util.List;

public class SentaListener implements Listener {

    List<String> kkkMembers = Arrays.asList(
            "Gf9",
            "nrth",
            "Wrezza",
            "YoloSanta"
    );

    @EventHandler
    public void onKKKMemberJoin(PlayerJoinEvent event) {
        for (String kkkUser : kkkMembers) {
            if (event.getPlayer().getName().equals(kkkUser)) {
                event.getPlayer().sendMessage(CC.translate("&6&lTrump 2020!"));
            }
        }
    }
}
