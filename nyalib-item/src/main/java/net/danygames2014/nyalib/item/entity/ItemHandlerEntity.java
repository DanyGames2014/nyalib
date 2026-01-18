package net.danygames2014.nyalib.item.entity;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * An Item Handler interface to be implemented on {@link net.minecraft.entity.Entity}
 */
@SuppressWarnings("unused")
public interface ItemHandlerEntity {
    /**
     * Check if the entity supports extracting items, if this returns false there
     * should be no point in trying to use any <code>extractItem</code> method.
     *
     * @return <code>true</code> if the entity supports item extraction
     */
    boolean canExtractItem();

    /**
     * Extract any item from any slot
     *
     * @return The extracted ItemStack, <code>null</code> if nothing is extracted
     */
    default ItemStack extractItem() {
        for (int i = 0; i < getItemSlots(); i++) {
            if (getItem(i) != null) {
                return extractItem(i, Integer.MAX_VALUE);
            }
        }
        return null;
    }

    /**
     * Extract a specified amount of any item from any slot
     *
     * @param amount The amount of items to extract (can be larger than the maximum stack size)
     * @return The extracted ItemStack, <code>null</code> if nothing is extracted
     */
    default ItemStack extractItem(int amount) {
        for (int i = 0; i < getItemSlots(); i++) {
            if (getItem(i) != null) {
                return extractItem(i, amount);
            }
        }
        return null;
    }

    /**
     * Extract an item in the given slot
     *
     * @param slot   The slot to extract from
     * @param amount The amount to extract (can be larger than the maximum stack size)
     * @return The extracted {@link ItemStack}, <code>null</code> if nothing is extracted
     */
    ItemStack extractItem(int slot, int amount);

    /**
     * Extract the given {@link Item} with any meta from any slot
     *
     * @param item   The Item to extract
     * @param amount The amount to extract (can be larger than the maximum stack size)
     * @return The extracted ItemStack, <code>null</code> if nothing is extracted
     */
    default ItemStack extractItem(Item item, int amount) {
        return extractItem(item, -1, amount);
    }

    /**
     * Extract the given {@link Item} with the specified meta from any slot
     *
     * @param item   The Item to extract
     * @param meta   The meta of the item to extract (-1 to match any meta)
     * @param amount The amount to extract (can be larger than the maximum stack size)
     * @return The extracted ItemStack, <code>null</code> if nothing is extracted
     */
    default ItemStack extractItem(Item item, int meta, int amount) {
        ItemStack currentStack = null;
        int remaining = amount;

        for (int i = 0; i < getItemSlots(); i++) {
            if (remaining <= 0) {
                break;
            }

            if (currentStack != null) {
                if (this.getItem(i).isItemEqual(currentStack)) {
                    ItemStack extractedStack = extractItem(i, remaining);
                    remaining -= extractedStack.count;
                    currentStack.count += extractedStack.count;
                }
            } else {
                if (getItem(i).isOf(item) && (meta == -1 || getItem(i).getDamage() == meta)) {
                    ItemStack extractedStack = extractItem(i, remaining);
                    remaining -= extractedStack.count;
                    currentStack = extractedStack;
                }
            }
        }

        return currentStack;
    }

    /**
     * Check if the entity supports inserting items, if this returns false there
     * should be no point in trying to use any <code>insertItem</code> method.
     *
     * @return <code>true</code> if the entity supports item insertion
     */
    boolean canInsertItem();

    /**
     * Insert item into the given slot and return the remainder
     *
     * @param stack The {@link ItemStack} to insert
     * @param slot  Slot to insert into
     * @return The remainder of the ItemStack (<code>null</code> if it was inserted entirely), this should be a new ItemStack, however it can be the same if it was not modified
     */
    ItemStack insertItem(ItemStack stack, int slot);

    /**
     * Insert item into any slot and return the remainder
     *
     * @param stack The {@link ItemStack} to insert
     * @return The remainder of the ItemStack (<code>null</code> if it was inserted entirely), this should be a new ItemStack, however it can be the same if it was not modified
     */
    ItemStack insertItem(ItemStack stack);

    /**
     * Get the {@link ItemStack} in the given slot, If there is no {@link ItemStack}, then return null
     * <p>
     * <bold>DO NOT MODIFY THIS ITEMSTACK</bold>
     *
     * @param slot The slot to get the {@link ItemStack} from
     * @return The {@link ItemStack} in the slot, <code>null</code> if the slot is empty
     */
    ItemStack getItem(int slot);

    /**
     * Directly sets the given slot to the given ItemStack
     *
     * @param stack The {@link ItemStack} to set the slot to
     * @param slot  The slot to put the stack into
     * @return Whether the action was succesfull
     */
    boolean setItem(ItemStack stack, int slot);

    /**
     * Get the size of the entities's inventory
     *
     * @return The number of slots this entities's inventory has
     */
    int getItemSlots();

    /**
     * Get the entire inventory of the entity
     *
     * @return An array of all the ItemStacks
     */
    ItemStack[] getInventory();
}
