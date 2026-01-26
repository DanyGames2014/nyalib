package net.danygames2014.nyalib.mixininterface;

import net.modificationstation.stationapi.api.util.Util;

public interface RedstoneLevelWorld {
    default int getStrongPowerLevelOnSide(int x, int y, int z, int side) {
        return Util.assertImpl();
    }

    default int getStrongPowerLevel(int x, int y, int z) {
        return Util.assertImpl();
    }

    default int getPowerLevelOnSide(int x, int y, int z, int side) {
        return Util.assertImpl();
    }

    default int getPowerLevel(int x, int y, int z) {
        return Util.assertImpl();
    }
}
