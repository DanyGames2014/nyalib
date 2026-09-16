package net.danygames2014.nyalib.entity.datatracker.init;

import net.danygames2014.nyalib.NyaLib;
import net.danygames2014.nyalib.entity.datatracker.datatype.*;
import net.danygames2014.nyalib.entity.datatracker.event.ExtendedDataTrackerDataTypeRegistryEvent;
import net.mine_diver.unsafeevents.listener.EventListener;

public class ExtendedDataTrackerListener {
    @EventListener
    public void registerExtendedDataTrackerDataTypes(ExtendedDataTrackerDataTypeRegistryEvent event) {
        event.register(ExtendedDataTrackerDataTypes.BOOLEAN = new BooleanExtendedDataTrackerDataType(NyaLib.NAMESPACE.id("boolean")));
        event.register(ExtendedDataTrackerDataTypes.CHARACTER = new CharacterExtendedDataTrackerDataType(NyaLib.NAMESPACE.id("character")));
        event.register(ExtendedDataTrackerDataTypes.BYTE = new ByteExtendedDataTrackerDataType(NyaLib.NAMESPACE.id("byte")));
        event.register(ExtendedDataTrackerDataTypes.SHORT = new ShortExtendedDataTrackerDataType(NyaLib.NAMESPACE.id("short")));
        event.register(ExtendedDataTrackerDataTypes.INTEGER = new IntegerExtendedDataTrackerDataType(NyaLib.NAMESPACE.id("integer")));
        event.register(ExtendedDataTrackerDataTypes.LONG = new LongExtendedDataTrackerDataType(NyaLib.NAMESPACE.id("long")));
        event.register(ExtendedDataTrackerDataTypes.FLOAT = new FloatExtendedDataTrackerDataType(NyaLib.NAMESPACE.id("float")));
        event.register(ExtendedDataTrackerDataTypes.DOUBLE = new DoubleExtendedDataTrackerDataType(NyaLib.NAMESPACE.id("double")));
        event.register(ExtendedDataTrackerDataTypes.STRING = new StringExtendedDataTrackerDataType(NyaLib.NAMESPACE.id("string")));
        event.register(ExtendedDataTrackerDataTypes.VEC3I = new Vec3iExtendedDataTrackerDataType(NyaLib.NAMESPACE.id("vec3i")));
        event.register(ExtendedDataTrackerDataTypes.ITEM_STACK = new ItemStackExtendedDataTrackerDataType(NyaLib.NAMESPACE.id("item_stack")));
        event.register(ExtendedDataTrackerDataTypes.NBT_COMPOUND = new NbtCompoundExtendedDataTrackerDataType(NyaLib.NAMESPACE.id("nbt_compound")));
    }
}
