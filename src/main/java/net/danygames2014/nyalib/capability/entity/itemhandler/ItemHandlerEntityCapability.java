package net.danygames2014.nyalib.capability.entity.itemhandler;

import net.danygames2014.nyalib.capability.entity.EntityCapability;
import net.danygames2014.nyalib.item.ItemHandler;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public abstract class ItemHandlerEntityCapability extends EntityCapability implements ItemHandler {
    public abstract boolean canExtractItem();

    public abstract ItemStack extractItem(int slot, int amount);

    public abstract boolean canInsertItem();

    public abstract ItemStack insertItem(ItemStack stack, int slot);

    public abstract ItemStack insertItem(ItemStack stack);

    public abstract ItemStack getItemInSlot(int slot);

    public abstract int getItemSlots();

    public abstract ItemStack[] getInventory();

    public ItemStack extractItem() {
        return this.extractItem(null);
    }

    // The underlying interface
    @Override
    public boolean canExtractItem(@Nullable Direction direction) {
        return this.canExtractItem();
    }

    @Override
    public ItemStack extractItem(int slot, int amount, @Nullable Direction direction) {
        return this.extractItem(slot, amount);
    }

    @Override
    public boolean canInsertItem(@Nullable Direction direction) {
        return this.canInsertItem();
    }

    @Override
    public ItemStack insertItem(ItemStack stack, int slot, @Nullable Direction direction) {
        return this.insertItem(stack, slot);
    }

    @Override
    public ItemStack insertItem(ItemStack stack, @Nullable Direction direction) {
        return this.insertItem(stack);
    }

    @Override
    public ItemStack getItemInSlot(int slot, @Nullable Direction direction) {
        return this.getItemInSlot(slot);
    }

    @Override
    public int getItemSlots(@Nullable Direction direction) {
        return this.getItemSlots();
    }

    @Override
    public ItemStack[] getInventory(@Nullable Direction direction) {
        return this.getInventory();
    }

    @Override
    public boolean canConnectItem(Direction direction) {
        return false;
    }
}
