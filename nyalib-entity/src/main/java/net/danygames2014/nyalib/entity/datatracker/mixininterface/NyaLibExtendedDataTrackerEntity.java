package net.danygames2014.nyalib.entity.datatracker.mixininterface;

import net.danygames2014.nyalib.entity.datatracker.ExtendedDataTracker;
import net.modificationstation.stationapi.api.util.Util;

public interface NyaLibExtendedDataTrackerEntity {
    default ExtendedDataTracker getExtendedDataTracker() {
        return Util.assertImpl();
    }
    
    default void initExtendedDataTracker() {
        
    }
}
