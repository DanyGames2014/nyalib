package net.danygames2014.nyalib.fluid;

import net.minecraft.item.ItemStack;

/**
 * A fluid handler interface to be implemented on {@link net.minecraft.item.Item}
 *  The first parameter is always the ItemStack of the Item on which this code is called on
 */
public interface FluidHandlerItem {
    /**
     * Check if the entity supports extracting fluid if this returns false there
     * should be no point in trying to use {@link #extractFluid(ItemStack, int, int)}
     *
     * @return <code>true</code> if the handler supports fluid extraction
     */
    boolean canExtractFluid(ItemStack thiz);

    /**
     * Extract a fluid in the given slot from the entity
     *
     * @param slot      The slot to extract from
     * @param amount    The amount in mB to extract
     * @return The FluidStack extracted, null if nothing is extracted
     */
    FluidStack extractFluid(ItemStack thiz, int slot, int amount);

    /**
     * Extract any fluid from the entity
     *
     * @return The extracted {@link FluidStack}
     */
    default FluidStack extractFluid(ItemStack thiz) {
        return extractFluid(thiz, Integer.MAX_VALUE);
    }

    /**
     * Extract a specified amount of any fluid from the entity
     *
     * @param amount    The amount of fluid in mB to extract
     * @return The extracted {@link FluidStack}
     */
    default FluidStack extractFluid(ItemStack thiz, int amount) {
        for (int i = 0; i < getFluidSlots(thiz); i++) {
            if (getFluid(thiz, i) != null) {
                return this.extractFluid(thiz, i, amount);
            }
        }
        return null;
    }

    /**
     * Extract the given fluid in any slot from the handler
     *
     * @param fluid     The Fluid to extract
     * @param amount    The amount in mB to extract
     * @return The FluidStack extracted, null if nothing is extracted
     */
    default FluidStack extractFluid(ItemStack thiz, Fluid fluid, int amount) {
        FluidStack currentStack = null;
        int remaining = amount;

        for (int i = 0; i < getFluidSlots(thiz); i++) {
            if (remaining <= 0) {
                break;
            }

            if (currentStack != null) {
                if (this.getFluid(thiz,i).isFluidEqual(currentStack)) {
                    FluidStack extractedStack = extractFluid(thiz,i, remaining);
                    remaining -= extractedStack.amount;
                    currentStack.amount += extractedStack.amount;
                }
            } else {
                FluidStack extractedStack = extractFluid(thiz,i, remaining);
                remaining -= extractedStack.amount;
                currentStack = extractedStack;
            }
        }

        return currentStack;
    }

    /**
     * Check if the entity supports inserting fluids, if this returns false there
     * should be no point in trying to use {@link #insertFluid(ItemStack, FluidStack)} or {@link #insertFluid(ItemStack, FluidStack, int)}
     *
     * @return <code>true</code> if the entity supports fluid insertion
     */
    boolean canInsertFluid(ItemStack thiz);

    /**
     * Insert fluid into the given slot and return the remainder
     *
     * @param stack     The {@link FluidStack} to insert
     * @param slot      Slot to insert into
     * @return The remainder of the FluidStack (null if it was inserted entirely), this should be a new FluidStack, however it can be the same if it was not modified
     */
    FluidStack insertFluid(ItemStack thiz, FluidStack stack, int slot);

    /**
     * Insert fluid into any slot and return the remainder
     *
     * @param stack     The {@link FluidStack} to insert
     * @return The remainder of the FluidStack (null if it was inserted entirely), this should be a new FluidStack, however it can be the same if it was not modified
     */
    FluidStack insertFluid(ItemStack thiz, FluidStack stack);

    /**
     * Get the {@link FluidStack} in the given slot, If there is no {@link FluidStack}, then return null
     * <p>
     *
     * @param slot      The slot to get the {@link FluidStack} from
     * @return The {@link FluidStack} in the slot
     */
    FluidStack getFluid(ItemStack thiz, int slot);

    /**
     * Sets a {@link FluidStack} into the given slot
     *
     * @param slot      The slot to set the {@link FluidStack} into
     * @param stack     The {@link FluidStack} to set into the slot
     * @return Whether the action was succesfull
     */
    boolean setFluid(ItemStack thiz, int slot, FluidStack stack);
    
    /**
     * Get the size of the entity fluid inventory
     *
     * @return The number of slots this entity has
     */
    int getFluidSlots(ItemStack thiz);

    /**
     * Get the capacity of the given slot
     *
     * @param slot      The slot to query for capacity
     * @return The capacity of the slot
     */
    int getFluidCapacity(ItemStack thiz, int slot);

    /**
     * Get the entire fluid inventory of the entity
     *
     * @return An array of all the FluidStacks
     */
    FluidStack[] getFluids(ItemStack thiz);
}
