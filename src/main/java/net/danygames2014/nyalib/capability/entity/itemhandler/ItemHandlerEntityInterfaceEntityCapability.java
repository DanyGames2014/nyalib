package net.danygames2014.nyalib.capability.entity.itemhandler;

import net.danygames2014.nyalib.item.ItemHandlerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemHandlerEntityInterfaceEntityCapability extends ItemHandlerEntityCapability {
    private final ItemHandlerEntity itemHandler;

    public ItemHandlerEntityInterfaceEntityCapability(ItemHandlerEntity itemHandler) {
        this.itemHandler = itemHandler;
    }

    @Override
    public boolean canExtractItem() {
        return itemHandler.canExtractItem();
    }

    @Override
    public ItemStack extractItem(int slot, int amount) {
        return itemHandler.extractItem(slot, amount);
    }

    @Override
    public ItemStack extractItem() {
        return itemHandler.extractItem();
    }

    @Override
    public ItemStack extractItem(Item item, int amount) {
        return itemHandler.extractItem(item, amount);
    }

    @Override
    public boolean canInsertItem() {
        return itemHandler.canInsertItem();
    }

    @Override
    public ItemStack insertItem(ItemStack stack, int slot) {
        return itemHandler.insertItem(stack, slot);
    }

    @Override
    public ItemStack insertItem(ItemStack stack) {
        return itemHandler.insertItem(stack);
    }

    @Override
    public ItemStack getItemInSlot(int slot) {
        return itemHandler.getItemInSlot(slot);
    }

    @Override
    public int getItemSlots() {
        return itemHandler.getItemSlots();
    }

    @Override
    public ItemStack[] getInventory() {
        return itemHandler.getInventory();
    }
}
