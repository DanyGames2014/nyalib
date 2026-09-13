package net.danygames2014.nyalib.entity.datatracker;

import com.mojang.serialization.Lifecycle;
import net.danygames2014.nyalib.NyaLib;
import net.danygames2014.nyalib.entity.datatracker.datatype.ExtendedDataTrackerDataType;
import net.modificationstation.stationapi.api.event.registry.RegistryAttribute;
import net.modificationstation.stationapi.api.event.registry.RegistryAttributeHolder;
import net.modificationstation.stationapi.api.registry.Registries;
import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.api.registry.RegistryKey;
import net.modificationstation.stationapi.api.registry.SimpleRegistry;
import net.modificationstation.stationapi.api.util.Identifier;

public class ExtendedDataTrackerDataTypeRegistry extends SimpleRegistry<ExtendedDataTrackerDataType<?>> {
    public static final RegistryKey<Registry<ExtendedDataTrackerDataType<?>>> KEY = RegistryKey.ofRegistry(NyaLib.NAMESPACE.id("extended_data_tracker_data_type"));
    public static final ExtendedDataTrackerDataTypeRegistry INSTANCE = Registries.create(KEY, new ExtendedDataTrackerDataTypeRegistry(), Lifecycle.experimental());
    
    public ExtendedDataTrackerDataTypeRegistry() {
        super(KEY, Lifecycle.experimental(), false);
        RegistryAttributeHolder.get(this).addAttribute(RegistryAttribute.SYNCED);
        RegistryAttributeHolder.get(this).addAttribute(RegistryAttribute.MODDED);
    }
    
    public void register(Identifier id, ExtendedDataTrackerDataType<?> dataType) {
        add(RegistryKey.of(KEY, id), dataType, Lifecycle.experimental());
    }
}
