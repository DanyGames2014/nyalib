package net.danygames2014.nyalib.capability.item.fluidhandler;

import net.danygames2014.nyalib.capability.item.ItemCapability;
import net.danygames2014.nyalib.fluid.Fluid;
import net.danygames2014.nyalib.fluid.FluidStack;

public abstract class FluidHandlerItemCapability extends ItemCapability {
    /**
     * Check if the entity supports extracting fluid if this returns false there
     * should be no point in trying to use {@link #extractFluid(int, int)}
     *
     * @return <code>true</code> if the handler supports fluid extraction
     */
    public abstract boolean canExtractFluid();

    /**
     * Extract a fluid in the given slot from the entity
     *
     * @param slot   The slot to extract from
     * @param amount The amount in mB to extract
     * @return The FluidStack extracted, null if nothing is extracted
     */
    public abstract FluidStack extractFluid(int slot, int amount);

    /**
     * Extract the given fluid in any slot from the handler
     *
     * @param fluid  The Fluid to extract
     * @param amount The amount in mB to extract
     * @return The FluidStack extracted, null if nothing is extracted
     */
    public abstract FluidStack extractFluid(Fluid fluid, int amount);

    /**
     * Extract a specified amount of any fluid from the entity
     *
     * @param amount The amount of fluid in mB to extract
     * @return The extracted {@link FluidStack}
     */
    public abstract FluidStack extractFluid(int amount);

    /**
     * Extract any fluid from the entity
     *
     * @return The extracted {@link FluidStack}
     */
    public abstract FluidStack extractFluid();

    /**
     * Check if the entity supports inserting fluids, if this returns false there
     * should be no point in trying to use {@link #insertFluid(FluidStack)} or {@link #insertFluid(FluidStack, int)}
     *
     * @return <code>true</code> if the entity supports fluid insertion
     */
    public abstract boolean canInsertFluid();

    /**
     * Insert fluid into the given slot and return the remainder
     *
     * @param stack The {@link FluidStack} to insert
     * @param slot  Slot to insert into
     * @return The remainder of the FluidStack (null if it was inserted entirely), this should be a new FluidStack, however it can be the same if it was not modified
     */
    public abstract FluidStack insertFluid(FluidStack stack, int slot);

    /**
     * Insert fluid into any slot and return the remainder
     *
     * @param stack The {@link FluidStack} to insert
     * @return The remainder of the FluidStack (null if it was inserted entirely), this should be a new FluidStack, however it can be the same if it was not modified
     */
    public abstract FluidStack insertFluid(FluidStack stack);

    /**
     * Get the {@link FluidStack} in the given slot, If there is no {@link FluidStack}, then return null
     * <p>
     *
     * @param slot The slot to get the {@link FluidStack} from
     * @return The {@link FluidStack} in the slot
     */
    public abstract FluidStack getFluid(int slot);

    /**
     * Sets a {@link FluidStack} into the given slot
     *
     * @param slot  The slot to set the {@link FluidStack} into
     * @param stack The {@link FluidStack} to set into the slot
     * @return Whether the action was succesfull
     */
    public abstract boolean setFluid(int slot, FluidStack stack);

    /**
     * Get the size of the entity fluid inventory
     *
     * @return The number of slots this entity has
     */
    public abstract int getFluidSlots();

    /**
     * Get the capacity of the given slot
     *
     * @param slot The slot to query for capacity
     * @return The capacity of the slot
     */
    public abstract int getFluidCapacity(int slot);

    /**
     * Get the entire fluid inventory of the entity
     *
     * @return An array of all the FluidStacks
     */
    public abstract FluidStack[] getFluids();
}
