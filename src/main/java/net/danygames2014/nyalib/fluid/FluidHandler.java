package net.danygames2014.nyalib.fluid;

import net.modificationstation.stationapi.api.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public interface FluidHandler extends FluidCapable {
    /**
     * Check if the block supports extracting fluid on this side, if this returns false there
     * should be no point in trying to use {@link #extractFluid(int, int, Direction)}
     *
     * @param direction Direction to check
     * @return <code>true</code> if the device supports fluid extraction from the given direction
     */
    boolean canExtractFluid(@Nullable Direction direction);

    /**
     * Extract a fluid in the given slot from the block
     *
     * @param slot      The slot to extract from
     * @param amount    The amount in mB to extract
     * @param direction The direction to extract from
     * @return The FluidStack extracted, null if nothing is extracted
     */
    FluidStack extractFluid(int slot, int amount, @Nullable Direction direction);


    /**
     * Extract the given fluid in any slot from the block
     *
     * @param fluid     The Fluid to extract
     * @param amount    The amount in mB to extract
     * @param direction The direction to extract from
     * @return The FluidStack extracted, null if nothing is extracted
     */
    default FluidStack extractFluid(Fluid fluid, int amount, @Nullable Direction direction) {
        FluidStack currentStack = null;
        int remaining = amount;

        for (int i = 0; i < getFluidSlots(direction); i++) {
            if (remaining <= 0) {
                break;
            }

            if (currentStack != null) {
                if (this.getFluidInSlot(i, direction).isFluidEqual(currentStack)) {
                    FluidStack extractedStack = extractFluid(i, remaining, direction);
                    remaining -= extractedStack.amount;
                    currentStack.amount += extractedStack.amount;
                }
            } else {
                FluidStack extractedStack = extractFluid(i, remaining, direction);
                remaining -= extractedStack.amount;
                currentStack = extractedStack;
            }
        }

        return currentStack;
    }

    /**
     * Check if the block supports inserting fluids on this side, if this returns false there
     * should be no point in trying to use {@link #insertFluid(FluidStack, Direction)} or {@link #insertFluid(FluidStack, int, Direction)}
     *
     * @param direction Direction to check
     * @return <code>true</code> if the device supports fluid insertion on the given face
     */
    boolean canInsertFluid(@Nullable Direction direction);

    /**
     * Insert fluid into the given slot and return the remainder
     *
     * @param stack     The {@link FluidStack} to insert
     * @param slot      Slot to insert into
     * @param direction Direction to insert from
     * @return The remainder of the FluidStack (null if it was inserted entirely), this should be a new FluidStack, however it can be the same if it was not modified
     */
    FluidStack insertFluid(FluidStack stack, int slot, @Nullable Direction direction);

    /**
     * Insert fluid into any slot and return the remainder
     *
     * @param stack     The {@link FluidStack} to insert
     * @param direction Direction to insert from
     * @return The remainder of the FluidStack (null if it was inserted entirely), this should be a new FluidStack, however it can be the same if it was not modified
     */
    FluidStack insertFluid(FluidStack stack, @Nullable Direction direction);

    /**
     * Get the {@link FluidStack} in the given slot, If there is no {@link FluidStack}, then return null
     * <p>
     *
     * @param slot      The slot to get the {@link FluidStack} from
     * @param direction The direction to query from
     * @return The {@link FluidStack} in the slot
     */
    FluidStack getFluidInSlot(int slot, @Nullable Direction direction);

    /**
     * Get the size of the block inventory
     *
     * @param direction The direction to get the size from
     * @return The number of slots this block has
     */
    int getFluidSlots(@Nullable Direction direction);

    /**
     * Get the capacity of the given slot
     *
     * @param slot      The slot to query for capacity
     * @param direction The direction to query from
     * @return The capacity of the slot
     */
    int getFluidCapacity(int slot, @Nullable Direction direction);

    /**
     * Get the entire inventory of the block
     *
     * @param direction The direction to get the inventory from
     * @return An array of all the FluidStacks
     */
    FluidStack[] getFluids(@Nullable Direction direction);
}
