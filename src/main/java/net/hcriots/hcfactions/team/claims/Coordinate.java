/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.team.claims;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class Coordinate {

    @Getter
    @Setter
    int x;
    @Getter
    @Setter
    int z;

    @Override
    public String toString() {
        return (x + ", " + z);
    }

}