package net.danygames2014.nyalib.entity.datatracker.datatype;

import net.danygames2014.nyalib.entity.datatracker.ExtendedDataTrackerEntry;
import net.minecraft.util.math.Vec3i;
import net.modificationstation.stationapi.api.util.Identifier;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Vec3iExtendedDataTrackerDataType extends ExtendedDataTrackerDataType<Vec3i>{
    public Vec3iExtendedDataTrackerDataType(Identifier id) {
        super(id);
    }

    @Override
    public Vec3i getDefaultValue() {
        return null;
    }

    @Override
    public void write(ExtendedDataTrackerEntry entry, DataOutputStream stream) throws IOException {
        Vec3i vec = (Vec3i) entry.getValue();
        stream.writeInt(vec.x);
        stream.writeInt(vec.y);
        stream.writeInt(vec.z);
    }

    @Override
    public void read(ExtendedDataTrackerEntry entry, DataInputStream stream) throws IOException {
        int x = stream.readInt();
        int y = stream.readInt();
        int z = stream.readInt();
        entry.setValue(new Vec3i(x, y, z));
    }
}
