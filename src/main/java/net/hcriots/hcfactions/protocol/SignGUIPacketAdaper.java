/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import net.hcriots.hcfactions.Hulu;
import org.bukkit.Location;

public class SignGUIPacketAdaper extends PacketAdapter {

    public SignGUIPacketAdaper() {
        super(Hulu.getInstance(), PacketType.Play.Server.OPEN_SIGN_ENTITY);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        Location location = new Location(event.getPlayer().getWorld(), event.getPacket().getIntegers().read(0), event.getPacket().getIntegers().read(1), event.getPacket().getIntegers().read(2));

        if (location.getBlock().getState().hasMetadata("noSignPacket")) {
            event.setCancelled(true);
        }
    }

}