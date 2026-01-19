package net.danygames2014.nyalib.item.item;

import net.danygames2014.nyalib.item.InventoryManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.util.Util;

public interface ManagedItemHandlerItem extends ItemHandlerItem {
    @Override
    default boolean canExtractItem(ItemStack thiz) {
        return true;
    }

    @Override
    default ItemStack extractItem(ItemStack thiz) {
        return ItemHandlerItem.super.extractItem(thiz);
    }

    @Override
    default ItemStack extractItem(ItemStack thiz, int amount) {
        return ItemHandlerItem.super.extractItem(thiz, amount);
    }

    @Override
    default ItemStack extractItem(ItemStack thiz, int slot, int amount) {
        InventoryManager.ItemSlotEntry slotEntry = getItemSlot(thiz, slot);
        if (!canExtractItem(thiz) || slotEntry == null) {
            return null;
        }

        ItemStack currentStack = getItem(thiz, slot);

        if (currentStack != null) {
            ItemStack returnStack;

            if (currentStack.count <= amount) {
                returnStack = getItem(thiz, slot);
                this.setItem(thiz, null, slot);
            } else {
                returnStack = getItem(thiz, slot).split(amount);
                if (getItem(thiz, slot).count == 0) {
                    setItem(thiz, null, slot);
                }
            }

            return returnStack;
        }

        return null;
    }

    @Override
    default ItemStack extractItem(ItemStack thiz, Item item, int amount) {
        return ItemHandlerItem.super.extractItem(thiz, item, amount);
    }

    @Override
    default ItemStack extractItem(ItemStack thiz, Item item, int meta, int amount) {
        return ItemHandlerItem.super.extractItem(thiz, item, meta, amount);
    }

    @Override
    default boolean canInsertItem(ItemStack thiz) {
        return true;
    }

    @Override
    default ItemStack insertItem(ItemStack thiz, ItemStack stack, int slot) {
        InventoryManager.ItemSlotEntry slotEntry = getItemSlot(thiz, slot);
        if (!canInsertItem(thiz) || slotEntry == null || stack.getItem() == null || !slotEntry.isItemAllowed(stack.getItem())) {
            return stack;
        }
        
        ItemStack slotStack;

        slotStack = this.getItem(thiz, slot);

        if (slotStack == null) {
            this.setItem(thiz, stack, slot);
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
    default ItemStack insertItem(ItemStack thiz, ItemStack stack) {
        ItemStack insertedStack = stack.copy();

        for (int i = 0; i < this.getItemSlots(thiz); ++i) {
            insertedStack = insertItem(thiz, insertedStack, i);
            if (insertedStack == null) {
                return insertedStack;
            }
        }

        return insertedStack;
    }

    @Override
    default ItemStack getItem(ItemStack thiz, int slot) {
        return getInventoryManager(thiz).getItem(slot, null);
    }

    @Override
    default boolean setItem(ItemStack thiz, ItemStack stack, int slot) {
        return getInventoryManager(thiz).setItem(stack, slot, null);
    }

    @Override
    default int getItemSlots(ItemStack thiz) {
        return getInventoryManager(thiz).getItemSlots(null);
    }

    @Override
    default ItemStack[] getInventory(ItemStack thiz) {
        return getInventoryManager(thiz).getInventory(null);
    }

    // Managed Item Handler
    default InventoryManager getInventoryManager(ItemStack thiz) {
        return Util.assertImpl();
    }

    default InventoryManager.ItemSlotEntry addItemSlot() {
        return addItemSlot(64);
    }

    default InventoryManager.ItemSlotEntry addItemSlot(int maxStackSize) {
        return Util.assertImpl();
    }
    
    default InventoryManager.ItemSlotEntry addItemSlot(ItemStack thiz) {
        return addItemSlot(thiz, 64);
    }

    default InventoryManager.ItemSlotEntry addItemSlot(ItemStack thiz, int maxStackSize) {
        return getInventoryManager(thiz).addSlot(maxStackSize);
    }

    default InventoryManager.ItemSlotEntry getItemSlot(ItemStack thiz, int slot) {
        return getInventoryManager(thiz).getSlot(slot);
    }
}
