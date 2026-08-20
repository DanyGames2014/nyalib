package net.danygames2014.nyalib.multipart;

import net.modificationstation.stationapi.api.util.math.Direction;

public interface RedstoneComponent {
    int getStrongPowerLevel(Direction side);
    int getPowerLevel(Direction side);
    boolean canConnectRedstone(Direction side);
}
