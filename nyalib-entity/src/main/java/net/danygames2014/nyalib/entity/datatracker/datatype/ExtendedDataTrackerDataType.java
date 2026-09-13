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
    
    public abstract void write(ExtendedDataTrackerEntry entry, DataOutputStream stream) throws IOException;
    
    public abstract void read(ExtendedDataTrackerEntry entry, DataInputStream stream) throws IOException;
}
