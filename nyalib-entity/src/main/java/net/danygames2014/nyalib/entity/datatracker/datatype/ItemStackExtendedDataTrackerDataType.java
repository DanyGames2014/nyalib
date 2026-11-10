package net.danygames2014.nyalib.entity.datatracker.datatype;

import net.danygames2014.nyalib.entity.datatracker.ExtendedDataTrackerEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.modificationstation.stationapi.api.util.Identifier;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ItemStackExtendedDataTrackerDataType extends ExtendedDataTrackerDataType<ItemStack> {
    public ItemStackExtendedDataTrackerDataType(Identifier id) {
        super(id);
    }

    @Override
    public ItemStack getDefaultValue() {
        return null;
    }

    @Override
    public void write(ExtendedDataTrackerEntry entry, DataOutputStream stream) throws IOException {
        ItemStack stack = (ItemStack) entry.getValue();
        NbtCompound stackNbt = new NbtCompound();
        stack.writeNbt(stackNbt);
        NbtElement.writeTag(stackNbt, stream);
    }

    @Override
    public void read(ExtendedDataTrackerEntry entry, DataInputStream stream) throws IOException {
        NbtCompound stackNbt = (NbtCompound) NbtElement.readTag(stream);
        ItemStack stack = new ItemStack(stackNbt);
        entry.setValue(stack);
    }
}
