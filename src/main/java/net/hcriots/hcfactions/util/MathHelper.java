/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.util;

import java.util.Random;

public class MathHelper {

    public static int getRandom(int min, int max) {
        return (new Random().nextInt(max - min) + min);
    }
}
