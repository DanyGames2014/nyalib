package net.danygames2014.nyalib.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public interface ItemHandler extends ItemCapable {
    /**
     * Check if the block supports extracting items on this side, if this returns false there
     * should be no point in trying to use {@link #extractItem(int, int, Direction)}
     *
     * @param direction Direction to check
     * @return <code>true</code> if the device supports item extraction from the given direction
     */
    boolean canExtractItem(@Nullable Direction direction);

    /**
     * Extract an item in the given slot from the block
     *
     * @param slot      The slot to extract from
     * @param amount    The amount to extract (can be larger than the maximum stack size)
     * @param direction The direction to extract from
     * @return The ItemStack extracted, null if nothing is extracted
     */
    ItemStack extractItem(int slot, int amount, @Nullable Direction direction);


    /**
     * Extract any item from the given direction
     *
     * @param direction The direction to extract from
     * @return The extracted ItemStack
     */
    default ItemStack extractItem(@Nullable Direction direction) {
        for (int i = 0; i < getItemSlots(direction); i++) {
            if (getItemInSlot(i, direction) != null) {
                return extractItem(i, Integer.MAX_VALUE, direction);
            }
        }
        return null;
    }


    /**
     * Extract the given item in any slot from the block
     *
     * @param item      The Item to extract
     * @param amount    The amount to extract (can be larger than the maximum stack size)
     * @param direction The direction to extract from
     * @return The ItemStack extracted, null if nothing is extracted
     */
    default ItemStack extractItem(Item item, int amount, @Nullable Direction direction) {
        ItemStack currentStack = null;
        int remaining = amount;

        for (int i = 0; i < getItemSlots(direction); i++) {
            if (remaining <= 0) {
                break;
            }

            if (currentStack != null) {
                if (this.getItemInSlot(i, direction).isItemEqual(currentStack)) {
                    ItemStack extractedStack = extractItem(i, remaining, direction);
                    remaining -= extractedStack.count;
                    currentStack.count += extractedStack.count;
                }
            } else {
                if (getItemInSlot(i, direction).isOf(item)) {
                    ItemStack extractedStack = extractItem(i, remaining, direction);
                    remaining -= extractedStack.count;
                    currentStack = extractedStack;
                }
            }
        }

        return currentStack;
    }

    /**
     * Check if the block supports inserting items on this side, if this returns false there
     * should be no point in trying to use {@link #insertItem(ItemStack, Direction)} or {@link #insertItem(ItemStack, int, Direction)}
     *
     * @param direction Direction to check
     * @return <code>true</code> if the device supports item insertion from the given direction
     */
    boolean canInsertItem(@Nullable Direction direction);

    /**
     * Insert item into the given slot and return the remainder
     *
     * @param stack     The {@link ItemStack} to insert
     * @param slot      Slot to insert into
     * @param direction Direction to insert from
     * @return The remainder of the ItemStack (null if it was inserted entirely), this should be a new ItemStack, however it can be the same if it was not modified
     */
    ItemStack insertItem(ItemStack stack, int slot, @Nullable Direction direction);

    /**
     * Insert item into any slot and return the remainder
     *
     * @param stack     The {@link ItemStack} to insert
     * @param direction Direction to insert from
     * @return The remainder of the ItemStack (null if it was inserted entirely), this should be a new ItemStack, however it can be the same if it was not modified
     */
    ItemStack insertItem(ItemStack stack, @Nullable Direction direction);

    /**
     * Get the {@link ItemStack} in the given slot, If there is no {@link ItemStack}, then return null
     * <p>
     *
     * @param slot      The slot to get the {@link ItemStack} from
     * @param direction The direction to query from
     * @return The {@link ItemStack} in the slot
     */
    ItemStack getItemInSlot(int slot, @Nullable Direction direction);

    /**
     * Get the size of the block inventory
     *
     * @param direction The direction to get the size from
     * @return The number of slots this block has
     */
    int getItemSlots(@Nullable Direction direction);

    /**
     * Get the entire inventory of the block
     *
     * @param direction The direction to get the inventory from
     * @return An array of all the ItemStacks
     */
    ItemStack[] getInventory(@Nullable Direction direction);
}
