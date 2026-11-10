package net.danygames2014.nyalib.entity.datatracker;

import net.danygames2014.nyalib.entity.datatracker.datatype.ExtendedDataTrackerDataType;
import net.modificationstation.stationapi.api.util.Identifier;

import java.io.DataInputStream;
import java.io.IOException;

public class ExtendedDataTrackerEntry {
    public final Identifier id;
    public final ExtendedDataTrackerDataType<?> type;
    private Object value;
    public boolean dirty;

    public ExtendedDataTrackerEntry(Identifier id, ExtendedDataTrackerDataType<?> type, Object value) {
        this.id = id;
        this.type = type;
        this.value = value;
        this.dirty = true;
    }

    public ExtendedDataTrackerEntry(Identifier id, ExtendedDataTrackerDataType<?> type, DataInputStream stream) throws IOException {
        this.id = id;
        this.type = type;
        this.dirty = false;
        type.readEntry(this, stream);
    }

    public Object getValue() {
        return value;
    }
    
    public void setValue(Object value) {
        this.value = value;
    }
    
    public void markDirty() {
        this.dirty = true;
    }

    @Override
    public String toString() {
        return "ExtendedDataTrackerEntry{" +
                "id=" + id +
                ", type=" + type +
                ", value=" + value +
                '}';
    }
}
