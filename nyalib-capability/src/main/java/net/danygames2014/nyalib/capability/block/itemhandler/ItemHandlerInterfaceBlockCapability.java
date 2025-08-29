package net.danygames2014.nyalib.capability.block.itemhandler;

import net.danygames2014.nyalib.item.ItemHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.jetbrains.annotations.Nullable;

class ItemHandlerInterfaceBlockCapability extends ItemHandlerBlockCapability {
    private final ItemHandler itemHandler;
    
    public ItemHandlerInterfaceBlockCapability(ItemHandler itemHandler) {
        this.itemHandler = itemHandler;
    }

    @Override
    public boolean canConnectItem(Direction side) {
        return itemHandler.canConnectItem(side);
    }

    @Override
    public boolean canExtractItem(@Nullable Direction side) {
        return itemHandler.canExtractItem(side);
    }

    @Override
    public ItemStack extractItem(int slot, int amount, @Nullable Direction side) {
        return itemHandler.extractItem(slot, amount, side);
    }

    @Override
    public ItemStack extractItem(@Nullable Direction side) {
        return itemHandler.extractItem(side);
    }

    @Override
    public ItemStack extractItem(int amount, @Nullable Direction side) {
        return itemHandler.extractItem(amount, side);
    }

    @Override
    public ItemStack extractItem(Item item, int amount, @Nullable Direction side) {
        return itemHandler.extractItem(item, amount, side);
    }

    @Override
    public ItemStack extractItem(Item item, int meta, int amount, @Nullable Direction side) {
        return itemHandler.extractItem(item, meta, amount, side);
    }

    @Override
    public boolean canInsertItem(@Nullable Direction side) {
        return itemHandler.canInsertItem(side);
    }

    @Override
    public ItemStack insertItem(ItemStack stack, int slot, @Nullable Direction side) {
        return itemHandler.insertItem(stack, slot, side);
    }

    @Override
    public ItemStack insertItem(ItemStack stack, @Nullable Direction side) {
        return itemHandler.insertItem(stack, side);
    }

    @Override
    public ItemStack getItem(int slot, @Nullable Direction side) {
        return itemHandler.getItem(slot, side);
    }

    @Override
    public boolean setItem(ItemStack stack, int slot, @Nullable Direction side) {
        return itemHandler.setItem(stack, slot, side);
    }

    @Override
    public int getItemSlots(@Nullable Direction side) {
        return itemHandler.getItemSlots(side);
    }

    @Override
    public ItemStack[] getInventory(@Nullable Direction side) {
        return itemHandler.getInventory(side);
    }
}
