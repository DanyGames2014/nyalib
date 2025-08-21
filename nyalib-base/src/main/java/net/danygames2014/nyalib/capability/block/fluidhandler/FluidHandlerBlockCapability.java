package net.danygames2014.nyalib.capability.block.fluidhandler;

import net.danygames2014.nyalib.capability.block.BlockCapability;
import net.danygames2014.nyalib.fluid.Fluid;
import net.danygames2014.nyalib.fluid.FluidHandler;
import net.danygames2014.nyalib.fluid.FluidStack;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public abstract class FluidHandlerBlockCapability extends BlockCapability implements FluidHandler {
    /**
     * If this block can connect with the block in the supplied direction
     *
     * @param direction The direction to check from the block's perspective.
     *                  Example: If a machine is to the West of a pipe, that pipe should call this with East as a parameter because the pipe will be to the East of the machine
     * @return <code>true</code> if the block can connect, <code>false</code> if it cannot connect
     */
    @Override
    public abstract boolean canConnectFluid(Direction direction);

    /**
     * Check if the handler supports extracting fluid on this side, if this returns false there
     * should be no point in trying to use {@link #extractFluid(int, int, Direction)}
     *
     * @param direction Direction to check
     * @return <code>true</code> if the handler supports fluid extraction from the given direction
     */
    @Override
    public abstract boolean canExtractFluid(@Nullable Direction direction);

    /**
     * Extract a fluid in the given slot from the handler
     *
     * @param slot      The slot to extract from
     * @param amount    The amount in mB to extract
     * @param direction The direction to extract from
     * @return The FluidStack extracted, null if nothing is extracted
     */
    @Override
    public abstract FluidStack extractFluid(int slot, int amount, @Nullable Direction direction);

    /**
     * Extract any fluid from the given direction
     *
     * @param direction The direction to extract from
     * @return The extracted {@link FluidStack}
     */
    @Override
    public abstract FluidStack extractFluid(@Nullable Direction direction);

    /**
     * Extract a specified amount of any fluid from the given direction
     *
     * @param amount    The amount of fluid in mB to extract
     * @param direction The direction to extract from
     * @return The extracted {@link FluidStack}
     */
    @Override
    public abstract FluidStack extractFluid(int amount, @Nullable Direction direction);

    /**
     * Extract the given fluid in any slot from the handler
     *
     * @param fluid     The Fluid to extract
     * @param amount    The amount in mB to extract
     * @param direction The direction to extract from
     * @return The FluidStack extracted, null if nothing is extracted
     */
    @Override
    public abstract FluidStack extractFluid(Fluid fluid, int amount, @Nullable Direction direction);

    /**
     * Check if the handler supports inserting fluids on this side, if this returns false there
     * should be no point in trying to use {@link #insertFluid(FluidStack, Direction)} or {@link #insertFluid(FluidStack, int, Direction)}
     *
     * @param direction Direction to check
     * @return <code>true</code> if the device supports fluid insertion on the given face
     */
    @Override
    public abstract boolean canInsertFluid(@Nullable Direction direction);

    /**
     * Insert fluid into the given slot and return the remainder
     *
     * @param stack     The {@link FluidStack} to insert
     * @param slot      Slot to insert into
     * @param direction Direction to insert from
     * @return The remainder of the FluidStack (null if it was inserted entirely), this should be a new FluidStack, however it can be the same if it was not modified
     */
    @Override
    public abstract FluidStack insertFluid(FluidStack stack, int slot, @Nullable Direction direction);

    /**
     * Insert fluid into any slot and return the remainder
     *
     * @param stack     The {@link FluidStack} to insert
     * @param direction Direction to insert from
     * @return The remainder of the FluidStack (null if it was inserted entirely), this should be a new FluidStack, however it can be the same if it was not modified
     */
    @Override
    public abstract FluidStack insertFluid(FluidStack stack, @Nullable Direction direction);

    /**
     * Get the {@link FluidStack} in the given slot, If there is no {@link FluidStack}, then return null
     * <p>
     *
     * @param slot      The slot to get the {@link FluidStack} from
     * @param direction The direction to query from
     * @return The {@link FluidStack} in the slot
     */
    @Override
    public abstract FluidStack getFluid(int slot, @Nullable Direction direction);

    /**
     * Sets a {@link FluidStack} into the given slot
     *
     * @param slot      The slot to set the {@link FluidStack} into
     * @param stack     The fluidstack to set into the slot
     * @param direction The direction to set from
     * @return Whether the action was succesfull
     */
    @Override
    public abstract boolean setFluid(int slot, FluidStack stack, @Nullable Direction direction);

    /**
     * Get the size of the handler inventory
     *
     * @param direction The direction to get the size from
     * @return The number of slots this handler has
     */
    @Override
    public abstract int getFluidSlots(@Nullable Direction direction);

    /**
     * Get the capacity of the given slot
     *
     * @param slot      The slot to query for capacity
     * @param direction The direction to query from
     * @return The capacity of the slot
     */
    @Override
    public abstract int getFluidCapacity(int slot, @Nullable Direction direction);

    /**
     * Get the entire inventory of the handler
     *
     * @param direction The direction to get the inventory from
     * @return An array of all the FluidStacks
     */
    @Override
    public abstract FluidStack[] getFluids(@Nullable Direction direction);
}
