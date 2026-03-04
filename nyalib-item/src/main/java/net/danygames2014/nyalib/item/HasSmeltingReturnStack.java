package net.danygames2014.nyalib.item;

import net.minecraft.item.ItemStack;

/**
 * Implement on an {@link net.minecraft.item.Item} to provide a custom return {@link ItemStack} for when it's smelted
 */
public interface HasSmeltingReturnStack {
    /**
     * Called when the item is smelted
     * <p><b>Note: This is for each smelted item, not just when the last one is smelted!</b>
     * 
     * @param stack The stack being smelted.
     * @return The stack which should be put int he place of the input item after smelting is done.
     */
    ItemStack getSmeltingReturnStack(ItemStack stack);
}
