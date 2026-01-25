package net.danygames2014.nyalib.mixininterface;

import net.danygames2014.nyalib.multipart.MultipartHitResult;
import net.modificationstation.stationapi.api.util.Util;

public interface MultipartMinecraft {
    default MultipartHitResult getMultipartCrosshairTarget(){
        return Util.assertImpl();
    }

    default void setMultipartCrosshairTarget(MultipartHitResult hitResult){
        Util.assertImpl();
    }
}
