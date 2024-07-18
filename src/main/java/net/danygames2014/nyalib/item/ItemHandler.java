package net.danygames2014.nyalib.item;

import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public interface ItemHandler {
    /**
     * Check if the block supports extracting items on this side, if this returns false there
     * should be no point in trying to use {@link #extractItem(int, int, Direction)}
     * 
     * @param direction Direction to check
     * @return <code>true</code> if the device supports item extraction from the given direction
     */
    boolean canExtractItem(@Nullable Direction direction);

    /**
     * Extract an item from the block
     *
     * @param slot The slot to extract from
     * @param amount The amount to extract (can be larger than the maximum stack size)
     * @param direction The direction to extract from
     * @return The ItemStack extracted, null if nothing is extracted
     */
    ItemStack extractItem(int slot, int amount, @Nullable Direction direction);

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
     * @param stack The {@link ItemStack} to insert
     * @param slot Slot to insert into
     * @param direction Direction to insert from
     * @return The remainder of the ItemStack (null if it was inserted entirely), this should be a new ItemStack, however it can be the same if it was not modified
     */
    ItemStack insertItem(ItemStack stack, int slot, @Nullable Direction direction);

    /**
     * Insert item into any slot and return the remainder
     *
     * @param stack The {@link ItemStack} to insert
     * @param direction Direction to insert from
     * @return The remainder of the ItemStack (null if it was inserted entirely), this should be a new ItemStack, however it can be the same if it was not modified
     */
    ItemStack insertItem(ItemStack stack, @Nullable Direction direction);

    /**
     * Get the {@link ItemStack} in the given slot, If there is no {@link ItemStack}, then return null
     * <p>
     *
     * @param slot The slot to get the {@link ItemStack} from
     * @param direction The direction to query from
     * @return The {@link ItemStack} in the slot
     */
    ItemStack getStackInSlot(int slot, @Nullable Direction direction);

    /**
     * Get the size of the block inventory
     *
     * @return The number of slots this block has
     */
    int getSize();
}
