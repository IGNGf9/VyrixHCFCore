/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.bosses.particles;

/**
 * Created by InspectMC
 * Date: 7/20/2020
 * Time: 6:18 PM
 */

@SuppressWarnings("serial")
public class ParticleException extends RuntimeException {
    public ParticleException() {
    }

    public ParticleException(String message) {
        super(message);
    }

    public ParticleException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParticleException(Throwable cause) {
        super(cause);
    }

    public ParticleException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}