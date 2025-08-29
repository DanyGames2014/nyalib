package net.danygames2014.nyalib.capability.item.itemhandler;

import net.danygames2014.nyalib.capability.item.ItemCapability;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@SuppressWarnings("unused")
public abstract class ItemHandlerItemCapability extends ItemCapability {
    /**
     * Check if the item supports extracting items, if this returns false there
     * should be no point in trying to use any <code>extractItem</code> method.
     *
     * @return <code>true</code> if the item supports item extraction
     */
    public abstract boolean canExtractItem();

    /**
     * Extract an item in the given slot
     *
     * @param slot   The slot to extract from
     * @param amount The amount to extract (can be larger than the maximum stack size)
     * @return The extracted {@link ItemStack}, <code>null</code> if nothing is extracted
     */
    public abstract ItemStack extractItem(int slot, int amount);

    /**
     * Extract any item from any slot
     *
     * @return The extracted ItemStack, <code>null</code> if nothing is extracted
     */
    public abstract ItemStack extractItem();

    /**
     * Extract a specified amount of any item from any slot
     *
     * @param amount The amount of items to extract (can be larger than the maximum stack size)
     * @return The extracted ItemStack, <code>null</code> if nothing is extracted
     */
    public abstract ItemStack extractItem(int amount);

    /**
     * Extract the given {@link Item} with any meta from any slot
     *
     * @param item   The Item to extract
     * @param amount The amount to extract (can be larger than the maximum stack size)
     * @return The extracted ItemStack, <code>null</code> if nothing is extracted
     */
    public abstract ItemStack extractItem(Item item, int amount);

    /**
     * Extract the given {@link Item} with the specified meta from any slot
     *
     * @param item   The Item to extract
     * @param meta   The meta of the item to extract (-1 to match any meta)
     * @param amount The amount to extract (can be larger than the maximum stack size)
     * @return The extracted ItemStack, <code>null</code> if nothing is extracted
     */
    public abstract ItemStack extractItem(Item item, int meta, int amount);

    /**
     * Check if the item supports inserting items, if this returns false there
     * should be no point in trying to use any <code>insertItem</code> method.
     *
     * @return <code>true</code> if the item supports item insertion
     */
    public abstract boolean canInsertItem();

    /**
     * Insert item into the given slot and return the remainder
     *
     * @param stack The {@link ItemStack} to insert
     * @param slot  Slot to insert into
     * @return The remainder of the ItemStack (<code>null</code> if it was inserted entirely), this should be a new ItemStack, however it can be the same if it was not modified
     */
    public abstract ItemStack insertItem(ItemStack stack, int slot);

    /**
     * Insert item into any slot and return the remainder
     *
     * @param stack The {@link ItemStack} to insert
     * @return The remainder of the ItemStack (<code>null</code> if it was inserted entirely), this should be a new ItemStack, however it can be the same if it was not modified
     */
    public abstract ItemStack insertItem(ItemStack stack);

    /**
     * Get the {@link ItemStack} in the given slot, If there is no {@link ItemStack}, then return null
     * <p>
     * <bold>DO NOT MODIFY THIS ITEMSTACK</bold>
     *
     * @param slot The slot to get the {@link ItemStack} from
     * @return The {@link ItemStack} in the slot, <code>null</code> if the slot is empty
     */
    public abstract ItemStack getItem(int slot);

    /**
     * Directly sets the given slot to the given ItemStack
     *
     * @param stack The {@link ItemStack} to set the slot to
     * @param slot  The slot to put the stack into
     * @return Whether the action was succesfull
     */
    public abstract boolean setItem(ItemStack stack, int slot);

    /**
     * Get the size of the item's inventory
     *
     * @return The number of slots this item's inventory has
     */
    public abstract int getItemSlots();

    /**
     * Get the entire inventory of the item
     *
     * @return An array of all the ItemStacks
     */
    public abstract ItemStack[] getInventory();
}
