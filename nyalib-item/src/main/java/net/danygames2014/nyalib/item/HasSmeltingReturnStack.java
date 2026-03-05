package net.danygames2014.nyalib.item;

import net.minecraft.item.ItemStack;

/**
 * Implement on an {@link net.minecraft.item.Item} to provide a custom return {@link ItemStack} for when it's smelted
 */
public interface HasSmeltingReturnStack {
    /**
     * <p>Called when the item is smelted
     * <p>If you return the same stack that was passed into the method, the normal vanilla code path will be taken
     * <p><b>Note: This is called for each smelted item, not just when the last one is smelted!</b>
     * <p>If you wish to only pass in a return stack on the last item you can do something like this:
     * 
     * <pre>
     *     {@code
     *     @Override
     *     public ItemStack getSmeltingReturnStack(ItemStack stack) {
     *         if (stack.count == 1) {
     *             return new ItemStack(Item.COAL, 1, 1);
     *         }
     *
     *         return stack;
     *     }
     *     }
     * </pre>
     * 
     * @param stack The stack being smelted.
     * @return The stack which should be put in the place of the input item after smelting is done.
     */
    ItemStack getSmeltingReturnStack(ItemStack stack);
}
