package net.danygames2014.nyalib.multipart;

import net.minecraft.nbt.NbtCompound;

public abstract class SlottedMultipartComponent extends MultipartComponent implements SlottedMultipart{
    public MultipartSlot slot;

    public SlottedMultipartComponent(MultipartSlot slot) {
        this.slot = slot;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        if(nbt.contains("slot")) {
            slot = MultipartSlot.fromSlotIndex(nbt.getByte("slot"));
        }
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        if(slot != null) {
            nbt.putByte("slot", (byte) slot.slotIndex);
        }
    }

    @Override
    public MultipartSlot getSlot() {
        return slot;
    }

    @Override
    public void setSlot(MultipartSlot slot) {
        this.slot = slot;
    }

    @Override
    public int getMask() {
        return this.slot.mask;
    }
}
