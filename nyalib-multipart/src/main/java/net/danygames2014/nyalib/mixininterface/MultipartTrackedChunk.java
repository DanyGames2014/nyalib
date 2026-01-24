package net.danygames2014.nyalib.mixininterface;

import net.modificationstation.stationapi.api.util.Util;

public interface MultipartTrackedChunk {
    default void updatePlayerMultipart(int x, int y, int z) {
        Util.assertImpl();
    }
}
