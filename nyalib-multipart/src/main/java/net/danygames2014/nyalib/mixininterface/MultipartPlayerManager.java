package net.danygames2014.nyalib.mixininterface;

import net.modificationstation.stationapi.api.util.Util;

public interface MultipartPlayerManager {
    default void markMultipartDirty(int x, int y, int z, int dimensionId) {
        Util.assertImpl();
    }
}
