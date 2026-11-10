package net.danygames2014.nyalib.entity.datatracker;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.danygames2014.nyalib.NyaLib;
import net.danygames2014.nyalib.entity.datatracker.datatype.ExtendedDataTrackerDataType;
import net.danygames2014.nyalib.entity.datatracker.datatype.ExtendedDataTrackerDataTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3i;
import net.modificationstation.stationapi.api.util.Identifier;
import org.jetbrains.annotations.Nullable;

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
    public <T, D extends ExtendedDataTrackerDataType<T>> void startTracking(Identifier id, D dataType, @Nullable T value) {
        if (value == null) {
            value = dataType.getDefaultValue();
        }
        
        entries.put(id, new ExtendedDataTrackerEntry(id, dataType, value));
    }

    public void startTrackingBoolean(Identifier id, boolean value) {
        startTracking(id, ExtendedDataTrackerDataTypes.BOOLEAN, value);
    }
    
    public void startTrackingBoolean(Identifier id) {
        startTracking(id, ExtendedDataTrackerDataTypes.BOOLEAN, ExtendedDataTrackerDataTypes.BOOLEAN.getDefaultValue());
    }  
    
    public void startTrackingCharacter(Identifier id, char value) {
        startTracking(id, ExtendedDataTrackerDataTypes.CHARACTER, value);
    }  
    
    public void startTrackingCharacter(Identifier id) {
        startTracking(id, ExtendedDataTrackerDataTypes.CHARACTER, ExtendedDataTrackerDataTypes.CHARACTER.getDefaultValue());
    } 

    public void startTrackingByte(Identifier id, byte value) {
        startTracking(id, ExtendedDataTrackerDataTypes.BYTE, value);
    }
    
    public void startTrackingByte(Identifier id) {
        startTracking(id, ExtendedDataTrackerDataTypes.BYTE, ExtendedDataTrackerDataTypes.BYTE.getDefaultValue());
    }

    public void startTrackingShort(Identifier id, short value) {
        startTracking(id, ExtendedDataTrackerDataTypes.SHORT, value);
    }
    
    public void startTrackingShort(Identifier id) {
        startTracking(id, ExtendedDataTrackerDataTypes.SHORT, ExtendedDataTrackerDataTypes.SHORT.getDefaultValue());
    }
    
    public void startTrackingInt(Identifier id, int value) {
        startTracking(id, ExtendedDataTrackerDataTypes.INTEGER, value);
    }
    
    public void startTrackingInt(Identifier id) {
        startTracking(id, ExtendedDataTrackerDataTypes.INTEGER, ExtendedDataTrackerDataTypes.INTEGER.getDefaultValue());
    }
    
    public void startTrackingLong(Identifier id, long value) {
        startTracking(id, ExtendedDataTrackerDataTypes.LONG, value);
    }
    
    public void startTrackingLong(Identifier id) {
        startTracking(id, ExtendedDataTrackerDataTypes.LONG, ExtendedDataTrackerDataTypes.LONG.getDefaultValue());
    }
    
    public void startTrackingFloat(Identifier id, float value) {
        startTracking(id, ExtendedDataTrackerDataTypes.FLOAT, value);
    }
    
    public void startTrackingFloat(Identifier id) {
        startTracking(id, ExtendedDataTrackerDataTypes.FLOAT, ExtendedDataTrackerDataTypes.FLOAT.getDefaultValue());
    }
    
    public void startTrackingDouble(Identifier id, double value) {
        startTracking(id, ExtendedDataTrackerDataTypes.DOUBLE, value);
    }
    
    public void startTrackingDouble(Identifier id) {
        startTracking(id, ExtendedDataTrackerDataTypes.DOUBLE, ExtendedDataTrackerDataTypes.DOUBLE.getDefaultValue());
    } 
    
    public void startTrackingString(Identifier id, @Nullable String value) {
        startTracking(id, ExtendedDataTrackerDataTypes.STRING, value);
    }
    
    public void startTrackingString(Identifier id) {
        startTracking(id, ExtendedDataTrackerDataTypes.STRING, ExtendedDataTrackerDataTypes.STRING.getDefaultValue());
    }
    
    public void startTrackingVec3i(Identifier id, @Nullable Vec3i value) {
        startTracking(id, ExtendedDataTrackerDataTypes.VEC3I, value);
    }
    
    public void startTrackingVec3i(Identifier id) {
        startTracking(id, ExtendedDataTrackerDataTypes.VEC3I, ExtendedDataTrackerDataTypes.VEC3I.getDefaultValue());
    }  
    
    public void startTrackingItemStack(Identifier id, @Nullable ItemStack value) {
        startTracking(id, ExtendedDataTrackerDataTypes.ITEM_STACK, value);
    }
    
    public void startTrackingItemStack(Identifier id) {
        startTracking(id, ExtendedDataTrackerDataTypes.ITEM_STACK, ExtendedDataTrackerDataTypes.ITEM_STACK.getDefaultValue());
    } 
    
    public void startTrackingNbtCompound(Identifier id, @Nullable NbtCompound value) {
        startTracking(id, ExtendedDataTrackerDataTypes.NBT_COMPOUND, value);
    }
    
    public void startTrackingNbtCompound(Identifier id) {
        startTracking(id, ExtendedDataTrackerDataTypes.NBT_COMPOUND, ExtendedDataTrackerDataTypes.NBT_COMPOUND.getDefaultValue());
    }

    // Getters
    public <T, D extends ExtendedDataTrackerDataType<T>> T get(Identifier id, D dataType) {
        ExtendedDataTrackerEntry entry = entries.get(id);
        
        if (entry == null) {
            NyaLib.LOGGER.error("Tried to get extended data tracker value for an entry with id " + id + " but it doesn't exist!");
            return dataType.getDefaultValue();
        }

        if (entry.type != dataType) {
            NyaLib.LOGGER.error("Tried to get extended data tracker value for an entry with type " + entry.type + " but the value has type " + dataType + "!");
            return dataType.getDefaultValue();
        }

        //noinspection unchecked
        return (T) entry.getValue();
    }

    public boolean getBoolean(Identifier id) {
        return get(id, ExtendedDataTrackerDataTypes.BOOLEAN);
    }

    public char getCharacter(Identifier id) {
        return get(id, ExtendedDataTrackerDataTypes.CHARACTER);
    }

    public byte getByte(Identifier id) {
        return get(id, ExtendedDataTrackerDataTypes.BYTE);
    }

    public short getShort(Identifier id) {
        return get(id, ExtendedDataTrackerDataTypes.SHORT);
    }

    public int getInt(Identifier id) {
        return get(id, ExtendedDataTrackerDataTypes.INTEGER);
    }

    public long getLong(Identifier id) {
        return get(id, ExtendedDataTrackerDataTypes.LONG);
    }

    public float getFloat(Identifier id) {
        return get(id, ExtendedDataTrackerDataTypes.FLOAT);
    }

    public double getDouble(Identifier id) {
        return get(id, ExtendedDataTrackerDataTypes.DOUBLE);
    }

    @Nullable
    public String getString(Identifier id) {
        return get(id, ExtendedDataTrackerDataTypes.STRING);
    }

    @Nullable
    public Vec3i getVec3i(Identifier id) {
        return get(id, ExtendedDataTrackerDataTypes.VEC3I);
    }

    @Nullable
    public ItemStack getItemStack(Identifier id) {
        return get(id, ExtendedDataTrackerDataTypes.ITEM_STACK);
    }

    @Nullable
    public NbtCompound getNbtCompound(Identifier id) {
        return get(id, ExtendedDataTrackerDataTypes.NBT_COMPOUND);
    }

    // Setters
    public <T, D extends ExtendedDataTrackerDataType<T>> void set(Identifier id, D dataType, @Nullable T value) {
        ExtendedDataTrackerEntry entry = entries.get(id);
        if (entry == null || entry.getValue() == value) {
            return;
        }
        
        if (value == null) {
            value = dataType.getDefaultValue();
        }

        if (entry.type != dataType) {
            NyaLib.LOGGER.error("Tried to set value for an entry with type " + entry.type + " but the value has type " + dataType + "!");
            return;
        }

        entry.setValue(value);
        entry.markDirty();
        this.dirty = true;
    }

    public void setBoolean(Identifier id, boolean value) {
        set(id, ExtendedDataTrackerDataTypes.BOOLEAN, value);
    }
    
    public void setCharacter(Identifier id, char value) {
        set(id, ExtendedDataTrackerDataTypes.CHARACTER, value);   
    }
    
    public void setByte(Identifier id, byte value) {
        set(id, ExtendedDataTrackerDataTypes.BYTE, value);
    }

    public void setShort(Identifier id, short value) {
        set(id, ExtendedDataTrackerDataTypes.SHORT, value);
    }
    
    public void setInt(Identifier id, int value) {
        set(id, ExtendedDataTrackerDataTypes.INTEGER, value);
    }
    
    public void setLong(Identifier id, long value) {
        set(id, ExtendedDataTrackerDataTypes.LONG, value);
    }
    
    public void setFloat(Identifier id, float value) {
        set(id, ExtendedDataTrackerDataTypes.FLOAT, value);
    }
    
    public void setDouble(Identifier id, double value) {
        set(id, ExtendedDataTrackerDataTypes.DOUBLE, value);
    }
    
    public void setString(Identifier id, @Nullable String value) {
        set(id, ExtendedDataTrackerDataTypes.STRING, value);
    }
    
    public void setVec3i(Identifier id, @Nullable Vec3i value) {
        set(id, ExtendedDataTrackerDataTypes.VEC3I, value);
    }
    
    public void setItemStack(Identifier id, @Nullable ItemStack value) {
        set(id, ExtendedDataTrackerDataTypes.ITEM_STACK, value);
    }
    
    public void setNbtCompound(Identifier id, @Nullable NbtCompound value) {
        set(id, ExtendedDataTrackerDataTypes.NBT_COMPOUND, value);
    }

    // Dirty
    public boolean isDirty() {
        return dirty;
    }

    // Networking
    public void updateEntries(List<ExtendedDataTrackerEntry> entries) {
        for (ExtendedDataTrackerEntry entry : entries) {
            ExtendedDataTrackerEntry localEntry = this.entries.get(entry.id);
            if (localEntry != null) {
                System.err.println("Updated " + entry.id + " to " + entry.getValue() + " from " + localEntry.getValue() + " (type: " + entry.type + ")");
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
        entry.type.writeEntry(entry, stream);
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
