package net.danygames2014.nyalib.entity.datatracker.event;

import net.danygames2014.nyalib.entity.datatracker.ExtendedDataTrackerDataTypeRegistry;
import net.danygames2014.nyalib.entity.datatracker.datatype.ExtendedDataTrackerDataType;
import net.mine_diver.unsafeevents.Event;
import net.modificationstation.stationapi.api.util.Identifier;

public class ExtendedDataTrackerDataTypeRegistryEvent extends Event {
    public ExtendedDataTrackerDataTypeRegistry registry;

    public ExtendedDataTrackerDataTypeRegistryEvent() {
        this.registry = ExtendedDataTrackerDataTypeRegistry.INSTANCE;
    }
    
    public void register(Identifier id, ExtendedDataTrackerDataType<?> dataType) {
        registry.register(id, dataType);
    }
    
    public void register(ExtendedDataTrackerDataType<?> dataType) {
        registry.register(dataType.id, dataType);
    }
}
