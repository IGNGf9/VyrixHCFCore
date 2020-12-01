/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.util;

public enum PortalDirection {

    NORTH_SOUTH((byte) 2, (byte) 0),
    EAST_WEST((byte) 1);

    private final byte[] portalData;

    PortalDirection(byte... portalData) {
        this.portalData = portalData;
    }

    public static PortalDirection fromPortalData(byte portalData) {
        for (PortalDirection portalDirection : values()) {
            for (byte data : portalDirection.portalData) {
                if (data == portalData) {
                    return (portalDirection);
                }
            }
        }

        return (null);
    }

}