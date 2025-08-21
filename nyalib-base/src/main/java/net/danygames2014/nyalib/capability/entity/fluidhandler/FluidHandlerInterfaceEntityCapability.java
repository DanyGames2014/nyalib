package net.danygames2014.nyalib.capability.entity.fluidhandler;

import net.danygames2014.nyalib.fluid.*;

public class FluidHandlerInterfaceEntityCapability extends FluidHandlerEntityCapability{
    private final FluidHandlerEntity fluidHandler;
    
    
    public FluidHandlerInterfaceEntityCapability(FluidHandlerEntity fluidHandler) {
        this.fluidHandler = fluidHandler;
    }

    @Override
    public boolean canExtractFluid() {
        return fluidHandler.canExtractFluid();
    }

    @Override
    public FluidStack extractFluid(int slot, int amount) {
        return fluidHandler.extractFluid(slot, amount);
    }

    @Override
    public FluidStack extractFluid() {
        return fluidHandler.extractFluid();
    }

    @Override
    public FluidStack extractFluid(int amount) {
        return fluidHandler.extractFluid(amount);
    }

    @Override
    public FluidStack extractFluid(Fluid fluid, int amount) {
        return fluidHandler.extractFluid(fluid, amount);
    }

    @Override
    public boolean canInsertFluid() {
        return fluidHandler.canInsertFluid();
    }

    @Override
    public FluidStack insertFluid(FluidStack stack, int slot) {
        return fluidHandler.insertFluid(stack, slot);
    }

    @Override
    public FluidStack insertFluid(FluidStack stack) {
        return fluidHandler.insertFluid(stack);
    }

    @Override
    public FluidStack getFluid(int slot) {
        return fluidHandler.getFluid(slot);
    }

    @Override
    public boolean setFluid(int slot, FluidStack stack) {
        return fluidHandler.setFluid(slot, stack);
    }

    @Override
    public int getFluidSlots() {
        return fluidHandler.getFluidSlots();
    }

    @Override
    public int getFluidCapacity(int slot) {
        return fluidHandler.getFluidCapacity(slot);
    }

    @Override
    public int getRemainingFluidCapacity(int slot) {
        return fluidHandler.getRemainingFluidCapacity(slot);
    }

    @Override
    public FluidStack[] getFluids() {
        return fluidHandler.getFluids();
    }
}
