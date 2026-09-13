package net.danygames2014.nyalib.entity.datatracker.init;

import net.danygames2014.nyalib.NyaLib;
import net.danygames2014.nyalib.entity.datatracker.datatype.ByteExtendedDataTrackerDataType;
import net.danygames2014.nyalib.entity.datatracker.datatype.ExtendedDataTrackerDataTypes;
import net.danygames2014.nyalib.entity.datatracker.datatype.ShortExtendedDataTrackerDataType;
import net.danygames2014.nyalib.entity.datatracker.event.ExtendedDataTrackerDataTypeRegistryEvent;
import net.mine_diver.unsafeevents.listener.EventListener;

public class ExtendedDataTrackerListener {
    @EventListener
    public void registerExtendedDataTrackerDataTypes(ExtendedDataTrackerDataTypeRegistryEvent event) {
        event.register(ExtendedDataTrackerDataTypes.BYTE = new ByteExtendedDataTrackerDataType(NyaLib.NAMESPACE.id("byte")));
        event.register(ExtendedDataTrackerDataTypes.SHORT = new ShortExtendedDataTrackerDataType(NyaLib.NAMESPACE.id("short")));
    }
}
