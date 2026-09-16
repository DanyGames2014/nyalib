package net.danygames2014.nyalib.entity.datatracker.datatype;

import net.danygames2014.nyalib.entity.datatracker.ExtendedDataTrackerEntry;
import net.modificationstation.stationapi.api.util.Identifier;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class ExtendedDataTrackerDataType<T> {
    public final Identifier id;

    public ExtendedDataTrackerDataType(Identifier id) {
        this.id = id;
    }
    
    public abstract T getDefaultValue();

    public void writeEntry(ExtendedDataTrackerEntry entry, DataOutputStream stream) throws IOException {
        if (entry.getValue() == null) {
            stream.writeBoolean(true);
            return;
        }
        
        stream.writeBoolean(false);
        write(entry, stream);
    }
    
    public abstract void write(ExtendedDataTrackerEntry entry, DataOutputStream stream) throws IOException;

    public void readEntry(ExtendedDataTrackerEntry entry, DataInputStream stream) throws IOException {
        if (stream.readBoolean()) {
            entry.setValue(getDefaultValue());
            return;
        }
        
        read(entry, stream);
    }
    
    public abstract void read(ExtendedDataTrackerEntry entry, DataInputStream stream) throws IOException;
}
