package net.danygames2014.nyalib.mixininterface;

import net.modificationstation.stationapi.api.util.Util;

public interface MultipartChunkMap {
    default void markMultipartForUpdate(int x, int y, int z) {
        Util.assertImpl();
    }
    
    default void updateChunkMultiparts() {
        Util.assertImpl();
    }
}
