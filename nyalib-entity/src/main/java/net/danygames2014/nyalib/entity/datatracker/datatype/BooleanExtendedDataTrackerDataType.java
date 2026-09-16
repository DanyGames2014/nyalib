package net.danygames2014.nyalib.entity.datatracker.datatype;

import net.danygames2014.nyalib.entity.datatracker.ExtendedDataTrackerEntry;
import net.modificationstation.stationapi.api.util.Identifier;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class BooleanExtendedDataTrackerDataType extends ExtendedDataTrackerDataType<Boolean> {
    public BooleanExtendedDataTrackerDataType(Identifier id) {
        super(id);
    }

    @Override
    public Boolean getDefaultValue() {
        return false;
    }

    @Override
    public void write(ExtendedDataTrackerEntry entry, DataOutputStream stream) throws IOException {
        stream.writeBoolean((Boolean) entry.getValue());
    }

    @Override
    public void read(ExtendedDataTrackerEntry entry, DataInputStream stream) throws IOException {
        entry.setValue(stream.readBoolean());
    }
}
