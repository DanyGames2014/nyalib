package net.danygames2014.nyalib.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * An Item Handler interface to be implemented on {@link Item}
 * The first parameter is always the ItemStack of the Item on which this code is called on
 */
@SuppressWarnings("unused")
public interface ItemHandlerItem {
    /**
     * Check if the handler supports extracting items, if this returns false there
     * should be no point in trying to use {@link #extractItem(ItemStack, int, int)}
     *
     * @param thiz The stack, on which this action is called on
     * @return <code>true</code> if the device supports item extraction
     */
    boolean canExtractItem(ItemStack thiz);

    /**
     * Extract an item in the given slot
     *
     * @param thiz   The stack, on which this action is called on
     * @param slot   The slot to extract from
     * @param amount The amount to extract (can be larger than the maximum stack size)
     * @return The ItemStack extracted, null if nothing is extracted
     */
    ItemStack extractItem(ItemStack thiz, int slot, int amount);

    /**
     * Extract any item
     *
     * @param thiz The stack, on which this action is called on
     * @return The extracted ItemStack
     */
    default ItemStack extractItem(ItemStack thiz) {
        return extractItem(thiz, Integer.MAX_VALUE);
    }

    /**
     * Extract a specified amount of any item
     *
     * @param thiz The stack, on which this action is called on
     * @return The extracted ItemStack
     */
    default ItemStack extractItem(ItemStack thiz, int amount) {
        for (int i = 0; i < getItemSlots(thiz); i++) {
            if (getItem(thiz, i) != null) {
                return extractItem(thiz, i, amount);
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
        return extractItem(thiz, item, -1, amount);
    }

    /**
     * Extract the given item with the given meta in any slot from the handler
     *
     * @param thiz   The stack, on which this action is called on
     * @param item   The Item to extract
     * @param meta   The meta of the item to extract. -1 to not match meta.
     * @param amount The amount to extract (can be larger than the maximum stack size)
     * @return The ItemStack extracted, null if nothing is extracted
     */
    default ItemStack extractItem(ItemStack thiz, Item item, int meta, int amount) {
        ItemStack currentStack = null;
        int remaining = amount;

        for (int i = 0; i < getItemSlots(thiz); i++) {
            if (remaining <= 0) {
                break;
            }

            if (currentStack != null) {
                if (this.getItem(thiz, i).isItemEqual(currentStack)) {
                    ItemStack extractedStack = extractItem(thiz, i, remaining);
                    remaining -= extractedStack.count;
                    currentStack.count += extractedStack.count;
                }
            } else {
                if (getItem(thiz, i).isOf(item) && (meta == -1 || getItem(thiz, i).getDamage() == meta)) {
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
     * @param thiz The stack, on which this action is called on
     * @return <code>true</code> if the device supports item insertion
     */
    boolean canInsertItem(ItemStack thiz);

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
     * @param thiz  The stack, on which this action is called on
     * @param stack The {@link ItemStack} to insert
     * @return The remainder of the ItemStack (null if it was inserted entirely), this should be a new ItemStack, however it can be the same if it was not modified
     */
    ItemStack insertItem(ItemStack thiz, ItemStack stack);

    /**
     * Get the {@link ItemStack} in the given slot, If there is no {@link ItemStack}, then return null
     * <p>
     *
     * @param thiz The stack, on which this action is called on
     * @param slot The slot to get the {@link ItemStack} from
     * @return The {@link ItemStack} in the slot
     */
    ItemStack getItem(ItemStack thiz, int slot);

    /**
     * Directly sets the given slot to the given ItemStack
     *
     * @param thiz  The stack, on which this action is called on
     * @param stack The {@link ItemStack} to set the slot to
     * @param slot  The slot to put the stack into
     * @return Whether the action was successfull
     */
    boolean setItem(ItemStack thiz, ItemStack stack, int slot);

    /**
     * Get the size of the handler inventory
     *
     * @param thiz The stack, on which this action is called on
     * @return The number of slots this handler has
     */
    int getItemSlots(ItemStack thiz);

    /**
     * Get the entire inventory of the handler
     *
     * @param thiz The stack, on which this action is called on
     * @return An array of all the ItemStacks
     */
    ItemStack[] getInventory(ItemStack thiz);
}
