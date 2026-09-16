package net.danygames2014.nyalib.entity.datatracker.datatype;

import net.danygames2014.nyalib.entity.datatracker.ExtendedDataTrackerEntry;
import net.modificationstation.stationapi.api.util.Identifier;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ShortExtendedDataTrackerDataType extends ExtendedDataTrackerDataType<Short> {
    public ShortExtendedDataTrackerDataType(Identifier id) {
        super(id);
    }

    @Override
    public Short getDefaultValue() {
        return 0;
    }

    @Override
    public void write(ExtendedDataTrackerEntry entry, DataOutputStream stream) throws IOException {
        stream.writeShort((Short) entry.getValue());
    }

    @Override
    public void read(ExtendedDataTrackerEntry entry, DataInputStream stream) throws IOException {
        entry.setValue(stream.readShort());
    }
}
