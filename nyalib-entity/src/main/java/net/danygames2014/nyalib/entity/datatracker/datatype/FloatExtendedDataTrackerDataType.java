package net.danygames2014.nyalib.entity.datatracker.datatype;

import net.danygames2014.nyalib.entity.datatracker.ExtendedDataTrackerEntry;
import net.modificationstation.stationapi.api.util.Identifier;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class FloatExtendedDataTrackerDataType extends ExtendedDataTrackerDataType<Float> {
    public FloatExtendedDataTrackerDataType(Identifier id) {
        super(id);
    }

    @Override
    public Float getDefaultValue() {
        return 0.0F;
    }

    @Override
    public void write(ExtendedDataTrackerEntry entry, DataOutputStream stream) throws IOException {
        stream.writeFloat((Float) entry.getValue());
    }

    @Override
    public void read(ExtendedDataTrackerEntry entry, DataInputStream stream) throws IOException {
        entry.setValue(stream.readFloat());
    }
}
