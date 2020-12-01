/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.killtheking;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.entity.Player;

/**
 * Created by InspectMC
 * Date: 6/23/2020
 * Time: 4:50 AM
 */

@Data
@AllArgsConstructor
public class KillTheKing {

    private Player activeKing;
    private boolean running;
}
