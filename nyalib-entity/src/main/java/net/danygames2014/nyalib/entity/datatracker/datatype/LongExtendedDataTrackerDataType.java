package net.danygames2014.nyalib.entity.datatracker.datatype;

import net.danygames2014.nyalib.entity.datatracker.ExtendedDataTrackerEntry;
import net.modificationstation.stationapi.api.util.Identifier;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class LongExtendedDataTrackerDataType extends ExtendedDataTrackerDataType<Long> {
    public LongExtendedDataTrackerDataType(Identifier id) {
        super(id);
    }

    @Override
    public Long getDefaultValue() {
        return 0L;
    }

    @Override
    public void write(ExtendedDataTrackerEntry entry, DataOutputStream stream) throws IOException {
        stream.writeLong((Long) entry.getValue());
    }

    @Override
    public void read(ExtendedDataTrackerEntry entry, DataInputStream stream) throws IOException {
        entry.setValue(stream.readLong());
    }
}
