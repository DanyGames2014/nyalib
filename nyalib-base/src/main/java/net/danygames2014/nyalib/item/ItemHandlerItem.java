package net.danygames2014.nyalib.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * An Item Handler interface to be implemented on items
 * The first parameter is always the ItemStack of the Item on which this code is called on
 */
@SuppressWarnings("unused")
public interface ItemHandlerItem extends ItemCapable {
    /**
     * Check if the handler supports extracting items, if this returns false there
     * should be no point in trying to use {@link #extractItem(ItemStack, int, int)}
     *
     * @return <code>true</code> if the device supports item extraction
     */
    boolean canExtractItem(ItemStack thiz);

    /**
     * Extract an item in the given slot from the handler
     *
     * @param slot   The slot to extract from
     * @param amount The amount to extract (can be larger than the maximum stack size)
     * @return The ItemStack extracted, null if nothing is extracted
     */
    ItemStack extractItem(ItemStack thiz, int slot, int amount);


    /**
     * Extract any item from the given direction
     *
     * @return The extracted ItemStack
     */
    default ItemStack extractItem(ItemStack thiz) {
        for (int i = 0; i < getItemSlots(thiz); i++) {
            if (getItemInSlot(thiz, i) != null) {
                return extractItem(thiz, i, Integer.MAX_VALUE);
            }
        }
        return null;
    }


    /**
     * Extract the given item in any slot from the handler
     *
     * @param item   The Item to extract
     * @param amount The amount to extract (can be larger than the maximum stack size)
     * @return The ItemStack extracted, null if nothing is extracted
     */
    default ItemStack extractItem(ItemStack thiz, Item item, int amount) {
        ItemStack currentStack = null;
        int remaining = amount;

        for (int i = 0; i < getItemSlots(thiz); i++) {
            if (remaining <= 0) {
                break;
            }

            if (currentStack != null) {
                if (this.getItemInSlot(thiz, i).isItemEqual(currentStack)) {
                    ItemStack extractedStack = extractItem(thiz, i, remaining);
                    remaining -= extractedStack.count;
                    currentStack.count += extractedStack.count;
                }
            } else {
                if (getItemInSlot(thiz, i).isOf(item)) {
                    ItemStack extractedStack = extractItem(thiz, i, remaining);
                    remaining -= extractedStack.count;
                    currentStack = extractedStack;
                }
            }
        }

        return currentStack;
    }

    /**
     * Check if the handler supports inserting items, if this returns false there
     * should be no point in trying to use {@link #insertItem(ItemStack, ItemStack)} or {@link #insertItem(ItemStack, ItemStack, int)}
     *
     * @return <code>true</code> if the device supports item insertion
     */
    boolean canInsertItem();

    /**
     * Insert item into the given slot and return the remainder
     *
     * @param thiz  The stack, on which this action is called on
     * @param stack The {@link ItemStack} to insert
     * @param slot  Slot to insert into
     * @return The remainder of the ItemStack (null if it was inserted entirely), this should be a new ItemStack, however it can be the same if it was not modified
     */
    ItemStack insertItem(ItemStack thiz, ItemStack stack, int slot);

    /**
     * Insert item into any slot and return the remainder
     *
     * @param stack The {@link ItemStack} to insert
     * @return The remainder of the ItemStack (null if it was inserted entirely), this should be a new ItemStack, however it can be the same if it was not modified
     */
    ItemStack insertItem(ItemStack thiz, ItemStack stack);

    /**
     * Get the {@link ItemStack} in the given slot, If there is no {@link ItemStack}, then return null
     * <p>
     *
     * @param slot The slot to get the {@link ItemStack} from
     * @return The {@link ItemStack} in the slot
     */
    ItemStack getItemInSlot(ItemStack thiz, int slot);

    /**
     * Get the size of the handler inventory
     *
     * @return The number of slots this handler has
     */
    int getItemSlots(ItemStack thiz);

    /**
     * Get the entire inventory of the handler
     *
     * @return An array of all the ItemStacks
     */
    ItemStack[] getInventory(ItemStack thiz);
}
