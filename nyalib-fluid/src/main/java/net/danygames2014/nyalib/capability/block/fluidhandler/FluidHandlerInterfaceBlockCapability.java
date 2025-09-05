package net.danygames2014.nyalib.capability.block.fluidhandler;

import net.danygames2014.nyalib.fluid.Fluid;
import net.danygames2014.nyalib.fluid.FluidHandler;
import net.danygames2014.nyalib.fluid.FluidStack;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public class FluidHandlerInterfaceBlockCapability extends FluidHandlerBlockCapability {
    private final FluidHandler fluidHandler;

    public FluidHandlerInterfaceBlockCapability(FluidHandler fluidHandler) {
        this.fluidHandler = fluidHandler;
    }

    @Override
    public boolean canExtractFluid(@Nullable Direction direction) {
        return fluidHandler.canExtractFluid(direction);
    }

    @Override
    public FluidStack extractFluid(int slot, int amount, @Nullable Direction direction) {
        return fluidHandler.extractFluid(slot, amount, direction);
    }

    @Override
    public FluidStack extractFluid(@Nullable Direction direction) {
        return fluidHandler.extractFluid(direction);
    }

    @Override
    public FluidStack extractFluid(int amount, @Nullable Direction direction) {
        return fluidHandler.extractFluid(amount, direction);
    }

    @Override
    public FluidStack extractFluid(Fluid fluid, int amount, @Nullable Direction direction) {
        return fluidHandler.extractFluid(fluid, amount, direction);
    }

    @Override
    public boolean canInsertFluid(@Nullable Direction direction) {
        return fluidHandler.canInsertFluid(direction);
    }

    @Override
    public FluidStack insertFluid(FluidStack stack, int slot, @Nullable Direction direction) {
        return fluidHandler.insertFluid(stack, slot, direction);
    }

    @Override
    public FluidStack insertFluid(FluidStack stack, @Nullable Direction direction) {
        return fluidHandler.insertFluid(stack, direction);
    }

    @Override
    public FluidStack getFluid(int slot, @Nullable Direction direction) {
        return fluidHandler.getFluid(slot, direction);
    }

    @Override
    public boolean setFluid(int slot, FluidStack stack, @Nullable Direction direction) {
        return fluidHandler.setFluid(slot, stack, direction);
    }

    @Override
    public int getFluidSlots(@Nullable Direction direction) {
        return fluidHandler.getFluidSlots(direction);
    }

    @Override
    public int getFluidCapacity(int slot, @Nullable Direction direction) {
        return fluidHandler.getFluidCapacity(slot, direction);
    }

    @Override
    public int getRemainingFluidCapacity(int slot, @Nullable Direction direction) {
        return fluidHandler.getRemainingFluidCapacity(slot, direction);
    }

    @Override
    public FluidStack[] getFluids(@Nullable Direction direction) {
        return fluidHandler.getFluids(direction);
    }

    @Override
    public boolean canConnectFluid(Direction direction) {
        return fluidHandler.canConnectFluid(direction);
    }
}
