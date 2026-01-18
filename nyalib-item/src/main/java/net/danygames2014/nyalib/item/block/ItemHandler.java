package net.danygames2014.nyalib.item.block;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.jetbrains.annotations.Nullable;

/**
 * An Item Handler interface to be implemented on a {@link net.minecraft.block.entity.BlockEntity}
 */
@SuppressWarnings("unused")
public interface ItemHandler extends ItemCapable {
    /**
     * Check if the block supports extracting items on the given side, if this returns false there
     * should be no point in trying to use any <code>extractItem</code> method.
     *
     * @param side The side to check
     * @return <code>true</code> if the block supports item extraction on the given side
     */
    boolean canExtractItem(@Nullable Direction side);

    /**
     * Extract any item from any slot
     *
     * @param side The side to extract from
     * @return The extracted ItemStack, <code>null</code> if nothing is extracted
     */
    default ItemStack extractItem(@Nullable Direction side) {
        return extractItem(Integer.MAX_VALUE, side);
    }

    /**
     * Extract a specified amount of any item from any slot
     *
     * @param amount The amount of items to extract (can be larger than the maximum stack size)
     * @param side   The side to extract from
     * @return The extracted ItemStack, <code>null</code> if nothing is extracted
     */
    default ItemStack extractItem(int amount, @Nullable Direction side) {
        for (int i = 0; i < getItemSlots(side); i++) {
            if (getItem(i, side) != null) {
                return extractItem(i, amount, side);
            }
        }
        return null;
    }

    /**
     * Extract an item in the given slot
     *
     * @param slot   The slot to extract from
     * @param amount The amount to extract (can be larger than the maximum stack size)
     * @param side   The side to extract from
     * @return The extracted {@link ItemStack}, <code>null</code> if nothing is extracted
     */
    ItemStack extractItem(int slot, int amount, @Nullable Direction side);

    /**
     * Extract the given {@link Item} with any meta from any slot
     *
     * @param item   The Item to extract
     * @param amount The amount to extract (can be larger than the maximum stack size)
     * @param side   The side to extract from
     * @return The extracted ItemStack, <code>null</code> if nothing is extracted
     */
    default ItemStack extractItem(Item item, int amount, @Nullable Direction side) {
        return extractItem(item, -1, amount, side);
    }


    /**
     * Extract the given {@link Item} with the specified meta from any slot
     *
     * @param item   The Item to extract
     * @param meta   The meta of the item to extract (-1 to match any meta)
     * @param amount The amount to extract (can be larger than the maximum stack size)
     * @param side   The side to extract from
     * @return The extracted ItemStack, <code>null</code> if nothing is extracted
     */
    default ItemStack extractItem(Item item, int meta, int amount, @Nullable Direction side) {
        ItemStack currentStack = null;
        int remaining = amount;

        for (int i = 0; i < getItemSlots(side); i++) {
            if (remaining <= 0) {
                break;
            }

            if (currentStack != null) {
                if (this.getItem(i, side).isItemEqual(currentStack)) {
                    ItemStack extractedStack = extractItem(i, remaining, side);
                    remaining -= extractedStack.count;
                    currentStack.count += extractedStack.count;
                }
            } else {
                if (getItem(i, side) != null && getItem(i, side).isOf(item) && (meta == -1 || getItem(i, side).getDamage() == meta)) {
                    ItemStack extractedStack = extractItem(i, remaining, side);
                    remaining -= extractedStack.count;
                    currentStack = extractedStack;
                }
            }
        }

        return currentStack;
    }

    /**
     * Check if the block supports inserting items on the given side, if this returns false there
     * should be no point in trying to use any <code>insertItem</code> method.
     *
     * @param side The side to check
     * @return <code>true</code> if the block supports item insertion on the given side
     */
    boolean canInsertItem(@Nullable Direction side);

    /**
     * Insert item into the given slot and return the remainder
     *
     * @param stack The {@link ItemStack} to insert
     * @param slot  Slot to insert into
     * @param side  The side to insert into
     * @return The remainder of the ItemStack (<code>null</code> if it was inserted entirely), this should be a new ItemStack, however it can be the same if it was not modified
     */
    ItemStack insertItem(ItemStack stack, int slot, @Nullable Direction side);

    /**
     * Insert item into any slot and return the remainder
     *
     * @param stack The {@link ItemStack} to insert
     * @param side  The side to insert into
     * @return The remainder of the ItemStack (<code>null</code> if it was inserted entirely), this should be a new ItemStack, however it can be the same if it was not modified
     */
    ItemStack insertItem(ItemStack stack, @Nullable Direction side);

    /**
     * Get the {@link ItemStack} in the given slot, If there is no {@link ItemStack}, then return null
     * <p>
     * <bold>DO NOT MODIFY THIS ITEMSTACK</bold>
     *
     * @param slot The slot to get the {@link ItemStack} from
     * @param side The side to query
     * @return The {@link ItemStack} in the slot, <code>null</code> if the slot is empty
     */
    ItemStack getItem(int slot, @Nullable Direction side);

    /**
     * Directly sets the given slot to the given ItemStack
     *
     * @param stack The {@link ItemStack} to set the slot to
     * @param slot  The slot to put the stack into
     * @param side  The side to set it on
     * @return Whether the action was succesfull
     */
    boolean setItem(ItemStack stack, int slot, @Nullable Direction side);

    /**
     * Get the size of the block's inventory
     *
     * @param side The side to get the size of
     * @return The number of slots this block's inventory has
     */
    int getItemSlots(@Nullable Direction side);

    /**
     * Get the entire inventory of the block
     *
     * @param side The side to get the inventory of
     * @return An array of all the ItemStacks
     */
    ItemStack[] getInventory(@Nullable Direction side);
}
