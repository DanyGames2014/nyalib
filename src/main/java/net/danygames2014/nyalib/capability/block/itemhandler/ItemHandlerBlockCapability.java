package net.danygames2014.nyalib.capability.block.itemhandler;

import net.danygames2014.nyalib.capability.block.BlockCapability;
import net.danygames2014.nyalib.item.ItemHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public abstract class ItemHandlerBlockCapability extends BlockCapability implements ItemHandler {
    @Override
    public abstract boolean canConnectItem(Direction direction);
    
    @Override
    public abstract boolean canExtractItem(@Nullable Direction direction);

    @Override
    public abstract ItemStack extractItem(int slot, int amount, @Nullable Direction direction);

    @Override
    public ItemStack extractItem(@Nullable Direction direction) {
        return ItemHandler.super.extractItem(direction);
    }

    @Override
    public ItemStack extractItem(Item item, int amount, @Nullable Direction direction) {
        return ItemHandler.super.extractItem(item, amount, direction);
    }

    @Override
    public abstract boolean canInsertItem(@Nullable Direction direction);

    @Override
    public abstract ItemStack insertItem(ItemStack stack, int slot, @Nullable Direction direction);

    @Override
    public abstract ItemStack insertItem(ItemStack stack, @Nullable Direction direction);

    @Override
    public abstract ItemStack getItemInSlot(int slot, @Nullable Direction direction);

    @Override
    public abstract int getItemSlots(@Nullable Direction direction);

    @Override
    public abstract ItemStack[] getInventory(@Nullable Direction direction);
}
