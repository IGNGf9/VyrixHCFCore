/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.util;

import java.util.List;
import java.util.Random;

public class MathUtils {

    public static int getBetween(int min, int max) {
        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }

    public static Object getRandomObject(List item) {
        Random random = new Random();
        return item.get(random.nextInt(item.size()));
    }
}
