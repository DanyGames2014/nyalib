package net.danygames2014.nyalib.mixininterface;

import net.danygames2014.nyalib.multipart.MultipartComponent;
import net.danygames2014.nyalib.multipart.MultipartState;
import net.modificationstation.stationapi.api.util.Util;

public interface MultipartWorld {
    default MultipartState getMultipartState(int x, int y, int z) {
        System.err.println("BRUH GETMULTIPARTSTATE");
        return Util.assertImpl();
    }

    default boolean setMultipartState(int x, int y, int z, MultipartState multipartState) {
        System.err.println("BRUH SETMULTIPARTSTATE");
        return Util.assertImpl();
    }
    
    default boolean addMultipartComponent(int x, int y, int z, MultipartComponent component) {
        System.err.println("BRUH ADDMULTIPARTCOMPONENT");
        return Util.assertImpl();
    }
}
