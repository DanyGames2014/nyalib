package net.danygames2014.nyalib.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * An Item Handler interface to be implemented on entities
 */
@SuppressWarnings("unused")
public interface ItemHandlerEntity extends ItemCapable {
    /**
     * Check if the handler supports extracting items, if this returns false there
     * should be no point in trying to use {@link #extractItem(int, int)}
     *
     * @return <code>true</code> if the entity supports item extraction
     */
    boolean canExtractItem();

    /**
     * Extract an item in the given slot from the handler
     *
     * @param slot   The slot to extract from
     * @param amount The amount to extract (can be larger than the maximum stack size)
     * @return The ItemStack extracted, null if nothing is extracted
     */
    ItemStack extractItem(int slot, int amount);


    /**
     * Extract any item
     *
     * @return The extracted ItemStack
     */
    default ItemStack extractItem() {
        for (int i = 0; i < getItemSlots(); i++) {
            if (getItemInSlot(i) != null) {
                return extractItem(i, Integer.MAX_VALUE);
            }
        }
        return null;
    }


    /**
     * Extract the given item in any slot
     *
     * @param item   The Item to extract
     * @param amount The amount to extract (can be larger than the maximum stack size)
     * @return The ItemStack extracted, null if nothing is extracted
     */
    default ItemStack extractItem(Item item, int amount) {
        ItemStack currentStack = null;
        int remaining = amount;

        for (int i = 0; i < getItemSlots(); i++) {
            if (remaining <= 0) {
                break;
            }

            if (currentStack != null) {
                if (this.getItemInSlot(i).isItemEqual(currentStack)) {
                    ItemStack extractedStack = extractItem(i, remaining);
                    remaining -= extractedStack.count;
                    currentStack.count += extractedStack.count;
                }
            } else {
                if (getItemInSlot(i).isOf(item)) {
                    ItemStack extractedStack = extractItem(i, remaining);
                    remaining -= extractedStack.count;
                    currentStack = extractedStack;
                }
            }
        }

        return currentStack;
    }

    /**
     * Check if the handler supports inserting items, if this returns false there
     * should be no point in trying to use {@link #insertItem(ItemStack)} or {@link #insertItem(ItemStack, int)}
     *
     * @return <code>true</code> if the entity supports item insertion
     */
    boolean canInsertItem();

    /**
     * Insert item into the given slot and return the remainder
     *
     * @param stack     The {@link ItemStack} to insert
     * @param slot      Slot to insert into
     * @return The remainder of the ItemStack (null if it was inserted entirely), this should be a new ItemStack, however it can be the same if it was not modified
     */
    ItemStack insertItem(ItemStack stack, int slot);

    /**
     * Insert item into any slot and return the remainder
     *
     * @param stack     The {@link ItemStack} to insert
     * @return The remainder of the ItemStack (null if it was inserted entirely), this should be a new ItemStack, however it can be the same if it was not modified
     */
    ItemStack insertItem(ItemStack stack);

    /**
     * Get the {@link ItemStack} in the given slot, If there is no {@link ItemStack}, then return null
     * <p>
     *
     * @param slot The slot to get the {@link ItemStack} from
     * @return The {@link ItemStack} in the slot
     */
    ItemStack getItemInSlot(int slot);

    /**
     * Get the size of the handler inventory
     *
     * @return The number of slots this handler has
     */
    int getItemSlots();

    /**
     * Get the entire inventory of the handler
     *
     * @return An array of all the ItemStacks
     */
    ItemStack[] getInventory();
}
