package net.danygames2014.nyalib.entity.datatracker.datatype;

import net.danygames2014.nyalib.entity.datatracker.ExtendedDataTrackerEntry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.modificationstation.stationapi.api.util.Identifier;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class NbtCompoundExtendedDataTrackerDataType extends ExtendedDataTrackerDataType<NbtCompound> {
    public NbtCompoundExtendedDataTrackerDataType(Identifier id) {
        super(id);
    }

    @Override
    public NbtCompound getDefaultValue() {
        return null;
    }

    @Override
    public void write(ExtendedDataTrackerEntry entry, DataOutputStream stream) throws IOException {
        NbtElement.writeTag((NbtCompound) entry.getValue(), stream);
    }

    @Override
    public void read(ExtendedDataTrackerEntry entry, DataInputStream stream) throws IOException {
        entry.setValue(NbtElement.readTag(stream));
    }
}
