package net.danygames2014.nyalib.mixininterface;

import net.danygames2014.nyalib.multipart.MultipartComponent;
import net.danygames2014.nyalib.multipart.MultipartState;
import net.modificationstation.stationapi.api.util.Util;

public interface MultipartWorld {
    default MultipartState getMultipartState(int x, int y, int z) {
        return Util.assertImpl();
    }

    default boolean setMultipartState(int x, int y, int z, MultipartState multipartState) {
        return Util.assertImpl();
    }
    
    default boolean addMultipartComponent(int x, int y, int z, MultipartComponent component) {
        return Util.assertImpl();
    }
}
