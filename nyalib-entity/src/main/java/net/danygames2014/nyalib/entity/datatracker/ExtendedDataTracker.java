package net.danygames2014.nyalib.entity.datatracker;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.danygames2014.nyalib.NyaLib;
import net.danygames2014.nyalib.entity.datatracker.datatype.ExtendedDataTrackerDataType;
import net.danygames2014.nyalib.entity.datatracker.datatype.ExtendedDataTrackerDataTypes;
import net.modificationstation.stationapi.api.util.Identifier;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

public class ExtendedDataTracker {
    private final Object2ObjectOpenHashMap<Identifier, ExtendedDataTrackerEntry> entries = new Object2ObjectOpenHashMap<>();
    private boolean dirty = false;

    public ExtendedDataTracker() {

    }

    // Init value
    public <T, D extends ExtendedDataTrackerDataType<T>> void startTracking(Identifier id, D dataType, T value) {
        entries.put(id, new ExtendedDataTrackerEntry(id, dataType, value));
    }

    // Get value
    public <T, D extends ExtendedDataTrackerDataType<T>> T get(Identifier id, D dataType) {
        ExtendedDataTrackerEntry entry = entries.get(id);
        if (entry != null) {
            if (entry.type != dataType) {
                NyaLib.LOGGER.error("Tried to get value for an entry with type " + entry.type + " but the value has type " + dataType + "!");
                return null;
            }

            //noinspection unchecked
            return (T) entry.getValue();
        }
        return null;
    }

    public byte getByte(Identifier id) {
        return get(id, ExtendedDataTrackerDataTypes.BYTE);
    }

    public short getShort(Identifier id) {
        return get(id, ExtendedDataTrackerDataTypes.SHORT);
    }

    // Set value
    public <T, D extends ExtendedDataTrackerDataType<T>> void set(Identifier id, D dataType, T value) {
        ExtendedDataTrackerEntry entry = entries.get(id);
        if (entry != null && entry.getValue() != value) {
            if (entry.type != dataType) {
                NyaLib.LOGGER.error("Tried to set value for an entry with type " + entry.type + " but the value has type " + dataType + "!");
            }
            entry.setValue(value);
            entry.markDirty();
            this.dirty = true;
        }
    }

    public void setByte(Identifier id, byte value) {
        set(id, ExtendedDataTrackerDataTypes.BYTE, value);
    }

    public void setShort(Identifier id, short value) {
        set(id, ExtendedDataTrackerDataTypes.SHORT, value);
    }

    public boolean isDirty() {
        return dirty;
    }

    // Networking
    public void updateEntries(List<ExtendedDataTrackerEntry> entries) {
        for (ExtendedDataTrackerEntry entry : entries) {
            ExtendedDataTrackerEntry localEntry = this.entries.get(entry.id);
            if (localEntry != null) {
                localEntry.setValue(entry.getValue());
            }
        }
    }
    
    public List<ExtendedDataTrackerEntry> getDirtyEntries() {
        if (!this.dirty) {
            return null;
        }
        
        List<ExtendedDataTrackerEntry> dirtyEntries = new ObjectArrayList<>();
        for (ExtendedDataTrackerEntry entry : entries.values()) {
            if (entry.dirty) {
                dirtyEntries.add(entry);
                entry.dirty = false;
            }
        }
        
        this.dirty = false;
        return dirtyEntries;
    }
    
    public void readEntries(DataInputStream stream) throws IOException {
        for (int dataTypeId = stream.readInt(); dataTypeId != -1; dataTypeId = stream.readInt()) {
            Identifier id = Identifier.tryParse(stream.readUTF());
            ExtendedDataTrackerDataType<?> dataType = ExtendedDataTrackerDataTypeRegistry.INSTANCE.get(dataTypeId);
            if (dataType == null) {
                NyaLib.LOGGER.error("Received an entry with an unknown data type id " + dataTypeId + "!");
                continue;
            }
            
            entries.put(id, new ExtendedDataTrackerEntry(id, dataType, stream));
        }
    }

    // Serializing & Deserializing
    public static void serializeEntries(List<ExtendedDataTrackerEntry> entries, DataOutputStream stream) throws IOException {
        if (entries != null) {
            for (ExtendedDataTrackerEntry entry : entries) {
                writeEntry(stream, entry);
            }
        }
        
        stream.writeInt(-1);
    }

    public void writeEntries(DataOutputStream stream) throws IOException {
        for (ExtendedDataTrackerEntry entry : entries.values()) {
            writeEntry(stream, entry);
        }

        stream.writeInt(-1);
    }
    
    public static void writeEntry(DataOutputStream stream, ExtendedDataTrackerEntry entry) throws IOException {
        int dataTypeId = ExtendedDataTrackerDataTypeRegistry.INSTANCE.getRawId(entry.type);
        stream.writeInt(dataTypeId);
        stream.writeUTF(entry.id.toString());
        entry.type.write(entry, stream);
    }
    
    public static List<ExtendedDataTrackerEntry> deserializeEntries(DataInputStream stream) throws IOException {
        List<ExtendedDataTrackerEntry> entries = new ObjectArrayList<>();
        
        for (int dataTypeId = stream.readInt(); dataTypeId != -1; dataTypeId = stream.readInt()) {
            Identifier id = Identifier.tryParse(stream.readUTF());
            ExtendedDataTrackerDataType<?> dataType = ExtendedDataTrackerDataTypeRegistry.INSTANCE.get(dataTypeId);
            if (dataType == null) {
                NyaLib.LOGGER.error("Received an entry with an unknown data type id " + dataTypeId + "!");
                continue;
            }

            entries.add(new ExtendedDataTrackerEntry(id, dataType, stream));
        }
        
        return entries;
    }
}
