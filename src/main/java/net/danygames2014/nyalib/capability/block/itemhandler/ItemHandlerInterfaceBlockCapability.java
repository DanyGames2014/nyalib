package net.danygames2014.nyalib.capability.block.itemhandler;

import net.danygames2014.nyalib.item.ItemHandler;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public class ItemHandlerInterfaceBlockCapability extends ItemHandlerBlockCapability {
    ItemHandler itemHandler;
    
    public ItemHandlerInterfaceBlockCapability(ItemHandler itemHandler) {
        this.itemHandler = itemHandler;
    }

    @Override
    public boolean canConnectItem(Direction direction) {
        return itemHandler.canConnectItem(direction);
    }

    @Override
    public boolean canExtractItem(@Nullable Direction direction) {
        return itemHandler.canExtractItem(direction);
    }

    @Override
    public ItemStack extractItem(int slot, int amount, @Nullable Direction direction) {
        return itemHandler.extractItem(slot, amount, direction);
    }

    @Override
    public boolean canInsertItem(@Nullable Direction direction) {
        return itemHandler.canInsertItem(direction);
    }

    @Override
    public ItemStack insertItem(ItemStack stack, int slot, @Nullable Direction direction) {
        return itemHandler.insertItem(stack, slot, direction);
    }

    @Override
    public ItemStack insertItem(ItemStack stack, @Nullable Direction direction) {
        return itemHandler.insertItem(stack, direction);
    }

    @Override
    public ItemStack getItemInSlot(int slot, @Nullable Direction direction) {
        return itemHandler.getItemInSlot(slot, direction);
    }

    @Override
    public int getItemSlots(@Nullable Direction direction) {
        return itemHandler.getItemSlots(direction);
    }

    @Override
    public ItemStack[] getInventory(@Nullable Direction direction) {
        return itemHandler.getInventory(direction);
    }
}
