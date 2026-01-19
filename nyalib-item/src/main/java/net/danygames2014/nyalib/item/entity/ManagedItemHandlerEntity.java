package net.danygames2014.nyalib.item.entity;

import net.danygames2014.nyalib.item.InventoryManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.util.Util;

public interface ManagedItemHandlerEntity extends ItemHandlerEntity {
    @Override
    default boolean canExtractItem() {
        return true;
    }

    @Override
    default ItemStack extractItem() {
        return ItemHandlerEntity.super.extractItem();
    }

    @Override
    default ItemStack extractItem(int amount) {
        return ItemHandlerEntity.super.extractItem(amount);
    }

    @Override
    default ItemStack extractItem(int slot, int amount) {
        InventoryManager.ItemSlotEntry slotEntry = getItemSlot(slot);
        if (!canExtractItem() || slotEntry == null) {
            return null;
        }

        ItemStack currentStack = getItem(slot);

        if (currentStack != null) {
            ItemStack returnStack;

            if (currentStack.count <= amount) {
                returnStack = getItem(slot);
                this.setItem(null, slot);
            } else {
                returnStack = getItem(slot).split(amount);
                if (getItem(slot).count == 0) {
                    setItem(null, slot);
                }
            }

            return returnStack;
        }

        return null;
    }

    @Override
    default ItemStack extractItem(Item item, int amount) {
        return ItemHandlerEntity.super.extractItem(item, amount);
    }

    @Override
    default ItemStack extractItem(Item item, int meta, int amount) {
        return ItemHandlerEntity.super.extractItem(item, meta, amount);
    }

    @Override
    default boolean canInsertItem() {
        return true;
    }

    @Override
    default ItemStack insertItem(ItemStack stack, int slot) {
        InventoryManager.ItemSlotEntry slotEntry = getItemSlot(slot);
        if (!canInsertItem() || slotEntry == null || stack.getItem() == null || !slotEntry.isItemAllowed(stack.getItem())) {
            return stack;
        }
        
        ItemStack slotStack;

        slotStack = this.getItem(slot);

        if (slotStack == null) {
            this.setItem(stack, slot);
            return null;
        }

        if (slotStack.isItemEqual(stack)) {
            int addedCount = Math.min(slotStack.getItem().getMaxCount() - slotStack.count, stack.count);

            slotStack.count += addedCount;

            if (addedCount >= stack.count) {
                return null;
            } else {
                return new ItemStack(stack.getItem(), stack.count - addedCount, stack.getDamage());
            }
        }

        return stack;
    }

    @Override
    default ItemStack insertItem(ItemStack stack) {
        ItemStack insertedStack = stack.copy();

        for (int i = 0; i < this.getItemSlots(); ++i) {
            insertedStack = insertItem(insertedStack, i);
            if (insertedStack == null) {
                return insertedStack;
            }
        }

        return insertedStack;
    }

    @Override
    default ItemStack getItem(int slot) {
        return getInventoryManager().getItem(slot, null);
    }

    @Override
    default boolean setItem(ItemStack stack, int slot) {
        return getInventoryManager().setItem(stack, slot, null);
    }

    @Override
    default int getItemSlots() {
        return getInventoryManager().getItemSlots(null);
    }

    @Override
    default ItemStack[] getInventory() {
        return getInventoryManager().getInventory(null);
    }

    // Managed Item Handler
    default InventoryManager getInventoryManager() {
        return Util.assertImpl();
    }

    default InventoryManager.ItemSlotEntry addItemSlot() {
        return addItemSlot(64);
    }
    
    default InventoryManager.ItemSlotEntry addItemSlot(int maxStackSize) {
        return getInventoryManager().addSlot(maxStackSize);
    }

    default InventoryManager.ItemSlotEntry getItemSlot(int slot) {
        return getInventoryManager().getSlot(slot);
    }
}
