package net.danygames2014.nyalib.item.block;

import net.danygames2014.nyalib.item.InventoryManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.util.Util;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public interface ManagedItemHandler extends ItemHandler {
    @Override
    default boolean canExtractItem(@Nullable Direction side) {
        return true;
    }

    @Override
    default ItemStack extractItem(@Nullable Direction side) {
        return ItemHandler.super.extractItem(side);
    }

    @Override
    default ItemStack extractItem(int amount, @Nullable Direction side) {
        return ItemHandler.super.extractItem(amount, side);
    }

    @Override
    default ItemStack extractItem(int slot, int amount, @Nullable Direction side) {
        InventoryManager.ItemSlotEntry slotEntry = getItemSlot(slot, side);
        if (!canExtractItem(side) || slotEntry == null || !slotEntry.isSideAllowed(side)) {
            return null;
        }

        ItemStack currentStack = getItem(slot, side);

        if (currentStack != null) {
            ItemStack returnStack;

            if (currentStack.count <= amount) {
                returnStack = getItem(slot, side);
                this.setItem(null, slot, side);
            } else {
                returnStack = getItem(slot, side).split(amount);
                if (getItem(slot, side).count == 0) {
                    setItem(null, slot, side);
                }
            }
            
            return returnStack;
        }

        return null;
    }

    @Override
    default ItemStack extractItem(Item item, int amount, @Nullable Direction side) {
        return ItemHandler.super.extractItem(item, amount, side);
    }

    @Override
    default ItemStack extractItem(Item item, int meta, int amount, @Nullable Direction side) {
        return ItemHandler.super.extractItem(item, meta, amount, side);
    }

    @Override
    default boolean canInsertItem(@Nullable Direction side) {
        return true;
    }

    @Override
    default ItemStack insertItem(ItemStack stack, int slot, @Nullable Direction side) {
        InventoryManager.ItemSlotEntry slotEntry = getItemSlot(slot, side);
        if (!canInsertItem(side) || slotEntry == null || !slotEntry.isSideAllowed(side) || stack.getItem() == null || !slotEntry.isItemAllowed(stack.getItem())) {
            return stack;
        }

        ItemStack slotStack;

        slotStack = this.getItem(slot, side);

        if (slotStack == null) {
            this.setItem(stack, slot, side);
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
    default ItemStack insertItem(ItemStack stack, @Nullable Direction side) {
        ItemStack insertedStack = stack.copy();

        for (int i = 0; i < this.getItemSlots(side); ++i) {
            insertedStack = insertItem(insertedStack, i, side);
            if (insertedStack == null) {
                return insertedStack;
            }
        }

        return insertedStack;
    }

    @Override
    default ItemStack getItem(int slot, @Nullable Direction side) {
        return getInventoryManager().getItem(slot, side);
    }

    @Override
    default boolean setItem(ItemStack stack, int slot, @Nullable Direction side) {
        return getInventoryManager().setItem(stack, slot, side);
    }

    @Override
    default int getItemSlots(@Nullable Direction side) {
        return getInventoryManager().getItemSlots(side);
    }

    @Override
    default ItemStack[] getInventory(@Nullable Direction side) {
        return getInventoryManager().getInventory(side);
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

    default InventoryManager.ItemSlotEntry getItemSlot(int slot, @Nullable Direction side) {
        return getInventoryManager().getSlot(slot, side);
    }
}
