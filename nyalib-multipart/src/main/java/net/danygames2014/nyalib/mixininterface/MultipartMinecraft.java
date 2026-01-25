package net.danygames2014.nyalib.mixininterface;

import net.danygames2014.nyalib.multipart.MultipartHitResult;
import net.modificationstation.stationapi.api.util.Util;
import org.jetbrains.annotations.Nullable;

public interface MultipartMinecraft {
    @Nullable
    default MultipartHitResult getMultipartCrosshairTarget(){
        return Util.assertImpl();
    }

    default void setMultipartCrosshairTarget(@Nullable MultipartHitResult hitResult){
        Util.assertImpl();
    }
}
