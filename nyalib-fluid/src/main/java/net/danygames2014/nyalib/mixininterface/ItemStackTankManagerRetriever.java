package net.danygames2014.nyalib.mixininterface;

import net.danygames2014.nyalib.fluid.TankManager;
import net.modificationstation.stationapi.api.util.Util;

/**
 * DO NOT implement this class on anything!
 */
public interface ItemStackTankManagerRetriever {
    default TankManager nyalib$getTankManager() {
        return Util.assertImpl();
    }
}
