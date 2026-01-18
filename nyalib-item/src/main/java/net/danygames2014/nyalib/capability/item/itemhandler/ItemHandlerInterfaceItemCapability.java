package net.danygames2014.nyalib.capability.item.itemhandler;

import net.danygames2014.nyalib.item.item.ItemHandlerItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemHandlerInterfaceItemCapability extends ItemHandlerItemCapability {
    private final ItemHandlerItem itemHandler;
    private final ItemStack itemStack;

    public ItemHandlerInterfaceItemCapability(ItemHandlerItem itemHandler, ItemStack itemStack) {
        this.itemHandler = itemHandler;
        this.itemStack = itemStack;
    }

    @Override
    public boolean canExtractItem() {
        return itemHandler.canExtractItem(itemStack);
    }

    @Override
    public ItemStack extractItem() {
        return itemHandler.extractItem(itemStack);
    }

    @Override
    public ItemStack extractItem(int amount) {
        return itemHandler.extractItem(itemStack, amount);
    }

    @Override
    public ItemStack extractItem(int slot, int amount) {
        return itemHandler.extractItem(itemStack, slot, amount);
    }

    @Override
    public ItemStack extractItem(Item item, int amount) {
        return itemHandler.extractItem(itemStack, item, amount);
    }

    @Override
    public ItemStack extractItem(Item item, int meta, int amount) {
        return itemHandler.extractItem(itemStack, item, meta, amount);
    }

    @Override
    public boolean canInsertItem() {
        return itemHandler.canInsertItem(itemStack);
    }

    @Override
    public ItemStack insertItem(ItemStack stack, int slot) {
        return itemHandler.insertItem(itemStack, stack, slot);
    }

    @Override
    public ItemStack insertItem(ItemStack stack) {
        return itemHandler.insertItem(itemStack, stack);
    }

    @Override
    public ItemStack getItem(int slot) {
        return itemHandler.getItem(itemStack, slot);
    }

    @Override
    public boolean setItem(ItemStack stack, int slot) {
        return itemHandler.setItem(itemStack, stack, slot);
    }

    @Override
    public int getItemSlots() {
        return itemHandler.getItemSlots(itemStack);
    }

    @Override
    public ItemStack[] getInventory() {
        return itemHandler.getInventory(itemStack);
    }
}
