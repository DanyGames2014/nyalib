package net.danygames2014.nyalib.item.block;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

public interface ManagedItemHandlerWithInventory extends ManagedItemHandler, Inventory {
    @Override
    default int size() {
        return this.getItemSlots(null);
    }

    @Override
    default ItemStack getStack(int slot) {
        return this.getItem(slot, null);
    }

    @Override
    default ItemStack removeStack(int slot, int amount) {
        return this.extractItem(slot, amount, null);
    }

    @Override
    default void setStack(int slot, ItemStack stack) {
        this.setItem(stack, slot, null);
    }

    @Override
    default int getMaxCountPerStack() {
        return 64;
    }
}
