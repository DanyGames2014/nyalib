package net.danygames2014.nyalib.entity.datatracker.datatype;

import net.danygames2014.nyalib.entity.datatracker.ExtendedDataTrackerEntry;
import net.modificationstation.stationapi.api.util.Identifier;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class DoubleExtendedDataTrackerDataType extends ExtendedDataTrackerDataType<Double> {
    public DoubleExtendedDataTrackerDataType(Identifier id) {
        super(id);
    }

    @Override
    public Double getDefaultValue() {
        return 0.0D;
    }

    @Override
    public void write(ExtendedDataTrackerEntry entry, DataOutputStream stream) throws IOException {
        stream.writeDouble((Double) entry.getValue());
    }

    @Override
    public void read(ExtendedDataTrackerEntry entry, DataInputStream stream) throws IOException {
        entry.setValue(stream.readDouble());
    }
}
