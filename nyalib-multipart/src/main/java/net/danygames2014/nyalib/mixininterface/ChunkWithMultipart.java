package net.danygames2014.nyalib.mixininterface;

import it.unimi.dsi.fastutil.objects.ObjectCollection;
import net.danygames2014.nyalib.multipart.MultipartState;
import net.modificationstation.stationapi.api.util.Util;

public interface ChunkWithMultipart {
    default MultipartState getMultipartState(int chunkX, int y, int chunkZ) {
        return Util.assertImpl();
    }
    
    default boolean setMultipartState(int chunkX, int y, int chunkZ, MultipartState multipartState) {
        return Util.assertImpl();
    }
    
    default ObjectCollection<MultipartState> getMultipartStates() {
        return Util.assertImpl();
    }
}
