package net.danygames2014.nyalibtest.multipart;

import net.danygames2014.nyalib.multipart.MultipartComponent;
import net.minecraft.nbt.NbtCompound;
import net.modificationstation.stationapi.api.util.Identifier;

public class CoverMultipartComponent extends MultipartComponent {
    public Identifier blockId;
    
    public CoverMultipartComponent(Identifier blockId) {
        this.blockId = blockId;
    }

    public CoverMultipartComponent() {
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        nbt.putString("blockId", blockId.toString());
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        this.blockId = Identifier.of(nbt.getString("blockId"));
    }
}
