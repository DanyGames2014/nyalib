package net.danygames2014.nyalib.entity.datatracker.datatype;

import net.danygames2014.nyalib.entity.datatracker.ExtendedDataTrackerEntry;
import net.modificationstation.stationapi.api.util.Identifier;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class IntegerExtendedDataTrackerDataType extends ExtendedDataTrackerDataType<Integer> {
    public IntegerExtendedDataTrackerDataType(Identifier id) {
        super(id);
    }

    @Override
    public Integer getDefaultValue() {
        return 0;
    }

    @Override
    public void write(ExtendedDataTrackerEntry entry, DataOutputStream stream) throws IOException {
        stream.writeInt((Integer) entry.getValue());
    }

    @Override
    public void read(ExtendedDataTrackerEntry entry, DataInputStream stream) throws IOException {
        entry.setValue(stream.readInt());
    }
}
