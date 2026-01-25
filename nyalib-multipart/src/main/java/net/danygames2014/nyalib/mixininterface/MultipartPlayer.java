package net.danygames2014.nyalib.mixininterface;

import net.danygames2014.nyalib.multipart.MultipartComponent;
import net.modificationstation.stationapi.api.util.Util;

public interface MultipartPlayer {
    default float getMultipartBreakingSpeed(int x, int y, int z, MultipartComponent component) {
        return Util.assertImpl();
    }

    default boolean canHarvestMultipart(int x, int y, int z, MultipartComponent component) {
        return Util.assertImpl();
    }
}
